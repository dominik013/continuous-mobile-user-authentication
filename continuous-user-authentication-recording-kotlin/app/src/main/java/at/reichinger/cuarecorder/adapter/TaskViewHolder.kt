package at.reichinger.cuarecorder.adapter

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.service.data.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskViewHolder(private val view: View, private val context: Context, private val listener: TaskClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        listener.onTaskClicked(adapterPosition)
    }

    fun bindTask(task: Task) {
        val color = if (task.isDone) ContextCompat.getColor(context, R.color.colorStatusDone) else ContextCompat.getColor(context, R.color.colorStatusNotDone)

        view.textTaskTitle.text = task.title
        view.imageTaskStatus.setColorFilter(color)
    }
}