package act_get_hora

import android.content.Intent
import exemple.sha.horas.R
import main_activity.act_main.ActMain
import models.HoraInfoDto
import models.HoraType
import utils.intent
import utils.timeStringAsDate

class PresenterActGetHora(
    private val view: MVPActGetHora.ViewActGetHora,
    private val idEmprego: String,
    private val horaInfoDto: HoraInfoDto,
    private val calendarPosition: Int
): MVPActGetHora.PresenterActGetHoraImpl {

    private val model = ModelActGetHora(this, horaInfoDto.idHora, horaInfoDto.data, idEmprego)

    init {
        view.putHoraInicial(model.getHoraInicial())
        view.putHoraTermino(model.getHoraTermino())
        view.putTipoHora(model.getTipoHora())
        if(!model.hasHoraDif()) view.hideRdbDif()
    }

    override fun closeRealm() {
        model.closeRealm()
    }

    override fun getIdHora() = horaInfoDto.idHora

    override fun getIdEmprego() = idEmprego

    override fun setHoraInicial(hora: Int, minuto: Int) {
        val m = if(minuto < 10) "0$minuto" else minuto.toString()
        val h = "$hora:$m"
        model.setHoraInicial(h)
        view.putHoraInicial(h)
    }

    override fun setHoraTermino(hora: Int, minuto: Int) {
        val m = if(minuto < 10) "0$minuto" else minuto.toString()
        val h = "$hora:$m"
        model.setHoraTermino(h)
        view.putHoraTermino(h)
    }

    override fun setTipoHora(tipoHora: Int) {
        model.setTipoHora(getHoraTypeByItemId(tipoHora))
    }

    override fun isInsert() = horaInfoDto.idHora.isBlank()

    override fun isValidated(): Boolean {
        val timeIni = model.getHoraInicial().timeStringAsDate().time
        val timeEnd = model.getHoraTermino().timeStringAsDate().time

        val setFalse = { msg: Int ->
            view.showSnackBar(msg)
            false
        }

        return when {
            timeIni == timeEnd -> setFalse(R.string.war_horas_iguais)
            timeIni > timeEnd -> setFalse(R.string.war_horas_invalidas)
            else -> true
        }
    }

    override fun provideInsertResult(): Intent {
        model.commitHora(idEmprego)
        refreshHoraInfoDto()
        return intent {
            putExtra(ActMain.RES_IDEMPREGO, idEmprego)
            putExtra(ActMain.RES_ADAPTER_POS, calendarPosition)
            putExtra(ActMain.RES_HORAINFO, horaInfoDto)
        }
    }

    override fun provideUpdateResult(): Intent {
        refreshHoraInfoDto()
        return intent {
            putExtra(ActMain.RES_IDEMPREGO, idEmprego)
            putExtra(ActMain.RES_ADAPTER_POS, calendarPosition)
            putExtra(ActMain.RES_HORAINFO, horaInfoDto)
        }
    }

    override fun handleDialogInicioClick() {
        val hora = model.getHoraInicial().split(":").map { it.toInt() }
        view.showDialogInicio(hora[0], hora[1])
    }

    override fun handleDialogTerminoClick() {
        val hora = model.getHoraTermino().split(":").map { it.toInt() }
        view.showDialogTermino(hora[0], hora[1])
    }

    override fun onHoraTypeChanged(itemId: Int) {
        model.setTipoHora(getHoraTypeByItemId(itemId))
    }

    private fun getHoraTypeByItemId(buttonPos: Int) = when(buttonPos) {
        R.id.rdb_normal -> HoraType.NORMAL
        R.id.rdb_feriado -> HoraType.COMPLETA
        R.id.rdb_diff -> HoraType.DIFF
        else -> HoraType.NORMAL
    }

    private fun refreshHoraInfoDto() {
        horaInfoDto.copy(model.provideInstance(), model.getValorHora())
    }
}