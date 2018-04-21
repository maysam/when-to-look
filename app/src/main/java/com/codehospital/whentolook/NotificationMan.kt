package com.codehospital.whentolook

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import java.util.*
import android.R.attr.name
import android.app.Notification
import android.app.NotificationChannel
import android.os.Build


class NotificationMan {
    fun updateNotification(context: Context?, millisUntilFinished: Long) {

        val mNotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel("id", "name", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
        }
        val mBuilder = NotificationCompat.Builder(context!!, "id")
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        val date = TimeCalculator(context).whenToLook()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val diff:Long = calendar.timeInMillis - System.currentTimeMillis()+millisUntilFinished
        mBuilder.setContentTitle("Notification Alert, Click Me!"+diff)
        mBuilder.setContentText("Hi, This is Android Notification Detail!"+diff)


// notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build())

    }
}