package utils

import java.math.BigDecimal

object CalculoHorasHelper {

    fun calcValorHora(salario: Double, cargaHoraria: Int, porcentagem: Int): BigDecimal {
        val valHora = BigDecimal.valueOf(salario) DIV BigDecimal.valueOf(cargaHoraria.toDouble())
        return valHora.multiply(porcentagem.percent())
    }

    fun calcValorHora(salario: Double, cargaHoraria: String, porcentagem: Int): BigDecimal {
        return when(cargaHoraria){
            "220", "200", "180", "150" -> calcValorHora(salario, cargaHoraria.toInt(), porcentagem)
            else -> TODO("VERIFICAR CALCULO DE 18X36 E 12X36")
        }
    }

    fun calcValorMinutoExtra(valorHora: BigDecimal): BigDecimal = valorHora DIV 60.0.toBigD()

    fun calcTotalReceber(valorMinutoExtra: BigDecimal, minutosExtras: Int): BigDecimal =
        valorMinutoExtra.multiply(minutosExtras.toBigD())

    fun Int.percent(): BigDecimal {
        return (this.toBigD() DIV 100.toBigD()).add(1.0.toBigD())
    }

    private fun Double.toBigD(): BigDecimal = BigDecimal.valueOf(this)

    private fun Int.toBigD(): BigDecimal = BigDecimal.valueOf(this.toDouble())

    private infix fun BigDecimal.DIV(other: BigDecimal): BigDecimal =
        this.divide(other, 4, java.math.BigDecimal.ROUND_HALF_EVEN)
}