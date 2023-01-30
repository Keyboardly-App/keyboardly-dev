package app.keyboardly.lib.helper

import android.widget.EditText
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * Created by zainal on 11/23/21 - 2:23 PM
 */
interface ChipGroupCallBack {
    /**
     * triggered when chip options clicked
     * @param chip : clicked chip
     * @param isChecked : condition the chip after click, is checked or not
     */
    fun onChipCheckedChange(chip: Chip, isChecked: Boolean, chipGroup: ChipGroup)

    /**
     * triggered when user choose end the option view.
     * @param editText: edittext that appear when option mode active
     * @param chipGroup: ChipGroup of the options
     */
    fun onDoneChip(editText: EditText, chipGroup: ChipGroup)
}