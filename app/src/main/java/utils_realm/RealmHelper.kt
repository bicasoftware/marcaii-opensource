package utils_realm

import android.util.Log
import io.realm.*
import utils.defFormat
import java.util.*

inline fun <reified T: RealmObject>getUUID(): String{

    val r = Realm.getDefaultInstance()

    var isRandom = false
    var newId = ""

    while(!isRandom) {
        newId = UUID.randomUUID().toString() //gera valor aleatorio, mas é possível que repita o valor

        //se o valor gerado não existir no realm, quebra o while
        if(r.where(T::class.java).equalTo("id", newId).count().toInt() == 0) {
            isRandom = true
        }
    }

    r.close()
    return newId
}

object RealmHelper {

    fun getRealmConfiguration(): RealmConfiguration = RealmConfiguration.Builder()
        .name("marcaii.realm")
        .schemaVersion(4)
        .migration(migration)
        .build()

    private val migration = RealmMigration { dynamicRealm, oldVersion, newVersion ->
        Log.d("saulo", "oldVersion: $oldVersion - newVersion: $newVersion")
        val fmt = defFormat
        when(oldVersion.toInt()) {
            1 -> {
                dynamicRealm.schema.let {
                    it.get("RHoras")?.let {
                        it.renameField("horarioInicial", "horaInicial")
                        it.renameField("horarioTermino", "horaTermino")
                        it.setNullable("quantidade", false)
                    }

                    it.get("RSalarios")?.setRequired("valorSalario", true)

                    it.get("RPorcentagemDiferenciada")?.let {
                        it.setNullable("diaSemana", false)
                        it.setNullable("porcAdicional", false)
                    }

                    it.get("REmpregos")?.let {
                        it.setNullable("diaFechamento", false)
                        it.setNullable("porcNormal", false)
                        it.setNullable("porcFeriados", false)
                        it.setNullable("cargaHoraria", false)
                        it.setNullable("bancoHoras", false)
                        it.setNullable("notificacoes", false)
                    }
                }
            }

            2 -> {
                dynamicRealm.schema.let {

                    it.get("RSalarios")?.let {
                        it.addField("vigencia_", String::class.java)
                        it.addField("_id", String::class.java)
                        it.transform {
                            it.setString("vigencia_", fmt.format(it.getDate("vigencia")))
                            it.setString("_id", it.getInt("id").toString())
                        }

                        it.removeField("vigencia")
                        it.renameField("vigencia_", "vigencia")

                        it.removeField("id")
                        it.renameField("_id", "id")
                    }

                    it.get("RHoras")?.let {
                        it.addField("dta_", String::class.java)
                        it.addField("_id", String::class.java)
                        it.transform {
                            it.setString("dta_", fmt.format(it.getDate("dta")))
                            it.setString("_id", it.getInt("id").toString())
                        }

                        it.removeField("dta")
                        it.renameField("dta_", "dta")

                        it.removeField("id")
                        it.renameField("_id", "id")

                    }

                    it.get("RPorcentagemDiferenciada")?.let {
                        it.addField("_id", String::class.java)
                        it.transform {
                            it.setString("_id", it.getInt("id").toString())
                        }
                        it.removeField("id")
                        it.renameField("_id", "id")
                    }

                    it.get("REmpregos")?.let {
                        it.addField("_id", String::class.java)
                        it.transform {
                            it.setString("_id", it.getInt("id").toString())
                        }
                        it.removeField("id")
                        it.renameField("_id", "id")
                    }
                }
            }

            3 -> {
                dynamicRealm.schema.let {

                    it.get("REmpregos")?.let {
                        it.addField("idEmprego", String::class.java)
                        it.addField("_cargaHoraria", String::class.java)
                        it.transform { t ->
                            t.setString("_cargaHoraria", t.getInt("cargaHoraria").toString())
                        }
                        it.removeField("cargaHoraria")
                        it.renameField("_cargaHoraria", "cargaHoraria")
                    }
                }
            }

        }

        oldVersion.inc()
    }
}
