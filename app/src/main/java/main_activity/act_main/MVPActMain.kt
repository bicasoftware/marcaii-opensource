package main_activity.act_main

import android.content.Intent
import io.realm.RealmResults
import models.HoraInfoDto
import models.MainContentDto
import utils_realm.REmpregos

interface MVPActMain {

    interface ViewActMainImpl {
        fun setContent(pageContent: MainContentDto)
        fun setFabIcon(icon: Int)
        fun setTbarTitle(title: Int)
        fun showFragEmpregos()
        fun showFragCalendar(content: MainContentDto)
        fun setBottombarPosition(pos: Int)
        fun showActGetEmpregos()
        fun showActUpdateHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int)
        fun refreshContentItem(i: Int, horaInfoDto: HoraInfoDto)
        fun invalidateMenu()
        fun showActListHoras(idEmprego:String, ano: Int, mes: Int)
    }

    interface PresenterActMainImpl {
        fun closeRealm()
        fun isConnected(): Boolean
        fun handleBottomNavClick(itemId: Int?)
        fun handleFABClick(bottomBarPosition: Int, currentPagePosition: Int)
        fun incMonth()
        fun decMonth()
        fun setYear(year: Int)
        fun setMonth(month: Int)
        fun handleActivityResult(reqCode: Int, data: Intent?)
        fun deleteHora(pos: Int, horaInfoDto: HoraInfoDto)
        fun updateHora(idEmprego: String, itemCaledar: HoraInfoDto, pos: Int)
        fun deleteEmprego(idEmprego: String)
        fun updateCalendar(idEmprego: String)
        fun insertCalendarPage(idEmprego: String)
        fun putCurrentPagePosition(pos: Int)
        fun getCurrentPagePosition() : Int

        fun hasEmpregos(): Boolean
        fun onStartUp()
    }

    interface ModelActMainImpl {
        fun closeRealm()
        fun deleteEmprego(idEmprego: String)
        fun deleteHora(idHora: String)
        fun provideEmpregos(): RealmResults<REmpregos>
        fun provideEmprego(idEmprego: String): REmpregos
    }
}