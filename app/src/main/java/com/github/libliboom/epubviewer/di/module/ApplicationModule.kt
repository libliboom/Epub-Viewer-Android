package com.github.libliboom.epubviewer.di.module

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.github.libliboom.epubviewer.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        ActivityModule::class,
        AndroidSupportInjectionModule::class
    ]
)
object ApplicationModule {

    @ApplicationScope
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions()
    }

    @ApplicationScope
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application).setDefaultRequestOptions(requestOptions)
    }
}
