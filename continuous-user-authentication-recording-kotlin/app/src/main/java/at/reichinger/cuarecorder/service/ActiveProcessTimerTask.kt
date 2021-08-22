package at.reichinger.cuarecorder.service

import android.app.KeyguardManager
import android.content.Context
import android.os.PowerManager
import at.reichinger.cuarecorder.persistence.CUADatabase
import at.reichinger.cuarecorder.persistence.ProcessUsage
import com.topjohnwu.superuser.Shell
import java.util.*


/**
 * This TimerTask is obtaining the currently active process using a root shell in the run method
 * and then saves a generated ProcessUsage object into the database
 * @see ProcessUsage
 */
class ActiveProcessTimerTask(
    private val context: Context,
    private val locationService: LocationService
) : TimerTask() {

    companion object {
        /**
         * Dumps the "window windows" system information, grep's for the line containing
         * "mFocusedApp" and then cut's out the application package (e.g. com.example.application)
         */
        private const val GET_FOREGROUND_PROCESS =
            "dumpsys window windows | grep -E 'mFocusedApp'| cut -d / -f 1 | cut -d \" \" -f 7"

        private const val PROCESS_SCREEN_OFF = "screen_off"
        private const val PROCESS_SCREEN_LOCKED = "screen_locked"
    }

    override fun run() {
        var process = PROCESS_SCREEN_OFF
        val timeStamp = System.currentTimeMillis()

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!powerManager.isInteractive) {
            // Device is off
            process = PROCESS_SCREEN_OFF
        } else if (powerManager.isInteractive && keyguardManager.isDeviceLocked) {
            // Device is on and locked
            process = PROCESS_SCREEN_LOCKED
        } else if (powerManager.isInteractive && !keyguardManager.isDeviceLocked) {
            // Device is on and unlocked
            val result = Shell.su(GET_FOREGROUND_PROCESS).exec()
            if (result.isSuccess && result.out.size > 0) {
                process = result.out[0]
            }
        }

        if (locationService.locationUpdatesEnabled()) {
            val locations = locationService.getCurrentLocations()
            if (locations != null && locations.isNotEmpty()) {
                val loc = locations[0]
                val processUsage = ProcessUsage(0, process, loc.longitude, loc.latitude, timeStamp)
                CUADatabase(context).processDao().insertAll(processUsage)
            }
        }
    }

}