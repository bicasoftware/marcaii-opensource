package act_get_empregos.core

import act_get_empregos.frag_emprego.FragEmpregoDto
import act_get_empregos.frag_porcentagem.FragPorcentagensDto
import android.content.Intent
import android.support.v4.app.Fragment
import utils_realm.REmpregos
import java.math.BigDecimal
import java.math.BigInteger

interface MVPGetEmpregos {

    interface ViewActGetEmpregos {
        fun showSnackBar(msg: Int)
        fun setSalario(valor: BigDecimal)
        fun setPorcentagemNormal(porc: Int, valor: Double)
        fun setPorcentagemCompleta(porc: Int, valor: Double)
        fun setPorcentagemDto(porcentagensDto: FragPorcentagensDto)
        fun setDiaFechamento(dia: Int)
        fun resetPorcentDifByPos(pos: Int)
        fun updatePorcentDifByPos(pos: Int, percent: Int, valor: Double)
        fun showDialogSalario(salario: BigDecimal)
        fun showBtsGetSalario(salario: Double)
        fun showActSalarios(idEmprego: String)
    }

    interface PresenterActGetEmpregosImpl {
        fun closeRealm()
        fun provideEmprego(): REmpregos

        fun isInsert(): Boolean
        fun provideTabs(): ArrayList<Pair<Fragment, String>>
        fun isValidData(): Boolean
        fun getIntentForInsert(): Intent
        fun getIntentForBackPress(): Intent

        fun getPorcNormal(): Int
        fun getPorcCompleta(): Int

        fun setBancoHoras(status: Boolean)
        fun setCargaHoraria(pos: Int)
        fun setDiaFechamento(dia: BigInteger?)
        fun setHorarioSaida(hs: String)
        fun setNomeEmprego(nomeEmprego: String)
        fun setValorSalario(valor: BigDecimal?)
        fun appendAumento(mes: Int, ano: Int, valor: Double)
        fun setPorcNormal(porc: BigInteger?)
        fun setPorcCompleta(porc: BigInteger?)
        fun resetPorcentDifByPos(pos: Int)
        fun updatePorcentDif(pos: Int, porcent: BigInteger?)
        fun providePorcentagemDto(): FragPorcentagensDto
        fun provideSalario(): Double
        fun handleOnSalarioClick()
    }

    interface ModelActGetEmpregosImpl {
        fun provideEmprego(): REmpregos
        fun commitEmprego()

        fun getPorcNormal(): Int
        fun getPorcCompleta(): Int

        fun setBancoHoras(status: Boolean)
        fun setCargaHoraria(cargaHoraria: String)
        fun setDiaFechamento(dia: Int)
        fun setHorarioSaida(hs: String)
        fun setNomeEmprego(nome: String)
        fun setValorSalario(valor: Double)
        fun appendAumento(vigencia: String, valor: Double)
        fun setPorcNormal(porc: Int)
        fun setPorcCompleta(porc: Int)
        fun setPorcentDif(diaSemana: Int, porcent: Int)
        fun removePercentDifByPos(diaSemana: Int)
        fun providePorcentagemDto(): FragPorcentagensDto
        fun provideEmpregoDto(): FragEmpregoDto
        fun provideSalario(): Double

        fun calcValorNormal(): Double
        fun calcValorCompleto(): Double
        fun calcPorcentDif(porcent: Int): Double
        fun closeRealm()
    }

}