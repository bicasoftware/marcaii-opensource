package main_activity.frag_calendario.calendario

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_calendar_page_cell.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import models.HoraInfoDto
import models.HoraType
import utils.setTint
import utils.weekDay
import java.lang.ref.WeakReference

class RecAdapterCalendario(
    private val cont: WeakReference<Context>,
    private var content: ArrayList<HoraInfoDto>,
    private val calendarListener: CalendarioItemListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val inf: LayoutInflater by lazy { LayoutInflater.from(cont.get()) }

    /** Quantidade de itens antes do primeiro dia */
    private fun getStart() = content.first().data.weekDay() - 1

    /** Quantidade de itens depois do último dia */
    private fun getEnd() = 7 - content.last().data.weekDay()

    /** Quantidade de dias no mês */
    private fun getCount() = content.count()

    /** somatória dos itens do calendário */
    private fun getSum() = getStart() + getCount() + getEnd()

    /**
     * retorna EmptyType para position < o dia da semana do primeiro dia do mês
     * retorna EmptyType para position >= primeiro dia da semana + quantidade de dias
     * retorna ItemViewType para position entre o primeiro dia do mes e o último dia do mês
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            position < getStart() -> EmptyType.viewType
            position >= getStart() && position < getCount() + getStart() -> ItemType.viewType
            position >= getStart() + getCount() && position < getSum() -> EmptyType.viewType
            else -> {
                Log.e("saulo", "posição: $position")
                EmptyType.viewType
            }
        }
    }

    override fun getItemCount() = getSum()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            EmptyType.viewType -> EmptyItemHolder(
                inf.inflate(
                    R.layout.lay_frag_calendar_page_cell_empty,
                    parent,
                    false
                )
            )
            ItemType.viewType -> ItemHolder(inf.inflate(R.layout.lay_frag_calendar_page_cell, parent, false))
            else -> throw Exception("UNKNOW VIEW")
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemHolder) holder.bind(content[position - getStart()])
    }

    inner class ItemHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {
        fun bind(item: HoraInfoDto) {
            with(itemView) {
                tv_calendario_item.text = item.dia
                if(item.horaType == HoraType.NONE) {
                    img_hora_indicator.setImageDrawable(null)
                } else {
                    img_hora_indicator.setImageResource(R.drawable.ic_indicator)
                    img_hora_indicator.setTint(context, item.horaType.colorId)
                }
            }

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            calendarListener.onCalendarItemClicked(content[adapterPosition - getStart()], adapterPosition)
        }
    }

    fun resetContent(content: ArrayList<HoraInfoDto>) {
        launch(UI) {
            this@RecAdapterCalendario.content = content
            notifyDataSetChanged()
        }
    }

    fun updateItemByPos(pos: Int, horaInfoDto: HoraInfoDto) {
        launch(UI){
            content[pos - getStart()].copy(horaInfoDto)
            notifyItemChanged(pos)
        }
    }

    inner class EmptyItemHolder(v: View): RecyclerView.ViewHolder(v)
}