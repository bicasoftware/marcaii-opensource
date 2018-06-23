package act_get_hora

import exemple.sha.horas.R
import io.realm.kotlin.where
import main_activity.frag_calendario.calendario.CalendarBuilder
import models.HoraType
import utils.*
import utils_realm.*
import java.math.BigDecimal
import java.util.*

class ModelActGetHora(
    private val presenter: MVPActGetHora.PresenterActGetHoraImpl,
    private val idHora: String,
    private val dataHora: Date,
    private val idEmprego: String
): MVPActGetHora.ModelActGetHoraImpl {

    private val manager: RealmObjectManager<RHoras> by lazy {
        RealmObjectManager(RHoras::class.java, idHora).also {
            it.updateInstance { dta = dateToString(dataHora) }
        }
    }

    private val e: REmpregos by lazy {
        manager.getRealm().where<REmpregos>().equalTo("id", idEmprego).findFirst()!!
    }

    override fun closeRealm() {
        manager.closeRealm()
    }

    override fun setHoraInicial(hora: String) {
        manager.updateInstance {
            horaInicial = hora
            quantidade = getHorasEMinutos(this.horaInicial, this.horaTermino)
        }
    }

    override fun setHoraTermino(hora: String) {
        manager.updateInstance {
            horaTermino = hora
            quantidade = getHorasEMinutos(this.horaInicial, this.horaTermino)
        }
    }

    override fun setTipoHora(horaType: HoraType) {
        manager.updateInstance {
            tipoHora = horaType.tipo
        }
    }

    override fun getHoraInicial() = manager.provideInstance().horaInicial

    override fun getHoraTermino() = manager.provideInstance().horaTermino

    override fun getTipoHora(): Int {
        return when(manager.provideInstance().tipoHora) {
            HoraType.NORMAL.tipo -> R.id.rdb_normal
            HoraType.COMPLETA.tipo -> R.id.rdb_feriado
            HoraType.DIFF.tipo -> R.id.rdb_diff
            else -> R.id.rdb_normal
        }
    }

    override fun getHoraType(): HoraType {
        return HoraType.values().first { it.tipo == manager.provideInstance().tipoHora }
    }

    override fun getIdHora(): String {
        return manager.provideInstance().id
    }

    override fun hasHoraDif(): Boolean {
        val count = e.porcentagemDiferenciadas.count { it.diaSemana == dataHora.weekDay() - 1 }
        return count > 0
    }

    override fun commitHora(idEmprego: String) {
        manager.updateInstance {
            this.idEmprego = idEmprego
            quantidade = getHorasEMinutos(this.horaInicial, this.horaTermino)
        }

        manager.getRealm().executeTransaction {
            val e = it.where<REmpregos>().equalTo("id", idEmprego).findFirst()
            e?.horas?.add(manager.provideInstance())
        }
    }

    override fun provideInstance(): RHoras {
        return manager.provideInstance()
    }

    override fun getValorHora(): BigDecimal {
        return CalendarBuilder.getValorHora(e, manager.provideInstance())
    }
}