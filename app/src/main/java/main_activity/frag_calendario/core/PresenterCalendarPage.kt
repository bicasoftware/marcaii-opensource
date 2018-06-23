package main_activity.frag_calendario.core

import main_activity.frag_calendario.calendario.*
import models.*

class PresenterCalendarPage(
    private val view: MVPCalendarioPage.ViewContentPageImpl,
    private var empregoInfoDto: EmpregoInfoDto
):
    MVPCalendarioPage.PresenterContentPageImpl,
    CalendarioItemListener {

    private val calendarAdapter: RecAdapterCalendario by lazy {
        RecAdapterCalendario(view.provideWeakReference(), empregoInfoDto.horasInfo, this)
    }

    override fun provideIdEmprego() = empregoInfoDto.idEmprego

    override fun onCalendarChange(pageContent: EmpregoInfoDto) {
        this.empregoInfoDto = pageContent
        calendarAdapter.resetContent(pageContent.horasInfo)
    }

    override fun updateItemByPos(pos: Int, horaInfoDto: HoraInfoDto) {
        calendarAdapter.updateItemByPos(pos, horaInfoDto)
    }

    override fun provideCalendarAdapter(): RecAdapterCalendario {
        return calendarAdapter
    }

    override fun setCalendarAdapter() {
        view.setCalendarAdapter(calendarAdapter)
    }

    override fun refreshContent(content: EmpregoInfoDto) {
        empregoInfoDto = content
        calendarAdapter.resetContent(content.horasInfo)
    }

    override fun getEmpregoId(): String {
        return empregoInfoDto.idEmprego
    }

    /** CalendarioItemListener */
    override fun onCalendarItemClicked(itemCalendario: HoraInfoDto, adapterPosition: Int) {
        if(itemCalendario.idHora.isBlank()) {
            view.showActGetHoras(empregoInfoDto.idEmprego, itemCalendario, adapterPosition)
        } else {
            view.showBtsInfoHoras(empregoInfoDto.idEmprego, itemCalendario, adapterPosition)
        }
    }
    /** END CalendarioItemListener */
}