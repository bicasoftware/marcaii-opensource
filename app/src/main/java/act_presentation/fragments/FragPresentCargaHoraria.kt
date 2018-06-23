package act_presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import br.sha.commommodule.BaseFragment
import br.sha.commommodule.BaseSpinnerAdapter
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_act_presentation_frag_carga_horaria.*
import utils.instanceBuilder

class FragPresentCargaHoraria: BaseFragment(R.layout.lay_act_presentation_frag_carga_horaria) {

    private lateinit var contract: PresentationContract

    companion object {
        fun newInstance() = instanceBuilder<FragPresentCargaHoraria> { }
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {
        sp_present_carga_horaria.adapter = CargaHorariaAdapter()

        sp_present_carga_horaria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                contract.onCargaHorariaChanged(position)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contract = context as PresentationContract
    }

    inner class CargaHorariaAdapter: BaseSpinnerAdapter(context!!) {

        private val cargas: Array<String> by lazy {
            context!!.resources.getStringArray(R.array.arr_cargas_horarias)
        }

        override fun getItemCount() = cargas.size

        override fun bindItemText(componente: TextView, p: Int) {
            componente.text = cargas[p]
        }

        override fun bindSubItemText(componente: TextView, p: Int) {
            componente.text = cargas[p]
        }
    }
}


