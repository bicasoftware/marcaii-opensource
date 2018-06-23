package act_get_hora

import android.os.Bundle
import android.view.View
import br.sha.commommodule.BaseDataManageActivity
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_act_get_hora.*
import utils.showSnack

class ActGetHoras: BaseDataManageActivity(
    R.layout.lay_act_get_hora, R.string.str_nova_hora
),
    RadialTimePickerDialogFragment.OnTimeSetListener,
    MVPActGetHora.ViewActGetHora {

    private lateinit var presenter: PresenterActGetHora

    companion object {
        private const val TAG_HORA_INICIO = "TAGHORAINI"
        private const val TAG_HORA_TERMINO = "TAGHORATERMINO"

        const val BUNDLE_IDEMPREGO = "IDEMPREGO"
        const val BUNDLE_POSITION = "ADAPTERPOSITION"
        const val BUNDLE_ITEM_CALENDARIO = "ITEMCALENDARIO"
    }

    override fun prepareAct(b: Bundle?) {
        presenter = PresenterActGetHora(
            view = this,
            idEmprego = b?.getString(BUNDLE_IDEMPREGO) ?: "",
            horaInfoDto = b?.getParcelable(BUNDLE_ITEM_CALENDARIO)!!,
            calendarPosition = b?.getInt(BUNDLE_POSITION) ?: -1
        )

        ll_bts_get_inicio.setOnClickListener {
            presenter.handleDialogInicioClick()
        }

        ll_bts_get_termino.setOnClickListener {
            presenter.handleDialogTerminoClick()
        }

        rdg_bts_get_tipo_hora.setOnCheckedChangeListener { _, checkedId ->
            presenter.onHoraTypeChanged(checkedId)
        }
    }

    override fun onStop() {
        presenter.closeRealm()
        super.onStop()
    }

    override fun showSnackBar(msg: Int) {
        ll_bts_get_inicio.showSnack(msg)
    }

    override fun hideRdbDif() {
        rdg_bts_get_tipo_hora.weightSum = 2F
        rdb_diff.visibility = View.GONE
    }

    override fun isDataValidated() = presenter.isValidated()

    override fun isInsert() = presenter.isInsert()

    override fun intentOnBack() = presenter.provideUpdateResult()

    override fun intentOnInsert() = presenter.provideInsertResult()

    override fun putHoraInicial(horaInicial: String) {
        tv_bts_get_inicio_hora.text = horaInicial
    }

    override fun putHoraTermino(horaTermino: String) {
        tv_bts_get_termino_hora.text = horaTermino
    }

    override fun putTipoHora(position: Int) {
        rdg_bts_get_tipo_hora.check(position)
    }

    override fun showDialogInicio(hora: Int, minuto: Int) {
        with(RadialTimePickerDialogFragment()) {
            setOnTimeSetListener(this@ActGetHoras)
            setStartTime(hora, minuto)
            setDoneText("Pronto!")
            setCancelText("Cancelar")
            setForced24hFormat()
        }.show(supportFragmentManager, TAG_HORA_INICIO)
    }

    override fun showDialogTermino(hora: Int, minuto: Int) {
        with(RadialTimePickerDialogFragment()) {
            setOnTimeSetListener(this@ActGetHoras)
            setStartTime(hora + 1, minuto)
            setDoneText("Pronto!")
            setCancelText("Cancelar")
            setForced24hFormat()
        }.show(supportFragmentManager, TAG_HORA_TERMINO)
    }

    override fun onTimeSet(dialog: RadialTimePickerDialogFragment?, hourOfDay: Int, minute: Int) {
        when(dialog?.tag) {
            TAG_HORA_INICIO -> presenter.setHoraInicial(hourOfDay, minute)
            TAG_HORA_TERMINO -> presenter.setHoraTermino(hourOfDay, minute)
        }
    }
}