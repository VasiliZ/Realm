package com.github.rtyvz.realm

import android.app.Application
import io.realm.Realm

const val realmVersion = 1L

class RealmApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}