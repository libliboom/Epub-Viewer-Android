package com.github.libliboom.epubviewer.di

import android.app.Application
import com.github.libliboom.epubviewer.app.BaseApplication
import com.github.libliboom.epubviewer.app.di.scope.ApplicationScope
import com.github.libliboom.epubviewer.di.module.ApplicationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : AndroidInjector<BaseApplication> {

  override fun inject(application: BaseApplication)

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance application: Application): ApplicationComponent
  }
}
