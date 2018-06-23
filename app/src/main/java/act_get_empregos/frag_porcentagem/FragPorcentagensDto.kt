package act_get_empregos.frag_porcentagem

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FragPorcentagensDto(
    var porcNormal: Int = 50,
    var porcCompleta: Int = 100,
    var valorNormal: Double = 0.0,
    var valorCompleta: Double = 0.0,
    var horasDiferenciais: ArrayList<HoraDiferDto> = arrayListOf()
): Parcelable