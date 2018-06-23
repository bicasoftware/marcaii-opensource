package marcaii

import android.app.Application
import android.support.multidex.MultiDexApplication
import io.realm.Realm
import utils_realm.RealmHelper

class Marcaii: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        Realm.setDefaultConfiguration(RealmHelper.getRealmConfiguration())
    }
}