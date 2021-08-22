package at.reichinger.cuarecorder.persistence

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_press")
data class KeyPress(
    @PrimaryKey(autoGenerate = true)    val id: Int,
    @Nullable                           val text: Char,
    @ColumnInfo(name = "time_stamp")    val timeStamp: Long
)