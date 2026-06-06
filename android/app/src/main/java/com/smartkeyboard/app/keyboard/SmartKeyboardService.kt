package com.smartkeyboard.app.keyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.KeyEvent
import android.view.View
import com.smartkeyboard.app.R

class SmartKeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private var isCapital = false

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.keys_layout)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                val ic = currentInputConnection
                ic?.deleteSurroundingText(1, 0)
            }
            Keyboard.KEYCODE_SHIFT -> {
                isCapital = !isCapital
                keyboard.setShifted(isCapital)
                keyboardView.invalidateAllKeys()
            }
            Keyboard.KEYCODE_DONE -> {
                currentInputConnection?.sendKeyEvent(
                    KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
                )
            }
            else -> {
                val code = primaryCode.toChar()
                val text = if (isCapital) code.uppercaseChar() else code
                currentInputConnection?.commitText(text.toString(), 1)
                isCapital = false
                keyboard.setShifted(false)
                keyboardView.invalidateAllKeys()
            }
        }
    }

    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onText(text: CharSequence?) {
        currentInputConnection?.commitText(text, 1)
    }

    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}
