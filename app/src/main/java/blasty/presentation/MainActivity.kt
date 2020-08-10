package blasty.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import blasty.R
import blasty.presentation.start.StartActivity
import blasty.utils.await
import blasty.utils.onClick
import com.connectycube.users.ConnectycubeUsers
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fab.onClick {
//            FirebaseAuth.getInstance().signOut()
//            startActivity(StartActivity.intent(this@MainActivity))
//            finish()
//        }
    }

    companion object {
        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}