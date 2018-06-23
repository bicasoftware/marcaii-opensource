package act_get_empregos.bts_get_aumento

interface BtsGetAumentoContract {
    fun showDialogGetAumento(salario: Double)
    fun onSaveAumento(mes: Int, ano: Int, valor: Double)
    fun onCancel()
}