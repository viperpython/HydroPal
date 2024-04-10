package com.anudeep.hydropal

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
class MyJobService : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        Log.d("MyJobService", "Job started")
        // If your job is offloaded to a separate thread, return true. Otherwise, return false.
        val notificationManager = NotificationManager(applicationContext)
        notificationManager.showNotification("HydroPal Reminder", notiStrings.waterReminderMessages.random())

        return false
    }


    override fun onStopJob(params: JobParameters): Boolean {
        Log.d("MyJobService", "Job stopped")
        // If your job needs to be rescheduled, return true. Otherwise, return false.
        return true
    }
}