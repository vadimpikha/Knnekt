package knnekt.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import knnekt.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
    }

    companion object {
        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}