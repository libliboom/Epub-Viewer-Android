package com.github.libliboom.epubviewer.app.ui

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<Binding : ViewBinding> : DaggerAppCompatActivity() {

  private lateinit var binding: Binding

  override fun onCreate(savedInstanceState: Bundle?) {
    getArgs()
    super.onCreate(savedInstanceState)
    binding = inflateBinding()
    setContentView(binding.root)
    initView(binding)
  }

  open fun getArgs() {}

  abstract fun inflateBinding(): Binding

  open fun initView(binding: Binding) {}
}
