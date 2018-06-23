package utils

import android.support.v4.app.FragmentManager
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder
import exemple.sha.horas.R
import java.math.BigDecimal

object DialogUtils {

    fun showNumberDialog(
        fragMan: FragmentManager,
        labelText: String = "Valor",
        visibleValue: Int = 100,
        minValue: Int = 0,
        maxValue: Int = 999999,
        ref: Int
    ) {
        NumberPickerBuilder().apply {
            setFragmentManager(fragMan)
            setLabelText(labelText)
            setMinNumber(BigDecimal(minValue))
            setMaxNumber(BigDecimal(maxValue))
            setCurrentNumber(visibleValue)
            setStyleResId(R.style.BetterPickersDialogFragment_Light)
            setReference(ref)
        }.show()
    }

    fun showDialogSalario(
        fragMan: FragmentManager,
        minVal: Double,
        currVal: Double = 0.0,
        curBigDVal: BigDecimal = BigDecimal.valueOf(0.0),
        ref: Int
    ) {
        NumberPickerBuilder().apply {
            setFragmentManager(fragMan)
            setLabelText("reais")
            setMinNumber(BigDecimal(minVal))
            setMaxNumber(BigDecimal("99999.99"))
            if(curBigDVal > BigDecimal.valueOf(0.0)) {
                setCurrentNumber(curBigDVal)
            } else {
                setCurrentNumber(BigDecimal(currVal))
            }
            setStyleResId(R.style.BetterPickersDialogFragment_Light)
            setReference(ref)
        }.show()
    }

}