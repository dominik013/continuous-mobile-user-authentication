package at.reichinger.cuarecorder.adapter

interface TaskClickListener {
    /**
     * Gets called when a task is clicked in the adapter
     * @param position The position of the task that was clicked
     */
    fun onTaskClicked(position: Int)
}