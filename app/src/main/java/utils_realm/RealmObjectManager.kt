@file:Suppress("unused")

package utils_realm

import io.realm.Realm
import io.realm.RealmObject

class RealmObjectManager<T: RealmObject>(
    private val clazz: Class<T>,
    private val objectId: String = ""
) {

    private val r: Realm by RealmDelegate()

    private var instance: T

    init {
        instance = if(objectId.isBlank()) {
            clazz.newInstance()
        } else {
            getInstanceById(objectId)
        }
    }

    fun provideInstance() = instance

    fun injectInstance(instance: T) {
        this.instance = instance
    }

    fun injectId(id: String) = getInstanceById(id)

    private fun getInstanceById(id: String): T {
        return r.where(clazz).equalTo("id", id).findFirst()
            ?: throw Exception("no record for ${clazz.name} with id $objectId")
    }

    fun getId() = objectId

    fun updateInstance(doOnInstance: T.() -> Unit) {
        if(instance.isManaged) {
            r.executeTransaction {
                doOnInstance(instance)
            }
        } else {
            doOnInstance(instance)
        }
    }

    fun commitInstance() {
        if(!instance.isManaged) r.executeTransaction {
            it.insert(instance)
        }
    }

    fun closeRealm(){
        r.close()
    }

    fun provideCopy(): T {
        return r.copyFromRealm(instance)
    }

    fun <H> search(inst: T.() -> H): H {
        return this.instance.inst()
    }

    fun getRealm() = r

    fun setSubNodes(doWithInstance: T.() -> Unit) {
        if(objectId.isBlank()) {
            r.executeTransaction {
                instance.doWithInstance()
            }
        }
    }
}