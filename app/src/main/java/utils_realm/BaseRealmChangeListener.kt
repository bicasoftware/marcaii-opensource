package utils_realm

import br.sha.commommodule.BaseRecyclerView
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmList

fun <T> BaseRecyclerView.provideRealmListenerForList(): OrderedRealmCollectionChangeListener<RealmList<T>> {

    return OrderedRealmCollectionChangeListener { _, changeSet ->

        val remCount = changeSet.deletionRanges
        for(pos in remCount.size - 1 downTo 0) {
            val item = remCount[pos]
            this.notifyItemRangeRemoved(item.startIndex, item.length)
        }

        changeSet.changeRanges?.forEach {
            this.notifyItemRangeChanged(it.startIndex, it.length)
        }

        changeSet.insertions?.forEach {
            this.notifyItemInserted(it)
        }
    }
}

