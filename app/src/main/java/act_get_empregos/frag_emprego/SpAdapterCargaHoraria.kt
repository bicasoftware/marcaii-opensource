package act_get_empregos.frag_emprego

import android.content.Context
import android.widget.TextView
import br.sha.commommodule.BaseSpinnerAdapter
import exemple.sha.horas.R

class SpAdapterCargaHoraria(ctx: Context): BaseSpinnerAdapter(
    cont = ctx,
    spinner_item = R.layout.lay_spinner_item_white,
    spinner_subitem = R.layout.lay_spinner_subitem_white
) {

    private val cargas = ctx.resources.getStringArray(R.array.arr_cargas_horarias)

    override fun bindItemText(componente: TextView, p: Int) {
        componente.text = getText(p)
    }

    override fun bindSubItemText(componente: TextView, p: Int) {
        componente.text = getText(p)
    }

    override fun getItemCount() = cargas.size

    private fun getText(p: Int) = cargas[p]
}