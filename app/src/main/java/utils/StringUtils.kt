package utils

object StringUtils {

    fun padRight(s: String, n: Int): String {
        return String.format("%1$-" + n + "s", s)
    }

    fun Int.zeroFill(): String {
        return if(this < 10) "0$this" else this.toString()
    }

}

