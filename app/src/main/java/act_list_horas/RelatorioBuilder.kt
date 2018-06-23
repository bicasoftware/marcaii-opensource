package act_list_horas

import io.realm.Realm
import io.realm.kotlin.where
import main_activity.frag_calendario.calendario.CalendarBuilder.getValorHora
import models.*
import utils.dateToString
import utils.getPeriodoMes
import utils_realm.REmpregos
import java.util.*

object RelatorioBuilder {

    fun buildRelatorio(idEmprego: String, ano: Int, mes: Int): RelatorioInfoDto {
        val r = Realm.getDefaultInstance()
        val emprego = r.where<REmpregos>().equalTo("id", idEmprego).findFirst() ?: throw Exception("Emprego inválido")
        val (dtInicio, dtTermino) = getPeriodoMes(emprego.diaFechamento, mes, ano)
        val c = Calendar.getInstance().apply { time = dtInicio }
        val horas = emprego.horas
        val relatorioInfoDto = RelatorioInfoDto(
            dtInicio,
            dtTermino,
            emprego.nomeEmprego,
            emprego.id
        )

        while(c.time <= dtTermino) {

            val dta = dateToString(c.time)
            if(horas.count { it.dta == dta } > 0) {
                val hora = horas.first { it.dta == dta }
                if(hora != null) {
                    val horaType = HoraType.values().first { it.tipo == hora.tipoHora }
                    val valor = getValorHora(emprego, hora)

                    when(horaType) {
                        HoraType.NORMAL -> relatorioInfoDto.totalNormal.apply {
                            quantidade += hora.quantidade
                            total += valor
                        }
                        HoraType.COMPLETA -> relatorioInfoDto.totalCompleto.apply {
                            quantidade += hora.quantidade
                            total += valor
                        }
                        HoraType.DIFF -> relatorioInfoDto.totalDifer.apply {
                            quantidade += hora.quantidade
                            total += valor
                        }
                        else -> {
                        }
                    }

                    relatorioInfoDto.horasInRange.add(
                        HoraInfoDto(
                            data = c.time,
                            idHora = hora.id,
                            horaType = horaType,
                            horaInicial = hora.horaInicial,
                            horaTermino = hora.horaTermino,
                            minutos = hora.quantidade,
                            valor = valor
                        )
                    )
                }
            }

            c.add(Calendar.DAY_OF_MONTH, 1)
        }

        r.close()
        return relatorioInfoDto
    }
}