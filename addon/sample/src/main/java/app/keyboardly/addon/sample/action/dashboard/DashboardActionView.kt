package app.keyboardly.addon.sample.action.dashboard

import android.text.InputType
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.addon.sample.databinding.SampleDashboardLayoutBinding
import app.keyboardly.addon.sample.databinding.SampleShoppingLayoutBinding
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
            backBtn.setOnClickListener {
                dependency.viewAddOnNavigation()
            }

            val textWatcher = currencyTextWatcher(amount)
            amount.addTextChangedListener(textWatcher)

            amount.setOnClickListener {
                dependency.requestInput(amount, isCurrency = true)
            }

            discount.setOnClickListener {
                dependency.requestInput(discount, inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL)
            }

            note.setOnClickListener {
                dependency.requestInput(note)
            }
        }
    }
}
