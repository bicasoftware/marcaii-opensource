package main_activity.frag_calendario

interface FragCalendarioContract {
    fun nextMonth()
    fun previousMonth()
    fun showDialogGetMonth()
    fun showDialogGetYear()
    fun setCurrentPagePosition(position: Int)
}