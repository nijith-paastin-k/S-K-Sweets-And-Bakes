package com.skylink.sksweetsandbakes

import android.app.Application
import com.skylink.sksweetsandbakes.core.firebase.FirebaseConfig
import com.skylink.sksweetsandbakes.di.AppContainer
import com.skylink.sksweetsandbakes.di.AppModule

class SKSweetsApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppModule(this)
        // Safe Firebase initialization with fallback
        FirebaseConfig.initialize(this)
    }
}
