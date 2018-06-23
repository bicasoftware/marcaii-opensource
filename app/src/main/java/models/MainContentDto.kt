package models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainContentDto(
    var ano: Int = 2010,
    var mes: Int = 0,
    val empregoInfo: ArrayList<EmpregoInfoDto> = arrayListOf()
): Parcelable

