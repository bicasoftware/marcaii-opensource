package act_get_empregos.frag_porcentagem.core

import act_get_empregos.ActGetEmpregoContract
import act_get_empregos.frag_porcentagem.FragPorcentagensDto
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.sha.commommodule.BaseFragment
import br.sha.commommodule.BaseRecyclerViewDivider
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_empregos_act_get_emprego_porcentagem.*
import utils.instanceBuilder

class FragPorcentagem:
    BaseFragment(R.layout.lay_frag_empregos_act_get_emprego_porcentagem),
    MVPFragPorcentagem.ViewFragPorcentagemImpl {

    private lateinit var actGetEmprego: ActGetEmpregoContract
    private lateinit var presenter: PresenterFragPorcentagem

    private val porcentageDto: FragPorcentagensDto
        get() = arguments!!.getParcelable(CONST_PORCENTAGENS)

    companion object {
        private const val CONST_PORCENTAGENS = "PORCENTAGENS"

        fun newInstance(porcentagensDto: FragPorcentagensDto) = instanceBuilder<FragPorcentagem> {
            putParcelable(CONST_PORCENTAGENS, porcentagensDto)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        actGetEmprego = context as ActGetEmpregoContract
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {
        presenter = PresenterFragPorcentagem(this, porcentageDto)

        ll_hora_normal.setOnClickListener { actGetEmprego.showDialogPorcentagemNormal() }
        ll_hora_completa.setOnClickListener { actGetEmprego.showDialogPorcentagemCompleta() }

        rv_horas.layoutManager = LinearLayoutManager(context)
        rv_horas.addItemDecoration(BaseRecyclerViewDivider(context!!))
        rv_horas.adapter = presenter.provideAdapterHorasDifer()
    }

    override fun showOptionDialogHorasDiff(pos: Int) {
        actGetEmprego.showOptionDialogHorasDiff(pos)
    }

    override fun showGetPorcDiffValorDialog(pos: Int, porc: Int) {
        actGetEmprego.showGetPorcDiffValorDialog(pos, porc)
    }

    override fun provideContext(): Context {
        return context!!
    }

    override fun refreshTVPorcCompleta(porc: String, valor: String) {
        tv_hora_completa.text = porc
        tv_valor_completo.text = valor
    }

    override fun refreshTVPorcNormal(porc: String, valor: String) {
        tv_hora_normal.text = porc
        tv_valor_normal.text = valor
    }

    fun updatePorcentagemDto(porcentagensDto: FragPorcentagensDto) {
        arguments?.putParcelable(CONST_PORCENTAGENS, porcentagensDto)
        presenter.updatePorcentagemDto(porcentagensDto)
    }

    fun updatePorcNormal(porc: Int, valor: Double) {
        presenter.setPorcNormal(porc, valor)
    }

    fun updatePorcCompleta(porc: Int, valor: Double) {
        presenter.setPorcCompleta(porc, valor)
    }

    fun resetPorcentDifByPosition(pos: Int){
        presenter.resetItemAdapter(pos)
    }

    fun updatePorcentDif(pos: Int, percent: Int, valor: Double) {
        presenter.setPorcDif(pos, percent, valor)
    }
}
