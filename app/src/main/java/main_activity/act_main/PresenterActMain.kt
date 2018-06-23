package main_activity.act_main

import act_get_empregos.core.ActGetEmpregos
import android.content.Context
import android.content.Intent
import exemple.sha.horas.R
import main_activity.frag_calendario.calendario.CalendarBuilder
import models.*
import java.util.*

class PresenterActMain(private val view: MVPActMain.ViewActMainImpl): MVPActMain.PresenterActMainImpl {

    private val calendarStack = arrayListOf<MainContentDto>()

    private val model = ModelActMain(this)

    private val cont: Context
        get() = view as Context

    private val c = Calendar.getInstance()

    private val anos: List<Int> = cont.resources.getStringArray(R.array.arr_anos).map { it.toInt() }

    override fun closeRealm() {
        model.closeRealm()
    }

    override fun isConnected(): Boolean {
        return false
    }

    override fun handleBottomNavClick(itemId: Int?) {
        when(itemId) {
            R.id.menu_bbar_empregos -> {
                view.setTbarTitle(R.string.str_empregos)
                view.showFragEmpregos()
                view.setFabIcon(R.drawable.ic_add)
                view.setTbarTitle(R.string.str_empregos)
            }
            R.id.menu_bbar_calendario -> {
                view.setTbarTitle(R.string.str_calendario)
                view.showFragCalendar(provideCalendarContent())
                view.setFabIcon(R.drawable.ic_lista)
                view.setTbarTitle(R.string.str_calendario)
            }
        }

        view.invalidateMenu()
    }

    override fun handleFABClick(bottomBarPosition: Int, currentPagePosition: Int) {
        when(bottomBarPosition) {
            R.id.menu_bbar_empregos -> view.showActGetEmpregos()
            R.id.menu_bbar_calendario -> view.showActListHoras(
                idEmprego = calendarStack.first().empregoInfo[currentPagePosition].idEmprego,
                ano = c.get(Calendar.YEAR),
                mes = c.get(Calendar.MONTH)
            )
        }
    }

    override fun incMonth() {
        c.add(Calendar.MONTH, 1)
        view.setContent(provideCalendarContent())
    }

    override fun decMonth() {
        c.add(Calendar.MONTH, -1)
        view.setContent(provideCalendarContent())
    }

    override fun setYear(year: Int) {
        c.set(Calendar.YEAR, anos[year])
        view.setContent(provideCalendarContent())
    }

    override fun setMonth(month: Int) {
        c.set(Calendar.MONTH, month)
        view.setContent(provideCalendarContent())
    }

    override fun handleActivityResult(reqCode: Int, data: Intent?) {

        when(reqCode) {
            ActMain.REQUEST_INSERT_EMPREGO -> {
                val idEmprego = data?.getStringExtra(ActGetEmpregos.CONST_ID_EMPREGO) ?: ""
                insertCalendarPage(idEmprego)
            }
            ActMain.REQUEST_UPDATE_EMPREGO -> {
                val idEmprego = data?.getStringExtra(ActGetEmpregos.CONST_ID_EMPREGO) ?: ""
                updateCalendar(idEmprego)
            }
            ActMain.REQUEST_INSERT_HORAS -> {
                val adapterPosition = data?.getIntExtra(ActMain.RES_ADAPTER_POS, -1)
                val horaInfoDto = data?.getParcelableExtra<HoraInfoDto>(ActMain.RES_HORAINFO)
                if(horaInfoDto != null) {
                    view.refreshContentItem(adapterPosition ?: -1, horaInfoDto)
                }
            }
            ActMain.REQUEST_UPDATE_HORAS -> {
                val adapterPosition = data?.getIntExtra(ActMain.RES_ADAPTER_POS, -1)
                val horaInfoDto = data?.getParcelableExtra<HoraInfoDto>(ActMain.RES_HORAINFO)
                if(horaInfoDto != null){
                    view.refreshContentItem(adapterPosition ?: -1, horaInfoDto)
                }
            }
        }
    }

    override fun deleteHora(pos: Int, horaInfoDto: HoraInfoDto) {
        model.deleteHora(horaInfoDto.idHora)
        view.refreshContentItem(pos, horaInfoDto.apply { clearMe() })
    }

    override fun updateHora(idEmprego: String, itemCaledar: HoraInfoDto, pos: Int) {
        view.showActUpdateHoras(idEmprego, itemCaledar, pos)
    }

    override fun deleteEmprego(idEmprego: String) {
        model.deleteEmprego(idEmprego)
        calendarStack.forEach {
            it.empregoInfo.removeAt(it.empregoInfo.indexOfFirst { it.idEmprego == idEmprego })
        }
    }

    override fun insertCalendarPage(idEmprego: String) {
        val e = model.provideEmprego(idEmprego)
        calendarStack.forEach { stack ->
            stack.empregoInfo.add(
                EmpregoInfoDto(
                    idEmprego = idEmprego,
                    nomeEmprego = e.nomeEmprego,
                    horasInfo = CalendarBuilder.buildByEmprego(stack.mes, stack.ano, idEmprego)
                )
            )
        }
    }

    override fun updateCalendar(idEmprego: String) {
        val e = model.provideEmprego(idEmprego)
        calendarStack.forEach { stack ->
            stack.empregoInfo.filter { it.idEmprego == idEmprego }.forEach {
                it.nomeEmprego = e.nomeEmprego
                it.horasInfo = CalendarBuilder.buildByEmprego(stack.mes, stack.ano, idEmprego)
            }
        }
    }

    override fun hasEmpregos(): Boolean {
        return model.provideEmpregos().size > 0
    }

    override fun onStartUp() {
        if(model.provideEmpregos().size > 0) {
            view.setBottombarPosition(R.id.menu_bbar_calendario)
        } else {
            view.showActGetEmpregos()
        }
    }

    private var currentPagePosition: Int = 0

    override fun putCurrentPagePosition(pos: Int) {
        currentPagePosition = pos
    }

    override fun getCurrentPagePosition(): Int {
        return currentPagePosition
    }

    private fun provideCalendarContent(): MainContentDto {
        val ano = c.get(Calendar.YEAR)
        val mes = c.get(Calendar.MONTH)
        val index = calendarStack.indexOfFirst { it.ano == ano && it.mes == mes }

        return if(index == -1) {
            val mainContent = MainContentDto(ano, mes)

            model.provideEmpregos().mapTo(mainContent.empregoInfo) {
                EmpregoInfoDto(it.id, it.nomeEmprego, CalendarBuilder.buildByEmprego(mes, ano, it.id))
            }

            calendarStack.add(mainContent)
            mainContent
        } else {
            calendarStack[index]
        }
    }
}