package act_get_hora

import android.content.Intent
import models.HoraType
import utils_realm.RHoras
import java.math.BigDecimal

interface MVPActGetHora {

    interface ModelActGetHoraImpl {
        fun closeRealm()
        fun setHoraInicial(hora: String)
        fun setHoraTermino(hora: String)
        fun setTipoHora(horaType: HoraType)
        fun getHoraInicial(): String
        fun getHoraTermino(): String
        fun getTipoHora(): Int
        fun getHoraType(): HoraType
        fun getIdHora(): String
        fun hasHoraDif(): Boolean
        fun commitHora(idEmprego: String)
        fun provideInstance(): RHoras
        fun getValorHora(): BigDecimal
    }

    interface PresenterActGetHoraImpl {
        fun closeRealm()
        fun getIdHora(): String
        fun getIdEmprego(): String
        fun setHoraInicial(hora: Int, minuto: Int)
        fun setHoraTermino(hora: Int, minuto: Int)
        fun setTipoHora(tipoHora: Int)

        fun isValidated(): Boolean
        fun provideInsertResult(): Intent
        fun provideUpdateResult(): Intent
        fun handleDialogInicioClick()
        fun handleDialogTerminoClick()
        fun onHoraTypeChanged(itemId: Int)
        fun isInsert(): Boolean
    }

    interface ViewActGetHora {
        fun showSnackBar(msg: Int)
        fun hideRdbDif()
        fun putHoraInicial(horaInicial: String)
        fun putHoraTermino(horaTermino: String)
        fun putTipoHora(position: Int)
        fun showDialogInicio(hora: Int, minuto: Int)
        fun showDialogTermino(hora: Int, minuto: Int)
    }
}