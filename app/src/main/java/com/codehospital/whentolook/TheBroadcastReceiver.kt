package com.codehospital.whentolook

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.app.NotificationManager
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import java.util.*
import android.content.ContentValues.TAG

class TheBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.v("Noti", "onReceive")
        Toast.makeText(context, "Time Up... Now Vibrating !!!",
                Toast.LENGTH_LONG).show();
        if (intent?.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
        }
        val sb = StringBuilder()
        sb.append("Action: ${intent?.getAction()}")
        sb.append("URI: ${intent?.toUri(Intent.URI_INTENT_SCHEME).toString()}")
        val log = sb.toString()
        Log.d(TAG, log)
        Toast.makeText(context, log, Toast.LENGTH_LONG).show()

        object:CountDownTimer(60000,1000){
            override fun onTick(millisUntilFinished: Long) {
               NotificationMan().updateNotification(context ,millisUntilFinished)
            }

            override fun onFinish() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }.start()
    }

}