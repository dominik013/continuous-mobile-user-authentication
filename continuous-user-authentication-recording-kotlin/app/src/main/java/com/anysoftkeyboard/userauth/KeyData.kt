package com.anysoftkeyboard.userauth

import androidx.annotation.Nullable
import java.io.Serializable

/**
 * KeyData is used to represent information about a keyboard press and the corresponding keyCode
 * <p/>
 *
 * @param primaryCode The KeyCode corresponding to the pressed key (KeyCode's below 0 are Codes introduced by the
 * CUARecorder Keyboard. Keys with the code zero are emoji - when working with emoji consider querying for the text parameter
 * @param text The text placed when a key was pressed. This is usually null, expect when the pressed key was an emoji
 * @param timeStamp The timestamp in milliseconds when the key was pressed
 */
internal class KeyData(val primaryCode: Int, @Nullable val text: String, val timeStamp: Long) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}