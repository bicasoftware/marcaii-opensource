package models

import exemple.sha.horas.R

enum class HoraType(
    val tipo: String,
    val colorId: Int,
    val resString: Int
) {

    NORMAL("HORANORMAL_CONST", R.color.horanormal, R.string.str_hora_normal),
    COMPLETA("HORAFERIADO_CONST", R.color.horaferiado, R.string.str_hora_completa),
    DIFF("HORADIFF_CONST", R.color.horadiff, R.string.str_hora_diferencial),
    NONE("", 0, R.string.str_none)
}