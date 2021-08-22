package at.reichinger.cuarecorder.persistence

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class EntityReadWriteTest {
    private lateinit var keyDao: KeyDao
    private lateinit var db: CUADatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, CUADatabase::class.java).build()
        keyDao = db.keyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeKeyPressAndRead() {
        val keyPress = KeyPress(1, 10, "A", 100)
        keyDao.insertAll(keyPress)
        val keyPresses = keyDao.getAll()
        assertThat(keyPress, equalTo(keyPresses[0]))
    }
}