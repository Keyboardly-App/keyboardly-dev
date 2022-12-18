package com.rowland.kokokeyboard.keypad

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowland.kokokeyboard.layouts.KeyboardLayout
import com.rowland.kokokeyboard.manager.KeyboardManager.KeyboardListener
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuAdapter
import app.keyboardly.lib.navigation.NavigationMenuModel
import com.rowland.kokokeyboard.R
import com.rowland.kokokeyboard.manager.KeyboardManager
import java.util.HashMap

open class KokoKeyboardView : ExpandableLayout {
    private var activeEditField: EditText? = null
    private val keyboards = HashMap<EditText?, KeyboardLayout>()
    private var keyboardListener: KeyboardListener? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    protected fun init() {
        keyboardListener = object : KeyboardListener {
            override fun characterClicked(c: Char) {}
            override fun specialKeyClicked(keyCode: Int) {
                when (keyCode) {
                    KeyboardManager.KEYCODE_BACKSPACE -> {}
                    KeyboardManager.KEYCODE_SPACE -> {}
                    KeyboardManager.KEYCODE_ADDON -> gotoAddOn()
                    KeyboardManager.KEYCODE_DONE -> collapse()
                }
            }
        }
    }

    var TAG = KeyboardManager::class.java.simpleName
    private fun gotoAddOn() {
        Log.i(TAG, "key add on triggered")
    }

    fun registerEditText(type: Int, field: EditText) {
        if (!field.isEnabled) {
            return
        }
        field.setRawInputType(InputType.TYPE_CLASS_TEXT)
        field.isSoundEffectsEnabled = false
        field.isLongClickable = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            field.showSoftInputOnFocus = false
        } else {
            field.setTextIsSelectable(true)
        }
        val inputConnection = field.onCreateInputConnection(EditorInfo())
        val keyboard = generateCorrectKeyboard(type, inputConnection)
        keyboards[field] = keyboard
        keyboards[field]!!.registerListener(keyboardListener)
        field.onFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            if (hasFocus) {
                val imm =
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(field.windowToken, 0)
                activeEditField = field
                removeAllViews()
                val keyboard1 = keyboards[activeEditField]
                addView(keyboard1)
                if (!isExpanded) {
                    expand()
                }
            } else {
                if (isExpanded) {
                    for (editText in keyboards.keys) {
                        if (editText!!.hasFocus()) {
                            return@OnFocusChangeListener
                        }
                    }
                    collapse()
                }
            }
        }
        field.setOnClickListener { v: View? ->
            if (!isExpanded) {
                expand()
            }
        }
    }

    fun unregisterEditText(field: EditText?) {
        keyboards.remove(field)
    }

    private fun generateCorrectKeyboard(type: Int, ic: InputConnection): KeyboardLayout {
        val keypad = KeyboardLayout(context, KeyboardManager(ic))
        val view: View
        return when (type) {
            INPUT_TYPE_QWERTY -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.qwerty_keypad, null)
                keypad.addView(view)
                initView(view)
                keypad
            }
            INPUT_TYPE_QWERTY_NUM -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.qwerty_keypad_with_num, null)
                keypad.addView(view)
                keypad
            }
            else -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.qwerty_keypad_with_num, null)
                keypad.addView(view)
                keypad
            }
        }
    }

    private fun initView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.navigation)
        val model = NavigationMenuModel(
            1,
            R.string.nav_sample,
            R.drawable.ic_round_local_activity_24,
            true,
            featurePackageId = "com.keyboardly.sample"
        )

        val mutableList = mutableListOf<NavigationMenuModel>()
        mutableList.add(model)

        val adapterMenu = NavigationMenuAdapter(mutableList,object: NavigationCallback{
            override fun onClickMenu(data: NavigationMenuModel) {
                Log.d(TAG, "onClickMenu: data=${data.name}")
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false)
            adapter = adapterMenu
        }
    }

    companion object {
        const val INPUT_TYPE_QWERTY_NUM = -10
        const val INPUT_TYPE_QWERTY = -20
    }
}