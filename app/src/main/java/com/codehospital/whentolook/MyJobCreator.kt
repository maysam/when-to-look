package com.codehospital.whentolook

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator


class MyJobCreator : JobCreator {
    override fun create(tag: String): Job? {
        when (tag) {
            NotifyJob.TAG -> return NotifyJob()
            else -> return null
        }
    }

}
