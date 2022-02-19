package com.github.libliboom.epubviewer.di

import android.app.Application
import com.github.libliboom.epubviewer.base.BaseApplication
import com.github.libliboom.epubviewer.di.module.ApplicationModule
import com.github.libliboom.epubviewer.di.scope.ApplicationScope
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
