package com.akb.ownews.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.akb.ownews.R
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

const val emptyString = ""
const val emptyInt = -1

fun Context?.color(@ColorRes colorRes: Int) =
    this?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT

var TextView.textOrNull: CharSequence?
    get() = text.orEmpty
    set(value) = textOrNull(value)

fun TextView.textOrNull(
    text: CharSequence?,
    default: String = ""
) {
    this.text = text.orEmpty(default)
}

val CharSequence?.orEmpty get() = orEmpty(emptyString)

fun CharSequence?.orEmpty(
    default: String = emptyString,
    condition: Regex? = null
): CharSequence {
    val regex = condition ?: Regex("^(null|NULL|Null)")
    return if(this?.contains(regex) == true) replace(regex, default) else this ?: default
}

fun Context.alertDialog(
    view: View,
    isCancelable: Boolean = true,
    fullScreen: Boolean = true
): AlertDialog {
    return dialog(view = { view }, isCancelable = isCancelable).create().apply {
        if (fullScreen) {
            val params = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }

            window?.attributes = params
        }
    }
}

fun Context.dialog(
    title: String? = null,
    message: String? = null,
    icon: Drawable? = null,
    centered: Boolean = false,
    isCancelable: Boolean = true,
    style: DialogStyle = DialogStyle.DEFAULT,
    items: Array<String> = emptyArray(),
    view: ((MaterialAlertDialogBuilder) -> View)? = null,
    positiveMessage: String? = null,
    onClickedAction: ((dialog: DialogInterface, position: Int) -> Unit)? = null,
    onMultiChoiceAction: ((dialog: DialogInterface, position: Int, checked: Boolean) -> Unit)? = null
): MaterialAlertDialogBuilder {
    val styleRes =
        if (centered) R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered
        else R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog

    return MaterialAlertDialogBuilder(this, styleRes).apply {
        if (title != null) {
            setTitle(title)
        }
        if (message != null) {
            setMessage(message)
        }
        if (icon != null) {
            setIcon(icon)
        }
        if (view != null) {
            setView(view(this))
        }

        setCancelable(isCancelable)

        if (onClickedAction != null) {
            setPositiveButton(positiveMessage) { dialogInterface, position ->
                onClickedAction(dialogInterface, position)
            }
        }

        when {
            style == DialogStyle.DEFAULT && items.isEmpty() -> return@apply
            style == DialogStyle.SIMPLE && items.isNotEmpty() -> setItems(items) { dialog, which ->
                if (onClickedAction != null) onClickedAction(dialog, which)
            }

            style == DialogStyle.SINGLE && items.isNotEmpty() -> setSingleChoiceItems(
                items,
                0
            ) { dialog, which ->
                if (onClickedAction != null) onClickedAction(dialog, which)
            }

            style == DialogStyle.MULTI && items.isNotEmpty() -> setMultiChoiceItems(
                items,
                booleanArrayOf(false)
            ) { dialog, which, isChecked ->
                if (onMultiChoiceAction != null) onMultiChoiceAction(dialog, which, isChecked)
            }
        }
    }
}

fun ImageView.loadImage(
    source: Any?
) {
    Glide.with(context)
        .load(source ?: R.drawable.img_default)
        .into(this)
}

fun Context?.font(@FontRes fontRes: Int) =
    this?.let { ResourcesCompat.getFont(it, fontRes) }

fun Context?.toast(
    message: CharSequence?,
    length: Int = Toast.LENGTH_SHORT
) {
    this?.let { context ->
        Toast.makeText(context, message, length)?.apply {
            view?.apply {
                findViewById<TextView>(android.R.id.message)?.apply {
                    typeface = font(R.font.octarine_light)
                }
            }
        }?.show()
    }
}

fun getTimeZoneById(id: String = "GMT+07:00"): TimeZone {
    return TimeZone.getTimeZone(id)
}

val applicationTimeZone get() = getTimeZoneById()

fun dateFormatter(format: String = "yyyy-MM-dd"): SimpleDateFormat {
    val locale = Locale("us", "US")

    return SimpleDateFormat(format, locale).apply {
        timeZone = applicationTimeZone
    }
}

fun Activity?.hideKeyboard(view: View) {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

enum class DialogStyle {
    DEFAULT, SIMPLE, SINGLE, MULTI
}