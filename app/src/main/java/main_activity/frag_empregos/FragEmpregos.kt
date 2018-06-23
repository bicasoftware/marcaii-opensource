package main_activity.frag_empregos

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.sha.commommodule.BaseFragment
import exemple.sha.horas.R
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.lay_frag_empregos.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import utils.instanceBuilder
import utils_realm.REmpregos
import utils_realm.RealmDelegate

class FragEmpregos: BaseFragment(R.layout.lay_frag_empregos), FragEmpregosListener {

    private lateinit var contract: FragEmpregosContract

    val r: Realm by RealmDelegate()

    companion object {

        fun newInstance() = instanceBuilder<FragEmpregos> {}
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {
        launch(UI) {
            rv_empregos.layoutManager = LinearLayoutManager(this@FragEmpregos.context)
            rv_empregos.adapter = RecAdapterEmpregos(
                this@FragEmpregos.context!!,
                r.where<REmpregos>().findAll(),
                this@FragEmpregos
            )
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contract = context as FragEmpregosContract
    }

    override fun onItemClick(idEmprego: String, pos: Int) {
        contract.showActGetEmprego(idEmprego, pos)
    }

    override fun onLongItemClick(idEmprego: String, pos: Int) {
        contract.showOptionsActGetEmprego(idEmprego, pos)
    }
}