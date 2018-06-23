package main_activity.frag_calendario.calendario

import models.HoraInfoDto

interface CalendarioItemListener {
    fun onCalendarItemClicked(itemCalendario: HoraInfoDto, adapterPosition: Int)
}