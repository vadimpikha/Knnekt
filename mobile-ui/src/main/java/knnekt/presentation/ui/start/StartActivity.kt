package knnekt.presentation.ui.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import knnekt.R
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.start.StartViewModel
import knnekt.presentation.ui.main.MainActivity
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

class StartActivity : AppCompatActivity(R.layout.activity_start), DIAware {

    override val di by closestDI()
    private val viewModel: StartViewModel by viewModelInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userLoggedIn.observe(this) { loggedIn ->
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