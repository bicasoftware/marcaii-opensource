package utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


enum class DateFormats(val simpleFormat: SimpleDateFormat) {
    BR(SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))),
    GLOBAL(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())),
    TIME(SimpleDateFormat("HH:mm", Locale("pt", "BR")))
}

val brFormat = DateFormats.BR.simpleFormat
val defFormat = DateFormats.GLOBAL.simpleFormat

fun dateToString(data: Date, format: DateFormats = DateFormats.GLOBAL): String {
    return format.simpleFormat.format(data)
}

fun dateStringToBrDateString(dateString: String): String {
    return brFormat.format(defFormat.parse(dateString))
}

fun dateToNiceString(date: Date): String{
    return SimpleDateFormat("dd/MMMM", Locale("pt", "BR")).format(date)
}

fun String.stringDateToWeekDay(): Int {
    return defFormat.parse(this@stringDateToWeekDay).weekDay()
}

fun String.dateStringAsDate(): Date {
    return defFormat.parse(this)
}

fun String.timeStringAsDate(): Date {
    return DateFormats.TIME.simpleFormat.parse(this)
}

fun Date.weekDay(): Int {
    val c = Calendar.getInstance()
    c.time = this
    return c.get(Calendar.DAY_OF_WEEK)
}

val emptyDate: Date by lazy {
    defFormat.parse("1988-01-01")
}

fun Date.day(): String {
    return SimpleDateFormat("dd", Locale.getDefault()).format(this)
}

@Suppress("unused")
fun Date.year(): String {
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(this)
}

fun Date.month(): String {
    return SimpleDateFormat("MMMM", Locale.getDefault()).format(this)
}

fun getHorasEMinutos(HoraIni: String, HoraEnd: String): Int {
    val fmt = DateFormats.TIME.simpleFormat

    val dIni = fmt.parse(HoraIni)
    val dEnd = fmt.parse(HoraEnd)

    return if(dIni <= dEnd) {
        TimeUnit.MILLISECONDS.toMinutes(dEnd.time - dIni.time).toInt()
    } else {
        0
    }
}

fun Int.minutesToHours(): String {
    val horas = this / 60
    val m = this % 60
    return String.format("%d:%02d", horas, m)
}

fun getPeriodoMes(dia: Int, mes: Int, ano: Int): Pair<Date, Date> {

    /**
     * o mes util sempre começa um dia depois do dia de fechamento
     * portanto, adiciona 1 dia e subtrai 1 mes para a data inicial
     * assim, as horas do mes começam 1 dia depois do dia o fechamento e começando no mes anterior
     * de 26/12/2015 até 25/01/2016
     */
    val dataIni = Calendar.getInstance().apply {
        set(Calendar.YEAR, ano)
        set(Calendar.MONTH, mes - 1)
        set(Calendar.DAY_OF_MONTH, dia + 1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    val dataEnd = Calendar.getInstance().apply {
        time = dataIni
        add(Calendar.MONTH, 1)
        set(Calendar.DAY_OF_MONTH, dia)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    return Pair(dataIni, dataEnd)
}

