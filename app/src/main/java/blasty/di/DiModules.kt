package blasty.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import blasty.R
import blasty.data.repository.ConnectycubeServiceImpl
import blasty.domain.repository.ConnectycubeService
import blasty.domain.usecase.CheckUserSignedInUseCase
import blasty.presentation.start.LoginViewModel
import blasty.presentation.start.StartViewModel
import com.connectycube.auth.session.ConnectycubeSessionManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object DiModules {

    val all: List<Kodein.Module> get() = listOf(viewModelModule, repositoryModule, useCaseModule)

    val viewModelModule = Kodein.Module("viewModelModule") {
        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(this) }
        bindViewModel<LoginViewModel>() with provider { LoginViewModel() }
        bindViewModel<StartViewModel>() with provider { StartViewModel(instance()) }
    }

    val repositoryModule = Kodein.Module("repositoryModule") {
        bind<ConnectycubeService>() with provider {
            ConnectycubeServiceImpl(
                ConnectycubeSessionManager.getInstance(),
                instance<Context>().getString(R.string.firebase_project_id)
            )
        }
    }

    val useCaseModule = Kodein.Module("useCaseModule") {
        bind<CheckUserSignedInUseCase>() with provider { CheckUserSignedInUseCase(instance()) }
    }

}



