package act_get_empregos.core

import act_get_empregos.frag_emprego.core.FragEmprego
import act_get_empregos.frag_porcentagem.core.FragPorcentagem
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import exemple.sha.horas.R
import utils.intent
import utils.selectorDialog
import java.math.BigDecimal
import java.math.BigInteger

class PresenterActGetEmpregos(
    val view: MVPGetEmpregos.ViewActGetEmpregos,
    val idEmprego: String
):
    MVPGetEmpregos.PresenterActGetEmpregosImpl {

    private val model = ModelActGetEmprego(this, idEmprego)

    private val cargasHoraria: Array<String>
        get() = (view as Context).resources.getStringArray(R.array.arr_cargas_horarias)

    override fun closeRealm() {
        model.closeRealm()
    }

    override fun provideEmprego() = model.provideEmprego()

    override fun isInsert() = idEmprego.isBlank()

    val context: Context
        get() = view as Context



    override fun provideTabs(): ArrayList<Pair<Fragment, String>> {
        return arrayListOf(
            Pair(FragEmprego.newInstance(model.provideEmpregoDto()), "Informações"),
            Pair(FragPorcentagem.newInstance(model.providePorcentagemDto()), "Porcentagens")
        )
    }

    override fun isValidData(): Boolean {
        val setFalse = { msg: Int ->
            view.showSnackBar(msg)
            false
        }

        val e = model.provideEmprego()

        return when {
            e.nomeEmprego.isBlank() -> setFalse(R.string.war_nome_requerido)
            else -> true
        }
    }

    override fun getIntentForInsert(): Intent {
        model.commitEmprego()
        return baseIntent()
    }

    override fun getIntentForBackPress(): Intent {
        return baseIntent()
    }

    private fun baseIntent(): Intent {
        return Intent().apply {
            putExtra(ActGetEmpregos.CONST_ID_EMPREGO, model.idEmprego)
        }
    }

    override fun getPorcNormal() = model.getPorcNormal()

    override fun getPorcCompleta() = model.getPorcCompleta()

    override fun setNomeEmprego(nomeEmprego: String) {
        model.setNomeEmprego(nomeEmprego)
    }

    override fun setHorarioSaida(hs: String) {
        model.setHorarioSaida(hs)
    }

    override fun setDiaFechamento(dia: BigInteger?) {
        val d = dia?.toInt() ?: 25
        model.setDiaFechamento(d)
        view.setDiaFechamento(d)
    }

    override fun setValorSalario(valor: BigDecimal?) {
        model.setValorSalario(valor?.toDouble() ?: 0.0)
        view.setSalario(valor ?: BigDecimal(0.0))
        view.setPorcentagemDto(model.providePorcentagemDto())
    }

    override fun appendAumento(mes: Int, ano: Int, valor: Double) {
        model.appendAumento("$ano-$mes-01", valor)
        view.setSalario(BigDecimal(valor))
    }

    override fun setBancoHoras(status: Boolean) {
        model.setBancoHoras(status)
        view.setPorcentagemDto(model.providePorcentagemDto())
    }

    override fun setCargaHoraria(pos: Int) {
        model.setCargaHoraria(cargasHoraria[pos])
        view.setPorcentagemDto(model.providePorcentagemDto())
    }

    override fun setPorcNormal(porc: BigInteger?) {
        val p = porc?.toInt() ?: 30
        model.setPorcNormal(p)
        view.setPorcentagemNormal(p, model.calcValorNormal())
    }

    override fun setPorcCompleta(porc: BigInteger?) {
        val p = porc?.toInt() ?: 100
        model.setPorcCompleta(p)
        view.setPorcentagemCompleta(p, model.calcValorCompleto())
    }

    override fun providePorcentagemDto() = model.providePorcentagemDto()

    override fun provideSalario(): Double {
        return model.provideSalario()
    }

    override fun resetPorcentDifByPos(pos: Int) {
        model.removePercentDifByPos(pos)
        view.resetPorcentDifByPos(pos)
    }

    override fun updatePorcentDif(pos: Int, porcent: BigInteger?) {
        val valor = model.calcPorcentDif(porcent?.toInt() ?: 30)
        model.setPorcentDif(pos, porcent?.toInt() ?: 0)
        view.updatePorcentDifByPos(pos, porcent?.toInt() ?: 30, valor)
    }

    override fun handleOnSalarioClick() {
        if(idEmprego.isNotBlank()) {
            context.selectorDialog(R.string.str_salarios, R.array.arr_salarios) {
                when(it) {
                    0 -> view.showDialogSalario(BigDecimal(model.provideSalario()))
                    1 -> view.showBtsGetSalario(model.provideSalario())
                    2 -> if(idEmprego.isNotBlank()) view.showActSalarios(idEmprego)
                }
            }
        } else {
            view.showDialogSalario(BigDecimal(model.provideSalario()))
        }
    }
}
