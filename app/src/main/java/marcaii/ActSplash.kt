package marcaii

import act_presentation.ActPresentation
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import exemple.sha.horas.R
import io.realm.Realm
import io.realm.kotlin.where
import main_activity.act_main.ActMain
import org.jetbrains.anko.startActivity
import utils_realm.REmpregos
import utils_realm.RealmDelegate
import java.util.*

class ActSplash: AppCompatActivity() {

    private val r: Realm by RealmDelegate()

    private val timer = Timer()
    private lateinit var task: TimerTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lay_act_splash)

        callNextActivity()
    }

    override fun onStop() {
        super.onStop()
        r.close()
    }

    private fun callNextActivity() {
        if(r.where<REmpregos>().count() > 0) {
            startActByTimer<ActMain>()
        } else {
            startActivity<ActPresentation>()
            this.finish()
        }
    }

    private inline fun <reified T: AppCompatActivity> startActByTimer(vararg parametros: Pair<String, Any?>) {
        task = object: TimerTask() {
            override fun run() {
                startActivity<T>(*parametros)
                finish()
            }
        }
        timer.schedule(task, 1000)
    }

}
