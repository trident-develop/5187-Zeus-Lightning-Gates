package com.xd.xdtglobal

import android.app.Application
import com.xd.xdtglobal.di.dataStoreModule
import com.xd.xdtglobal.di.gameModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class GatesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GatesApp)
            modules(
                dataStoreModule,
                gameModule
            )
        }
    }
}