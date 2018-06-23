package act_list_horas

import android.content.Context
import asCurrency
import models.*
import utils.*
import utils.StringUtils.padRight

class RelatorioFileBuilder {

    companion object {

        fun genCSV(context: Context, content: RelatorioInfoDto): String {
            val mes = content.termino.month()
            val texto = "${genCsvBody(content, context)}\n\n${genCSVTotais(content)}"
            return FileFun.generateFileOnSDAndReturnDir(texto, getFileName(mes, "csv"))
        }

        fun genTXT(context: Context, content: RelatorioInfoDto): String {
            val title = "Relatório Horas Extras - ${content.nomeEmprego}"
            val inicio = dateToString(content.inicio, DateFormats.BR)
            val termino = dateToString(content.termino, DateFormats.BR)
            val periodo = "Relação de horas extras no periodo de $inicio até $termino"
            val mes = content.termino.month()
            val texto = "$title\n\n$periodo\n\n${getTxtHeader()}\n${genTxtBody(content, context)}\n\n${genTxtTotais(content)}"
            return FileFun.generateFileOnSDAndReturnDir(texto, getFileName(mes, "txt"))
        }

        private fun getTxtHeader(): String {
            return padRight("Dia", 20) +
                padRight("Inicio", 20) +
                padRight("Termino", 20) +
                padRight("Valor Receber", 20) +
                padRight("Horas", 20) +
                padRight("Tipo hora", 20)
        }

        private fun getFileName(mes: String, ext: String): String {
            return "Relação-$mes.$ext"
        }

        private fun genTxtBody(content: RelatorioInfoDto, context: Context): String {

            return content.horasInRange.fold("") { old, new ->
                old +
                    padRight(dateToString(new.data, DateFormats.BR), 20) +
                    padRight(new.horaInicial, 20) +
                    padRight(new.horaTermino, 20) +
                    padRight(new.valor.asCurrency(), 20) +
                    padRight(new.minutos.minutesToHours(), 20) +
                    padRight(context.getString(new.horaType.resString), 20) + "\n"
            }
        }

        private fun genCsvBody(content: RelatorioInfoDto, context: Context): String {

            return content.horasInRange.fold("") { texto, horaInfo ->
                texto +
                    dateToString(horaInfo.data, DateFormats.BR) + "," +
                    horaInfo.horaInicial + "," +
                    horaInfo.horaTermino + "," +
                    """\"${horaInfo.valor.asCurrency()}"\""" + "," +
                    horaInfo.minutos.minutesToHours() + "," +
                    context.getString(horaInfo.horaType.resString) + "\n"
            }
        }

        private fun genTxtTotais(content: RelatorioInfoDto): String {
            val totais = (content.totalNormal.quantidade + content.totalCompleto.quantidade + content.totalDifer.quantidade).minutesToHours()
            val valorTotal = (content.totalNormal.total + content.totalCompleto.total + content.totalDifer.total).asCurrency()

            return "" +
                "Horas Normal: " + content.totalNormal.quantidade.minutesToHours() + " horas - Valor: " + content.totalNormal.total.asCurrency() + "\n" +
                "Horas Completas: " + content.totalCompleto.quantidade.minutesToHours() + " horas  - Valor: " + content.totalCompleto.total.asCurrency() + "\n" +
                "Horas Diferenciais: " + content.totalDifer.quantidade.minutesToHours() + " horas  - Valor: " + content.totalDifer.total.asCurrency() + "\n" +
                "Totais: $totais horas -  Valor: $valorTotal"

        }

        private fun genCSVTotais(content: RelatorioInfoDto): String {
            val totais = (content.totalNormal.quantidade + content.totalCompleto.quantidade + content.totalDifer.quantidade).minutesToHours()
            val valorTotal = (content.totalNormal.total + content.totalCompleto.total + content.totalDifer.total).asCurrency()

            return "" +
                "Horas Normal,${content.totalNormal.quantidade.minutesToHours()},${content.totalNormal.total.asCurrency()};" +
                "Horas Completas,${content.totalCompleto.quantidade.minutesToHours()},${content.totalCompleto.total.asCurrency()};" +
                "Horas Diferenciais,${content.totalDifer.quantidade.minutesToHours()},${content.totalDifer.total.asCurrency()};" +
                "Totais,$totais horas,$valorTotal"
        }

    }
}