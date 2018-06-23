package utils

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

inline fun <reified F: Fragment> instanceBuilder(putArguments: Bundle.() -> Unit): F {

    val instance = F::class.java.newInstance()
    return instance.apply {
        arguments = Bundle().apply(putArguments)
    }
}

fun intent(putArguments: Intent.() -> Unit): Intent {
    return Intent().apply {
        putArguments()
    }
}