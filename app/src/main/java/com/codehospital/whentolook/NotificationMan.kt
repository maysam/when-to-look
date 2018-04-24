package com.codehospital.whentolook

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import java.util.*


class NotificationMan {
    fun updateNotification(context: Context?, millisUntilFinished: Long) {
        val id = "id"
        val mNotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var mChannel: NotificationChannel? = mNotificationManager.getNotificationChannel(id)
            if (mChannel == null) {
                mChannel = NotificationChannel(id, "name", NotificationManager.IMPORTANCE_HIGH)
                mChannel.description = "sdf"
                mChannel.enableVibration(false)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mNotificationManager.createNotificationChannel(mChannel)
            }
        }
        val mBuilder = NotificationCompat.Builder(context, id)
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        val date = TimeCalculator(context).whenToLook()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val diff: Long = System.currentTimeMillis() - calendar.timeInMillis + millisUntilFinished
        mBuilder.setContentTitle("Notification Alert, Click Me!" + diff)
        mBuilder.setContentText("Hi, This is Android Notification Detail!" + diff)


        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build())
    }
}