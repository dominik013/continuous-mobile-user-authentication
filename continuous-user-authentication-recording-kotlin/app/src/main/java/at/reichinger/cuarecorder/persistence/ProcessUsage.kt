package at.reichinger.cuarecorder.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class used to persist data about process usage in the database
 * This class is marked with Room annotations to make the process of object mapping possible
 */
@Entity(tableName = "process_usage")
data class ProcessUsage(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "active_process")
    val activeProcess: String,
    val longitude: Double,
    val latitude: Double,
    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long
)