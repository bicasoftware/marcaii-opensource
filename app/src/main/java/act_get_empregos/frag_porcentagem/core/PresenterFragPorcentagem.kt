package act_get_empregos.frag_porcentagem.core

import act_get_empregos.frag_porcentagem.*
import asCurrency

class PresenterFragPorcentagem(
    private val view: MVPFragPorcentagem.ViewFragPorcentagemImpl,
    porcentagemDto: FragPorcentagensDto
):
    MVPFragPorcentagem.PresenterFragPorcentagemImpl, HorasDiferContract {

    init {
        view.refreshTVPorcNormal(porcentagemDto.porcNormal.toString(), porcentagemDto.valorNormal.asCurrency())
        view.refreshTVPorcCompleta(porcentagemDto.porcCompleta.toString(), porcentagemDto.valorCompleta.asCurrency())
    }

    private val model = ModelFragPorcentagem(this, porcentagemDto)

    val adapter: RecAdapterHorasDiferenciais by lazy {
        RecAdapterHorasDiferenciais(view.provideContext(), model.getHorasDiff(), this)
    }

    override fun setPorcNormal(porc: Int, valor: Double) {
        view.refreshTVPorcNormal(porc.toString(), valor.asCurrency())
        model.setPorcNormal(porc)
        model.setValorNormal(valor)
    }

    override fun setPorcCompleta(porc: Int, valor: Double) {
        view.refreshTVPorcCompleta(porc.toString(), valor.asCurrency())
        model.setPorcCompleta(porc)
        model.setValorNormal(valor)
    }

    override fun setPorcDif(pos: Int, porc: Int, valor: Double) {
        adapter.updateByPosition(pos, porc, valor)
    }

    override fun provideAdapterHorasDifer() = adapter

    override fun resetItemAdapter(dia: Int) {
        adapter.resetItem(dia)
    }

    override fun updatePorcentagemDto(porcentagensDto: FragPorcentagensDto) {
        adapter.updateHorasDif(porcentagensDto.horasDiferenciais)
        model.porcentagensDto = porcentagensDto
        view.refreshTVPorcNormal(porcentagensDto.porcNormal.toString(), porcentagensDto.valorNormal.asCurrency())
        view.refreshTVPorcCompleta(porcentagensDto.porcCompleta.toString(), porcentagensDto.valorCompleta.asCurrency())
    }

    override fun onDeletePorc(pos: Int) {
        view.showOptionDialogHorasDiff(pos)
    }

    override fun onItemClicked(pos: Int, porc: Int) {
        view.showGetPorcDiffValorDialog(pos, porc)
    }
}