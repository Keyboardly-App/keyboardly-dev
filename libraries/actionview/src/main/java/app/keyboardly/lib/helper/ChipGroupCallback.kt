package app.keyboardly.lib.helper

import android.widget.EditText
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * Created by zainal on 11/23/21 - 2:23 PM
 */
interface ChipGroupCallback {
    fun onChipCheckedChange(chip: Chip, isChecked: Boolean, chipGroup: ChipGroup)
    fun onDoneChip(editText: EditText, chipGroup: ChipGroup)
}