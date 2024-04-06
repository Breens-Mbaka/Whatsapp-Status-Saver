package com.breens.whatsappstatussaver.di

import android.content.Context
import com.breens.whatsappstatussaver.statuses.data.GetStatusImagesRepositoryImpl
import com.breens.whatsappstatussaver.preferences.data.PreferenceRepositoryImpl
import com.breens.whatsappstatussaver.statuses.domain.GetStatusImagesRepository
import com.breens.whatsappstatussaver.preferences.domain.PreferencesRepository
import com.breens.whatsappstatussaver.save.data.SaveImagesRepositoryImpl
import com.breens.whatsappstatussaver.save.domain.SaveImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesPreferencesRepository(@ApplicationContext context: Context): PreferencesRepository {
        return PreferenceRepositoryImpl(context = context)
    }

    @Provides
    @Singleton
    fun providesImagesRepository(@ApplicationContext context: Context): GetStatusImagesRepository {
        return GetStatusImagesRepositoryImpl(context = context)
    }

    @Provides
    @Singleton
    fun providesSaveImagesRepository(@ApplicationContext context: Context): SaveImagesRepository {
        return SaveImagesRepositoryImpl(context = context)
    }
}
