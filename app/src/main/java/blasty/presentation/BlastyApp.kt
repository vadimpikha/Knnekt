package blasty.presentation

import android.app.Application
import blasty.R
import com.connectycube.auth.session.ConnectycubeSettings
import com.connectycube.core.LogLevel

class BlastyApp : Application() {

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
            logLevel = LogLevel.DEBUG
        }
    }

}