package at.reichinger.cuarecorder.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.persistence.CUADatabase
import at.reichinger.cuarecorder.persistence.KeyDao
import at.reichinger.cuarecorder.persistence.KeyPress
import at.reichinger.cuarecorder.service.data.Task
import kotlinx.android.synthetic.main.activity_keyboard_recording.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class KeyboardRecordingActivity : AppCompatActivity(), TextWatcher {

    companion object {
        private const val CLEAR_KEY_TEXT = '\b'
    }

    private val keyPressEventList = mutableListOf<KeyPress>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard_recording)
        keyboardInput.addTextChangedListener(this)
    }


    fun onDoneRecordingClick(view: View) {
        doneRecording.isEnabled = false
        GlobalScope.launch {
            val keyRecordings =
                CUADatabase(this@KeyboardRecordingActivity).keyDao().getNumberOfRecordings()

            if (keyRecordings >= KeyDao.NECESSARY_RECORDINGS) {
                Task.Settings.setTaskDone(
                    this@KeyboardRecordingActivity,
                    Task.ID.RECORD_KEYBOARD_INPUT
                )
            }

            CUADatabase(this@KeyboardRecordingActivity).keyDao().insertAll(keyPressEventList)
            finish()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        processKeyEvent(s, before, count)
    }


    override fun afterTextChanged(s: Editable?) {
    }

    private fun processKeyEvent(text: CharSequence?, before: Int, count: Int) {
        val timestamp = System.currentTimeMillis()
        if (text == null) return
        if (count == -1) {
            processClearKeyEvent(timestamp)
            return
        }
        if (before < count) {
            val character = text[text.length -1]
            val keyEvent = KeyPress(0, character, timestamp)
            keyPressEventList.add(keyEvent)
        }
    }

    private fun processClearKeyEvent(timestamp: Long) {
        val keyEvent = KeyPress(0, CLEAR_KEY_TEXT, timestamp)
        keyPressEventList.add(keyEvent)
    }
}
