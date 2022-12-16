package app.keyboardly.sample.action.discount

import android.util.Log
import android.widget.EditText
import app.keyboardly.lib.ChipGroupCallback
import app.keyboardly.lib.InputPresenter
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import app.keyboardly.style.helper.invisible
import app.keyboardly.sample.R
import app.keyboardly.sample.databinding.BotFeatureLayoutBinding

/**
 * Created by zainal on 6/8/22 - 2:59 PM
 */
class DiscountView (
    dependency: KeyboardActionDependency
) : KeyboardActionView(dependency), InputPresenter, ChipGroupCallback {

    private lateinit var binding: BotFeatureLayoutBinding

    override fun onCreate() {
        binding = BotFeatureLayoutBinding.inflate(getLayoutInflater())
        viewLayout = binding.root
        initClick()
        initFooter()
    }

    private fun initClick() {
        binding.apply {
            botEditText.setOnClickListener {
                dependency.requestInput(botEditText,this@DiscountView)
            }
            botEditTextNumber.setOnClickListener {
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
                dependency.showChipOptions(list, this@DiscountView, botEditTextNumber)
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
                    val text = botTextMain.text.toString()
                    dependency.commitText(text)
                }
            }
        }
    }

    override fun onDone(text: String, editText: EditText?) {
        Log.d("BotFeature", "text onDone=$text")
        dependency.viewLayoutAction()
        editText?.setText(text)
    }

    override fun onChipCheckedChange(chip: Chip, isChecked: Boolean, chipGroup: ChipGroup) {
        val selectedTags = getSelectedTags(chipGroup)
        Log.d("BotFeature", "selectedTags=$selectedTags")
        dependency.getEditTextInput().setText(selectedTags)
    }

    override fun onDoneChip(editText: EditText, chipGroup: ChipGroup) {
        val text = editText.text
        Log.d("BotFeature", "text=$text")
        binding.botEditTextNumber.text = text
        dependency.viewLayoutAction()
    }
}