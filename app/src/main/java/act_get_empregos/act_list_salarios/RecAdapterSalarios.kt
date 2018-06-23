package act_get_empregos.act_list_salarios

import android.content.Context
import asCurrency
import br.sha.commommodule.BaseRecyclerView
import exemple.sha.horas.R
import io.realm.RealmList
import kotlinx.android.synthetic.main.lay_frag_empregos_act_get_emprego_act_salarios_item.view.*
import utils_realm.provideRealmListenerForList
import utils.dateStringToBrDateString
import utils_realm.RSalarios

class RecAdapterSalarios(
    val cont: Context,
    private val salarios: RealmList<RSalarios>
):
    BaseRecyclerView(cont, R.layout.lay_frag_empregos_act_get_emprego_act_salarios_item) {

    init {
        salarios.addChangeListener(provideRealmListenerForList())
    }

    override fun getRecyclerCount() = salarios.size

    override fun BindViewHolder(h: BaseHolder, p: Int) {
        with(h.itemView) {
            salarios[p]?.let {
                tv_salario_valor.text = it.valorSalario.asCurrency()
                tv_salario_vigencia.text = dateStringToBrDateString(it.vigencia)
            }
        }
    }
}