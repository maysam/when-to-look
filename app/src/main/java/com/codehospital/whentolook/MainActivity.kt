package com.codehospital.whentolook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.evernote.android.job.JobManager



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JobManager.create(this).addJobCreator(MyJobCreator())
        setContentView(R.layout.activity_main)
    }
}
