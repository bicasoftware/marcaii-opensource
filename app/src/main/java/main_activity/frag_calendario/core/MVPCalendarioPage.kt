package main_activity.frag_calendario.core

import android.content.Context
import main_activity.frag_calendario.calendario.RecAdapterCalendario
import models.EmpregoInfoDto
import models.HoraInfoDto
import java.lang.ref.WeakReference

interface MVPCalendarioPage {

    interface PresenterContentPageImpl {
        fun provideIdEmprego(): String
        fun onCalendarChange(pageContent: EmpregoInfoDto)
        fun updateItemByPos(pos: Int, horaInfoDto: HoraInfoDto)
        fun provideCalendarAdapter(): RecAdapterCalendario
        fun setCalendarAdapter()
        fun refreshContent(content: EmpregoInfoDto)
        fun getEmpregoId(): String
    }

    interface ViewContentPageImpl {
        fun provideContext(): Context
        fun provideWeakReference(): WeakReference<Context>
        fun setAdapter(adapter: RecAdapterCalendario)
        fun showActGetHoras(
            idEmprego: String,
            itemCalendario: HoraInfoDto,
            adapterPosition: Int
        )

        fun showBtsInfoHoras(
            idEmprego: String,
            itemCalendario: HoraInfoDto,
            adapterPosition: Int
        )

        fun setCalendarAdapter(adapterCalendar: RecAdapterCalendario)

    }
}