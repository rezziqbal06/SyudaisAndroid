package com.su.service.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.su.service.R
import com.su.service.receiver.PopupReceiver
import com.su.service.ui.absen.AbsenActivity
import com.su.service.ui.absen.ScanActivity
import com.su.service.ui.absen2.Absen2Activity
import com.su.service.ui.artikel.admin.ApprovalActivity
import com.su.service.ui.login.LoginActivity
import com.su.service.ui.quran.SettingQuranDailyActivity
import com.su.service.ui.reset.ResetPasswordActivity
import com.su.service.utils.AppController
import com.su.service.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private val RC_SIGN_IN = 101
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth
    private var TAG = MainActivity::class.java.simpleName
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var imgView: ImageView
    private lateinit var cvLogin: CardView
    private lateinit var cvLoginEmail: CardView
    private lateinit var tvNama: TextView
    private lateinit var cAdmin: Chip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPrefManager = SharedPrefManager(this)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        Log.d(TAG, "apisess: ${sharedPrefManager.user.apiMobileToken}")

        //showDialog()
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavView.setupWithNavController(navController)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            main_toolbar.elevation = 0f
        }
        main_toolbar.setNavigationOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        navView.setNavigationItemSelectedListener(this)
        val toggle =
            ActionBarDrawerToggle(
                this, drawer_layout, main_toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        configureGoogleSignIn()

        val headerView = navView.getHeaderView(0)
        imgView = headerView.findViewById<ImageView>(R.id.img_profil)
        cvLogin = headerView.findViewById<CardView>(R.id.cv_login)
        cvLoginEmail = headerView.findViewById<CardView>(R.id.cv_login_email)
        tvNama = headerView.findViewById<TextView>(R.id.tv_nama)
        cAdmin = headerView.findViewById<Chip>(R.id.chip_admin)
        progressBar = headerView.findViewById<ProgressBar>(R.id.progressbar_login)

        cvLogin.setOnClickListener {
            if(!sharedPrefManager.isLoggedIn){
                progressBar.visibility = View.VISIBLE
                Log.d(TAG,"login dengan google")
                signIn()
            }
        }

        cvLoginEmail?.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivityForResult(intent, 102)
        }

        updateProfile(sharedPrefManager.isLoggedIn)
        checkUpdateApp()

    }



    override fun onStop() {
        super.onStop()
    }

    private fun permissionCamera(){
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    startActivity(intent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity, "Permission Camera Ditolak", Toast.LENGTH_SHORT).show()
                }

            })
            .check()

    }

    private fun checkUpdateApp() {
        val appInfo = packageManager.getPackageInfo(packageName,0)
        val versionName = appInfo.versionName
        viewModel.checkUpdate(versionName).observe(this, Observer {
            if(it != null){
                if(it.status == 200){
                    Log.d(TAG, "versionName: ${it.result?.update?.versionName} store: ${it.result?.update?.store}")
                    val builder = android.app.AlertDialog.Builder(this)
                    val dialog: android.app.AlertDialog = builder.setTitle("Update Aplikasi Terbaru")
                        .setMessage("Update versi ${it.result?.update?.versionName} untuk mendapatkan banyak fitur yang bermanfaat in syaa Allah")
                        .setIcon(R.drawable.icon_syudais)
                        .setPositiveButton("Update", DialogInterface.OnClickListener { dialogInterface, i ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${it.result?.update?.store}"))
                            startActivity(intent)
                            dialogInterface.dismiss()
                        })
                        .setNegativeButton("Nanti", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                        }).create()
                    dialog.show()
                    val positive = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                    positive.setTextColor(resources.getColor(R.color.dark))
                    positive.setBackgroundColor(resources.getColor(R.color.white))
                    val negative = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                    negative.setTextColor(resources.getColor(R.color.dark))
                    negative.setBackgroundColor(resources.getColor(R.color.white))
                }else{
                    Log.d(TAG, it.message.toString())
                }
            }else{
                Log.d(TAG, "failed cek update")
            }
        })
    }

    private fun updateProfile(isLogin: Boolean) {
        if(isLogin){
            Log.d(TAG, "image: ${sharedPrefManager.user.image}")
            Glide.with(this)
                .load(sharedPrefManager.user.image)
                .into(imgView)
            tvNama.text = sharedPrefManager.user.fnama

            nav_view?.menu?.clear()
            nav_view?.inflateMenu(R.menu.drawer_menu)
            if(sharedPrefManager.user.utype == "admin"){
                cAdmin?.visibility = View.VISIBLE
                nav_view?.menu?.clear()
                nav_view?.inflateMenu(R.menu.drawer_menu_admin)
            }else if(sharedPrefManager.user.utype == "tim"){
                cAdmin?.visibility = View.VISIBLE
                nav_view?.menu?.clear()
                nav_view?.inflateMenu(R.menu.drawer_menu_tim)
            }else{
                cAdmin?.visibility = View.GONE
                nav_view?.menu?.clear()
                nav_view?.inflateMenu(R.menu.drawer_menu_after_login)
            }
            cvLoginEmail?.visibility = View.GONE
        }else{
            cAdmin?.visibility = View.GONE
            Log.d(TAG,"belum Login")
            imgView.setImageResource(R.drawable.ic_icons8_google)
            tvNama.text = resources.getString(R.string.login)
            nav_view?.menu?.clear()
            cvLoginEmail?.visibility = View.VISIBLE
          //  nav_view?.inflateMenu(R.menu.drawer_menu)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(sharedPrefManager.isLoggedIn){
            menuInflater.inflate(R.menu.drawer_menu_after_login, menu)
        }else{
            menuInflater.inflate(R.menu.drawer_menu,menu)
        }
        return super.onCreateOptionsMenu(menu)
    }


    private fun configureGoogleSignIn() {
        firebaseAuth = FirebaseAuth.getInstance()
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun login(token: String, user: FirebaseUser) {
        Log.d(TAG, "Login user ${user.email}")
        viewModel.loginByGoogle(user.email, user.uid, token, user.phoneNumber).observe(this, Observer { data ->
            if (data != null) {
                if (data.status == 200) {
                    progressBar.visibility = View.GONE
                    Log.d(TAG, "login berhasil")
                    Log.d(TAG, "apisess: "+ data.data)
                    data.data?.let { sharedPrefManager.userLogin(it) }
                    drawer_layout.closeDrawer(GravityCompat.START)
                    Snackbar.make(container,"Berhasil login",Snackbar.LENGTH_SHORT).show()
                    updateProfile(sharedPrefManager.isLoggedIn)
                } else if (data.status== 1717 || data.status== 1711 || data.status== 1712) {
                    user.displayName?.let { forcetoRegister(it, user.email, user.uid, user.photoUrl.toString(), token) }
                } else {
                    progressBar.visibility = View.GONE
                    googleLogout()

                    Log.d(
                        TAG,
                        "error " + data.message
                    )
                    Toast.makeText(
                        this,
                        data.message.toString() + ", " + data.status,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                progressBar.visibility = View.GONE
                googleLogout()

                Log.d(
                    TAG,
                    "tidak dapat login untuk saat ini"
                )
                Toast.makeText(this, "Tidak dapat login untuk saat ini", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun forcetoRegister(nama: String, email: String?, uid: String, gambar: String, fcmToken: String) {
        Log.d(TAG, "force register")
        progressBar.visibility = View.GONE
        email?.let { viewModel.daftarByGoogle(nama, it, uid, gambar, fcmToken).observe(this, Observer { data->
            if (data != null) {
                if (data.status == 200) {
                    data.data?.let { sharedPrefManager.userLogin(it) }
                    drawer_layout.closeDrawer(GravityCompat.START)
                    Snackbar.make(container,"Berhasil login",Snackbar.LENGTH_SHORT).show()
                    updateProfile(sharedPrefManager.isLoggedIn)
                    Handler().postDelayed(Runnable {
                        val intent = Intent(this, ResetPasswordActivity::class.java)
                        intent.putExtra("extra_tag","buat_password")
                        startActivity(intent)
                    },1500)
                } else {
                    googleLogout()

                    Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Gagal daftar: ${data.message}")
                }
            } else {
                googleLogout()

                Toast.makeText(this, "Gagal, Silahkan coba beberapa saat lagi", Toast.LENGTH_SHORT)
                    .show()
                Log.d(TAG, "Gagal daftar")
            }
        }) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }else if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                updateProfile(sharedPrefManager.isLoggedIn)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user: FirebaseUser = firebaseAuth.getCurrentUser()!!
                getFcmBeforeLogin(user)
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getFcmBeforeLogin(user: FirebaseUser) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        TAG,
                        "getInstanceId failed",
                        task.exception
                    )
                    login("", user)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                login(token, user)
            })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.nav_update_password -> {
                val intent = Intent(this@MainActivity, ResetPasswordActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_panel_absen -> {
                val intent = Intent(this@MainActivity, Absen2Activity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_setting_qd -> {
                val intent = Intent(this@MainActivity, SettingQuranDailyActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_scan_absen -> {
                permissionCamera()
                true
            }
            R.id.nav_approval -> {
                val intent = Intent(this@MainActivity, ApprovalActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_logout -> {
                googleLogout()
                sharedPrefManager.logout()
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> false
        }
    }

    private fun googleLogout(){
        mGoogleSignInClient.signOut().addOnCompleteListener {
            mGoogleSignInClient.revokeAccess()
        }
    }

}
