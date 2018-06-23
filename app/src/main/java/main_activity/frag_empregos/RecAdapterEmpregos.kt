package main_activity.frag_empregos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import asCurrency
import exemple.sha.horas.R
import io.realm.RealmResults
import kotlinx.android.synthetic.main.lay_frag_empregos_items.view.*
import utils_realm.REmpregos

class RecAdapterEmpregos(
    private val cont: Context,
    private var empregos: RealmResults<REmpregos>,
    private val adapterListener: FragEmpregosListener
):
    RecyclerView.Adapter<RecAdapterEmpregos.EmpregoHolder>() {

    private val inf: LayoutInflater by lazy {
        LayoutInflater.from(cont)
    }

    init {
        empregos.addChangeListener {
            _, changeSet ->

            val remCount = changeSet.deletionRanges
            for(pos in remCount.size - 1 downTo 0) {
                val item = remCount[pos]
                this.notifyItemRangeRemoved(item.startIndex, item.length)
            }

            changeSet.changeRanges?.forEach {
                this.notifyItemRangeChanged(it.startIndex, it.length)
            }

            changeSet.insertions?.forEach {
                this.notifyItemInserted(it)
            }
        }
    }

    override fun getItemCount() = empregos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpregoHolder {
        return EmpregoHolder(inf.inflate(R.layout.lay_frag_empregos_items, parent, false))
    }

    override fun onBindViewHolder(holder: EmpregoHolder, position: Int) {
        holder.bind(empregos[position]!!, adapterListener)
    }

    inner class EmpregoHolder(v: View): RecyclerView.ViewHolder(v) {
        fun bind(emprego: REmpregos, listener: FragEmpregosListener) {

            with(itemView) {
                emprego.let {
                    val salario = it.salarios.where().equalTo("status", true).findFirst()?.valorSalario ?: 0.0
                    tv_item_descricao.text = it.nomeEmprego
                    tv_item_carga.text = it.cargaHoraria
                    tv_item_valor_salario.text = salario.asCurrency()
                    tv_item_dia_fechamento.text = it.diaFechamento.toString()
                    tv_item_hora_saida.text = it.horarioSaida
                }

                setOnClickListener {
                    listener.onItemClick(emprego.id, adapterPosition)
                }

                setOnLongClickListener {
                    if(empregos.size > 1) {
                        listener.onLongItemClick(emprego.id, adapterPosition)
                    }
                    true
                }
            }
        }
    }
}