package act_get_empregos.frag_porcentagem.core

import act_get_empregos.frag_porcentagem.FragPorcentagensDto

class ModelFragPorcentagem(
    val presenter: MVPFragPorcentagem.PresenterFragPorcentagemImpl,
    var porcentagensDto: FragPorcentagensDto
):
    MVPFragPorcentagem.ModelFragPorcentagemImpl {

    override fun setPorcNormal(porc: Int) {
        porcentagensDto.porcNormal = porc
    }

    override fun setPorcCompleta(porc: Int) {
        porcentagensDto.porcCompleta = porc
    }

    override fun setValorNormal(valor: Double) {
        porcentagensDto.valorNormal = valor
    }

    override fun setValorCompleto(valor: Double) {
        porcentagensDto.valorCompleta = valor
    }

    override fun resetHorasDiff() {
        porcentagensDto.horasDiferenciais.forEach { it.reset() }
    }

    override fun getHorasDiff() = porcentagensDto.horasDiferenciais

}