package com.github.libliboom.epubviewer.di.module

import com.github.libliboom.epubviewer.datasource.settings.SettingsLocalDatasource
import com.github.libliboom.epubviewer.domain.settings.SettingsUsecase
import com.github.libliboom.epubviewer.repository.settings.SettingsRepository
import dagger.Module
import dagger.Provides

@Module
object UsecaseModule {

  @Provides
  fun provideSettingsUsecase() = SettingsUsecase(SettingsRepository(SettingsLocalDatasource()))
}
