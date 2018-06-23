package utils_realm

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.Required
import models.HoraType
import utils.dateStringAsDate
import utils.emptyDate
import java.util.*

open class RHoras(
    open var id: String = getUUID<RHoras>(),
    open var quantidade: Int = 0,
    open var horaInicial: String = "18:00",
    open var horaTermino: String = "19:00",
    open var dta: String = "1988-01-01",
    open var tipoHora: String = HoraType.NORMAL.tipo,

    @Required
    open var idEmprego: String = ""
): RealmObject() {

    @Ignore
    var data: Date = emptyDate
        get() = dta.dateStringAsDate()
}