package com.su.service.ui.lembaga

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.su.service.R
import kotlinx.android.synthetic.main.activity_lembaga.*
import kotlinx.android.synthetic.main.content_lembaga.*

class LembagaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lembaga)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        tv_general?.setOnClickListener {
            openDetail()
        }

        tv_kontak?.setOnClickListener {
            openDetail()
        }

        tv_vm?.setOnClickListener {
            openDetail()
        }

        tv_organigram?.setOnClickListener {
            openDetail()
        }

        tv_kontak?.setOnClickListener {
            openDetail()
        }
    }

    private fun openDetail() {
        val fragment = DetailLembagaFragment()
        fragment.show(supportFragmentManager,"")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
