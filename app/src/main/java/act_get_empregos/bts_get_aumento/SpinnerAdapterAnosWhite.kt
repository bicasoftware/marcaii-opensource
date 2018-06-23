package act_get_empregos.bts_get_aumento

import android.content.Context
import android.widget.TextView
import br.sha.commommodule.BaseSpinnerAdapter
import exemple.sha.horas.R
import mapToArrayList

class SpinnerAdapterAnosWhite(cont: Context): BaseSpinnerAdapter(
    cont,
    spinner_item = R.layout.lay_spinner_item_white,
    spinner_subitem = R.layout.lay_spinner_subitem_white
) {

    private val anos: ArrayList<String> by lazy {
        (2008..2028).mapToArrayList {
            it.toString()
        }
    }

    override fun getItemCount(): Int = anos.size

    override fun bindItemText(componente: TextView, p: Int) {
        componente.text = anos[p]
    }

    override fun bindSubItemText(componente: TextView, p: Int) {
        componente.text = anos[p]
    }

    fun getAno(pos: Int): Int = anos[pos].toInt()
}