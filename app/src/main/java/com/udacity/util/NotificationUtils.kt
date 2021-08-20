package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

// Notification Id.
private val NOTIFICATION_ID = 0

/**
 * Extension function to send messages
 */
fun NotificationManager.sendNotification(
    fileName: String,
    applicationContext: Context,
    status: String) {
    // Create intent
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        .putExtra("fileName", fileName)
        .putExtra("status", status)

    // Create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Add style
    val downloadImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_assistant_black_24dp
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(null)

    // Get an instance of NotificationCompat.Builder
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )
            // Set title, text and icon
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(fileName)
            // Set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

            // Add action to open the details activity
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_action),
            contentPendingIntent
        )

            // Set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Call all notify
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancel all notifications
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}