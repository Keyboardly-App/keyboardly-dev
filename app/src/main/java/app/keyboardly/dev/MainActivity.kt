package app.keyboardly.dev

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import app.keyboardly.dev.keyboard.keypad.KokoKeyboardView
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyboardView = findViewById<KokoKeyboardView>(R.id.keyboardMain)
        val editText = findViewById<EditText>(R.id.inputTest)
        Timber.d("keyboardView=${keyboardView.isVisible} // ${keyboardView.isClickable}")
        keyboardView.registerEditText(KokoKeyboardView.INPUT_TYPE_QWERTY, editText)
    }
}