package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.udacity.util.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var fileName : String
    private lateinit var status : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        // Initialize Notification Manager
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager


        // fine name and download status
        fileName = intent.getStringExtra("fileName").toString()
        status = intent.getStringExtra("status").toString()

        file_name_answer.text = fileName
        status_text_answer.text = status

        if (status == "Fail") {
            status_text_answer.setTextColor(Color.RED)
        } else {
            status_text_answer.setTextColor(Color.BLACK)
        }

        /**
         * Finish the detail activity
         * */
        ok_button.setOnClickListener {
            finish()
        }
        // cancel notification
        notificationManager.cancelNotifications()

    }

}
