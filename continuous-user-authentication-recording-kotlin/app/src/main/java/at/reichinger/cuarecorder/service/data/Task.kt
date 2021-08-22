package at.reichinger.cuarecorder.service.data

import android.content.Context
import android.content.SharedPreferences
import at.reichinger.cuarecorder.R

/**
 * Class representing a Task that has to be fulfilled
 */
data class Task(val id: ID, val title: String, val isDone: Boolean) {

    companion object {
        private const val TASK_DONE_KEY_PREFIX = "taskDone"
    }

    /**
     * Inner enum class with ids for all possible tasks
     */
    enum class ID {
        RECORD_SPEECH,
        RECORD_FACE_IMAGES,
        RECORD_KEYBOARD_INPUT,
        GRANT_ROOT_ACCESS,
        ENOUGH_SERVICE_DATA_COLLECTED
    }

    /**
     * Static class containing methods to save the current status of tasks
     */
    object Settings {
        fun isTaskDone(context: Context, id: ID): Boolean {
            return getPrefs(context)
                .getBoolean(buildKey(id), false)
        }

        /**
         * Sets a task as done
         * @param id The touchInputEventId of the task to be set as done
         */
        fun setTaskDone(context: Context, id: ID) {
            getPrefs(context).edit().putBoolean(
                buildKey(id), true
            ).apply()
        }

        /**
         * Checks if all tasks were done
         * @return True if all tasks were done
         */
        fun allTasksDone(context: Context): Boolean {
            enumValues<ID>().forEach {
                if (!isTaskDone(context, it)) {
                    return false
                }
            }

            return true
        }

        private fun buildKey(id: ID): String {
            return TASK_DONE_KEY_PREFIX + id.name
        }

        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.getString(R.string.shared_preferences_tasks_key), Context.MODE_PRIVATE)
        }
    }
}