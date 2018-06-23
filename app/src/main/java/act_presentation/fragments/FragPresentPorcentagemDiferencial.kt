package act_presentation.fragments

import act_get_empregos.frag_porcentagem.*
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.sha.commommodule.BaseFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_act_presentation_frag_diferencial.*
import utils.instanceBuilder

class FragPresentPorcentagemDiferencial: BaseFragment(R.layout.lay_act_presentation_frag_diferencial), HorasDiferContract {

    private lateinit var contract: PresentationContract

    companion object {
        const val CONST_PORC_DIFER = "CONSTPORCDIFER"

        fun newInstance(porcDifer: ArrayList<HoraDiferDto>) = instanceBuilder<FragPresentPorcentagemDiferencial> {
            putSerializable(CONST_PORC_DIFER, porcDifer)
        }
    }

    private val adapter: RecAdapterHorasDiferenciais by lazy {
        RecAdapterHorasDiferenciais(context!!, getPorcDifer(), this)
    }

    private fun getPorcDifer(): ArrayList<HoraDiferDto> {
        return arguments?.getSerializable(CONST_PORC_DIFER) as ArrayList<HoraDiferDto>
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {
        rv_present_porc_difer.layoutManager = LinearLayoutManager(context)
        rv_present_porc_difer.adapter = adapter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contract = context as PresentationContract
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CONST_PORC_DIFER, getPorcDifer())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            arguments?.putSerializable(CONST_PORC_DIFER, it.getSerializable(CONST_PORC_DIFER))
        }
        adapter.refreshContent(getPorcDifer())
    }

    override fun onDeletePorc(pos: Int) {
        contract.showOptionDialogHorasDiff(pos)
    }

    override fun onItemClicked(pos: Int, porc: Int) {
        contract.showDialogGetPorcDifer(pos, porc)
    }

    fun updatePorcDifer(pos: Int, porc: Int, valor: Double) {
        adapter.updateByPosition(pos, porc, valor)
    }

    fun cleanPorcDifer(pos: Int) {
        adapter.resetItem(pos)
    }
}