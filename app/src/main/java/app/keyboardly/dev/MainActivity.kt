package app.keyboardly.dev

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import app.keyboardly.dev.keyboard.keypad.KokoKeyboardView
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        binding.keyboardTest.registerEditText(KokoKeyboardView.INPUT_TYPE_QWERTY, binding.inputTest)

        val keyboardView = findViewById<KokoKeyboardView>(R.id.keyboardMain)
        val editText = findViewById<EditText>(R.id.inputTest)
        editText.addTextChangedListener {

            Timber.d("text=${it?.toString()}")
        }

        Timber.d("keyboardView=${keyboardView.isVisible} // ${keyboardView.isClickable}")
        keyboardView.registerEditText(KokoKeyboardView.INPUT_TYPE_QWERTY, editText)

    }
}