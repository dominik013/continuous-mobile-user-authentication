package at.reichinger.cuarecorder.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.ui.MainActivity
import java.util.*


/**
 * Foreground service which is used to capture user data, that should be periodically or
 * automatically captured
 */
class DataRecordingService : Service() {

    companion object {
        var serviceRunning = false

        /* unused */
        private const val ACTION_KEY_EVENT = "at.reichinger.cuarecorder.KEY_ACTION"

        private const val TIMER_PERIOD_PROCESS_INFO: Long = 30 * 1000 // 30 seconds

        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val NOTIFICATION_CHANNEL_ID = "CUA_CHANNEL_ID"
    }

    private lateinit var locationService: LocationService
    private val activeProcessTimer = Timer()
    private val touchInputThread = TouchInputThread(this)


    override fun onCreate() {
        super.onCreate()
        Log.i(DataRecordingService::class.java.simpleName, "Service started")

        locationService = LocationService(this)
    }

    override fun onBind(intent: Intent): IBinder {
        throw NotImplementedError()
    }

    /**
     * Depending on the action, we either start the service or stop it
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null && intent.action != null) {
            when (intent.action) {
                ACTION_START_FOREGROUND_SERVICE -> startService()
                ACTION_STOP_FOREGROUND_SERVICE -> stopService()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService() {
        serviceRunning = true
        locationService.startLocationUpdates()
        /*
        activeProcessTimer.scheduleAtFixedRate(
            ActiveProcessTimerTask(this, locationService), 0, TIMER_PERIOD_PROCESS_INFO
        )*/
        touchInputThread.start()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.channel_name_data_recording),
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.description = getString(R.string.channel_description_data_recording)
            notificationChannel.enableLights(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_baseline_videocam_24px)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(getString(R.string.notification_title_data_recording))
            .setContentText(getString(R.string.notification_text_data_recording))
            .setContentIntent(createMainActivityIntent())

        val notification = notificationBuilder.build()
        startForeground(1, notification)
    }

    private fun stopService() {
        touchInputThread.stopReceivingTouchInput()
        //activeProcessTimer.cancel()
        locationService.stopLocationUpdates()

        stopForeground(true)
        stopSelf()
        serviceRunning = false
    }

    /**
     * Creates and intent to come back to the MainActivity when notification is pressed
     */
    private fun createMainActivityIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
