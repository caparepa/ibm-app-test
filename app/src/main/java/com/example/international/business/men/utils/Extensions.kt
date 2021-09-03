package com.example.international.business.men.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.example.international.business.men.BuildConfig
import com.example.international.business.men.utils.library.OnOneOffClickListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Type
import java.math.RoundingMode

inline fun <reified T : Any> Any?.mapTo(newClass: Class<T>): T? =
    Gson().run {
        fromJson(toJson(this@mapTo), newClass)
    }

/**
 * Parser extensions
 */
fun Any.toJsonString(): String? {
    justTry {
        val gson = Gson()
        return gson.toJson(this)
    }
    return null
}

fun <T> String.objectFromJson(classOfT: Class<T>): Any? {
    val gson = Gson()
    return gson.fromJson<Any>(this, classOfT as Class<*>)
}

inline fun <reified T : Any> String.toKotlinObject(): T {
    val gson = Gson()
    return gson.fromJson(this, T::class.java)
}

inline fun <reified T> parseArray(json: String, typeToken: Type): T {
    val gson = GsonBuilder().create()
    return gson.fromJson<T>(json, typeToken)
}

inline fun <reified T : Any> JsonObject.toKotlinObject(): T {
    val gson = Gson()
    return gson.fromJson(this, T::class.java)
}

inline fun <reified T : Any> JsonArray.toKotlinObject(): T {
    val gson = Gson()
    return gson.fromJson(this, T::class.java)
}


/**
 * Context Extensions
 */
fun Context.toastLong(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.toastShort(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

/**
 * General extensions
 */

fun getClassName(anyClass: Any): String? {
    return anyClass::class.java.simpleName
}

inline fun <T> justTry(block: () -> T) = try {
    block()
} catch (e: Throwable) {
}

inline fun debugMode(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

/**
 * Logger extension
 */
fun logger(mode: Int, tag: String, message: String, t: Throwable? = null) {

    if (t == null) {
        when (mode) {
            Log.DEBUG -> Log.d(tag, message)
            Log.ERROR -> Log.e(tag, message)
            Log.INFO -> Log.i(tag, message)
            Log.VERBOSE -> Log.v(tag, message)
            Log.WARN -> Log.w(tag, message)
            else -> Log.wtf(tag, message)
        }
    } else {
        when (mode) {
            Log.DEBUG -> Log.d(tag, message, t)
            Log.ERROR -> Log.e(tag, message, t)
            Log.INFO -> Log.i(tag, message, t)
            Log.VERBOSE -> Log.v(tag, message, t)
            Log.WARN -> Log.w(tag, message, t)
            else -> Log.wtf(tag, message, t)
        }
    }
}


fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setOpacity(percent: Int) {
    this.background.alpha = percent
}

/**
 * File & image handling
 */
@Throws(IOException::class)
fun copyStream(input: InputStream, output: OutputStream, bufferSize: Int): Long {
    val buffer = ByteArray(bufferSize)
    var totalByteCount: Long = 0
    var justReadCount: Int
    while (-1 != input.read(buffer, 0, bufferSize).also { justReadCount = it }) {
        output.write(buffer, 0, justReadCount)
        totalByteCount += justReadCount.toLong()
    }
    return totalByteCount
}

fun Uri.getName(context: Context): String? {
    val returnCursor = context.contentResolver.query(this, null, null, null, null)
    val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor?.moveToFirst()
    val fileName = returnCursor?.getString(nameIndex!!)
    returnCursor?.close()
    return fileName
}

fun View.setOneOffClickListener(action: () -> Unit) {
    setOnClickListener(object : OnOneOffClickListener() {
        override fun onSingleClick(v: View?) {
            action.invoke()
        }
    })
}

fun delayAction(delay: Long, action: () -> Unit) = CoroutineScope(Dispatchers.Main).launch {
    kotlinx.coroutines.delay(delay)
    action.invoke()
}

/*fun <T> MutableLiveData<T>.toSingleEvent(): MutableLiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}*/

//this is for checking if the datetime string we receive from api has time offset (+00:00)
//or is simple ISO 8601 format (Z)

/**
 * This makes the validation and regex replace
 */
fun String.getFormattedDateTime(): String {
    //val regexIso8601 = Regex("(\\d{4}-\\d{1,2}-\\d{1,2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,6}\\W\\d{2}:\\d{2})")
    val regexCustomIso8601 =
        Regex("(\\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2}:\\d{2}.\\d{1,6}\\W\\d{2}:\\d{2})")
    val regexCustomIso8601NoNano =
        Regex("(\\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2}:\\d{2}\\W\\d{2}:\\d{2})")

    return if (this.contains(regexCustomIso8601) xor this.contains(regexCustomIso8601NoNano)) {
        this.pregReplaceSpaceDateTime()
    } else {
        this
    }
}

/**
 * Validate whether the API date is timezoned or not
 * TODO: this might be deprecated
 */
fun String.validateDateTimeOffset(): Boolean {
    val regex = Regex("([+-]\\d{2}:\\d{2})")
    return this.contains(regex)
}

/**
 * Replaces the space for a T in the date given by the api
 * TODO: beware this method, it might change dependin on how API returns the dates
 */
fun String.pregReplaceSpaceDateTime(): String {
    return Regex(" ").replace(this, "T")
}

/**
 * View extensions
 */
fun View.isVisible() = visibility == View.VISIBLE

fun View.isViewGone() = visibility == View.GONE

fun View.makeVisibleAlpha(duration: Long, endAction: () -> Unit = {}) {
    if (!isVisible()) makeVisible()
    if (alpha != 0f) alpha = 0f
    animate().alpha(1f).setDuration(duration).setInterpolator(AccelerateInterpolator())
        .withEndAction {
            endAction.invoke()
        }
}

fun View.makeInvisibleAlpha(duration: Long, endAction: () -> Unit = {}) {
    animate().alpha(0f).setDuration(duration).setInterpolator(AccelerateInterpolator())
        .withEndAction {
            endAction.invoke()
        }
}

fun View.makeGoneAlpha(duration: Long, endAction: () -> Unit = {}) {
    animate().alpha(0f).setDuration(duration).setInterpolator(AccelerateInterpolator())
        .withEndAction {
            endAction.invoke()
            makeGone()
        }
}

fun View.animateAlpha(duration: Long, value: Float, endAction: () -> Unit = {}) =
    animate().alpha(value).setDuration(duration).setInterpolator(AccelerateInterpolator())
        .withEndAction {
            endAction.invoke()
        }

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

/**
 * Math functions
 */

fun Double.roundToHalfEven(): Double {
    return this.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
}