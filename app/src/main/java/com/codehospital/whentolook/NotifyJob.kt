package com.codehospital.whentolook

import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest



class NotifyJob : Job() {
    override fun onRunJob(params: Params): Result {
        return Job.Result.SUCCESS
    }

    fun scheduleJob() {
        JobRequest.Builder(NotifyJob.TAG)
                .setExecutionWindow(30_000L, 40_000L)
                .build()
                .schedule()
    }

    companion object {
        val TAG = "NotifyJobTag"
    }

}
