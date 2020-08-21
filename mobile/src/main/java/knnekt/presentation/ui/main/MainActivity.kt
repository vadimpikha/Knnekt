package knnekt.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.connectycube.chat.ConnectycubeChatService
import knnekt.R
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.util.configureDecorView
import knnekt.shared.data.connection.ChatConnectionManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.activity_main), KodeinAware {

    override val kodein by closestKodein()
    private val viewModel: MainViewModel by viewModelInstance()
    private val connectionManager: ChatConnectionManager by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.ping()
        connectionManager.initialize()
    }

    companion object {
        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}