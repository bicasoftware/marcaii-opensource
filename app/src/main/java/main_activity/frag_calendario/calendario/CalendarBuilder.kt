package main_activity.frag_calendario.calendario

import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.experimental.*
import models.HoraInfoDto
import models.HoraType
import utils.*
import utils_realm.REmpregos
import utils_realm.RHoras
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

data class CalendarResult(
    var start: Date = emptyDate,
    var end: Date = emptyDate,
    val content: ArrayList<HoraInfoDto> = arrayListOf()
)

object CalendarBuilder {

    private fun getCalendar(ano: Int, mes: Int) = Calendar.getInstance().apply {
        set(Calendar.YEAR, ano)
        set(Calendar.MONTH, mes)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    fun build(ano: Int, mes: Int): CalendarResult {
        val c = getCalendar(ano, mes)

        val start = Calendar.getInstance().apply {
            time = c.time
            add(Calendar.DAY_OF_MONTH, -1)
        }.time

        val result = CalendarResult(start)

        val m = c.get(Calendar.MONTH)
        while(c.get(Calendar.MONTH) == m) {
            result.content.add(HoraInfoDto(c.time))
            c.add(Calendar.DAY_OF_MONTH, 1)
        }

        result.end = c.time
        return result
    }

    fun buildByEmprego(mes: Int, ano: Int, idEmprego: String): ArrayList<HoraInfoDto> {
        val diasMes = arrayListOf<HoraInfoDto>()
        val r = Realm.getDefaultInstance()
        val e = r
            .where<REmpregos>()
            .equalTo("id", idEmprego)
            .findFirst() ?: throw Exception("Emprego não encontrado")

        val c = getCalendar(ano, mes)
        val m = c.get(Calendar.MONTH)

        while(c.get(Calendar.MONTH) == m) {
            val t = dateToString(c.time)
            if(e.horas.count { it.dta == t } > 0) {
                val hora = e.horas.first { it.dta == t }
                diasMes.add(
                    HoraInfoDto(
                        data = c.time,
                        idHora = hora.id,
                        horaType = HoraType.values().first { it.tipo == hora.tipoHora },
                        horaInicial = hora.horaInicial,
                        horaTermino = hora.horaTermino,
                        minutos = hora.quantidade,
                        valor = getValorHora(e, hora)
                    )
                )
            } else {
                diasMes.add(HoraInfoDto(c.time))
            }

            c.add(Calendar.DAY_OF_MONTH, 1)
        }

        r.close()
        return diasMes
    }

    fun getValorHora(e: REmpregos, hora: RHoras): BigDecimal {
        val salario = e
            .salarios
            .filter { it.vigencia <= hora.dta }
            .sortedByDescending { it.vigencia }
            .first()
            .valorSalario

        val porcentagem = when(hora.tipoHora) {
            HoraType.NORMAL.tipo -> e.porcNormal
            HoraType.COMPLETA.tipo -> e.porcFeriados
            HoraType.DIFF.tipo -> {
                e.porcentagemDiferenciadas
                    .first { it.diaSemana == hora.dta.stringDateToWeekDay() - 1 }
                    .porcAdicional
            }

            else -> 0
        }

        val valorHora = CalculoHorasHelper.calcValorHora(
            salario = salario,
            cargaHoraria = e.cargaHoraria.toInt(),
            porcentagem = porcentagem
        )
        val valorMinuto = CalculoHorasHelper.calcValorMinutoExtra(valorHora)
        return CalculoHorasHelper.calcTotalReceber(valorMinuto, hora.quantidade)
    }
}