package main_activity.frag_empregos

interface FragEmpregosListener {
    fun onItemClick(idEmprego: String, pos: Int)
    fun onLongItemClick(idEmprego: String, pos: Int)
}