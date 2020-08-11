package knnekt.presentation

import android.app.Application
import com.connectycube.auth.session.ConnectycubeSession
import com.connectycube.auth.session.ConnectycubeSessionManager
import com.connectycube.auth.session.ConnectycubeSessionParameters
import knnekt.presentation.di.PresentationLayerDi
import com.connectycube.auth.session.ConnectycubeSettings
import com.connectycube.core.LogLevel
import knnekt.BuildConfig
import knnekt.R
import knnekt.data.di.DataLayerDi
import knnekt.domain.di.DomainLayerDi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class KnnektApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KnnektApp))
        with(PresentationLayerDi) {
            importAll(
                bindConstants(getString(R.string.firebase_project_id)),
                viewModelModule
            )
        }
        with(DomainLayerDi) {
            import(useCaseModule)
        }
        with(DataLayerDi) {
            importAll(repositoryModule, dispatcherModule, dataSourceModule)
        }
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

        ConnectycubeSessionManager.getInstance().apply {
            addListener(
                object : ConnectycubeSessionManager.SessionListener {

                    override fun onSessionCreated(session: ConnectycubeSession) {
                        println("onSessionCreated")
                    }

                    override fun onSessionUpdated(sessionParameters: ConnectycubeSessionParameters) {
                        println("onSessionUpdated")
                    }

                    override fun onSessionDeleted() {
                        println("onSessionDeleted")
                    }

                    override fun onSessionRestored(session: ConnectycubeSession) {
                        println("onSessionRestored")
                    }

                    override fun onSessionExpired() {
                        println("onSessionExpired")
                    }

                    override fun onProviderSessionExpired(provider: String) {
                        println("onProviderSessionExpired")
                    }
                })
            init(applicationContext)
        }
    }

}