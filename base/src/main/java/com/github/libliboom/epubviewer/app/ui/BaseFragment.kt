package com.github.libliboom.epubviewer.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.Disposable

abstract class BaseFragment<Binding : ViewBinding> : DaggerFragment() {

  private var bindingReference: Binding? = null
  protected val binding by lazy { bindingReference!! }

  private lateinit var disposable: Disposable

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    getArgs()
    bindingReference = inflateBinding(container)
    afterInitView(binding)
    return binding.root
  }

  open fun getArgs() {}

  abstract fun inflateBinding(container: ViewGroup?): Binding

  open fun afterInitView(binding: Binding) {}

  protected fun rx(block: () -> Disposable) {
    disposable = block()
  }

  protected fun finish() {
    requireActivity().supportFragmentManager.popBackStack()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    bindingReference = null
    if (::disposable.isInitialized && disposable.isDisposed.not()) {
      disposable.dispose()
    }
  }
}
