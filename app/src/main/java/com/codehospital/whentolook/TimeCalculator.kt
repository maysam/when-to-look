package com.codehospital.whentolook

import android.content.Context
import android.util.Log
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*



class TimeCalculator(val context: Context) {

    val sharedpreferences = context.applicationContext.getSharedPreferences("abc", Context.MODE_PRIVATE);
    val TAG = "TimeCalculator"

    fun whenToLook(date: Date): Date {
        Log.i(TAG, "receiving $date")
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa")
        val key = dateFormat.format(date) // date.toString()
        var prev = sharedpreferences.getString(key, null)
        Log.i(TAG, "prev= $prev")

        if(prev == null) {
            Log.i(TAG, "prev was null")
            prev = dateFormat.format(Calendar.getInstance().time)
            with(sharedpreferences.edit()){
                putString(key, prev)
                apply()
            }
        }
        val convertedDate = Date()
        return dateFormat.parse(prev)
//        return Date.parse(prev)
//        return date
    }


    fun startOfDay(time: Timestamp): Date {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time.time
        cal.set(Calendar.HOUR_OF_DAY, 0) //set hours to zero
        cal.set(Calendar.MINUTE, 0) // set minutes to zero
        cal.set(Calendar.SECOND, 0) //set seconds to zero
        Log.i("Time", cal.time.toString())
        return cal.time
    }


    fun whenToLook() : Date {
        val date = startOfDay(Timestamp(System.currentTimeMillis()))
        return whenToLook(date)
    }
}