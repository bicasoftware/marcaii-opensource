package main_activity.frag_empregos

interface FragEmpregosContract {
    fun showActGetEmprego(idEmprego: String, adapterEmpregosPosition: Int)
    fun showOptionsActGetEmprego(idEmprego: String, adapterEmpregosPosition: Int)
    fun onEmpregoInsert(idEmprego: String)
    fun onEmpregoUpdate(idEmprego: String)
}