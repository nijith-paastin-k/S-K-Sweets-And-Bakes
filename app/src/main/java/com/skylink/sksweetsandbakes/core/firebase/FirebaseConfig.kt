package com.skylink.sksweetsandbakes.core.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage

/**
 * Robust Firebase initialization ensuring application stability across development,
 * emulator, and production environments.
 */
object FirebaseConfig {

    private var isInitialized = false

    fun initialize(context: Context, useEmulators: Boolean = false) {
        if (isInitialized) return

        checkNotNull(FirebaseApp.initializeApp(context)) {
            "Firebase is not configured. Add the Firebase project's google-services.json to app/."
        }

        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        if (useEmulators) {
            connectToEmulators("10.0.2.2")
        }

        isInitialized = true
    }

    fun connectToEmulators(host: String = "10.0.2.2") {
        FirebaseAuth.getInstance().useEmulator(host, 9099)
        FirebaseFirestore.getInstance().useEmulator(host, 8080)
        FirebaseStorage.getInstance().useEmulator(host, 9199)
        FirebaseFunctions.getInstance().useEmulator(host, 5001)
    }

    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    val firestore: FirebaseFirestore get() = FirebaseFirestore.getInstance()
    val storage: FirebaseStorage get() = FirebaseStorage.getInstance()
    val functions: FirebaseFunctions get() = FirebaseFunctions.getInstance()
}
