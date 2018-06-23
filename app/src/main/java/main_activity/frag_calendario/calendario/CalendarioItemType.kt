package main_activity.frag_calendario.calendario

sealed class CalendarioItemType {
    abstract fun getViewType(): Int
}

class EmptyType: CalendarioItemType() {
    companion object {
        const val viewType = 0
    }

    override fun getViewType() = viewType
}

class ItemType: CalendarioItemType() {
    companion object {
        const val viewType = 1
    }

    override fun getViewType() = viewType
}