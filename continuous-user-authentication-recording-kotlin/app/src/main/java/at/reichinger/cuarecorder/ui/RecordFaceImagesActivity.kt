package at.reichinger.cuarecorder.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.service.data.FaceImageRecording
import at.reichinger.cuarecorder.service.data.Task
import kotlinx.android.synthetic.main.activity_record_face_images.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordFaceImagesActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
    }

    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_face_images)
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val photoPath = currentPhotoPath

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK && photoPath != null) {
            FaceImageRecording.Settings.addOneRecording(this, photoPath)
            updateUI()
            if (FaceImageRecording.Settings.allRecordingsDone(this)) {
                Task.Settings.setTaskDone(
                    this,
                    Task.ID.RECORD_FACE_IMAGES
                )
                finish()
            }
        }
    }

    fun onTakePictureClick(view: View) {
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "at.reichinger.cuarecorder.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(
                        takePictureIntent,
                        REQUEST_TAKE_PHOTO
                    )
                }
            }
        }
    }

    private fun updateUI() {
        textRecordFaceAmount.text = String.format(getString(R.string.record_face_images_amount_taken),
            FaceImageRecording.Settings.getNumberOfRecordingsDone(this),
            FaceImageRecording.NUMBER_OF_RECORDINGS
        )
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val image = File.createTempFile(
                "JPEG_${System.currentTimeMillis()}_",
                ".jpg",
                storageDir
        )
        currentPhotoPath = image.absolutePath

        return image
    }

}
