package app.keyboardly.dev.keyboard.keypad.keyboardaction

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.dev.R
import com.google.android.material.chip.ChipGroup

/**
 * Created by zainal on 6/28/22 - 10:04 AM
 *
 * This is class full variable view for Woowa keyboard needed
 */
open class KeyboardBaseId(
    view: View
) {

    // default navigation recycler view
    val navigationView: RecyclerView = view.findViewById(R.id.navigation)
    val mainHeader: LinearLayout = view.findViewById(R.id.mainHeaderParent)

    val logoMainHeader: ImageView = view.findViewById(R.id.logoButton)
    val navigationBack: ImageView = view.findViewById(R.id.backButton)
    val titleHeader: TextView = view.findViewById(R.id.titleHeader)

    val keyboardActionWrapper: LinearLayout = view.findViewById(R.id.keyboard_action_wrapper)
    val keyboardView: FrameLayout = view.findViewById(R.id.keyboardView)
    val keyboardWrapper: ConstraintLayout = view.findViewById(R.id.keyboard_parent)
    val headerWrapper: RelativeLayout = view.findViewById(R.id.headerWrapper)

    val headerShadowAction: View = view.findViewById(R.id.header_shadow)
    val frame: FrameLayout = view.findViewById(R.id.container_frame)
    val progressMain: RelativeLayout = view.findViewById(R.id.progress_main_parent)
    val messageOnFrame: TextView = view.findViewById(R.id.message_nothing)
    val recyclerView: RecyclerView = view.findViewById(R.id.list_top_container_rv)
    val chipGroupOnFrame: ChipGroup = view.findViewById(R.id.chipgroup_top_container)
    val datePickerOnFrame: DatePicker = view.findViewById(R.id.date_picker_container)
    val navigationParentLayout: LinearLayout = view.findViewById(R.id.navigation_parent)
    val floatingRecyclerView: RecyclerView = view.findViewById(R.id.recyclerViewScrollTop)
    val floatingRoot: RelativeLayout = view.findViewById(R.id.relativeLayoutRvTop)
    val floatingFrame: FrameLayout = view.findViewById(R.id.floating_frame)

    /* ids for input layout */
    val defaultInputLayout: View = view.findViewById(R.id.default_input_layout)
    val mLayoutEdit: LinearLayout = view.findViewById(R.id.mainLayoutEdit)
    val mEditField: EditText = view.findViewById(R.id.editMain)
    val mEditFieldTIL: TextView = view.findViewById(R.id.editMainTIL)
    val parentEditMain: LinearLayout = view.findViewById(R.id.parent_edit_main)
    val mEditFieldLong: EditText = view.findViewById(R.id.default_input_long)
    val mEditFieldLongTIL: TextView = view.findViewById(R.id.editMainLongTIL)
    val parentEditMainLong: LinearLayout = view.findViewById(R.id.parent_edit_main_long)
    val doneEditButton: ImageView = view.findViewById(R.id.onBackMainEditText)
    val progressInput: ProgressBar = view.findViewById(R.id.progressInput)

}