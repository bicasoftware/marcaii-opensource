package act_get_empregos.bts_get_aumento

import android.content.Context
import android.widget.TextView
import br.sha.commommodule.BaseSpinnerAdapter
import exemple.sha.horas.R

class SpinnerAdapterMesesWhite(cont: Context): BaseSpinnerAdapter(
    cont,
    spinner_item = R.layout.lay_spinner_item_white,
    spinner_subitem = R.layout.lay_spinner_subitem_white
) {

    private val meses: Array<String> by lazy {
        cont.resources.getStringArray(R.array.arr_meses_ext)
    }

    override fun getItemCount(): Int = meses.size

    override fun bindItemText(componente: TextView, p: Int) {
        componente.text = meses[p]
    }

    override fun bindSubItemText(componente: TextView, p: Int) {
        componente.text = meses[p]
    }
}