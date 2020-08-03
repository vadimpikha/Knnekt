package blasty.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import blasty.R
import blasty.utils.await
import com.connectycube.users.ConnectycubeUsers
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val (user, _) = ConnectycubeUsers.signIn(null).await()
        }
    }
}