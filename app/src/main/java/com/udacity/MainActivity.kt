package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var fileName: String
    private lateinit var fileDescription: String


    private var urlId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // call download function when user click on custom button
        custom_button.setOnClickListener {
            download()
        }

     //Initialize the notification manager
        notificationManager = ContextCompat.getSystemService(
                applicationContext,
        NotificationManager::class.java
        ) as NotificationManager

        // create the channel
        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_description)
        )
    }

    /**
     * On radio button clicked select the right file name and url to download
     * */
    fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {

            //check if the radio button is checked
            val checked = view.isChecked


            // check which radio button was clicked
            custom_button.buttonState = ButtonState.Clicked

            when (view.id) {
                R.id.glide_radio_button ->
                    if (checked) {
                        // change the url
                        fileName = getString(R.string.glide_text)
                        fileDescription = getString(R.string.glide_repository)
                        urlId = GLIDE_URL
                    }

                R.id.loadApp_radio_button ->
                    if (checked) {
                        fileName = getString(R.string.load_app_text)
                        fileDescription = getString(R.string.load_app_repository)
                        urlId = LOAD_APP_URL
                    }

                R.id.retrofit_radio_button -> {
                    if (checked) {
                        fileName = getString(R.string.retrofit_text)
                        fileDescription = getString(R.string.retrofit_repository)
                        urlId = RETROFIT_URL
                    }
                }

                else -> {
                    Toast.makeText(this, "Please select a file to down", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     *  Broad cast receiver and send notification
     * */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

               if (id == downloadID) {

                    custom_button.buttonState = ButtonState.Completed

                    notificationManager.sendNotification(
                        fileName,
                        applicationContext,
                        getString(R.string.success))
                }

                else {
                    custom_button.buttonState = ButtonState.Completed

                    notificationManager.sendNotification(
                        fileName,
                        applicationContext,
                        getString(R.string.fail))
                }
            }
        }

    /**
     *   Down load function
     * */
    private fun download() {

        if (urlId== "") {
            Toast.makeText(this, "Please select the file to download", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "downloading", Toast.LENGTH_LONG).show()
//            custom_button.buttonState = ButtonState.Loading

            val request =
                DownloadManager.Request(Uri.parse(urlId))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager =
                getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.

            // change the state of the button while downloading
            custom_button.buttonState = ButtonState.Loading
        }
    }

    /**
     * Create channel
     * */
    private fun createChannel(channelId: String, channelName: String) {

        // create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.download_notification_channel_description)


            // Get an instance of notification manager
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )

            // create the notification channel
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    /**
     * Get all the url for link to download
     * */
    companion object {
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"
    }

}
