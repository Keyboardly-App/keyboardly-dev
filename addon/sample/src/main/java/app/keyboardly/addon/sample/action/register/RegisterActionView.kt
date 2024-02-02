package app.keyboardly.addon.sample.action.register

import android.text.InputType
import android.util.Log
import android.widget.EditText
import app.keyboardly.lib.helper.ChipGroupCallBack
import app.keyboardly.lib.helper.InputPresenter
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.addon.sample.R
import app.keyboardly.addon.sample.databinding.SampleRegisterFeatureLayoutBinding
import app.keyboardly.style.helper.gone
import app.keyboardly.style.helper.invisible
import app.keyboardly.style.helper.visible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class RegisterActionView (
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), InputPresenter, ChipGroupCallBack {

    private lateinit var binding: SampleRegisterFeatureLayoutBinding

    override fun onCreate() {
        binding = SampleRegisterFeatureLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root
        dependency.setKeyboardHeight(150)
        initClick()
        initFooter()
    }

    override fun onResume() {
        dependency.setKeyboardHeight(150)
        super.onResume()
    }

    private fun initClick() {
        binding.apply {
            email.setOnClickListener {
                dependency.requestInput(email,this@RegisterActionView)
            }
            name.setOnClickListener {
                dependency.requestInput(name,this@RegisterActionView)
            }
            number.setOnClickListener {
                dependency.requestInput(number,this@RegisterActionView,
                    inputType = InputType.TYPE_CLASS_NUMBER)
            }
            address.setOnClickListener {
                dependency.requestInput(address,this@RegisterActionView,
                    longInput = true)
            }
            dateBorn.setOnClickListener {
                dependency.showDatePicker(dateBorn, this@RegisterActionView)
            }
            group.setOnClickListener {
                val list = mutableListOf<Chip>()
                val listColor = arrayOf("Red","White","Yellow","Green","Blue","Black")

                listColor.forEach {
                    val chip = createChip().apply { text = it }
                    list.add(chip)
                }
                dependency.showChipOptions(list, this@RegisterActionView, group)
            }
        }
    }

    private fun createChip(): Chip {
        return getLayoutInflater().inflate(
            R.layout.sample_chip_layout, null,
            false
        ) as Chip
    }

    private fun initFooter() {
        binding.apply {
            footerDiscount.apply {
                footerBackBtn.setOnClickListener {
                    dependency.viewAddOnNavigation()
                }
                footerFormReset.gone()
                footerDeleteAll.visible()
                footerDeleteAll.setOnClickListener {
                    dependency.doBackSpace(true)
                }
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