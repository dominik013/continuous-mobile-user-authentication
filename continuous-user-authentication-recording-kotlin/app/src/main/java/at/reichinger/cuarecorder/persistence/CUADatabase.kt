package at.reichinger.cuarecorder.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * An abstract class which exposes the internal SQLite database
 * This class allows to get easy access to the Data Access Objects by calling for
 * example CUADatabase(context).keyDao().someTransaction(param)
 */
@Database(entities = [KeyPress::class, ProcessUsage::class, TouchInputEvent::class], version = 1)
abstract class CUADatabase : RoomDatabase() {
    /**
     * Returns the Key DAO which is used to modify KeyPress objects stored in the DB
     * @see KeyPress
     */
    abstract fun keyDao(): KeyDao

    /**
     * Returns the Process DAO which is used to modify ProcessUsage objects stored in the DB
     * @see ProcessUsage
     */
    abstract fun processDao(): ProcessDao

    /**
     * Returns the Touch DAO which is used to modify TouchSequence objects stored in the DB
     * @see TouchInputEvent
     */
    abstract fun touchDao(): TouchDao

    companion object {
        private const val DATABASE_NAME = "cua-db.db"
        @Volatile
        private var instance: CUADatabase? = null
        private val LOCK = Any()

        /**
         * Overloaded invoke Operator to return the instance of CUADatabase
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        fun getDatabaseFiles(context: Context): Set<String> {
            val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath
            val dbPathShm = "$dbPath-shm"
            val dbPathWal = "$dbPath-wal"

            return mutableSetOf<String>(dbPath, dbPathShm, dbPathWal)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            CUADatabase::class.java, DATABASE_NAME
        ).build()
    }
}