package utils_realm

import io.realm.RealmObject

open class RSalarios(
      open var id: String = getUUID<RSalarios>(),
      open var valorSalario: Double = 1200.0,
      open var status: Boolean = true,
      open var vigencia: String = "1988-01-01"
) : RealmObject()