package models

import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class TotalByType(
    val type: HoraType,
    var quantidade: Int = 0,
    var total: BigDecimal = BigDecimal(0.0)
)

data class RelatorioInfoDto(
    val inicio: Date,
    val termino: Date,
    val nomeEmprego: String,
    val idEmprego: String,
    val totalNormal: TotalByType = TotalByType(HoraType.NORMAL),
    val totalCompleto: TotalByType = TotalByType(HoraType.COMPLETA),
    val totalDifer: TotalByType = TotalByType(HoraType.DIFF),
    val horasInRange: ArrayList<HoraInfoDto> = arrayListOf()
)