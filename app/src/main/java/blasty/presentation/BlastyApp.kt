package blasty.presentation

import android.app.Application
import blasty.BuildConfig
import blasty.R
import blasty.di.DiModules
import com.connectycube.auth.session.ConnectycubeSettings
import com.connectycube.core.LogLevel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class BlastyApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@BlastyApp))
        importAll(DiModules.all)
    }

    override fun onCreate() {
        super.onCreate()
        initConnectyCubeSDK()
    }

    private fun initConnectyCubeSDK() {
        ConnectycubeSettings.getInstance().apply {
            init(
                applicationContext,
                getString(R.string.connectycube_app_id),
                getString(R.string.connectycube_auth_key),
                getString(R.string.connectycube_auth_secret)
            )
            accountKey = getString(R.string.connectycube_account_key)
            logLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.NOTHING
        }
    }

}