package main_activity.frag_calendario

import models.HoraInfoDto

interface CalendarioPageContract {
    fun showActGetHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int)
    fun showBtsInfoHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int)
}