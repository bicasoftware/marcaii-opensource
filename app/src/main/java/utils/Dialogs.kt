package utils

import android.app.Dialog
import android.content.Context
import exemple.sha.horas.R
import org.jetbrains.anko.*

fun Context.yesNoDialog(messageId: Int, onConfirm: () -> Unit) {
    this.alert(messageId) {
        positiveButton(R.string.str_sim) { onConfirm() }
        negativeButton(R.string.str_nao) {}
    }.show()
}

fun Context.selectorDialog(messageId: Int, optionsId: Int, onItemSelected: (i: Int) -> Unit) {
    val message = resources.getString(messageId)
    val options = resources.getStringArray(optionsId).asList()
    alert {
        selector(message, options) { _, i -> onItemSelected(i) }
    }
}

fun Context.buildIndeterminateProgressDialog(): Dialog {
    return indeterminateProgressDialog(R.string.str_aguarde, R.string.str_lendo_dados)
}