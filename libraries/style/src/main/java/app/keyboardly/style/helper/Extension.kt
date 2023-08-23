package app.keyboardly.style.helper

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.textfield.TextInputLayout
import app.keyboardly.style.R
import java.io.File
import java.io.FileOutputStream

fun View.visible() {
    val transition: Transition = Fade()
    transition.duration = 300
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    val transition: Transition = Fade()
    transition.duration = 300
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.clear() {
    setText("")
}

fun EditText.visiblePassword() {
    transformationMethod = null
}

fun EditText.invisiblePassword() {
    transformationMethod = PasswordTransformationMethod()
}

fun TextInputLayout.clearError() {
    error = ""
}

fun EditText.value(): String {
    return text.toString()
}

fun EditText.currencyFormatted() {
    addTextChangedListener(currencyTextWatcher(this))
}

fun EditText.removeTextWatcher() {
    removeTextChangedListener(currencyTextWatcher(this))
}

fun EditText.errorRemoveListener(textInputLayout: TextInputLayout) {
    addTextChangedListener(removeErrorTextWatcher(this, textInputLayout))
}

fun EditText.isValidEmail(): Boolean {
    val char = text.toString()
    return Patterns.EMAIL_ADDRESS.matcher(char).matches()
}

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.onlyNumber(): String = this.filter { "0123456789".contains(it) }

fun PackageManager.isAppInstalled(app_id: String): Boolean {
    return try {
        getPackageInfo(app_id, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun hexColorToColorDrawable(codeHexColor: String): ColorDrawable {
    val color = Color.parseColor(codeHexColor)
    return ColorDrawable(color)
}

fun getCurrentAppPackage(context: Context, info: EditorInfo?): String? {
    if (info?.packageName != null) {
        return info.packageName
    }
    val pm = context.packageManager
    //Get the Activity Manager Object
    val aManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    //Get the list of running Applications
    val rapInfoList = aManager.runningAppProcesses
    //Iterate all running apps to get their details
    for (rapInfo in rapInfoList) {
        //error getting package name for this process so move on
        if (rapInfo.pkgList.isEmpty()) {
            continue
        }
        try {
            val pkgInfo = pm.getPackageInfo(rapInfo.pkgList[0], PackageManager.GET_ACTIVITIES)
            return pkgInfo.packageName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
    return null
}

fun covertExpeditionStringToArray(lastExpeditionString: String): Array<String?> {
    return if (lastExpeditionString.isNotEmpty()) {
        if (lastExpeditionString.contains(":")) {
            val split = lastExpeditionString.split(":")
            val list = arrayOfNulls<String>(split.size)
            split.forEachIndexed { index, s ->
                list[index] = s
            }
            list
        } else {
            arrayOf(lastExpeditionString)
        }
    } else emptyArray()
}


fun Context.getImageToShare(bitmap: Bitmap, fileName: String): Uri? {
    val imagefolder = File(cacheDir, "images")
    var uri: Uri? = null
    try {
        if (!imagefolder.exists()) {
            imagefolder.mkdirs()
        }

        val file = File(imagefolder, fileName)
        val fileNameLowerCase = fileName.lowercase()
        val outputStream = FileOutputStream(file)
        val compressValue = 50
        if (fileNameLowerCase.contains(".jpg") || fileNameLowerCase.contains(".jpeg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressValue, outputStream)
        } else if (fileNameLowerCase.contains(".png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, compressValue, outputStream)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, compressValue, outputStream)
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressValue, outputStream)
            }
        }
        outputStream.flush()
        outputStream.close()
        uri = FileProvider.getUriForFile(
            this.applicationContext,
            "app.keyboardly.android.provider",
            file
        )
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return uri
}


fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TextView.disableCopyPaste() {
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
            menu?.apply {
                removeItem(android.R.id.copy)
                removeItem(android.R.id.cut)
                removeItem(android.R.id.paste)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    removeItem(android.R.id.pasteAsPlainText)
                }
            }
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = false

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean = false

        override fun onDestroyActionMode(p0: ActionMode?) {}

    }
}

fun TextView.enableCopyPaste() {
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
            menu?.apply {
                removeItem(android.R.id.copy)
                removeItem(android.R.id.cut)
                removeItem(android.R.id.paste)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    removeItem(android.R.id.pasteAsPlainText)
                }
            }
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = false

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean = false

        override fun onDestroyActionMode(p0: ActionMode?) {}

    }
}

fun showToast(context: Context, message: String) {
    try {
        // use the application extension function
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val inflater = LayoutInflater.from(context)
            val layout: View = inflater.inflate(
                R.layout.keyboardly_custom_toast, null
            )

            // set the text of the TextView of the message
            val textView = layout.findViewById<TextView>(R.id.toast_text)
            val imageView = layout.findViewById<ImageView>(R.id.toast_left_color)
            textView.text = message

            val lowercase = message.lowercase()
            if (isNegativeToastMessage(lowercase)) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.toast_left_error
                    )
                )
            }

            Toast(context).apply {
                duration = Toast.LENGTH_SHORT
                view = layout
                show()
            }
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

fun showToast(context: Context, messageResId: Int) {
    showToast(context, context.getString(messageResId))
}

fun showToastLong(context: Context, messageResId: Int) {
    showToastLong(context, context.getString(messageResId))
}

fun showToastLong(context: Context, message: String) {
    try {
        // use the application extension function
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            val inflater = LayoutInflater.from(context)
            val layout: View = inflater.inflate(
                R.layout.keyboardly_custom_toast, null
            )

            // set the text of the TextView of the message
            val textView = layout.findViewById<TextView>(R.id.toast_text)
            val imageView = layout.findViewById<ImageView>(R.id.toast_left_color)
            textView.text = message

            val lowercase = message.lowercase()
            if (isNegativeToastMessage(lowercase)
            ) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.toast_left_error
                    )
                )
            }
            Toast(context).apply {
                duration = Toast.LENGTH_LONG
                @Suppress("DEPRECATION")
                view = layout
                show()
            }
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

private fun isNegativeToastMessage(lowercase: String) =
    (lowercase.contains("error") || lowercase.contains("failed")
            || lowercase.contains(" not ") || lowercase.contains(" no ") || lowercase.contains("no data"))

fun isOnlyLetters(word: String): Boolean {
    /* letter a-z and A-Z and white space */
    val regex = "^[A-Za-z0-9 ,.]*$".toRegex()
    return regex.matches(word)
}

fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}