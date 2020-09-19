package knnekt.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import knnekt.R
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.start.StartViewModel
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

class MainActivity : AppCompatActivity(), DIAware {

    override val di by closestDI()
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        navController = findNavController(R.id.nav_host_fragment)
    }

    companion object {
        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}