package act_presentation.core

import act_get_empregos.frag_porcentagem.HoraDiferDto
import java.math.BigDecimal
import java.math.BigInteger

interface MVPActPresentation {

    interface ViewActPresentImpl{
        fun updateSalario(salario: Double)
        fun updatePorcNormal(porc: Int)
        fun updatePorcCompleta(porc: Int)
        fun updatePorcDifer(pos: Int, porc: Int, valor: Double)
        fun clearPorcDifer(pos: Int)
    }

    interface PresenterActPresentImpl{
        fun closeRealm()
        fun provideSalario(): Double
        fun providePorcNormal(): Int
        fun providePorcCompleta(): Int
        fun providePorcDifer(): ArrayList<HoraDiferDto>

        fun setCargaHoraria(pos: Int)
        fun resetPorcentDifByPos(pos: Int)

        fun commitEmprego()
        fun setPorcNormal(number: BigInteger?)
        fun setPorcCompleta(number: BigInteger?)
        fun setValorSalario(fullNumber: BigDecimal?)
        fun updatePorcentDif(pos: Int, porcent: BigInteger?)
    }

    interface ModelActPresentImpl{
        fun closeRealm()
        fun provideSalario(): Double
        fun providePorcNormal(): Int
        fun providePorcCompleta(): Int
        fun providePorcDifer(): ArrayList<HoraDiferDto>

        fun setSalario(valor: Double)
        fun setCargaHoraria(carga: String)
        fun setPorcNormal(porc: Int)
        fun setPorcCompleta(porc: Int)
        fun setPorcDifer(pos: Int, porc: Int)
        fun clearPorcDifByPos(pos: Int)

        fun calcValorNormal(): Double
        fun calcValorCompleto(): Double
        fun calcPorcentDif(porcent: Int): Double

        fun commitEmprego()
    }
}