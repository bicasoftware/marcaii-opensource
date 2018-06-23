package act_presentation.core

import act_get_empregos.core.ModelActGetEmprego
import act_get_empregos.frag_porcentagem.HoraDiferDto
import utils.CalculoHorasHelper
import utils_realm.*

class ModelActPresent(private val presenter: MVPActPresentation.PresenterActPresentImpl): MVPActPresentation.ModelActPresentImpl {

    private val manager: RealmObjectManager<REmpregos> by lazy {
        RealmObjectManager(REmpregos::class.java).apply {
            setSubNodes {
                salarios.add(RSalarios())
            }
        }
    }

    override fun closeRealm() {
        manager.closeRealm()
    }

    override fun provideSalario(): Double {
        return manager.provideInstance().salarios.first()?.valorSalario ?: 1200.0
    }

    override fun providePorcNormal(): Int {
        return manager.provideInstance().porcNormal
    }

    override fun providePorcCompleta(): Int {
        return manager.provideInstance().porcFeriados
    }

    override fun providePorcDifer(): ArrayList<HoraDiferDto> {
        return ModelActGetEmprego.provideHorasDifer(manager.provideInstance())
    }

    override fun commitEmprego() {
        manager.commitInstance()
    }

    override fun setSalario(valor: Double) {
        manager.updateInstance {
            salarios.first()?.valorSalario = valor
        }
    }

    override fun setCargaHoraria(carga: String) {
        manager.updateInstance {
            cargaHoraria = carga
        }
    }

    override fun setPorcNormal(porc: Int) {
        manager.updateInstance {
            porcNormal = porc
        }
    }

    override fun setPorcCompleta(porc: Int) {
        manager.updateInstance {
            porcFeriados = porc
        }
    }

    override fun setPorcDifer(pos: Int, porc: Int) {
        manager.updateInstance {
            if(porcentagemDiferenciadas.count { it.diaSemana == pos } > 0) {
                porcentagemDiferenciadas.first { it.diaSemana == pos }?.porcAdicional = porc
            } else {
                porcentagemDiferenciadas.add(
                    RPorcentagemDiferenciada(
                        diaSemana = pos, porcAdicional = porc
                    )
                )
            }
        }
    }

    override fun clearPorcDifByPos(pos: Int) {
        manager.updateInstance {
            porcentagemDiferenciadas.removeAt(
                porcentagemDiferenciadas.indexOfFirst { it.diaSemana == pos }
            )
        }
    }

    override fun calcValorNormal(): Double {
        val e = manager.provideInstance()

        return if(!e.bancoHoras) {
            CalculoHorasHelper.calcValorHora(
                e.salarios.first { it.status }.valorSalario,
                e.cargaHoraria,
                e.porcNormal
            ).toDouble()
        } else {
            0.0
        }

    }

    override fun calcValorCompleto(): Double {
        val e = manager.provideInstance()

        return if(!e.bancoHoras) {
            CalculoHorasHelper.calcValorHora(
                e.salarios.first { it.status }.valorSalario,
                e.cargaHoraria,
                e.porcFeriados
            ).toDouble()
        } else {
            0.0
        }
    }

    override fun calcPorcentDif(porcent: Int): Double {
        val e = manager.provideInstance()

        return if(!e.bancoHoras) {
            CalculoHorasHelper.calcValorHora(
                e.salarios.first { it.status }.valorSalario,
                e.cargaHoraria,
                porcent
            ).toDouble()
        } else {
            0.0
        }
    }
}
