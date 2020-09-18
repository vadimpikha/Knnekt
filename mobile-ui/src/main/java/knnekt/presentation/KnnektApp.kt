package knnekt.presentation

import android.app.Application
import com.connectycube.auth.session.ConnectycubeSessionManager
import com.connectycube.auth.session.ConnectycubeSettings
import com.connectycube.core.LogLevel
import knnekt.data.di.DataLayerDI
import knnekt.domain.di.DomainLayerDI
import knnekt.presentation.di.PresentationLayerDI
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import knnekt.R

class KnnektApp : Application(), DIAware {

    override val di by DI.lazy {
        import(androidXModule(this@KnnektApp))
        with(PresentationLayerDI) {
            import(viewModelModule)
        }
        with(DomainLayerDI) {
            import(useCaseModule)
        }
        with(DataLayerDI) {
            importAll(repositoryModule, dataSourceModule, mappersModule)
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
            logLevel =  LogLevel.NOTHING
        }

        ConnectycubeSessionManager.getInstance().init(applicationContext)
    }

}