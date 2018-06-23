package models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import utils.*
import utils_realm.RHoras
import java.math.BigDecimal
import java.util.*

@Parcelize
data class HoraInfoDto(
    var data: Date = emptyDate,
    var idHora: String = "",
    var horaType: HoraType = HoraType.NONE,
    var horaInicial: String = "18:00",
    var horaTermino: String = "19:00",
    var minutos: Int = 0,
    var valor: BigDecimal = BigDecimal.valueOf(0.0)

): Parcelable {

    val dia: String
        get() = data.day()

    fun clearMe() {
        idHora = ""
        horaType = HoraType.NONE
        horaInicial = "18:00"
        horaTermino = "19:00"
        minutos = 0
        valor = BigDecimal.valueOf(0.0)
    }

    fun copy(horaInfoDto: HoraInfoDto) {
        this.data = horaInfoDto.data
        this.idHora = horaInfoDto.idHora
        this.horaType = horaInfoDto.horaType
        this.horaInicial = horaInfoDto.horaInicial
        this.horaTermino = horaInfoDto.horaTermino
        this.minutos = horaInfoDto.minutos
        this.valor = horaInfoDto.valor
    }

    fun copy(h: RHoras, valor: BigDecimal) {
        this.data = h.data
        this.idHora = h.id
        this.horaType = HoraType.values().first { it.tipo == h.tipoHora }
        this.horaInicial = h.horaInicial
        this.horaTermino = h.horaTermino
        this.minutos = h.quantidade
        this.valor = valor
    }
}