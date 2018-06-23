package act_presentation.fragments

interface PresentationContract {
    fun showSalarioDialog()
    fun onCargaHorariaChanged(pos: Int)
    fun showDialogGetPorcNormal()
    fun showDialogGetPorcCompleta()
    fun showDialogGetPorcDifer(pos: Int, porc: Int)
    fun showOptionDialogHorasDiff(pos: Int)
}