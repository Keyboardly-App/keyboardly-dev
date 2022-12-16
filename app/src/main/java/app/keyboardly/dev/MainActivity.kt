package app.keyboardly.dev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.keyboardly.dev.databinding.ActivityMainBinding
import com.rowland.kokokeyboard.keypad.KokoKeyboardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.keyboardTest.registerEditText(KokoKeyboardView.INPUT_TYPE_QWERTY, binding.inputTest)
    }
}