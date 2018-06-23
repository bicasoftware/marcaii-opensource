package main_activity.frag_calendario.calendario

import android.content.Context
import br.sha.commommodule.BaseRecyclerView
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_calendar_page_cell_header.view.*

class RecAdapterHeader(cont: Context): BaseRecyclerView(cont, R.layout.lay_frag_calendar_page_cell_header) {

    private val dias = cont.resources.getStringArray(R.array.arr_dia_semana_abreviado)

    override fun getRecyclerCount() = dias.size

    override fun BindViewHolder(h: BaseHolder, p: Int) {
        h.itemView.tv_calendario_header_item.text = dias[p]
    }
}