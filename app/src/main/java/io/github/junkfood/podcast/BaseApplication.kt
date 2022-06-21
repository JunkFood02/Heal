package io.github.junkfood.podcast

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        applicationScope = CoroutineScope(SupervisorJob())
        context = this.applicationContext
    }

    companion object {
        lateinit var applicationScope: CoroutineScope
        lateinit var context: Context
    }
}