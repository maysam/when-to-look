package com.codehospital.whentolook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.widget.TextView
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, TheBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                this.applicationContext, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,  System.currentTimeMillis()+10, 1, pendingIntent)
        val context = applicationContext
        startCounting(context)
        Toast.makeText(this, "Alarm set in 1 second", Toast.LENGTH_LONG).show()
    }

    fun startCounting(context: Context): Unit {
        Handler().postDelayed(Runnable {
            object: CountDownTimer((60000+Random().nextInt(100000)).toLong(),50){
                override fun onTick(millisUntilFinished: Long) {
                    val secondsUntilFinished = millisUntilFinished/1000
                    NotificationMan().updateNotification(context ,secondsUntilFinished)
                    val a: TextView = findViewById(R.id.whentolooktextbox)
                    a.text = "${secondsUntilFinished.toString()}:${millisUntilFinished % 1000}"
                }

                override fun onFinish() {
                    startCounting(context)
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                }
            }.start()
        },11)

    }
}
