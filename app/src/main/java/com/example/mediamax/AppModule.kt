package com.example.mediamax

import android.content.Context
import com.example.mediamax.repositories.AudioRepository
import com.example.mediamax.repositories.Repository
import com.example.mediamax.repositories.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideImageRepository(@ApplicationContext context: Context): Repository {
        return Repository(context )
    }
    @Provides
    fun provideVideoRepository(@ApplicationContext context: Context): VideoRepository {
        return VideoRepository(context)
    }

    @Provides
    fun provideAudioRepository(@ApplicationContext context: Context): AudioRepository {
        return AudioRepository(context)
    }
}
