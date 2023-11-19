package com.breens.whatsappstatussaver.di

import android.content.Context
import com.breens.whatsappstatussaver.data.PreferenceRepositoryImplementation
import com.breens.whatsappstatussaver.data.PreferencesRepository
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
        return PreferenceRepositoryImplementation(context = context)
    }
}
