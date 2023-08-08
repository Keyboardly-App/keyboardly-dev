package app.keyboardly.dev.keyboard.keypad

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.dev.keyboard.keypad.keyboardaction.KeyboardInputManager
import app.keyboardly.dev.keyboard.utils.DynamicModuleHelper
import app.keyboardly.style.helper.gone
import app.keyboardly.style.helper.invisible
import app.keyboardly.style.helper.visible
import app.keyboardly.lib.helper.ChipGroupCallBack
import app.keyboardly.lib.helper.InputPresenter
import app.keyboardly.lib.helper.OnViewMessage
import app.keyboardly.lib.helper.OnViewReady
import com.google.android.material.chip.Chip
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Zainal on 09/01/2023 - 19:18
 */
class KeyboardActionContainer(
    view: View,
    moduleHelper: DynamicModuleHelper,
    kokoKeyboardView: KokoKeyboardView,
) : KeyboardInputManager(view, moduleHelper, kokoKeyboardView) {

    fun showMessageView(onViewMessageReady: OnViewMessage) {
        recyclerView.invisible()
        keyboardActionWrapper.visible()
        messageOnFrame.visible()
        onViewMessageReady.onTextViewReady(messageOnFrame)
        with(messageOnFrame) {
            setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent): Boolean {
                    val bottom = 3
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX >= bottom
                            - compoundDrawables[bottom].bounds.width()
                        ) {
                            viewInputMode(true)
                            return true
                        }
                    }
                    return false
                }
            })
        }
    }



    fun viewFloatingRv(onViewReady: OnViewReady, inputMode: Boolean?=true) {
        floatingRoot.visible()
        floatingRecyclerView.visible()


        if (inputMode == false) {
            getKeyboardViewWrapper().gone()
        } else {
            // TODO: neeed check this again, is need / bug to menu?
//            mEditField.performClick()
        }

        Timber.i("floating rv=${floatingRoot.isVisible} // ${floatingRecyclerView.isVisible}")
        onViewReady.onRecyclerViewReady(floatingRecyclerView)
        floatingRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val pxMax = 300.toPx()
                val minimumHeight = 200.toPx()
                val measuredHeight = recyclerView.measuredHeight
                Timber.d("check size --height=$measuredHeight ? $minimumHeight // $pxMax")
                val finalHeight = if (measuredHeight > pxMax) {
                    pxMax
                } else RelativeLayout.LayoutParams.WRAP_CONTENT

                if (getKeyboardViewWrapper().isVisible && measuredHeight > minimumHeight) {
                    val params = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        finalHeight
                    )
                    params.setMargins(0, 5, 0, 0)
                    recyclerView.layoutParams = params

                    Timber.i("set param to final = $finalHeight")
                    getKeyboardViewWrapper().gone()
                } else {
                    val paramsWrap = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    paramsWrap.setMargins(0, 80, 0, 0)
                    recyclerView.layoutParams = paramsWrap
                    Timber.i("set param to wrap")
                }
            }
        })
        floatingRecyclerView.layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT
    }


    fun loadingMain(loading: Boolean) {
        if (loading) {
            if (!keyboardActionWrapper.isVisible) {
                keyboardActionWrapper.visible()
            }
            if (frame.isVisible){
                frame.gone()
            }
            progressMain.visible()
//            Timber.i("keyboardActionWrapper visible="+keyboardActionWrapper.isVisible)
//            Timber.i("frame="+frame.isVisible)
//            Timber.i("progres visible="+progressMain.isVisible)
        } else {
            if (progressMain.isVisible) {
                progressMain.gone()
            }

            if (!frame.isVisible){
                frame.visible()
            }
//            frame.gone()
//            keyboardActionWrapper.gone()
        }
    }

    fun viewList(onViewReady: OnViewReady) {
        viewInputMode(false)
        showBaseViewForOptions()
        frame.gone()
        recyclerView.visible()
        messageOnFrame.gone()
        onViewReady.onRecyclerViewReady(recyclerView)
    }

    private fun showBaseViewForOptions() {
        getKeyboardViewWrapper().invisible()
        headerShadowAction.visible()
        keyboardActionWrapper.visible()
    }

    /**
     * for show loading on input text
     *
     * @param loading
     */
    fun loadingOnInput(loading: Boolean) {
        if (loading) {
            progressInput.visible()
            doneEditButton.invisible()
        } else {
            progressInput.gone()
            doneEditButton.visible()
        }
    }


    fun showChipOptions(
        list: MutableList<Chip>,
        callback: ChipGroupCallBack,
        editText: EditText?
    ) {
        viewChipGroup(editText)
        list.forEach {
            it.setOnCheckedChangeListener { d, isChecked ->
                Timber.i("checked =${d.text}")
                callback.onChipCheckedChange(it, isChecked, chipGroupOnFrame)
            }
            chipGroupOnFrame.addView(it)
        }

        doneEditButton.setImageResource(app.keyboardly.style.R.drawable.ic_done)
        doneEditButton.setOnClickListener {
            Timber.i("done edit.$editText")
            editText?.let{
                it.text = mEditField.text
                callback.onDoneChip(it,chipGroupOnFrame)
            }
        }
    }

    /**
     * to show chip group
     */
    private fun viewChipGroup(editText: EditText?) {
        viewBaseInput()
        enableEditText(false)
        showBaseViewForOptions()
        if (editText != null && editText.hint != null) {
            mEditField.text = editText.text
            setHint(editText.hint)
        }
        chipGroupOnFrame.visible()
        chipGroupOnFrame.removeAllViews()
    }


    fun showDatePicker(
        editTextTarget: EditText?,
        mPresenter: InputPresenter?,
        readableMode: Boolean? = true
    ) {
        viewBaseInput()
        enableEditText(false)
        showBaseViewForOptions()
        datePickerOnFrame.visible()
        chipGroupOnFrame.removeAllViews()

        editTextTarget?.hint?.apply {
            setHint(this)
        }

        val currentText = editTextTarget?.text.toString()
        var yearFromEdittext: Int? = null
        var monthFromEdittext: Int? = null
        var dayFromEdittext: Int? = null

        Timber.i("current text="+currentText)
        var selectedDate = ""

        if (currentText.contains("-") && currentText.length == 10 ) {
            try {
                val splits = currentText.split("-")
                if (splits.size == 3) {
                    yearFromEdittext = splits.first().toInt()
                    monthFromEdittext = splits[1].toInt()
                    dayFromEdittext = splits.last().toInt()

                    selectedDate = currentText
                    val readable = toReadableDate(currentText)
                    mEditField.setText(readable)
                } else {
                    mEditField.setText("")
                }
            } catch (e: Exception){
                e.printStackTrace()
                mEditField.setText("")
            }
        }

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val yearCalendar = yearFromEdittext?:calendar.get(Calendar.YEAR)
        val monthOfYear = monthFromEdittext?:calendar.get(Calendar.MONTH)
        val dateOfYear = dayFromEdittext?:calendar.get(Calendar.DAY_OF_MONTH)

        datePickerOnFrame.init(yearCalendar, monthOfYear, dateOfYear
        ) { _, year, month, day ->
            // TODO: add on date change here by interface class
            var dayStr = day.toString()
            var montString = (month + 1).toString()
            if (dayStr.length == 1)
                dayStr = "0$dayStr"
            if (montString.length == 1) montString = "0$montString"
            val dateSelected = "$year-$montString-$dayStr"
            selectedDate = dateSelected
            Timber.d("date-> $dateSelected//$selectedDate")
            dateSelected.apply {
                val readableDate = if (readableMode!!) toReadableDate(this) else this
                mEditField.setText(readableDate)
                mEditField.setSelection(readableDate.length)
                editTextTarget?.setText(readableDate)
            }
        }
        doneEditButton.setImageResource(app.keyboardly.style.R.drawable.ic_done)
        doneEditButton.setOnClickListener {
            val textEdittext = mEditField.text
            val text = if (textEdittext == null) "" else "date:$selectedDate"
            Timber.d("selected date=$selectedDate // $text")
            mPresenter?.onDone(text, editTextTarget)
        }
    }

    private fun toReadableDate(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dates = formatter.parse(date) as Date
        val locale = getLocale()
        val newFormat = SimpleDateFormat("dd MMMM yyyy", locale)
        return newFormat.format(dates)
    }

    private fun getLocale() = Locale.getDefault()



    /**
     * to set main keyboard view
     * */
    fun setTopActionView(view: View?) {
        floatingRecyclerView.gone()
        try {
            floatingRoot.visible()
            floatingFrame.removeAllViews()
            floatingFrame.addView(view)
            floatingFrame.visible()
        } catch (e: Exception) {
            toast("error:\n" + e.message)
        }
    }

    fun hideTopView() {
        floatingRoot.gone()
        floatingFrame.gone()
    }
}