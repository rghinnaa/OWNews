package com.akb.ownews.data.di

import android.app.Application
import android.content.Context
import com.akb.ownews.data.local.MainLocalDataSource
import com.akb.ownews.data.local.dao.BookmarkDao
import com.akb.ownews.data.remote.api.ApiCallback
import com.akb.ownews.data.remote.repository.MainRepository
import com.akb.ownews.data.remote.source.MainRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideApplication(application: Application): Context = application

    @Provides
    fun provideMainRepository(
        apiCallback: ApiCallback,
        bookmarkDao: BookmarkDao
    ) = MainRepository(
        MainRemoteDataSource(apiCallback),
        MainLocalDataSource(bookmarkDao)
    )

}