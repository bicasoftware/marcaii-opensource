package act_get_empregos.frag_porcentagem

import android.content.Context
import asCurrency
import br.sha.commommodule.BaseRecyclerView
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_empregos_act_get_emprego_porcentagem_diferenciais_item.view.*

class RecAdapterHorasDiferenciais(
    val cont: Context,
    var horas: ArrayList<HoraDiferDto>,
    private val contract: HorasDiferContract
):
    BaseRecyclerView(
        mCont = cont,
        layout = R.layout.lay_frag_empregos_act_get_emprego_porcentagem_diferenciais_item
    ) {

    private val diasSemana: Array<String> = cont.resources.getStringArray(R.array.arr_dia_semana)

    override fun getRecyclerCount(): Int = diasSemana.size

    override fun BindViewHolder(h: BaseHolder, p: Int) {
        with(h.itemView) {
            tv_stag_header_dia.text = diasSemana[p]
            tv_stag_porcentagem.text = if(horas[p].porcentagem == 0) "---" else horas[p].porcentagem.toString()
            tv_stag_valor.text = horas[p].valor.asCurrency()
            bt_clean_pdif.setOnClickListener {
                if(horas[p].porcentagem > 0) {
                    contract.onDeletePorc(p)
                }
            }

            setOnClickListener {
                contract.onItemClicked(p, horas[p].porcentagem)
            }
        }
    }

    fun updateByPosition(pos: Int, porc: Int, valor: Double) {
        with(horas[pos]) {
            porcentagem = porc
            this.valor = valor
        }

        notifyItemChanged(pos)
    }

    fun resetItem(pos: Int) {
        horas[pos].reset()
        notifyItemChanged(pos)
    }

    fun updateHorasDif(horasDif: ArrayList<HoraDiferDto>) {
        horas = horasDif
        notifyDataSetChanged()
    }

    fun refreshContent(porcDifer: ArrayList<HoraDiferDto>) {
        horas = porcDifer
        notifyDataSetChanged()
    }
}