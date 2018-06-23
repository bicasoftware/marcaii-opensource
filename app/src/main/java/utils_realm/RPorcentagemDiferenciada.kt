package utils_realm

import io.realm.RealmObject

open class RPorcentagemDiferenciada(
      open var id: String = getUUID<RPorcentagemDiferenciada>(),
      open var diaSemana: Int = 0,
      open var porcAdicional: Int = 0
) : RealmObject()