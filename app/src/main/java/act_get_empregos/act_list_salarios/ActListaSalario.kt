package act_get_empregos.act_list_salarios

import act_get_empregos.core.ActGetEmpregos
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import br.sha.commommodule.BaseActivityBackButton
import br.sha.commommodule.BaseRecyclerViewDivider
import exemple.sha.horas.R
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.lay_frag_empregos_act_get_emprego_act_salarios.*
import utils_realm.REmpregos
import utils_realm.RealmDelegate

class ActListaSalario: BaseActivityBackButton(
    layout = R.layout.lay_frag_empregos_act_get_emprego_act_salarios,
    fTitle = R.string.str_salarios
) {

    val r: Realm by RealmDelegate()

    override fun doAfterCreated(b: Bundle?) {
        val idEmprego = b?.getString(ActGetEmpregos.BUNDLE_IDEMPREGO) ?: ""
        val salarios = r.where<REmpregos>().equalTo("id", idEmprego).findFirst()?.salarios!!

        rv_salarios.layoutManager = LinearLayoutManager(applicationContext)
        rv_salarios.addItemDecoration(BaseRecyclerViewDivider(this))
        rv_salarios.adapter = RecAdapterSalarios(this, salarios)
    }

    override fun doOnBackPressed() = Unit

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home) finish()
        return true
    }

    override fun onDestroy() {
        r.close()
        super.onDestroy()
    }
}