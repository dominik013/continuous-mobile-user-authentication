package at.reichinger.cuarecorder.service.data

import android.content.Context
import android.content.SharedPreferences
import at.reichinger.cuarecorder.R

/**
 * Data class representing an face image recording
 */
data class FaceImageRecording(val content: String, val isDone: Boolean) {

    companion object {
        const val NUMBER_OF_RECORDINGS = 5
        private const val KEY_FACE_IMAGES_RECORDED = "faceImagesRecorded"
        private const val KEY_FACE_IMAGES_PATHS = "faceImagesPaths"
    }

    /**
     * Static class containing methods to save the current status of face image objects
     */
    object Settings {

        /**
         * Adds one recording to the recorded images set
         * @param path The path where the image was saved to
         */
        fun addOneRecording(context: Context, path: String) {
            val recordedImagesPaths =
                getRecordingPaths(
                    context
                )
            val recordedImagesCount = recordedImagesPaths.size
            if (recordedImagesCount >= NUMBER_OF_RECORDINGS) return
            val newRecordings = recordedImagesPaths.toMutableSet()
            val successfullyAdded = newRecordings.add(path)
            if (successfullyAdded) {
                getPrefs(context)
                    .edit().putInt(KEY_FACE_IMAGES_RECORDED, recordedImagesCount + 1).apply()
                getPrefs(context)
                    .edit().putStringSet(KEY_FACE_IMAGES_PATHS, newRecordings).apply()
            }
        }

        /**
         * Checks if all recordings were done
         * @return True if all face images were captured
         */
        fun allRecordingsDone(context: Context): Boolean {
            return getNumberOfRecordingsDone(
                context
            ) >= NUMBER_OF_RECORDINGS
        }

        /**
         * Get all paths of the face images
         * @return A set of strings containing all paths of the saved face images
         */
        fun getRecordingPaths(context: Context): Set<String> {
            val prefs = getPrefs(
                context
            ).getStringSet(KEY_FACE_IMAGES_PATHS, mutableSetOf()) ?: mutableSetOf()
            return prefs.toSet()
        }

        /**
         * Returns number of recorded face images
         * @return Number of recorded face images
         */
        fun getNumberOfRecordingsDone(context: Context): Int {
            return getRecordingPaths(
                context
            ).size
        }

        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                context.getString(R.string.shared_preferences_face_images_key),
                Context.MODE_PRIVATE
            )
        }
    }
}