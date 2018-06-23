package bts_view_horas

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import asCurrency
import br.sha.commommodule.BaseMaterialBottomSheet
import exemple.sha.horas.R
import models.HoraInfoDto
import utils.*

class BtsHoraInfo: BaseMaterialBottomSheet(R.layout.lay_bts_info_horas) {

    private lateinit var contract: BtsHoraInfoContract

    private fun getIdEmprego() = arguments?.getString(CONST_IDEMPREGO) ?: ""
    private fun getHoraInfoDto() = arguments?.getParcelable(CONST_ITEM_CALENDARIO) as HoraInfoDto
    private fun getPos() = arguments?.getInt(CONST_POS) ?: -1

    companion object {

        private const val CONST_IDEMPREGO = "CONST_IDEMPREGO"
        private const val CONST_ITEM_CALENDARIO = "CONST_ITEM_CALENDARIO"
        private const val CONST_POS = "CONST_POS"

        fun newInstance(idEmprego: String, itemCalendario: HoraInfoDto, pos: Int): BtsHoraInfo {
            return instanceBuilder {
                putString(CONST_IDEMPREGO, idEmprego)
                putParcelable(CONST_ITEM_CALENDARIO, itemCalendario)
                putInt(CONST_POS, pos)
            }
        }
    }

    override fun onAttach(context: Context?) {
        contract = context as BtsHoraInfoContract
        super.onAttach(context)
    }

    override fun prepareView(v: View) {

        val h = getHoraInfoDto()
        v.findViewById<TextView>(R.id.tv_info_hora_data).text = dateToString(h.data, DateFormats.BR)
        v.findViewById<TextView>(R.id.tv_info_hora_minutos).text = getString(R.string.pholder_minutos,h.minutos)
        v.findViewById<TextView>(R.id.tv_info_hora_periodo).text = getString(
            R.string.pholder_periodo,
            h.horaInicial,
            h.horaTermino
        )
        v.findViewById<TextView>(R.id.tv_info_hora_tipo_hora)?.let{
            it.text = getString(h.horaType.resString)
            it.setTextColor(ContextCompat.getColor(context!!, h.horaType.colorId))
        }
        v.findViewById<TextView>(R.id.tv_info_hora_valor).text = h.valor.asCurrency()


        v.findViewById<ImageButton>(R.id.bt_info_hora_update).setOnClickListener {
            contract.onHoraUpdate(
                getIdEmprego(),
                getHoraInfoDto(),
                getPos()
            )
            this.dismiss()
        }

        v.findViewById<ImageButton>(R.id.bt_info_hora_delete).setOnClickListener {
            contract.onHoraDelete(getPos(), getHoraInfoDto())
            this.dismiss()
        }
    }
}