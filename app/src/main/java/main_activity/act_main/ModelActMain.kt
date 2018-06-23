package main_activity.act_main

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import utils_realm.*

class ModelActMain(private val presenter: MVPActMain.PresenterActMainImpl): MVPActMain.ModelActMainImpl {
    private val r: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun closeRealm() {
        r.close()
    }

    override fun deleteEmprego(idEmprego: String) {
        r.executeTransaction { it.where<REmpregos>().equalTo("id", idEmprego).findFirst()?.deleteFromRealm() }
    }

    override fun deleteHora(idHora: String) {
        r.executeTransaction {
            it.where<RHoras>().equalTo("id", idHora).findFirst()?.deleteFromRealm()
        }
    }

    override fun provideEmpregos(): RealmResults<REmpregos> {
            return r.where<REmpregos>().findAll()
        }

    override fun provideEmprego(idEmprego: String): REmpregos {
        return r.where<REmpregos>().equalTo("id", idEmprego).findFirst() ?: throw Exception("Realm row not found with id $idEmprego")
    }
}
