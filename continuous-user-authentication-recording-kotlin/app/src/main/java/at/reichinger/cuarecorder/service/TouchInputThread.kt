package at.reichinger.cuarecorder.service

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import at.reichinger.cuarecorder.persistence.CUADatabase
import at.reichinger.cuarecorder.persistence.TouchInputEvent
import com.topjohnwu.superuser.CallbackList
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Thread that is executing the "getevent" command and assigning a callback to it, in order to
 * receive touch events and save them to the data base
 *
 * Only the starting point and the ending point of the touch input will be saved
 * Those are acquired by looking at the first X/Y eventValue after BTN_TOOL_FINGER DOWN and
 * on the last X/Y eventValue before BTN_TOOL_FINGER UP, which is produced by the "getevent" process
 */
class TouchInputThread(private val context: Context) : Thread(), SensorEventListener {

    companion object {
        private const val DEVICE = "/dev/input/event3"
        private const val GET_EVENT_COMMAND = "getevent $DEVICE"

        private const val EVENT_BTN_TOOL_FINGER = 0x145
        private const val EVENT_ABS_MT_POSITION_X = 0x35
        private const val EVENT_ABS_MT_POSITION_Y = 0x36
        private const val EVENT_ABS_TRACKING_ID = 0x39

        private const val VALUE_ABS_MT_TRACKING_ID_FINISHED = -1
        private const val VALUE_BTN_TOOL_FINGER_DOWN = 0x1
        private const val VALUE_BTN_TOOL_FINGER_UP = 0x0
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private var accelerometerAccuracy: Int = -1
    private var accelerometerX: Float = -1.0f
    private var accelerometerY: Float = -1.0f
    private var accelerometerZ: Float = -1.0f

    private var shell: Shell? = null
    private val input = mutableListOf<TouchInputEvent>()

    private var currentTrackingId = -1
    private var currentXPosition: Int = -1
    private var currentYPosition: Int = -1

    override fun run() {
        shell = Shell.newInstance()
        val callbackList = object : CallbackList<String>() {
            override fun onAddElement(line: String?) {
                if (line != null) {
                    val eventId = line.substring(5, 9).toInt(radix = 16)
                    val eventValue = getEventValueFrom(line.substring(10, 18))
                    val timestamp = System.currentTimeMillis()

                    if (eventId == EVENT_ABS_MT_POSITION_X) {
                        currentXPosition = eventValue
                    }

                    if (eventId == EVENT_ABS_MT_POSITION_Y) {
                        currentYPosition = eventValue
                    }

                    if (eventId == EVENT_ABS_TRACKING_ID && eventValue != VALUE_ABS_MT_TRACKING_ID_FINISHED) {
                        currentTrackingId = eventValue
                    }
                    val accX = accelerometerX
                    val accY = accelerometerY
                    val accZ = accelerometerZ
                    val accA = accelerometerAccuracy
                    // Insert event into list of events
                    input.add(
                        TouchInputEvent(
                            0,
                            currentTrackingId,
                            eventId,
                            eventValue,
                            currentXPosition,
                            currentYPosition,
                            accX,
                            accY,
                            accZ,
                            accA,
                            timestamp
                        )
                    )

                    // Batch process a whole touch and reset list of events
                    if (eventId == EVENT_ABS_TRACKING_ID && eventValue == VALUE_ABS_MT_TRACKING_ID_FINISHED) {
                        val inputToProcess = input.toList()
                        insertTouchInputData(inputToProcess)
                        input.clear()
                        currentXPosition = -1
                        currentYPosition = -1
                    }
                }
            }
        }

        if (shell?.status == Shell.ROOT_SHELL) {
            // Start sensors as well
            sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)

            val job = shell?.newJob()
            job?.add(GET_EVENT_COMMAND)
            job?.to(callbackList)
            job?.submit()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        accelerometerAccuracy = accuracy
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.values.size == 3) {
            accelerometerX = event.values[0]
            accelerometerY = event.values[1]
            accelerometerZ = event.values[2]
        }
    }

    /**
     * This method is used to close the shell, which is a permanent process
     * When using ADB we would use CTRL+C to terminate the process
     */
    fun stopReceivingTouchInput() {
        sensorManager.unregisterListener(this)
        shell?.close()
        shell = null
    }

    /**
     * Returns the event value or -1 if no valid value possible or (0xffffffff)
     */
    private fun getEventValueFrom(valueString: String): Int {
        return valueString.toIntOrNull(radix = 16) ?: -1
    }

    /**
     * Insert a list of TouchInputEvent objects into the database
     *
     * @param event A collection of EventData objects which hold the most important data for a
     *              "getevent" emitted line
     */
    private fun insertTouchInputData(event: List<TouchInputEvent>) {
        GlobalScope.launch {
            CUADatabase(context).touchDao().insertAll(event)
        }
    }
}