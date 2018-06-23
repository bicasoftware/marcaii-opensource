package act_get_empregos

interface ActGetEmpregoContract {
    fun showDialogPorcentagemNormal()
    fun showDialogPorcentagemCompleta()
    fun showDialogOptionsSalario()
    fun showDialogHoraSaida(hora: Pair<Int, Int>)
    fun showDialogDiaFechamento(diaFechamento: Int)

    fun setNomeEmprego(newValue: String)
    fun setBancoHoras(checked: Boolean)
    fun setCargaHoraria(selectPos: Int)

    fun showOptionDialogHorasDiff(pos: Int)
    fun showGetPorcDiffValorDialog(pos: Int, porc: Int)
}