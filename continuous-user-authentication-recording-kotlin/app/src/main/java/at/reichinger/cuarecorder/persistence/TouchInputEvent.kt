package at.reichinger.cuarecorder.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "touch_input")
data class TouchInputEvent(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "abs_mt_tracking_id") val absMtTrackingId: Int,
    @ColumnInfo(name = "event_id") val eventId: Int,
    @ColumnInfo(name = "event_value") val eventValue: Int,
    @ColumnInfo(name = "x_position") val xPosition: Int,
    @ColumnInfo(name = "y_position") val yPosition: Int,
    @ColumnInfo(name = "accelerometer_x") val accelerometerX: Float,
    @ColumnInfo(name = "accelerometer_y") val accelerometerY: Float,
    @ColumnInfo(name = "accelerometer_z") val accelerometerZ: Float,
    @ColumnInfo(name = "accelerometer_accuracy") val accelerometerAccuracy: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)