package at.reichinger.cuarecorder.service.data

/**
 * Contains information about a specific line emitted by the "getevent" process
 *
 * Important eventId types:
 * ABS_TRACKING_ID      ID of the touch gesture
 * ABS_MT_POSITION_X    X position of the input
 * ABS_MT_POSITION_Y    Y position of the input
 * BTN_TOOL_FINGER      Indicates whether UP or DOWN eventId was issued
 */
data class EventData(val eventId: Int, val value: Int, val timestamp: Long)