package act_get_empregos.core

import act_get_empregos.ActGetEmpregoContract
import act_get_empregos.act_list_salarios.ActListaSalario
import act_get_empregos.bts_get_aumento.BtsGetAumento
import act_get_empregos.bts_get_aumento.BtsGetAumentoContract
import act_get_empregos.frag_emprego.core.FragEmprego
import act_get_empregos.frag_porcentagem.FragPorcentagensDto
import act_get_empregos.frag_porcentagem.core.FragPorcentagem
import android.os.Bundle
import br.sha.commommodule.BasePagedDataManageActivity
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_empregos_act_get_emprego.*
import org.jetbrains.anko.startActivity
import utils.*
import utils.DialogUtils.showDialogSalario
import utils.DialogUtils.showNumberDialog
import utils.StringUtils.zeroFill
import java.math.BigDecimal
import java.math.BigInteger

class ActGetEmpregos: BasePagedDataManageActivity(
    layoutId = R.layout.lay_frag_empregos_act_get_emprego,
    titleId = R.string.str_empregos,
    tabLayoutId = R.id.tabLayoutEmpregos,
    viewPagerId = R.id.viewPagerEmprego
),
    RadialTimePickerDialogFragment.OnTimeSetListener,
    NumberPickerDialogFragment.NumberPickerDialogHandlerV2,
    MVPGetEmpregos.ViewActGetEmpregos,
    ActGetEmpregoContract,
    BtsGetAumentoContract {

    private lateinit var presenter: PresenterActGetEmpregos

    companion object {
        const val BUNDLE_IDEMPREGO = "IDEMPREGO"
        const val CONST_ID_EMPREGO = "CONST_ID_EMPREGO"
        private const val refAumento: Int = 8989
        private const val refDia: Int = 101
        private const val refSalario: Int = 102
        private const val refPorcNormal: Int = 103
        private const val refPorcFeriado: Int = 104
    }

    private val tabEmprego: FragEmprego by lazy {
        supportFragmentManager.fragments[0] as FragEmprego
    }

    private val tabPorcentagens: FragPorcentagem by lazy {
        supportFragmentManager.fragments[1] as FragPorcentagem
    }

    private val btsGetAumento: BtsGetAumento by lazy {
        BtsGetAumento.newInstance(presenter.provideSalario())
    }

    override fun preparePagedActivity(b: Bundle?) {
        val idEmprego = b?.getString(BUNDLE_IDEMPREGO, "") ?: ""
        presenter = PresenterActGetEmpregos(this, idEmprego)

        addTabs(presenter.provideTabs())
    }

    override fun onDestroy() {
        presenter.closeRealm()
        super.onDestroy()
    }

    override fun checkInsert() = presenter.isInsert()

    override fun checkDataValidated() = presenter.isValidData()

    override fun getIntentForInsert() = presenter.getIntentForInsert()

    override fun getIntentForBackPress() = presenter.getIntentForBackPress()

    /** ActGetEmpregoContract*/
    override fun showDialogPorcentagemNormal() {
        showDialogPorcentagem(presenter.getPorcNormal(), refPorcNormal)
    }

    override fun showDialogPorcentagemCompleta() {
        showDialogPorcentagem(presenter.getPorcCompleta(), refPorcFeriado)
    }

    override fun showDialogOptionsSalario() {
        presenter.handleOnSalarioClick()
    }

    override fun showDialogHoraSaida(hora: Pair<Int, Int>) {
        RadialTimePickerDialogFragment().apply {
            setOnTimeSetListener(this@ActGetEmpregos)
            setStartTime(hora.first, hora.second)
            setDoneText("Pronto!")
            setCancelText("Cancelar")
        }.show(supportFragmentManager, "")
    }

    override fun showDialogDiaFechamento(diaFechamento: Int) {
        showNumberDialog(
            supportFragmentManager,
            "Fechamento do mês",
            diaFechamento,
            minValue = 0,
            maxValue = 30,
            ref = refDia
        )
    }

    override fun setNomeEmprego(newValue: String) {
        presenter.setNomeEmprego(newValue)
    }

    override fun setBancoHoras(checked: Boolean) {
        presenter.setBancoHoras(checked)
    }

    override fun setCargaHoraria(selectPos: Int) {
        presenter.setCargaHoraria(selectPos)
    }
    /** END ActGetEmpregoContract */

    /** ViewActGetEmpregos */
    override fun showSnackBar(msg: Int) {
        viewPagerEmprego.showSnack(msg)
    }

    override fun setSalario(valor: BigDecimal) {
        tabEmprego.setValorSalario(valor)
    }

    override fun setPorcentagemNormal(porc: Int, valor: Double) {
        tabPorcentagens.updatePorcNormal(porc, valor)
    }

    override fun setPorcentagemCompleta(porc: Int, valor: Double) {
        tabPorcentagens.updatePorcCompleta(porc, valor)
    }

    override fun setPorcentagemDto(porcentagensDto: FragPorcentagensDto) {
        tabPorcentagens.updatePorcentagemDto(porcentagensDto)
    }

    override fun setDiaFechamento(dia: Int) {
        tabEmprego.setDiaFechamento(dia)
    }

    override fun resetPorcentDifByPos(pos: Int) {
        tabPorcentagens.resetPorcentDifByPosition(pos)
    }

    override fun updatePorcentDifByPos(pos: Int, percent: Int, valor: Double) {
        tabPorcentagens.updatePorcentDif(pos, percent, valor)
    }

    override fun showDialogSalario(salario: BigDecimal) {
        showDialogSalario(
            fragMan = supportFragmentManager,
            minVal = 0.0,
            curBigDVal = salario,
            ref = refSalario
        )
    }

    override fun showBtsGetSalario(salario: Double) {
        btsGetAumento.setSalario(salario)
        btsGetAumento.show(supportFragmentManager, "")
    }

    override fun showActSalarios(idEmprego: String) {
        startActivity<ActListaSalario>(BUNDLE_IDEMPREGO to presenter.idEmprego)
    }
    /** END ViewActGetEmpregos */

    /** BtsGetAumentoContract */
    override fun onSaveAumento(mes: Int, ano: Int, valor: Double) {
        presenter.appendAumento(mes = mes, ano = ano, valor = valor)
    }

    override fun onCancel() {
    }

    override fun showDialogGetAumento(salario: Double) {
        DialogUtils.showDialogSalario(supportFragmentManager, presenter.provideSalario(), salario, ref = refAumento)
    }

    /** END BtsGetAumentoContract */

    /** Dialogs */
    override fun onTimeSet(dialog: RadialTimePickerDialogFragment?, hourOfDay: Int, minute: Int) {
        tabEmprego.setHorarioSaida("${hourOfDay.zeroFill()}:${minute.zeroFill()}")
    }

    override fun onDialogNumberSet(
        reference: Int,
        number: BigInteger?,
        decimal: Double,
        isNegative: Boolean,
        fullNumber: BigDecimal?
    ) {
        when(reference) {
            refDia -> presenter.setDiaFechamento(number)
            refSalario -> presenter.setValorSalario(fullNumber)
            refPorcNormal -> presenter.setPorcNormal(number)
            refPorcFeriado -> presenter.setPorcCompleta(number)
            refAumento -> btsGetAumento.updateSalario(number!!.toDouble())

        /** de 0 a 6 pq é referente ao dia da semana, que é equivalente index do adapter**/
            in 0..6 -> presenter.updatePorcentDif(pos = reference, porcent = number)
        }
    }

    /** END Dialogs*/

    private fun showDialogPorcentagem(porc: Int = 100, ref: Int) {
        showNumberDialog(supportFragmentManager, "porcento", porc, minValue = 30, maxValue = 500, ref = ref)
    }

    override fun showGetPorcDiffValorDialog(pos: Int, porc: Int) {
        showDialogPorcentagem(porc = porc, ref = pos)
    }

    /** função chamada via RecAdapterHorasDiferenciais*/
    override fun showOptionDialogHorasDiff(pos: Int) {
        yesNoDialog(R.string.str_remove_pdif) {
            presenter.resetPorcentDifByPos(pos = pos)
        }
    }
}