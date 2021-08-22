package at.reichinger.cuarecorder.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object for calling transactions related to ProcessUsage objects
 */
@Dao
interface ProcessDao {

    companion object {
        /**
         * For the processes 172.800 entries will be created per day (when creating one entry per 30 seconds)
         */
        const val NECESSARY_RECORDINGS = 25000
    }

    /**
     * Returns a list of all ProcessUsage objects that are stored in the database
     * @see ProcessUsage
     */
    @Query("SELECT * FROM process_usage")
    fun getAll(): List<ProcessUsage>

    /**
     * Returns the number of ProcessUsage objects that are stored in the database
     * @see ProcessUsage
     */
    @Query("SELECT COUNT(*) FROM process_usage")
    fun getNumberOfRecordings(): Long

    /**
     * Inserts a variable number of ProcessUsage objects into the database
     * @see ProcessUsage
     */
    @Insert
    fun insertAll(vararg processUsage: ProcessUsage)


}