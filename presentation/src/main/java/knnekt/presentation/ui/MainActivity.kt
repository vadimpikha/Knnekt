package knnekt.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import knnekt.R

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