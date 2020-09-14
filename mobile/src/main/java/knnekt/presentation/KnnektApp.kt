package knnekt.presentation

import android.app.Application
import com.connectycube.auth.session.ConnectycubeSessionManager
import com.connectycube.auth.session.ConnectycubeSettings
import com.connectycube.core.LogLevel
import knnekt.BuildConfig
import knnekt.R
import knnekt.presentation.di.PresentationLayerDi
import knnekt.shared.data.di.DataLayerDi
import knnekt.shared.domain.di.DomainLayerDi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import timber.log.Timber

class KnnektApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@KnnektApp))
        with(PresentationLayerDi) {
            import(viewModelModule)
            import(uiModule)
        }
        with(DomainLayerDi) {
            import(useCaseModule)
        }
        with(DataLayerDi) {
            importAll(repositoryModule, dataSourceModule)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initConnectyCubeSDK()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
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