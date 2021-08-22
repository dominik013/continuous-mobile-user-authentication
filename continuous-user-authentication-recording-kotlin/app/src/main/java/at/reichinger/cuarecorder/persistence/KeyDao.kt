package at.reichinger.cuarecorder.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface KeyDao {

    companion object {
        const val NECESSARY_RECORDINGS = 2000
    }

    /**
     * Returns a list of all KeyPress objects that are stored in the database
     * @see KeyPress
     */
    @Query("SELECT * FROM key_press")
    fun getAll(): List<KeyPress>

    /**
     * Returns the number of KeyPress objects that are stored in the database
     * @see KeyPress
     */
    @Query("SELECT COUNT(*) FROM key_press")
    fun getNumberOfRecordings(): Long

    /**
     * Inserts a variable number of KeyPress objects into the database
     * @see KeyPress
     */
    @Insert
    fun insertAll(keyPress: List<KeyPress>)

}