package main_activity.act_main

import act_get_empregos.core.ActGetEmpregos
import act_get_hora.ActGetHoras
import act_list_horas.ActListHoras
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import br.sha.commommodule.BaseToolbarActivity
import br.sha.commommodule.base_toolbar.prepare
import bts_view_horas.BtsHoraInfo
import bts_view_horas.BtsHoraInfoContract
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_act_main.*
import main_activity.frag_calendario.*
import main_activity.frag_empregos.FragEmpregos
import main_activity.frag_empregos.FragEmpregosContract
import models.HoraInfoDto
import models.MainContentDto
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import utils.*

class ActMain: BaseToolbarActivity(R.layout.lay_act_main, R.string.app_name, R.string.str_calendario),
    MVPActMain.ViewActMainImpl,
    FragEmpregosContract,
    FragCalendarioContract,
    CalendarioPageContract,
    BtsHoraInfoContract {

    companion object {
        const val REQUEST_INSERT_HORAS = 1212
        const val REQUEST_UPDATE_HORAS = 1313
        const val REQUEST_INSERT_EMPREGO = 1414
        const val REQUEST_UPDATE_EMPREGO = 1515

        const val RES_IDEMPREGO = "RESIDEMPREGO"
        const val RES_ADAPTER_POS = "RESADAPTERPOS"
        const val RES_HORAINFO = "RESHORAINFO"
    }

    private val presenter: PresenterActMain by lazy {
        PresenterActMain(this@ActMain)
    }

    private fun getFragCalendario(): FragCalendario{
        return supportFragmentManager.fragments.first { it is FragCalendario } as FragCalendario
    }

    override fun afterCreate(b: Bundle?) {
        bottombar.setOnNavigationItemSelectedListener {
            presenter.handleBottomNavClick(it.itemId)
            true
        }

        fab_opcoes.setOnClickListener {
            val cPos = presenter.getCurrentPagePosition()
            presenter.handleFABClick(bottombar.selectedItemId, cPos)
        }

        presenter.onStartUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK) {
            presenter.handleActivityResult(requestCode, data)
        }
    }

    override fun onDestroy() {
        presenter.closeRealm()
        super.onDestroy()
    }

    /** ViewActMainImpl */

    override fun invalidateMenu() {
        invalidateOptionsMenu()
    }

    override fun setFabIcon(icon: Int) {
        fab_opcoes.setImageResource(icon)
    }

    override fun setContent(pageContent: MainContentDto) {
        getFragCalendario().putCalendarContent(pageContent)
    }

    override fun setTbarTitle(title: Int) {
        supportActionBar?.prepare(this) {
            _subtitle = title
        }
    }

    override fun showFragEmpregos() {
        replaceFragment(FragEmpregos.newInstance())
    }

    override fun showFragCalendar(content: MainContentDto) {
        replaceFragment(FragCalendario.newInstance(content))
    }

    override fun showActGetEmpregos() {
        showActGetEmprego("", -1)
    }

    override fun setBottombarPosition(pos: Int) {
        bottombar.selectedItemId = pos
    }

    override fun showActListHoras(idEmprego: String, ano: Int, mes: Int) {
        startActivity<ActListHoras>(
            ActListHoras.CONST_ANO to ano,
            ActListHoras.CONST_MES to mes,
            ActListHoras.CONST_IDEMPREGO to idEmprego
        )
    }

    override fun refreshContentItem(i: Int, horaInfoDto: HoraInfoDto) {
        getFragCalendario().updateContentByPosition(i, horaInfoDto)
    }

    override fun showActUpdateHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int) {
        callActGetHoras(REQUEST_UPDATE_HORAS, idEmprego, itemCalendario, adapterPosition)
    }
    /** END ViewActMainImpl */

    /** FragEmpregosContract */
    override fun showActGetEmprego(idEmprego: String, adapterEmpregosPosition: Int) {
        val request = if(idEmprego.isBlank()) REQUEST_INSERT_EMPREGO else REQUEST_UPDATE_EMPREGO
        startActivityForResult<ActGetEmpregos>(
            request,
            ActGetEmpregos.BUNDLE_IDEMPREGO to idEmprego
        )
    }

    override fun showOptionsActGetEmprego(idEmprego: String, adapterEmpregosPosition: Int) {
        selectorDialog(R.string.str_opcoes, R.array.arr_opcoes_emprego) {
            if(it == 0) presenter.deleteEmprego(idEmprego)
        }
    }

    override fun onEmpregoUpdate(idEmprego: String) {
        presenter.updateCalendar(idEmprego)
    }

    override fun onEmpregoInsert(idEmprego: String) {
        presenter.insertCalendarPage(idEmprego)
    }

    /** END FragEmpregosContract */

    /** FragCalendarioContract */
    override fun nextMonth() {
        presenter.incMonth()
    }

    override fun previousMonth() {
        presenter.decMonth()
    }

    override fun showDialogGetMonth() {
        //todo - gerar Dialog para pegar apenas o Mês
        selectorDialog(R.string.str_mes, R.array.arr_meses_ext) {
            presenter.setMonth(it)
        }
    }

    override fun showDialogGetYear() {
        //todo - gerar Dialog para pegar apenas o Ano
        selectorDialog(R.string.str_ano, R.array.arr_anos) {
            presenter.setYear(it)
        }
    }

    override fun setCurrentPagePosition(position: Int) {
        presenter.putCurrentPagePosition(position)
    }

    /** END FragEmpregosContract */

    /** CalendarioPageContract */
    override fun showActGetHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int) {
        callActGetHoras(REQUEST_INSERT_HORAS, idEmprego, itemCalendario, adapterPosition)
    }

    override fun showBtsInfoHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int) {
        BtsHoraInfo.newInstance(idEmprego, itemCalendario, adapterPosition).show(supportFragmentManager, "")
    }
    /** END CalendarioPageContract */

    /** BtsHoraInfoContract */
    override fun onHoraDelete(pos: Int, horaInfoDto: HoraInfoDto) {
        yesNoDialog(R.string.war_remover_hora) {
            presenter.deleteHora(pos, horaInfoDto)
        }
    }

    override fun onHoraUpdate(idEmprego: String, itemCaledar: HoraInfoDto, pos: Int) {
        presenter.updateHora(idEmprego, itemCaledar, pos)
    }

    /** END BtsHoraInfoContract */

    private fun replaceFragment(frag: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.frame_replace, frag, null)
            .commit()
    }

    private fun callActGetHoras(TAG: Int, idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int) {
        startActivityForResult<ActGetHoras>(
            TAG,
            ActGetHoras.BUNDLE_IDEMPREGO to idEmprego,
            ActGetHoras.BUNDLE_ITEM_CALENDARIO to itemCalendario,
            ActGetHoras.BUNDLE_POSITION to adapterPosition
        )
    }

}