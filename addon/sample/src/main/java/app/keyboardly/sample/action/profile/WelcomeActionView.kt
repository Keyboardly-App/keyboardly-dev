package app.keyboardly.sample.action.profile

import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.sample.databinding.SampleWelcomeLayoutBinding

/**
 * Created by Zainal on 18/02/2023 - 11:59
 */
class WelcomeActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency) {

    override fun onCreate() {
        val binding = SampleWelcomeLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root

        binding.apply {
            back.setOnClickListener {
                dependency.viewAddOnNavigation()
            }

            inputName.setOnClickListener {
                dependency.requestInput(inputName)
            }

            submitBtn.setOnClickListener {
                val name = inputName.text.toString()
                inputName.error = null
                if (name.isEmpty()){
                    inputName.error = "can't empty"
                } else {
                    toast("Welcome $name!")
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