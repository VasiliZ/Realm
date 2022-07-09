package com.github.rtyvz.realm

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

private const val realmVersion = 1L
private const val MAX_THREADS_COUNT = 1

class RealmApp : Application() {

    companion object {
        lateinit var realmConfig: RealmConfiguration
        val realmCoroutineDispatcher =
            Executors.newFixedThreadPool(MAX_THREADS_COUNT).asCoroutineDispatcher()
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        realmConfig =
            RealmConfiguration.Builder().schemaVersion(realmVersion).build()
    }
}