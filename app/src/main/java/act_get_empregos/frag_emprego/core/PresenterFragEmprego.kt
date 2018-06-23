package act_get_empregos.frag_emprego.core

import act_get_empregos.frag_emprego.FragEmpregoDto
import act_get_empregos.frag_emprego.SpAdapterCargaHoraria
import br.sha.commommodule.BaseSpinnerAdapter
import exemple.sha.horas.R

class PresenterFragEmprego(
    val view: MVPFragEmprego.ViewFragEmpregoImpl,
    empregoDto: FragEmpregoDto
):
    MVPFragEmprego.PresenterFragEmpregoImpl {

    private var model = ModelFragEmprego(this, empregoDto)

    private val cargasHoraria: Array<String>
        get() = view.provideContext().resources.getStringArray(R.array.arr_cargas_horarias)

    override fun provideCargaHorariaAdapter(): BaseSpinnerAdapter {
        return SpAdapterCargaHoraria(view.provideContext())
    }

    override fun getValorSalario(): Double {
        return model.getValorSalario()
    }

    override fun getCargaHoraria() = model.getCargaHoraria()

    override fun getPosByCargaHoraria(): Int {
        return cargasHoraria.indexOfFirst { it == model.getCargaHoraria() }
    }

    override fun getDiaFechamento(): Int {
        return model.getDiaFechamento()
    }

    override fun getHorarioSaida(): String {
        return model.getHorarioSaida()
    }

    override fun getSplitHorarioSaida(): Pair<Int, Int> {
        val splitTime = model.getHorarioSaida().split(":").map { it.toInt() }
        return Pair(splitTime[0], splitTime[1])
    }

    override fun getBancoHora(): Boolean {
        return model.getBancoHora()
    }

    override fun getNomeEmprego(): String {
        return model.getNomeEmprego()
    }
}