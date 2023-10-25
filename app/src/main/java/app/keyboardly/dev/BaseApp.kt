package app.keyboardly.dev

import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitcompat.SplitCompatApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

import timber.log.Timber

class BaseApp : SplitCompatApplication(){

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "%s>%s",
                        super.createStackElementTag(element),
                        element.methodName
                    )
                }
            })
        }

        startKoin {
            androidLogger(org.koin.core.logger.Level.NONE)
            androidContext(this@BaseApp)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

}
