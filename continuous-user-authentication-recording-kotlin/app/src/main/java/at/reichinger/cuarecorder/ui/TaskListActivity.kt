package at.reichinger.cuarecorder.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.adapter.TaskAdapter
import at.reichinger.cuarecorder.service.data.Task
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = TaskAdapter(this)
        recyclerTasks.adapter = adapter
        recyclerTasks.layoutManager = LinearLayoutManager(this)
        recyclerTasks.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )
    }

    override fun onResume() {
        super.onResume()
        adapter.updateTasks(getTasks())
        Toast.makeText(
            this,
            "All tasks done: " + Task.Settings.allTasksDone(this).toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getTasks(): MutableList<Task> {
        return mutableListOf(
            Task(
                Task.ID.RECORD_SPEECH,
                getString(R.string.task_title_record_speech),
                Task.Settings.isTaskDone(
                    this,
                    Task.ID.RECORD_SPEECH
                )
            ),
            Task(
                Task.ID.RECORD_FACE_IMAGES,
                getString(R.string.task_title_record_face_images),
                Task.Settings.isTaskDone(
                    this,
                    Task.ID.RECORD_FACE_IMAGES
                )
            ),
            Task(
                Task.ID.RECORD_KEYBOARD_INPUT,
                getString(R.string.task_title_enable_recording_keyboard),
                Task.Settings.isTaskDone(
                    this,
                    Task.ID.RECORD_KEYBOARD_INPUT
                )
            ),
            Task(
                Task.ID.GRANT_ROOT_ACCESS,
                getString(R.string.task_title_grant_root_access),
                Task.Settings.isTaskDone(
                    this,
                    Task.ID.GRANT_ROOT_ACCESS
                )
            ),
            Task(
                Task.ID.ENOUGH_SERVICE_DATA_COLLECTED,
                getString(R.string.task_title_enough_service_data_collected),
                Task.Settings.isTaskDone(
                    this,
                    Task.ID.ENOUGH_SERVICE_DATA_COLLECTED
                )
            )
        )
    }
}
