package app.keyboardly.style.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


fun toIDR(number: BigInteger): String =
    "Rp" + NumberFormat.getNumberInstance(Locale.US).format(number).replace(",", ".")

fun toIDR(number: Int): String =
    "Rp" + NumberFormat.getNumberInstance(Locale.US).format(number).replace(",", ".")

fun toIDR(number: Double): String =
    "Rp" + NumberFormat.getNumberInstance(Locale.US).format(number.toInt()).replace(",", ".")

fun toIDR(number: Long): String =
    "Rp" + NumberFormat.getNumberInstance(Locale.US).format(number).replace(",", ".")

fun toIDR(number: String): String =
    "Rp" + NumberFormat.getNumberInstance(Locale.US).format(number.toInt()).replace(",", ".")

fun toCurrency(number: Double?): String =
    String.format("%,d", number?.toInt()).replace(',', '.')

fun toCurrency(number: Int?): String =
    String.format("%,d", number).replace(',', '.')

fun toCurrency(number: BigInteger?): String =
    String.format("%,d", number).replace(',', '.')

fun toReadableDecimal(value: Double): String =
    DecimalFormat("0.#").format(value)

fun toLocalDate(dateString: String) =
    LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

fun toLocalDateTime(dateString: String) =
    LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

fun toConsumableDate(date: LocalDate) =
    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

fun toReadableDate(date: LocalDate) =
    date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

fun toConsumableTime(time: LocalTime) =
    time.format(DateTimeFormatter.ofPattern("HH:mm"))

fun toReadableTime(time: LocalTime) =
    time.format(DateTimeFormatter.ofPattern("HH:mm"))

fun toConsumableDateTime(date: LocalDate, time: LocalTime) =
    LocalDateTime.of(date, time).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

fun toReadableDateTime(date: LocalDate, time: LocalTime) =
    LocalDateTime.of(date, time).format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))

fun toConsumableDateTime(dateTime: LocalDateTime) =
    dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

fun toReadableDateTime(dateTime: LocalDateTime) =
    dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))

@SuppressLint("SimpleDateFormat")
fun toReadableDateTime(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return try {
        val dates = formatter.parse(date) as Date
        val locale = getLocale()
        val newFormat = SimpleDateFormat("d MMMM yyyy HH:mm", locale)
        newFormat.format(dates)
    } catch (e: ParseException) {
        e.printStackTrace()
        date
    }
}
/*
@SuppressLint("SimpleDateFormat")
fun Context.toReadableDateTimeElegant(date: String): String {
    return if (date.startsWith(getCurrentDate())) {
        val time = date.split(" ")[1]
        this.getString(R.string.today) + "\n- ${time.subSequence(0,5)} WIB"
    } else {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dates = formatter.parse(date) as Date
        val locale = getLocale()
        val newFormat = SimpleDateFormat("EEEE, dd MMM \n- HH:mm", locale)
        newFormat.format(dates) + " WIB"
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.toReadableDateTimeElegantInline(date: String): String {
    return if (date.startsWith(getCurrentDate())) {
        val time = date.split(" ")[1]
        this.getString(R.string.today) + " ${time.subSequence(0,5)} WIB"
    } else {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dates = formatter.parse(date) as Date
        val locale = getLocale()
        val newFormat = SimpleDateFormat("EEEE dd MMM, HH:mm", locale)
        newFormat.format(dates) + " WIB"
    }
}*/

@SuppressLint("SimpleDateFormat")
fun toReadableDate(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val dates = formatter.parse(date) as Date
    val locale = getLocale()
    val newFormat = SimpleDateFormat("d MMMM yyyy", locale)
    return newFormat.format(dates)
}

@SuppressLint("SimpleDateFormat")
fun toReadableDay(date: String): String {
    val formatter = SimpleDateFormat("yyyyMMddHHmm")
    val dates = formatter.parse(date) as Date
    val locale = getLocale()
    val newFormat = SimpleDateFormat("EEEE", locale)
    return newFormat.format(dates)
}

@SuppressLint("SimpleDateFormat")
fun toDate(date: String): Date? {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return try {
        val dates = formatter.parse(date) as Date
        dates
    }catch (e: Exception){
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun toReadableDayFromStrip(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dates = formatter.parse(date) as Date
    val locale = getLocale()
    val newFormat = SimpleDateFormat("EEEE", locale)
    return newFormat.format(dates)
}

@SuppressLint("SimpleDateFormat")
fun toReadableDayMonth(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val dates = formatter.parse(date) as Date
    val locale = getLocale()
    val newFormat = SimpleDateFormat("dd MMMM", locale)
    return newFormat.format(dates)
}

@SuppressLint("SimpleDateFormat")
fun getDay(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val dates = formatter.parse(date) as Date
    val locale = getLocale()
    val newFormat = SimpleDateFormat("dd", locale)
    return newFormat.format(dates)
}

@SuppressLint("SimpleDateFormat")
fun rangeToReadableDateOnly(dateStart: String, dateEnd: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val dates = formatter.parse(dateStart) as Date
    val datesEnd = formatter.parse(dateEnd) as Date

    val locale = getLocale()
    val formatDateReadable = SimpleDateFormat("dd MMMM", locale)
    val formatDateMonthOnly = SimpleDateFormat("MMMM", locale)

    val monthStart = formatDateMonthOnly.format(dates)
    val monthEnd = formatDateMonthOnly.format(datesEnd)

    val start = formatDateReadable.format(dates)
    val end = formatDateReadable.format(datesEnd)

    return if (monthEnd == monthStart) {
        "${start.substring(0, 2)}  - $end"
    } else {
        "$start - $end"
    }
}

@SuppressLint("SimpleDateFormat")
fun getCurrentTime(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentTimeFull(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("yyyy-MM-dd", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateSsFormat(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("yyyyMMdd-HH", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateSsFormatAlternatif(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("yyyy-MM-dd-HH", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateTime(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateTimeForNote(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentMonth(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("MM", locale)
    return newFormat.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentMonthYear(): String {
    val locale = getLocale()
    val newFormat = SimpleDateFormat("yyyy-MM", locale)
    return newFormat.format(Calendar.getInstance().time)
}

private fun getLocale() = Locale.getDefault()

fun isCorrectEndDateAfterStartDate(start: String, end: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val endDate = sdf.parse(end)
        val startDate = sdf.parse(start)
        val result = endDate!!.compareTo(startDate)
        // if result == -1 , end date before start date
        // if result == 1 , end date after start date
        // if result == 0 , end date == start date
        result == 1
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun formatNumber2Digit(number: String): String? {
    val numberFormat: NumberFormat = DecimalFormat("00")
    return numberFormat.format(number)
}

fun formatNumber2Digit(number: Int): String? {
    val numberFormat: NumberFormat = DecimalFormat("00")
    return numberFormat.format(number)
}

@SuppressLint("SimpleDateFormat")
fun getElegantTime(date: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val current = Calendar.getInstance().time.time

    val targetDate = dateFormat.parse(date) as Date
    val diff = current.minus(targetDate.time)

    val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hour = TimeUnit.MILLISECONDS.toHours(diff)
    val day = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        day.toInt() == 1 -> "Kemarin"
        day in 1..6 -> "$day hari lalu"
        hour < 24 -> "$hour jam lalu"
        minutes < 60 -> "$minutes menit lalu"
        seconds < 60 -> "Baru saja"
        else -> "Beberapa waktu lalu"
    }
}

/*
fun toReadableDateRanged(startDate: LocalDate, endDate: LocalDate): String {
    return when {
        startDate.year == endDate.year && startDate.yearMonth == endDate.yearMonth -> {
            startDate.format(DateTimeFormatter.ofPattern("dd")) + " - " + endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }
        startDate.year == endDate.year -> {
            startDate.format(DateTimeFormatter.ofPattern("dd MMM")) + " - " + endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }
        else -> {
            startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + " - " + endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }
    }
}*/

fun toReadableMonth(month: YearMonth) =
    month.format(DateTimeFormatter.ofPattern("MMMM"))

fun toBoolean(isDeleted: Int): Boolean {
    return when (isDeleted) {
        1 -> true
        else -> false
    }
}

fun currencyTextWatcher(editText: EditText) = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {
        var current = ""
        s.let { _ ->
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                var filtered = s.toString().filter { "0123456789".contains(it) }
                if (filtered.isBlank()) {
                    filtered = "0"
                }

                val formatted = toCurrency(filtered.toBigInteger())
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)

                editText.addTextChangedListener(this)
            }
        }
    }
}

fun textWatcherOnChange(onChange: (CharSequence?) -> Unit) = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onChange(s)
    }
    override fun afterTextChanged(s: Editable?) {
    }
}

val decimalRegex = "^(?:100(?:\\.0+)?|\\d{0,2}(?:\\.\\d{1,2})?)$".toRegex()

fun decimalTextWatcher(editText: EditText,
                       digitsBeforeZero: Int?=5,
                       digitsAfterZero: Int?=5,
                       ) = object :  TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    @SuppressLint("SetTextI18n")
    override fun afterTextChanged(s: Editable) {
        val decimalSymbol: Char = decimalSymbol()
        editText.removeTextChangedListener(this)
        editText.filters = arrayOf<InputFilter>(
            DecimalDigitsInputFilter(digitsBeforeZero?:5, digitsAfterZero?:5)
        )
        try {
            val value: String = editText.text.toString()
//            Log.i("Formatter","value edittext=$value")

            if (value != "") {
                if (value.startsWith(decimalSymbol)) {
                    editText.setText("0$decimalSymbol")
                    editText.setSelection(2)
                }
                if (!isValidDecimalInput(s)) {
                    // If the entered text is not a valid decimal, remove the last entered character
                    s.delete(s.length - 1, s.length)
                    editText.setSelection(value.length)
                }

/*                if (value.matches(decimalRegex)) {
                    editText.setText(value)
//                    val diff: Int = editText.text.toString().length - originalStr.length
                    editText.setSelection(value.length)
                } else {
                    Log.e("Formatter","not match regex=$value")
                }*/
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        editText.addTextChangedListener(this)
    }

}

fun isValidDecimalInput(text: CharSequence?): Boolean {
    val decimal = decimalSymbol()
    return text.isNullOrBlank() || Regex("^\\d*$decimal?\\d*\$").matches(text)
}


class DecimalDigitsInputFilter(
    private val digitsBeforeZero: Int,
    private val digitsAfterZero: Int
) :
    InputFilter {
    private lateinit var mPattern: Pattern

    init {
        applyPattern(digitsBeforeZero, digitsAfterZero)
    }

    private fun applyPattern(digitsBeforeZero: Int, digitsAfterZero: Int) {
        val decimalSymbol = decimalSymbol()
        val maxStart = digitsBeforeZero - 1
        val maxEnd = digitsAfterZero - 1
        mPattern = Pattern.compile("^\\d{0,$maxStart}+($decimalSymbol\\d{0,$maxEnd}+)?$")
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): String? {
        val decimalSymbol = decimalSymbol()
        if (dest.toString().contains(decimalSymbol)
            || source.toString().contains(decimalSymbol)) applyPattern(
            digitsBeforeZero + 2,
            digitsAfterZero
        ) else applyPattern(digitsBeforeZero, digitsAfterZero)
        val matcher: Matcher = mPattern.matcher(dest)
        return if (!matcher.matches()) "" else null
    }
}
private fun decimalSymbol(): Char {
    val format: DecimalFormat = DecimalFormat.getInstance(Locale.ENGLISH) as DecimalFormat
    val symbols: DecimalFormatSymbols = format.decimalFormatSymbols
    return symbols.decimalSeparator
}

class MoneyValueFilter(private val digits: Int) : DigitsKeyListener(false, true) {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        var source = source
        var start = start
        var end = end
        val out = super.filter(source, start, end, dest, dstart, dend)

        Log.i("Formatter","source=$source")


        // if changed, replace the source
        if (out != null) {
            source = out
            start = 0
            end = out.length
        }
        val len = end - start

        // if deleting, source is empty
        // and deleting can't break anything
        if (len == 0) {
            return source
        }
        val dlen = dest.length

        // Find the position of the decimal .
        for (i in 0 until dstart) {
            if (dest[i] == decimalSymbol()) {
                // being here means, that a number has
                // been inserted after the dot
                // check if the amount of digits is right
                return getDecimalFormattedString(
                    if (dlen - (i + 1) + len > digits) "" else SpannableStringBuilder(
                        source,
                        start,
                        end
                    ).toString()
                )
            }
        }
        for (i in start until end) {
            if (source[i] == decimalSymbol()) {
                // being here means, dot has been inserted
                // check if the amount of digits is right
                return if (dlen - dend + (end - (i + 1)) > digits) "" else break // return new SpannableStringBuilder(source,
                // start, end);
            }
        }

        // if the dot is after the inserted part,
        // nothing can break
        return getDecimalFormattedString(SpannableStringBuilder(source, start, end).toString())
    }
}

fun getDecimalFormattedString(value: String?): String {
    Log.i("Formatter","value=$value")
    if (value != null && !value.equals("", ignoreCase = true)) {
        val decimalSymbol = decimalSymbol()
        val decimalSymbolString = decimalSymbol.toString()
        val lst = StringTokenizer(value, decimalSymbolString)
        var str1: String = value
        var str2 = ""
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken()
            str2 = lst.nextToken()
        }
        var str3 = ""
        var i = 0
        var j = -1 + str1.length
        if (str1[-1 + str1.length] == decimalSymbol) {
            j--
            str3 = decimalSymbolString
        }
        var k = j
        while (true) {
            if (k < 0) {
                if (str2.isNotEmpty()) str3 = "$str3$decimalSymbol$str2"
                return str3
            }
            str3 = str1[k].toString() + str3
            i++
            k--
        }
    }
    return ""
}
fun removeErrorTextWatcher(editText: EditText, til: TextInputLayout) = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        til.clearError()
    }

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)
        editText.addTextChangedListener(this)
    }
}

fun percentageTextWatcher(editText: EditText) = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {
        var current = ""
        s.let { _ ->
            if (s.toString() != current) {
                editText.removeTextChangedListener(this)
                var filtered = s.toString().filter { "0123456789".contains(it) }
                if (filtered.isBlank()) {
                    filtered = "0"
                }
                if (filtered.toInt() > 100) {
                    filtered = "100"
                }

                val formatted = filtered
                current = formatted
                editText.setText(formatted)
                editText.setSelection(formatted.length)

                editText.addTextChangedListener(this)
            }
        }
    }
}

fun Context.bitmapToFile(bitmap: Bitmap): File {
    // Get the context wrapper
    val wrapper = ContextWrapper(applicationContext)

    // Initialize a new file instance to save bitmap object
    var file = wrapper.getDir("image", Context.MODE_PRIVATE)
    file = File(file, "${UUID.randomUUID()}.jpg")

    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return file
}
