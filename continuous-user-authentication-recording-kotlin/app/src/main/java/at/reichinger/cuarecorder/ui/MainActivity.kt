package at.reichinger.cuarecorder.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.ZipHelper
import at.reichinger.cuarecorder.service.DataRecordingService
import at.reichinger.cuarecorder.service.data.Task
import com.topjohnwu.superuser.Shell
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_ALL_PERMISSIONS = 1
        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Shell.Config.setFlags(Shell.FLAG_REDIRECT_STDERR)
        Shell.Config.verboseLogging(false)
        Shell.Config.setTimeout(10)


        setContentView(R.layout.activity_main)
        btnStartForegroundService.isEnabled = !DataRecordingService.serviceRunning
        btnStopForegroundService.isEnabled = DataRecordingService.serviceRunning

        handlePermissions(this)

        btnStartForegroundService.setOnClickListener {
            btnStartForegroundService.isEnabled = false
            val intent = Intent(this@MainActivity, DataRecordingService::class.java)
            intent.action = DataRecordingService.ACTION_START_FOREGROUND_SERVICE
            startService(intent)
            btnStopForegroundService.isEnabled = true
        }

        btnStopForegroundService.setOnClickListener {
            btnStopForegroundService.isEnabled = false
            val intent = Intent(this@MainActivity, DataRecordingService::class.java)
            intent.action = DataRecordingService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
            btnStartForegroundService.isEnabled = true
        }

        btnShowTasks.setOnClickListener {
            val intent = Intent(this@MainActivity, TaskListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun handlePermissions(activity: Activity) {
        if (!hasPermissions(
                activity,
                PERMISSIONS
            )
        ) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS,
                REQUEST_ALL_PERMISSIONS
            )
        }
    }

    /**
     * Checks if all tasks are completed
     * If they are completed compresses the generated data and saves it to a zip file
     */
    fun onCompressDataClick(view: View) {
        /*if (Task.Settings.allTasksDone(this)) {
            Toast.makeText(
                this,
                getString(R.string.notification_not_all_tasks_done),
                Toast.LENGTH_LONG
            ).show()
            return
        }*/

        ZipHelper.zipFilesAndSend(this)
    }
}
