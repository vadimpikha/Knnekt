package blasty.presentation.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import blasty.R
import blasty.di.viewModelInstance
import blasty.lifecycle.observeEvent
import blasty.presentation.MainActivity
import blasty.presentation.entity.UserStatus
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class StartActivity : AppCompatActivity(R.layout.activity_start), KodeinAware {

    override val kodein by closestKodein()
    private val startViewModel: StartViewModel by viewModelInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startViewModel.userStatus.observeEvent(this) { status ->
            when (status) {
                UserStatus.SIGNED_IN -> {
                    startActivity(MainActivity.intent(this))
                    finish()
                }
                UserStatus.SIGNED_OUT -> {
                    //stay here
                }
                UserStatus.WITHOUT_INFO -> TODO()
            }
        }
    }

}