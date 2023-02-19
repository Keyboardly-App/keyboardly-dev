package app.keyboardly.sample.action.profile

import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.sample.databinding.SampleProfileLayoutBinding

/**
 * Created by Zainal on 18/02/2023 - 11:59
 */
class ProfileActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency) {

    override fun onCreate() {
        val binding = SampleProfileLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root

        binding.apply {
            back.setOnClickListener {
                dependency.viewAddOnNavigation()
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