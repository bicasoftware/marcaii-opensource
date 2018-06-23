package act_get_empregos.frag_emprego.core

import act_get_empregos.frag_emprego.FragEmpregoDto


class ModelFragEmprego(
    private val presenter: MVPFragEmprego.PresenterFragEmpregoImpl,
    private val empregoDto: FragEmpregoDto
):
    MVPFragEmprego.ModelFragEmpregoImpl {

    override fun getNomeEmprego() = empregoDto.nomeEmprego

    override fun getValorSalario() = empregoDto.valorSalario

    override fun getCargaHoraria() = empregoDto.cargaHoraria

    override fun getDiaFechamento() = empregoDto.diaFechamento

    override fun getHorarioSaida() = empregoDto.horaSaida

    override fun getBancoHora() = empregoDto.bancoHoras
}