package act_get_empregos.frag_porcentagem.core

import act_get_empregos.frag_porcentagem.*
import android.content.Context

interface MVPFragPorcentagem {

    interface ModelFragPorcentagemImpl {
        fun setPorcNormal(porc: Int)
        fun setPorcCompleta(porc: Int)
        fun setValorNormal(valor: Double)
        fun setValorCompleto(valor: Double)
        fun resetHorasDiff()
        fun getHorasDiff(): ArrayList<HoraDiferDto>
    }

    interface PresenterFragPorcentagemImpl {
        fun resetItemAdapter(dia: Int)
        fun setPorcNormal(porc: Int, valor: Double)
        fun setPorcCompleta(porc: Int, valor: Double)
        fun setPorcDif(pos: Int, porc: Int, valor: Double)
        fun provideAdapterHorasDifer(): RecAdapterHorasDiferenciais
        fun updatePorcentagemDto(porcentagensDto: FragPorcentagensDto)
    }

    interface ViewFragPorcentagemImpl {
        fun refreshTVPorcNormal(porc: String, valor: String)
        fun refreshTVPorcCompleta(porc: String, valor: String)
        fun provideContext(): Context
        fun showOptionDialogHorasDiff(pos: Int)
        fun showGetPorcDiffValorDialog(pos: Int, porc: Int)
    }
}