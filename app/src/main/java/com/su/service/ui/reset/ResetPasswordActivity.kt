package com.su.service.ui.reset

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.su.service.R
import com.su.service.ui.login.LoginActivity
import com.su.service.ui.main.MainViewModel
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPrefManager: SharedPrefManager
    private var isBuat = false
    private var passwordId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(this)!!
        val tag = intent.getStringExtra("extra_tag")
        hideLoading()
        handlingIntent(intent)

        if(tag != null){
            if(tag == "buat_password"){
                isBuat = true
                panel_oldpassword?.visibility = View.GONE
                tv_title?.text = "Buat Password"
            }else{
                panel_oldpassword?.visibility = View.VISIBLE
            }
        }else{
            panel_oldpassword?.visibility = View.VISIBLE
        }
        btn_reset.setOnClickListener(View.OnClickListener { resetPassword(passwordId) })
    }

    private fun hideLoading() {
        progress_bar?.visibility = View.GONE
    }
    private fun showLoading() {
        progress_bar?.visibility = View.VISIBLE
    }

    private fun handlingIntent(intent: Intent?) {
        val appLinkIntent = intent!!
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            panel_oldpassword?.visibility = View.GONE
            tv_title?.text = "Reset Password"
            passwordId = appLinkData.lastPathSegment
        }else{
            tv_title?.text = "Edit Password"
            passwordId = null
        }
    }

    private fun resetPassword(passwordId: String?) {
        var oldpassword = ""
        val password: String = edt_newpassword.text.toString().trim()
        val repassword: String = edt_cpassword.text.toString().trim()
        var isUpdate = false
        if(passwordId == null){
            isUpdate = true
        }

        if(isUpdate){
            if(!isBuat) {
                oldpassword = edt_cpassword.text.toString().trim()

                if (TextUtils.isEmpty(oldpassword)) {
                    edt_oldpassword.setError("Password Lama belum terisi")
                    edt_oldpassword.requestFocus()
                    return
                }
            }else{
                oldpassword = "123456"
            }
        }


        if (TextUtils.isEmpty(password)) {
            edt_newpassword.setError("Password belum terisi")
            edt_newpassword.requestFocus()
            return
        }
        if (TextUtils.isEmpty(repassword)) {
            edt_cpassword.setError("Ulang Password belum terisi")
            edt_cpassword.requestFocus()
            return
        }
        if (password.length < 6) {
            edt_newpassword.setError("Password harus 6 karakter")
            edt_newpassword.requestFocus()
            return
        }
        if (password != repassword) {
            edt_cpassword.setError("Konfirmasi password salah")
            edt_cpassword.requestFocus()
            return
        }

        if(isUpdate){
            viewModel.updatePassword(sharedPrefManager.user.apiMobileToken,oldpassword,password,repassword).observe(this, Observer {
                hideLoading()
                if (it != null){
                    if(it.status == 200){
                        Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        Handler().postDelayed(Runnable { finish() }, 1500)
                    }else{
                        Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                }
            })
        }else{
            viewModel.resetPassword(passwordId,password,repassword).observe(this, Observer {
                hideLoading()
                if (it != null){
                    if(it.status == 200){
                        Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        Handler().postDelayed(Runnable {
                            startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
                            finish() }, 1500)
                    }else{
                        Snackbar.make(container, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    Snackbar.make(container, resources.getString(R.string.failed), Snackbar.LENGTH_SHORT).show()
                }
            })
        }

    }
}
