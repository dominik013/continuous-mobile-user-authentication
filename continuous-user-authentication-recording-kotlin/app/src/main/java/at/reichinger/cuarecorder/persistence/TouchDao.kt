package at.reichinger.cuarecorder.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TouchDao {

    companion object {
        // One touch event are roughly 100 recordings
        const val NECESSARY_RECORDINGS = 100000//500000 // 500.000
    }

    /**
     * Returns a list of all TouchSequence objects that are stored in the database
     * @see TouchInputEvent
     */
    @Query("SELECT * FROM touch_input")
    fun getAll(): List<TouchInputEvent>

    /**
     * Returns the number of TouchSequence objects that are stored in the database
     * @see TouchInputEvent
     */
    @Query("SELECT COUNT(*) FROM touch_input")
    fun getNumberOfRecordings(): Long

    /**
     * Inserts a variable number of TouchSequence objects into the database
     * @see TouchInputEvent
     */
    @Insert
    fun insertAll(processUsage: List<TouchInputEvent>)
}