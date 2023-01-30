package app.keyboardly.sample.action.discount

import android.text.InputType
import android.util.Log
import android.widget.EditText
import app.keyboardly.lib.helper.ChipGroupCallback
import app.keyboardly.lib.helper.InputPresenter
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.sample.R
import app.keyboardly.sample.databinding.DiscountFeatureLayoutBinding
import app.keyboardly.style.helper.invisible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class RegisterView (
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), InputPresenter, ChipGroupCallback {

    private lateinit var binding: DiscountFeatureLayoutBinding

    override fun onCreate() {
        binding = DiscountFeatureLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root
        initClick()
        initFooter()
    }

    private fun initClick() {
        binding.apply {
            email.setOnClickListener {
                dependency.requestInput(email,this@RegisterView)
            }
            name.setOnClickListener {
                dependency.requestInput(name,this@RegisterView)
            }
            number.setOnClickListener {
                dependency.requestInput(number,this@RegisterView,
                    inputType = InputType.TYPE_CLASS_NUMBER)
            }
            address.setOnClickListener {
                dependency.requestInput(address,this@RegisterView,
                    longInput = true)
            }
            dateBorn.setOnClickListener {
                dependency.showDatePicker(dateBorn, this@RegisterView)
            }
            group.setOnClickListener {
                val list = mutableListOf<Chip>()
                val listColor = arrayOf("Red","White","Yellow","Green","Blue","Black")

                listColor.forEach {
                    val chip = createChip().apply { text = it }
                    list.add(chip)
                }
                dependency.showChipOptions(list, this@RegisterView, group)
            }
        }
    }

    private fun createChip(): Chip {
        return getLayoutInflater().inflate(
            R.layout.chip_layout, null,
            false
        ) as Chip
    }

    private fun initFooter() {
        binding.apply {
            footerDiscount.apply {
                footerBackBtn.setOnClickListener {
                    dependency.viewAddOnNavigation()
                }
                footerFormReset.invisible()
                footerSubmitButton.text = "Save"
                footerSubmitButton.setOnClickListener {
                    val registerText = StringBuffer()
                    registerText.appendLine("Register")
                    registerText.appendLine()
                    registerText.appendLine("Name:")
                    registerText.appendLine(name.text)
                    registerText.appendLine("Number:")
                    registerText.appendLine(number.text)
                    registerText.appendLine("Email:")
                    registerText.appendLine(email.text)
                    registerText.appendLine("Date born:")
                    registerText.appendLine(dateBorn.text)
                    registerText.appendLine("Gender:")
                    registerText.appendLine(if (male.isChecked) "Male" else "Female")
                    registerText.appendLine("Group:")
                    registerText.appendLine(group.text)
                    registerText.appendLine("Address:")
                    registerText.appendLine(address.text)
                    val finalText = registerText.toString()
                    dependency.commitText(finalText)
                }
            }
        }
    }

    override fun onDone(text: String, editText: EditText?) {
        Log.d("discount", "text onDone=$text")
        val prefixDate = "date:"
        if (text.startsWith(prefixDate)){
            val dateSelected = text.replace(prefixDate,"")
            editText?.setText(dateSelected)
            Log.d("discount","Selected date=$dateSelected")
        } else {
            editText?.setText(text)
        }
        dependency.viewLayoutAction()
    }

    override fun onChipCheckedChange(chip: Chip, isChecked: Boolean, chipGroup: ChipGroup) {
        val selectedTags = getSelectedTags(chipGroup)
        Log.d("discount", "selectedTags=$selectedTags")
        dependency.getEditTextInput().setText(selectedTags)
    }

    override fun onDoneChip(editText: EditText, chipGroup: ChipGroup) {
        val text = editText.text
        Log.d("discount", "text=$text")
        binding.group.text = text
        dependency.viewLayoutAction()
    }
}