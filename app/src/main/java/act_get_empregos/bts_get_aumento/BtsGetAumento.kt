package act_get_empregos.bts_get_aumento

import android.content.Context
import android.view.View
import android.widget.*
import asCurrency
import br.sha.commommodule.BaseMaterialBottomSheet
import exemple.sha.horas.R
import utils.instanceBuilder

class BtsGetAumento: BaseMaterialBottomSheet(R.layout.lay_frag_empregos_act_get_emprego_bts_aumento) {

    private lateinit var tvSalario: TextView
    private lateinit var spnMes: Spinner
    private lateinit var spnAno: Spinner
    private lateinit var btSalvar: ImageButton
    private lateinit var btCancelar: ImageButton
    private lateinit var llSalario: LinearLayout

    private val salario: Double
        get() = arguments?.getDouble(BUNDLE_SALARIO) ?: 1200.0

    private lateinit var contract: BtsGetAumentoContract

    private val spinnerAdapterAnos: SpinnerAdapterAnosWhite by lazy {
        SpinnerAdapterAnosWhite(activity!!)
    }

    private val spinnerAdapterMeses: SpinnerAdapterMesesWhite by lazy {
        SpinnerAdapterMesesWhite(context!!)
    }

    companion object {
        const val BUNDLE_SALARIO = "BUNDLESALARIO"

        fun newInstance(salarioAnterior: Double) = instanceBuilder<BtsGetAumento> {
            putDouble(BUNDLE_SALARIO, salarioAnterior)
        }
    }

    override fun onAttach(context: Context?) {
        contract = context as BtsGetAumentoContract
        super.onAttach(context)
    }

    override fun prepareView(v: View) {
        inflate(v)
        bindData()
    }

    private fun inflate(v: View) {
        tvSalario = v.findViewById(R.id.tv_aumento_valor)
        spnAno = v.findViewById(R.id.spn_aumento_ano)
        spnMes = v.findViewById(R.id.spn_aumento_mes)
        btSalvar = v.findViewById(R.id.bt_aumento_salvar)
        btCancelar = v.findViewById(R.id.bt_aumento_cancelar)
        llSalario = v.findViewById(R.id.ll_aumento_valor)

        btSalvar.setOnClickListener {
            this.dismiss()
            contract.onSaveAumento(
                mes = spnMes.selectedItemPosition,
                ano = spinnerAdapterAnos.getAno(spnAno.selectedItemPosition),
                valor = arguments!!.getDouble(BUNDLE_SALARIO)
            )
        }

        btCancelar.setOnClickListener {
            this.dismiss()
            contract.onCancel()
        }

        llSalario.setOnClickListener {
            contract.showDialogGetAumento(salario)
        }
    }

    private fun bindData() {
        tvSalario.text = salario.asCurrency()
        spnAno.adapter = spinnerAdapterAnos
        spnMes.adapter = spinnerAdapterMeses
    }

    fun setSalario(valorSalario: Double){
        arguments?.putDouble(BUNDLE_SALARIO, valorSalario)
    }

    fun updateSalario(valorSalario: Double) {
        arguments?.putDouble(BUNDLE_SALARIO, valorSalario)
        tvSalario.text = valorSalario.asCurrency()
    }

}