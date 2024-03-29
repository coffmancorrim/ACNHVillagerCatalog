package me.proton.coffmancorrim.acnhvillagercatalog

import android.app.Application
import me.proton.coffmancorrim.acnhvillagercatalog.di.AppModule
import me.proton.coffmancorrim.acnhvillagercatalog.di.AppModuleImpl

class AnimalCrossingApplication : Application() {

    companion object{
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}