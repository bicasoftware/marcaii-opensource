package act_presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import asCurrency
import br.sha.commommodule.BaseFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_act_presentation_frag_salario.*
import utils.*

class FragPresentSalario: BaseFragment(R.layout.lay_act_presentation_frag_salario) {

    private lateinit var contract: PresentationContract
    private val emptySalario = 1200.0

    private fun getSalario(): Double {
        return arguments?.getDouble(CONST_PRESENT_SALARIO) ?: emptySalario
    }

    companion object {
        const val CONST_PRESENT_SALARIO = "CONSTPRESENTSALARIO"
        fun newInstance(sal: Double): FragPresentSalario = instanceBuilder {
            putDouble(CONST_PRESENT_SALARIO, sal)
        }
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {

        tv_present_salario.putText(getSalario().asCurrency())

        ll_present_salario.onClicked {
            contract.showSalarioDialog()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(CONST_PRESENT_SALARIO, getSalario())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            updateSalario(savedInstanceState.getDouble(CONST_PRESENT_SALARIO))
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contract = context as PresentationContract
    }

    fun updateSalario(newSal: Double) {
        arguments?.putDouble(CONST_PRESENT_SALARIO, newSal)
        tv_present_salario.putText(newSal.asCurrency())
    }
}