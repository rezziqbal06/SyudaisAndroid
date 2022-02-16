package com.su.service.ui.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.Provider
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.android.youtube.player.YouTubePlayerView
import com.su.service.R
import com.su.service.ui.main.MainViewModel
import com.su.service.ui.user.UserViewModel
import com.su.service.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_lupa.*
import kotlinx.android.synthetic.main.fragment_lupa.edt_email
import kotlinx.android.synthetic.main.fragment_video_player.*


class LupaFragment() : BottomSheetDialogFragment(){
    companion object{
        const val EXTRA_TEXT = "extra_text"
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lupa,container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_lupa?.setOnClickListener {
            val email = edt_email?.text.toString()
            if(email.isEmpty()){
                edt_email?.setError("Email belum diisi")
                edt_email?.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edt_email?.setError("Email tidak valid")
                edt_email?.requestFocus()
                return@setOnClickListener
            }
            viewModel.lupa(email).observe(this, Observer {
                if(it != null){
                    if(it.status == 200){
                        Toast.makeText(context, it.message.toString()+", Tunggu email dari kami", Toast.LENGTH_LONG).show()
                        Handler().postDelayed(Runnable { dismiss() }, 2500)
                    }else{
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(context, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                }
            })

        }
    }


}


