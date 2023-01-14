package app.keyboardly.sample.action.discount

import android.text.InputType
import android.util.Log
import android.widget.EditText
import app.keyboardly.lib.ChipGroupCallback
import app.keyboardly.lib.InputPresenter
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
class DiscountView (
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
                dependency.requestInput(email,this@DiscountView)
            }
            name.setOnClickListener {
                dependency.requestInput(name,this@DiscountView)
            }
            number.setOnClickListener {
                dependency.requestInput(number,this@DiscountView,
                    inputType = InputType.TYPE_CLASS_NUMBER)
            }
            address.setOnClickListener {
                dependency.requestInput(address,this@DiscountView,
                    longInput = true)
            }
            dateBorn.setOnClickListener {
                dependency.showDatePicker(dateBorn, this@DiscountView)
            }
            group.setOnClickListener {
                val list = mutableListOf<Chip>()

                val chip = createChip().apply {
                    text = "Merah"
                }
                list.add(chip)

                val chip2 = createChip().apply {
                    text = "Putih"
                }
                list.add(chip2)

                val chip3 = createChip().apply {
                    text = "Hijau"
                }
                list.add(chip3)
                dependency.showChipOptions(list, this@DiscountView, group)
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
                    val text = textMain.text.toString()
                    dependency.commitText(text)
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