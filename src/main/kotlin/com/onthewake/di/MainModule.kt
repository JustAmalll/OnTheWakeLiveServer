package com.onthewake.di

import com.google.gson.Gson
import com.onthewake.data.queue.QueueDataSource
import com.onthewake.data.queue.QueueDataSourceImpl
import com.onthewake.data.remote.OneSignalService
import com.onthewake.data.remote.OneSignalServiceImpl
import com.onthewake.data.trick_list.TrickListSource
import com.onthewake.data.trick_list.TrickListSourceImpl
import com.onthewake.data.user.MongoUserDataSource
import com.onthewake.data.user.UserDataSource
import com.onthewake.room.RoomController
import io.ktor.util.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

@InternalAPI
val mainModule = module {
    single {
        val mongodbUri = System.getenv("MONGODB_URI")
        val dbName = "onthewakelivedatabase"
        KMongo.createClient(mongodbUri).coroutine.getDatabase(dbName)
    }
    single<QueueDataSource> { QueueDataSourceImpl(get()) }
    single<UserDataSource> { MongoUserDataSource(get()) }
    single<TrickListSource> { TrickListSourceImpl(get()) }
    single<OneSignalService> { OneSignalServiceImpl(get(), get()) }
    single { RoomController(get()) }
    single { Gson() }
}