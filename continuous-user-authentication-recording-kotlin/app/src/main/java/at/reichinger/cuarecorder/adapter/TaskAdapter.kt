package at.reichinger.cuarecorder.adapter

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.extension.inflate
import at.reichinger.cuarecorder.service.data.Task
import at.reichinger.cuarecorder.ui.*

class TaskAdapter(
    private val context: Context,
    private val taskList: MutableList<Task> = mutableListOf()
) : RecyclerView.Adapter<TaskViewHolder>(), TaskClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TaskViewHolder {
        val inflatedView = parent.inflate(R.layout.item_task, false)
        return TaskViewHolder(inflatedView, context, this)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bindTask(task)
    }

    override fun onTaskClicked(position: Int) {
        if (taskList[position].isDone) {
            //return
        }

        when (taskList[position].id) {
            Task.ID.RECORD_SPEECH -> {
                context.startActivity(Intent(context, RecordSpeechActivity::class.java))
            }
            Task.ID.RECORD_FACE_IMAGES -> {
                context.startActivity(Intent(context, RecordFaceImagesActivity::class.java))
            }
            Task.ID.RECORD_KEYBOARD_INPUT -> {
                // TODO: Input will now be rather done in-app
                context.startActivity(Intent(context, KeyboardRecordingActivity::class.java))
            }
            Task.ID.GRANT_ROOT_ACCESS -> {
                context.startActivity(Intent(context, GrantRootActivity::class.java))
            }
            Task.ID.ENOUGH_SERVICE_DATA_COLLECTED -> {
                context.startActivity(Intent(context, CheckServiceDataActivity::class.java))
            }
        }
    }

    fun updateTasks(taskList: List<Task>) {
        this.taskList.clear()
        this.taskList.addAll(taskList)
        notifyDataSetChanged()
    }

}