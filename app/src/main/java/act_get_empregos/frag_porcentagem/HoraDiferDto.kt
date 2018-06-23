package act_get_empregos.frag_porcentagem

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HoraDiferDto(
    var porcentagem: Int = 0,
    val dia: Int,
    var valor: Double = 0.0
): Parcelable {

    fun reset(){
        porcentagem = 0
        valor = 0.0
    }
}