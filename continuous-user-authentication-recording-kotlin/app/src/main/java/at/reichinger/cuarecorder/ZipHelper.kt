package at.reichinger.cuarecorder

import android.content.Context
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import at.reichinger.cuarecorder.persistence.CUADatabase
import at.reichinger.cuarecorder.service.data.AudioRecording
import at.reichinger.cuarecorder.service.data.FaceImageRecording
import kotlinx.coroutines.*
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipHelper {
    private const val LOG_TAG = "ZipHelper"
    private const val BUFFER_SIZE = 32 * 1024 // 32kB
    private const val DESTINATION_FILE_NAME = "cua-recorder-data.zip"
    private val DESTINATION_FILE_DIR =
        "${Environment.getExternalStorageDirectory().absolutePath}/${BuildConfig.APPLICATION_ID}"

    /**
     * Gathers the paths of all the recorded data, zip's the data and stores it in the external
     * storage
     */
    fun zipFilesAndSend(context: Context) {
        val faceImagePaths = FaceImageRecording.Settings.getRecordingPaths(context)
        val audioRecordings = AudioRecording.Settings.getAudioRecordingPaths()
        val databaseFiles = CUADatabase.getDatabaseFiles(context)

        val allPaths = mutableListOf<String>()
        //allPaths.addAll(faceImagePaths)
        //allPaths.addAll(audioRecordings)
        allPaths.addAll(databaseFiles)

        GlobalScope.launch {
            zip(allPaths, DESTINATION_FILE_DIR, DESTINATION_FILE_NAME)
        }
        Toast.makeText(context, "Done and saved at: $DESTINATION_FILE_DIR", Toast.LENGTH_LONG).show()
    }

    /**
     * Will zip all files provided by the first parameter and save it to the location
     * specified in the second parameter. If the output directory does not exist, it tries to create
     * it
     *
     * @param outputDir The directory where the file should be stored
     * @param zipFileName The name of the zipped file (e.g. test_file.zip)
     */
    private fun zip(files: Collection<String>, outputDir: String, zipFileName: String) {
        val dir = File(outputDir)
        if (!dir.exists()) {
            val success = dir.mkdirs()

            if (!success) {
                Log.e(LOG_TAG, "Could not create output directory")
                return
            }
        }

        var out: ZipOutputStream? = null

        try {
            out = ZipOutputStream(
                BufferedOutputStream(
                    FileOutputStream("$outputDir/$zipFileName")
                )
            )

            for (file in files) {
                val f = File(file)
                if (!f.exists()) {
                    Log.e(LOG_TAG, "Compress: $f does not exist")
                    continue
                }

                Log.i(LOG_TAG, "Compress Adding: $file")
                val bufferedInputStream = BufferedInputStream(
                    FileInputStream(file),
                    BUFFER_SIZE
                )

                val entry = ZipEntry(file.substring(file.lastIndexOf("/") + 1))
                out.putNextEntry(entry)

                val buffer = ByteArray(BUFFER_SIZE)

                while (true) {
                    val byteCount = bufferedInputStream.read(buffer)
                    if (byteCount < 0) break
                    out.write(buffer, 0, byteCount)
                }

                bufferedInputStream.close()
            }
            Log.i(LOG_TAG, "All files successfully zipped and stored in $outputDir/$zipFileName.")
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Exception occurred when compressing files: ${e.message}")
            e.printStackTrace()
        } finally {
            out?.close()
        }
    }
}