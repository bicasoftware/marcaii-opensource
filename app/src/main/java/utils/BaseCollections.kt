@file:Suppress("LoopToCallChain", "unused")

import android.content.Context

inline fun <T, R> Iterable<T>?.mapToMutableList(transform: (T) -> R): MutableList<R> {
    val resultList = mutableListOf<R>()
    if(this != null) for(item in this) resultList.add(transform(item))
    return resultList
}

inline fun <T, R> Iterable<T>.mapToMutableListWithCounter(transform: (Int, T) -> R): MutableList<R> {
    return mutableListOf<R>().also {
        forEachIndexed { i, t -> it.add(transform(i, t)) }
    }
}

inline fun <T, R> Iterable<T>.mapToList(transform: (T) -> R): List<R> {
    return mutableListOf<R>().also { l ->
        for(item in this) l.add(transform(item))
    }
}

inline fun <T, R> Iterable<T>.mapToListWithCounter(transform: (Int, T) -> R): List<R> {
    return mutableListOf<R>().also {
        forEachIndexed { i, t -> it.add(transform(i, t)) }
    }
}

inline fun <T, R> Iterable<T>?.mapToArrayList(transform: (T) -> R): ArrayList<R> {
    val resultList = arrayListOf<R>()
    if(this != null) for(item in this) resultList.add(transform(item))
    return resultList
}

fun getResourceArrayAsList(cont: Context, resourceId: Int): List<String> {
    return cont.resources.getStringArray(resourceId).asList()
}

fun <T> ArrayList<T>.copy(): ArrayList<T> {
    return arrayListOf<T>().also {
        it.addAll(this@copy)
    }
}

fun <T> MutableList<T>.copy(): MutableList<T> {
    return mutableListOf<T>().also {
        it.addAll(this@copy)
    }
}

object BaseCollections {
    /**
     * retorna sempre 0 mesmo se a collection for null
     * */
    fun <T> Collection<T>?.size(): Int {
        return this?.size ?: 0
    }
}
