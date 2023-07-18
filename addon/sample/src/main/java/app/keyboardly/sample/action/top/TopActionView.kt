package app.keyboardly.sample.action.top

import android.text.InputType
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.sample.databinding.SampleTopViewLayoutBinding

/**
 * Created by Zainal on 18/02/2023 - 11:59
 */
class TopActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency) {

    override fun onCreate() {
        val binding = SampleTopViewLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root

        binding.apply {
            back.setOnClickListener { dependency.hideTopView() }

            inputName.setOnClickListener {
                inputName.requestFocus()
                dependency.requestInput(inputName, inputOnFloatingView = true)
            }

            inputNumber.setOnClickListener {
                inputNumber.requestFocus()
                dependency.requestInput(inputNumber, inputOnFloatingView = true, inputType = InputType.TYPE_CLASS_NUMBER)
            }

            submitBtn.setOnClickListener {
                val name = inputName.text.toString()
                val number = inputNumber.text.toString()
                inputName.error = null
                if (name.isEmpty()){
                    inputName.error = "can't empty"
                } else if (number.isEmpty()){
                    inputNumber.error = "can't empty"
                } else {
                    toast("Welcome $name!\nNumber: $number")
                }
            }
        }
    }

    override fun onResume() {
        getLatestData()
        super.onResume()
    }

    private fun getLatestData() {

    }

}