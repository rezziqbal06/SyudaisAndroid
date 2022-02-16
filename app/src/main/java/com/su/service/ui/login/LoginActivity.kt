package com.su.service.ui.login

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.su.service.R
import com.su.service.ui.main.MainViewModel
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val TAG = LoginActivity::class.java.simpleName
    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        hideLoading()
        btn_login?.setOnClickListener {
            getFcmTokenBeforeLogin()
        }
    }

    private fun getFcmTokenBeforeLogin(){
        showLoading()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        TAG,
                        "getInstanceId failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                login(token)
            })
    }

    private fun login(token: String) {
        val email = edt_email?.text.toString()
        val password = edt_password?.text.toString()

        if(email.isEmpty()){
            edt_email?.setError("Email belum diisi")
            edt_email?.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email?.setError("Email tidak valid")
            edt_email?.requestFocus()
            return
        }
        if(password.isEmpty()){
            edt_password?.setError("Password belum diisi")
            edt_password?.requestFocus()
            return
        }
        if(password.length < 6){
            edt_password?.setError("Password minimal 6 karakter")
            edt_password?.requestFocus()
            return
        }

        viewModel.login(email, password,token).observe(this, Observer {
            hideLoading()
            if(it != null){
                Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                if(it.status == 200){
                    it.data?.let { it1 -> sharedPrefManager.userLogin(it1) }
                    Handler().postDelayed(Runnable { setResult(Activity.RESULT_OK)
                        finish() }, 1500)
                }
            }else{
                Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun hideLoading() {
        progress_bar?.visibility = View.GONE
    }
    private fun showLoading() {
        progress_bar?.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
