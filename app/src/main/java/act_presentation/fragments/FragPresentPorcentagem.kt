package act_presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import br.sha.commommodule.BaseFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_act_presentation_frag_porcentagens.*
import utils.*

class FragPresentPorcentagem: BaseFragment(R.layout.lay_act_presentation_frag_porcentagens) {

    private lateinit var contract: PresentationContract

    private fun getPorcNormal(): Int {
        return arguments?.getInt(CONST_PORC_NORMAL) ?: 50
    }

    private fun getPorcCompleta(): Int {
        return arguments?.getInt(CONST_PORC_COMPLETA) ?: 100
    }

    companion object {

        const val CONST_PORC_NORMAL = "CONSTPORCNORMAL"
        const val CONST_PORC_COMPLETA = "CONSTPORCCOMPLETA"

        fun newInstance(porcNormal: Int, porcCompleta: Int) = instanceBuilder<FragPresentPorcentagem> {
            putInt(CONST_PORC_NORMAL, porcNormal)
            putInt(CONST_PORC_COMPLETA, porcCompleta)
        }
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {

        tv_present_porc_normal.putText("${getPorcNormal()} %")
        tv_present_porc_completa.putText("${getPorcCompleta()} %")

        ll_present_porc_normal.setOnClickListener {
            contract.showDialogGetPorcNormal()
        }

        ll_present_porc_completa.setOnClickListener {
            contract.showDialogGetPorcCompleta()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CONST_PORC_NORMAL, getPorcNormal())
        outState.putInt(CONST_PORC_COMPLETA, getPorcCompleta())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null) {
            updatePorcNormal(savedInstanceState.getInt(CONST_PORC_NORMAL))
            updatePorcCompleta(savedInstanceState.getInt(CONST_PORC_COMPLETA))
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contract = context as PresentationContract
    }

    fun updatePorcNormal(porc: Int) {
        arguments?.putInt(CONST_PORC_NORMAL, porc)
        tv_present_porc_normal.putText("$porc %")
    }

    fun updatePorcCompleta(porc: Int) {
        arguments?.putInt(CONST_PORC_COMPLETA, porc)
        tv_present_porc_completa.putText("$porc %")
    }
}