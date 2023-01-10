package app.keyboardly.dev.keyboard.keypad

import android.view.View
import app.keyboardly.dev.keyboard.keypad.keyboardaction.KeyboardInputManager
import app.keyboardly.dev.keyboard.utils.DynamicModuleHelper

/**
 * Created by Zainal on 09/01/2023 - 19:18
 */
class KeyboardActionContainer(
    view: View,
    private val moduleHelper: DynamicModuleHelper
) : KeyboardInputManager(view, moduleHelper){
}