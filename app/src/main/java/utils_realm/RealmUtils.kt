package utils_realm

import io.realm.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RealmDelegate: ReadOnlyProperty<Any, Realm> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Realm {
        return Realm.getDefaultInstance()
    }
}