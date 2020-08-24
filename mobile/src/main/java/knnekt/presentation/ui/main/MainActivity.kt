package knnekt.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.MyToolbarOnDestinationChangedListener
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.TransitionResolver
import knnekt.R
import knnekt.databinding.ActivityMainBinding
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.util.viewBinding
import knnekt.shared.data.connection.ChatConnectionManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModel: MainViewModel by viewModelInstance()
    private val connectionManager: ChatConnectionManager by instance()
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            setContentView(root)
            navController = findNavController(R.id.nav_host_fragment)
        }
        connectionManager.initialize()
    }

    companion object {
        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}

fun Toolbar.setupWithNavController(
    navController: NavController,
    configuration: AppBarConfiguration = AppBarConfiguration(navController.graph)
) {
    navController.addOnDestinationChangedListener(
        MyToolbarOnDestinationChangedListener(this, configuration) { dest ->
            dest.id == R.id.chatFragment
        }
    )
    this.setNavigationOnClickListener {
        NavigationUI.navigateUp(navController, configuration)
    }
}