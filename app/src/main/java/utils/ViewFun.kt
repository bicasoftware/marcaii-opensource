package utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

fun View.showSnack(msg: Int) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun View.showSnack(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.setTint(context: Context, colorId: Int) {
    this.setColorFilter(ContextCompat.getColor(context, colorId))
}

fun View.onClicked(onClick: () -> Unit) {
    launch(UI) {
        this@onClicked.setOnClickListener {
            onClick()
        }
    }
}

fun TextView.putText(text: String) {
    launch(UI) {
        this@putText.text = text
    }
}

inline fun <reified T: Fragment> FragmentManager.findFragment(): T? {
    return this@findFragment.fragments.first { it is T } as T
}