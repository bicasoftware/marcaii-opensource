import java.math.BigDecimal
import java.text.NumberFormat

fun Double.asCurrency(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}

fun BigDecimal.asCurrency(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}