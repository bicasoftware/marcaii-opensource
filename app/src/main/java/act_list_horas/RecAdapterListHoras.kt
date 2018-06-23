package act_list_horas

import android.content.Context
import asCurrency
import br.sha.commommodule.BaseRecyclerView
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_bts_list_horas_item.view.*
import models.HoraInfoDto
import utils.dateToNiceString
import utils.setTint

class RecAdapterListHoras(
    private val ctx: Context,
    private var content: List<HoraInfoDto> = arrayListOf()
): BaseRecyclerView(ctx, R.layout.lay_bts_list_horas_item) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getRecyclerCount() = content.size

    override fun BindViewHolder(h: BaseHolder, p: Int) {
        with(h.itemView) {
            content[p].let {
                tv_rel_dia.text = ctx.getString(R.string.pholder_dia_mes_periodo, dateToNiceString(it.data), it.horaInicial, it.horaTermino)
                img_rel_tipo_hora.setTint(ctx, it.horaType.colorId)
                tv_rel_minutos.text = ctx.getString(R.string.pholder_minutos, it.minutos)
                tv_rel_valor_hora.text = it.valor.asCurrency()
            }
        }
    }
}