package at.reichinger.cuarecorder.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.persistence.CUADatabase
import at.reichinger.cuarecorder.persistence.KeyDao
import at.reichinger.cuarecorder.persistence.ProcessDao
import at.reichinger.cuarecorder.persistence.TouchDao
import at.reichinger.cuarecorder.service.data.Task
import kotlinx.android.synthetic.main.activity_check_service_data.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckServiceDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_service_data)
        checkIfEnoughCaptured()
    }

    fun onCheckServiceDataClick(view: View) {
        checkIfEnoughCaptured()
    }

    private fun checkIfEnoughCaptured() {
        GlobalScope.launch {
            val keyRecordings =
                CUADatabase(this@CheckServiceDataActivity).keyDao().getNumberOfRecordings()
            val processRecordings =
                CUADatabase(this@CheckServiceDataActivity).processDao().getNumberOfRecordings()
            val touchRecordings =
                CUADatabase(this@CheckServiceDataActivity).touchDao().getNumberOfRecordings()

            runOnUiThread {
                textAmountKeyInput.text = String.format(
                    getString(R.string.check_service_data_amount_key_input),
                    keyRecordings,
                    KeyDao.NECESSARY_RECORDINGS
                )
                textAmountProcessInfo.text = String.format(
                    getString(R.string.check_service_data_amount_process_info),
                    processRecordings,
                    ProcessDao.NECESSARY_RECORDINGS
                )
                textAmountTouchInput.text = String.format(
                    getString(R.string.check_service_data_amount_touch_input),
                    touchRecordings,
                    TouchDao.NECESSARY_RECORDINGS
                )

                if (keyRecordings >= KeyDao.NECESSARY_RECORDINGS &&
                    processRecordings >= ProcessDao.NECESSARY_RECORDINGS &&
                    touchRecordings >= TouchDao.NECESSARY_RECORDINGS
                ) {
                    Task.Settings.setTaskDone(
                        this@CheckServiceDataActivity,
                        Task.ID.ENOUGH_SERVICE_DATA_COLLECTED
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@CheckServiceDataActivity,
                        getString(R.string.check_service_data_not_enough),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
