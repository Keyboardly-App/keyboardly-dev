package app.keyboardly.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.keyboardly.dev.databinding.ActivityMainBinding
import app.keyboardly.samplekeyboard.components.keyboard.CustomKeyboardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.keyboardview.registerEditText(CustomKeyboardView.KeyboardType.QWERTY,
            binding.inputTest)
    }
}