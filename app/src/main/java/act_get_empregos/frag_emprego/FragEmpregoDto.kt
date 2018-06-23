package act_get_empregos.frag_emprego

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FragEmpregoDto(
    var nomeEmprego: String = "",
    var valorSalario: Double = 1200.0,
    var diaFechamento: Int = 25,
    var cargaHoraria: String = "220",
    var horaSaida: String = "18:00",
    var bancoHoras: Boolean = false
): Parcelable