package models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmpregoInfoDto(
    var idEmprego: String,
    var nomeEmprego: String,
    var horasInfo: ArrayList<HoraInfoDto> = arrayListOf()
): Parcelable