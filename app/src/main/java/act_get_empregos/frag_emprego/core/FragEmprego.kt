package act_get_empregos.frag_emprego.core

import act_get_empregos.ActGetEmpregoContract
import act_get_empregos.frag_emprego.FragEmpregoDto
import act_get_empregos.frag_emprego.MVTagGetEmprego.TAGBANCOHORAS
import act_get_empregos.frag_emprego.MVTagGetEmprego.TAGCARGAHORARIA
import act_get_empregos.frag_emprego.MVTagGetEmprego.TAGDIAFECHAMENTO
import act_get_empregos.frag_emprego.MVTagGetEmprego.TAGHORASAIDA
import act_get_empregos.frag_emprego.MVTagGetEmprego.TAGNOMEEMPREGO
import act_get_empregos.frag_emprego.MVTagGetEmprego.TAGVALORSALARIO
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.View
import asCurrency
import br.sha.commommodule.multiview.containers.FragmentMultiView
import br.sha.commommodule.multiview.dsl.*
import br.sha.commommodule.multiview.multiview.*
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_empregos_act_get_emprego_emprego.*
import utils.instanceBuilder
import java.math.BigDecimal


class FragEmprego:
    FragmentMultiView(R.layout.lay_frag_empregos_act_get_emprego_emprego),
    MVPFragEmprego.ViewFragEmpregoImpl,
    EditTextTypeListener,
    SwitchTypeListener,
    SpinnerTypeListener {

    private lateinit var presenter: PresenterFragEmprego

    private lateinit var actGetEmpregos: ActGetEmpregoContract

    companion object {

        private const val CONST_EMPREGO_DTO = "EMPREGO_DTO"

        fun newInstance(empregoDto: FragEmpregoDto): FragEmprego = instanceBuilder {
            putParcelable(CONST_EMPREGO_DTO, empregoDto)
        }
    }

    override fun afterCreated(b: Bundle?) {
        presenter = PresenterFragEmprego(
            view = this,
            empregoDto = arguments?.getParcelable(CONST_EMPREGO_DTO) ?: FragEmpregoDto()
        )
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        actGetEmpregos = context!! as ActGetEmpregoContract
    }

    override fun provideContext(): Context {
        return context!!
    }

    override fun setMultiviewAdapter(adapter: MultiViewAdapter) {
        rv_get_emprego.layoutManager = LinearLayoutManager(context)
        rv_get_emprego.adapter = adapter
    }

    override fun prepareViewList(view: MultiView) {
        view.also {
            it += editTextType(TAGNOMEEMPREGO) {
                hint = R.string.str_nome_emprego
                text = presenter.getNomeEmprego()
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                listener = this@FragEmprego
                digits = DigitsHelper.nameDigits
            }

            it += dialogType(TAGVALORSALARIO) {
                title = R.string.str_valor_salario
                value = presenter.getValorSalario().asCurrency()
                onClick = View.OnClickListener {
                    actGetEmpregos.showDialogOptionsSalario()
                }
            }

            it += dialogType(TAGDIAFECHAMENTO) {
                title = R.string.dia_do_fechamento
                value = presenter.getDiaFechamento().toString()
                onClick = View.OnClickListener {
                    actGetEmpregos.showDialogDiaFechamento(presenter.getDiaFechamento())
                }
            }

            it += dialogType(TAGHORASAIDA) {
                title = R.string.horario_de_saida
                value = presenter.getHorarioSaida()
                onClick = View.OnClickListener { actGetEmpregos.showDialogHoraSaida(presenter.getSplitHorarioSaida()) }
            }

            it += switchType(TAGBANCOHORAS) {
                title = R.string.str_banco_horas
                checked = presenter.getBancoHora()
                listener = this@FragEmprego
            }

            it += spinnerType(TAGCARGAHORARIA) {
                title = R.string.str_carga_horaria
                adapter = presenter.provideCargaHorariaAdapter()
                pos = presenter.getPosByCargaHoraria()
                listener = this@FragEmprego
            }
        }
    }

    override fun onEditTextItemChanged(tag: String, newValue: String) {
        if(tag == TAGNOMEEMPREGO) actGetEmpregos.setNomeEmprego(newValue)
    }

    override fun onSwitchItemChanged(tag: String, checked: Boolean) {
        if(tag == TAGBANCOHORAS) actGetEmpregos.setBancoHoras(checked)
    }

    override fun onItemSelected(tag: String, selectPos: Int) {
        if(tag == TAGCARGAHORARIA) actGetEmpregos.setCargaHoraria(selectPos)
    }

    fun setHorarioSaida(horaSaida: String) {
        putValue(TAGHORASAIDA, horaSaida)
    }

    fun setDiaFechamento(diaFechamento: Int) {
        putValue(TAGDIAFECHAMENTO, diaFechamento.toString())
    }

    fun setValorSalario(valorSalario: BigDecimal) {
        putValue(TAGVALORSALARIO, valorSalario.asCurrency())
    }
}