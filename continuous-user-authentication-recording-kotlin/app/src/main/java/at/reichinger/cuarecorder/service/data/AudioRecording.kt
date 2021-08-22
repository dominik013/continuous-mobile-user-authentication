package at.reichinger.cuarecorder.service.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import at.reichinger.cuarecorder.R

/**
 * Data class representing an audio/speech recording
 */
data class AudioRecording(val id: ID, val content: String, val isDone: Boolean) {

    companion object {
        private const val AUDIO_RECORDING_DONE_KEY_PREFIX = "audioRecordingDone"
        private val BASE_PATH: String = Environment.getExternalStorageDirectory().absolutePath

        fun getFilePath(id: ID): String {
            return "$BASE_PATH/$id.acc"
        }
    }

    /**
     * Enum class, where all different recordings are defined
     */
    enum class ID {
        CONTENT_A,
        CONTENT_B
    }

    /**
     * Static class containing methods to save the current status of audio objects
     */
    object Settings {
        /**
         * Returns if the audio recording with the specified touchInputEventId is finished
         *
         * @param id The ID of the audio to check
         * @return True if the audio was already recorded, false otherwise
         */
        fun isAudioRecordingDone(context: Context, id: ID): Boolean {
            return getPrefs(context).getBoolean(
                buildKey(
                    id
                ), false
            )
        }

        /**
         * Set the specified recording as done
         * @param id The ID of the audio to set as done
         */
        @SuppressLint("ApplySharedPref")
        fun setAudioRecordingDone(context: Context, id: ID) {
            getPrefs(context).edit().putBoolean(
                buildKey(id), true
            ).commit()
        }

        /**
         * Returns if all audio tasks were recorded
         * @return If all audio tasks were recorded
         */
        fun allAudioRecordingsDone(context: Context): Boolean {
            enumValues<ID>().forEach {
                if (!isAudioRecordingDone(
                        context,
                        it
                    )
                ) {
                    return false
                }
            }

            return true
        }

        /**
         * Returns the paths on where the audio files were saved to
         * @return A set of String containing the paths of all recorded audio files
         */
        fun getAudioRecordingPaths(): Set<String> {
            val paths = mutableSetOf<String>()

            enumValues<ID>().forEach {
                paths.add(
                    getFilePath(
                        it
                    )
                )
            }

            return paths
        }

        private fun buildKey(id: ID): String {
            return AUDIO_RECORDING_DONE_KEY_PREFIX + id.name
        }

        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.getString(R.string.shared_preferences_audio_key), Context.MODE_PRIVATE)
        }
    }
}