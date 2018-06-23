package act_presentation.core

import act_get_empregos.frag_porcentagem.HoraDiferDto
import android.content.Context
import exemple.sha.horas.R
import java.math.BigDecimal
import java.math.BigInteger

class PresenterActPresent(private val view: MVPActPresentation.ViewActPresentImpl): MVPActPresentation.PresenterActPresentImpl {

    private val model = ModelActPresent(this)

    private val cargasHoraria: Array<String> by lazy {
        (view as Context).resources.getStringArray(R.array.arr_cargas_horarias)
    }

    override fun closeRealm() {
        model.closeRealm()
    }

    override fun provideSalario(): Double {
        return model.provideSalario()
    }

    override fun providePorcNormal(): Int {
        return model.providePorcNormal()
    }

    override fun providePorcCompleta(): Int {
        return model.providePorcCompleta()
    }

    override fun providePorcDifer(): ArrayList<HoraDiferDto> {
        return model.providePorcDifer()
    }

    override fun commitEmprego() {
        model.commitEmprego()
    }

    override fun setPorcNormal(number: BigInteger?) {
        val p = number?.toInt() ?: 50
        model.setPorcNormal(p)
        view.updatePorcNormal(p)
    }

    override fun setPorcCompleta(number: BigInteger?) {
        val p = number?.toInt() ?: 100
        model.setPorcCompleta(p)
        view.updatePorcCompleta(p)
    }

    override fun setValorSalario(fullNumber: BigDecimal?) {
        val salario = fullNumber?.toDouble() ?: 1000.0
        model.setSalario(salario)
        view.updateSalario(salario)
    }

    override fun setCargaHoraria(pos: Int) {
        model.setCargaHoraria(cargasHoraria[pos])
    }

    override fun resetPorcentDifByPos(pos: Int) {
        model.clearPorcDifByPos(pos)
        view.clearPorcDifer(pos)
    }

    override fun updatePorcentDif(pos: Int, porcent: BigInteger?) {
        val p = porcent?.toInt() ?: 50
        val valor = model.calcPorcentDif(p)

        model.setPorcDifer(pos, p)
        view.updatePorcDifer(pos, p, valor)
    }
}