package bts_view_horas

import models.HoraInfoDto

interface BtsHoraInfoContract {
    fun onHoraDelete(pos: Int, horaInfoDto: HoraInfoDto)
    fun onHoraUpdate(idEmprego: String, itemCaledar: HoraInfoDto, pos: Int)
}