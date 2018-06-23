package utils_realm

import io.realm.RealmList
import io.realm.RealmObject

open class REmpregos(
    var id: String = getUUID<REmpregos>(),
    var diaFechamento: Int = 25,
    var porcNormal: Int = 50,
    var porcFeriados: Int = 100,
    var nomeEmprego: String = "Emprego",
    var horarioSaida: String = "18:00",
    var cargaHoraria: String = "220",
    var bancoHoras: Boolean = false,
    var salarios: RealmList<RSalarios> = RealmList(),

    var porcentagemDiferenciadas: RealmList<RPorcentagemDiferenciada> = RealmList(),
    var horas: RealmList<RHoras> = RealmList()

): RealmObject()