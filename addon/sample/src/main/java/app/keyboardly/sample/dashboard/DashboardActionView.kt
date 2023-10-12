package app.keyboardly.sample.dashboard

import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.sample.databinding.SampleDashboardLayoutBinding
import app.keyboardly.sample.databinding.SampleShoppingLayoutBinding
import app.keyboardly.style.helper.currencyTextWatcher

class DashboardActionView(
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency){

    private lateinit var binding: SampleDashboardLayoutBinding

    override fun onCreate() {
        binding = SampleDashboardLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root
        initAction()
    }

    private fun initAction() {
        binding.apply {

            val textWatcher = currencyTextWatcher(amount)
            amount.addTextChangedListener(textWatcher)

            amount.setOnClickListener {
                dependency.requestInput(amount, isCurrency = true)
            }

            note.setOnClickListener {
                dependency.requestInput(note)
            }
        }
    }
}
