package at.reichinger.cuarecorder.ui

import android.media.MediaRecorder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.service.data.AudioRecording
import at.reichinger.cuarecorder.service.data.Task
import kotlinx.android.synthetic.main.activity_record_speech.*

class RecordSpeechActivity : AppCompatActivity() {

    private var isRecording = false
    private var mediaRecorder: MediaRecorder? = null
    private var currentAudioRecording: AudioRecording.ID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_speech)
        nextAudioTask()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
    }

    fun onRecordClick(view: View) {
        isRecording = !isRecording
        btnRecord.setImageResource(getRecordingImage(isRecording))

        if (isRecording) {
            mediaRecorder?.start()
        } else {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            currentAudioRecording?.let {
                AudioRecording.Settings.setAudioRecordingDone(this, it)
            }
            Toast.makeText(this, "Going to next task", Toast.LENGTH_SHORT).show()
            nextAudioTask()
        }
    }

    private fun nextAudioTask() {
        btnRecord.isEnabled = false

        if (AudioRecording.Settings.allAudioRecordingsDone(this)) {
            Task.Settings.setTaskDone(
                this,
                Task.ID.RECORD_SPEECH
            )
            finish()
        }

        // Find next recording to do
        val recordings = getAudioRecordings()
        for (i in 0 until recordings.size) {
            if (!recordings[i].isDone) {
                // Setup this audio
                currentAudioRecording = recordings[i].id
                textRecordTaskContent.text = recordings[i].content
                setupMediaRecorder()
                mediaRecorder?.setOutputFile(
                    AudioRecording.getFilePath(
                        recordings[i].id
                    )
                )
                break
            }
        }

        mediaRecorder?.prepare()
        btnRecord.isEnabled = true
    }

    private fun getRecordingImage(isRecording: Boolean): Int {
        if (isRecording) {
            return R.drawable.ic_baseline_stop_48px
        }

        return R.drawable.ic_baseline_mic_48px
    }

    private fun setupMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }

    private fun getAudioRecordings(): MutableList<AudioRecording> {
        return mutableListOf(
            AudioRecording(
                AudioRecording.ID.CONTENT_A,
                getString(R.string.record_speech_text_a),
                AudioRecording.Settings.isAudioRecordingDone(
                    this,
                    AudioRecording.ID.CONTENT_A
                )
            ),
            AudioRecording(
                AudioRecording.ID.CONTENT_B,
                getString(R.string.record_speech_text_b),
                AudioRecording.Settings.isAudioRecordingDone(
                    this,
                    AudioRecording.ID.CONTENT_B
                )
            )
        )
    }
}
