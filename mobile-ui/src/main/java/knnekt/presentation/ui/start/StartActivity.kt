package knnekt.presentation.ui.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import knnekt.R
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

class StartActivity : AppCompatActivity(R.layout.activity_start), DIAware {

    override val di by closestDI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun intent(context: Context) = Intent(context, StartActivity::class.java)
    }

}