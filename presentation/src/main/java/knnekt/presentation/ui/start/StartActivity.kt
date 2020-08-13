package knnekt.presentation.ui.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import knnekt.R
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.ui.MainActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class StartActivity : AppCompatActivity(R.layout.activity_start), KodeinAware {

    override val kodein by closestKodein()
    private val startViewModel: StartViewModel by viewModelInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startViewModel.userLoggedIn.observeEvent(this) { loggedIn ->
            if (loggedIn) {
                startActivity(MainActivity.intent(this))
                finish()
            }
        }
    }

    companion object {
        fun intent(context: Context) = Intent(context, StartActivity::class.java)
    }

}