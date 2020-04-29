package com.github.libliboom.epubviewer.main.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class ViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionbar()
    }

    private fun setupActionbar() {
        val actionbar = supportActionBar
        actionbar!!.title = getTitleToolbar()
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected open fun getTitleToolbar(): String {
        return "";
    }
}