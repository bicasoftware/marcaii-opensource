package act_get_empregos.core

import act_get_empregos.frag_emprego.FragEmpregoDto
import act_get_empregos.frag_porcentagem.FragPorcentagensDto
import act_get_empregos.frag_porcentagem.HoraDiferDto
import utils.CalculoHorasHelper
import utils_realm.*

class ModelActGetEmprego(
    val presenter: MVPGetEmpregos.PresenterActGetEmpregosImpl,
    var idEmprego: String = ""
):
    MVPGetEmpregos.ModelActGetEmpregosImpl {

    private val manager: RealmObjectManager<REmpregos> by lazy {
        RealmObjectManager(REmpregos::class.java, idEmprego).apply {
            setSubNodes {
                salarios.add(RSalarios())
            }
        }
    }

    companion object {
        fun provideHorasDifer(e: REmpregos): ArrayList<HoraDiferDto> {

            val resultList = arrayListOf<HoraDiferDto>()
            for(i in 0..6) {
                val horaDif = HoraDiferDto(dia = i)

                if(e.porcentagemDiferenciadas.count { it.diaSemana == i } > 0) {
                    val porcentagem = e.porcentagemDiferenciadas.first { it.diaSemana == i }.porcAdicional
                    val valorDiferencial = if(!e.bancoHoras) {
                        CalculoHorasHelper.calcValorHora(
                            e.salarios.first { it.status }.valorSalario,
                            e.cargaHoraria,
                            porcentagem
                        ).toDouble()
                    } else {
                        0.0
                    }

                    horaDif.valor = valorDiferencial
                    horaDif.porcentagem = porcentagem
                }

                resultList.add(horaDif)
            }

            return resultList
        }

    }

    override fun closeRealm() {
        manager.closeRealm()
    }

    override fun provideEmprego(): REmpregos {
        return manager.provideInstance()
    }

    override fun commitEmprego() {
        manager.commitInstance()
        idEmprego = manager.provideInstance().id
    }

    override fun getPorcNormal(): Int {
        return manager.provideInstance().porcNormal
    }

    override fun getPorcCompleta(): Int {
        return manager.provideInstance().porcFeriados
    }

    override fun setNomeEmprego(nome: String) {
        manager.updateInstance { nomeEmprego = nome }
    }

    override fun setHorarioSaida(hs: String) {
        manager.updateInstance { horarioSaida = hs }
    }

    override fun setDiaFechamento(dia: Int) {
        manager.updateInstance { diaFechamento = dia }
    }

    override fun setValorSalario(valor: Double) {
        manager.updateInstance {
            salarios.first { it.status }.valorSalario = valor
        }
    }

    override fun appendAumento(vigencia: String, valor: Double) {
        manager.updateInstance {
            salarios.forEach { it.status = false }
            salarios.add(
                RSalarios(valorSalario = valor, status = true, vigencia = vigencia)
            )
        }
    }

    override fun setBancoHoras(status: Boolean) {
        manager.updateInstance { bancoHoras = status }
    }

    override fun setCargaHoraria(cargaHoraria: String) {
        manager.updateInstance { this.cargaHoraria = cargaHoraria }
    }

    override fun setPorcNormal(porc: Int) {
        manager.updateInstance { porcNormal = porc }
    }

    override fun setPorcCompleta(porc: Int) {
        manager.updateInstance { porcFeriados = porc }
    }

    override fun setPorcentDif(diaSemana: Int, porcent: Int) {
        manager.updateInstance {
            if(porcentagemDiferenciadas.count { it.diaSemana == diaSemana } > 0) {
                porcentagemDiferenciadas.first { it.diaSemana == diaSemana }?.porcAdicional = porcent
            } else {
                porcentagemDiferenciadas.add(
                    RPorcentagemDiferenciada(
                        diaSemana = diaSemana, porcAdicional = porcent
                    )
                )
            }
        }
    }

    override fun removePercentDifByPos(diaSemana: Int) {
        manager.updateInstance {
            if(idEmprego.isBlank()) {
                porcentagemDiferenciadas
                    .removeAt(
                        porcentagemDiferenciadas.indexOfFirst { it.diaSemana == diaSemana }
                    )
            } else if(porcentagemDiferenciadas.count { it.diaSemana == diaSemana } > 0) {
                porcentagemDiferenciadas
                    .first { it.diaSemana == diaSemana }
                    .deleteFromRealm()
            }
        }
    }

    override fun providePorcentagemDto(): FragPorcentagensDto {
        val i = manager.provideInstance()
        return FragPorcentagensDto(
            porcNormal = i.porcNormal,
            porcCompleta = i.porcFeriados,
            valorNormal = calcValorNormal(),
            valorCompleta = calcValorCompleto(),
            horasDiferenciais = provideHorasDifer(i)
        )
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

    override fun provideEmpregoDto(): FragEmpregoDto {
        val e = manager.provideInstance()
        return FragEmpregoDto(
            nomeEmprego = e.nomeEmprego,
            valorSalario = e.salarios.first { it.status }.valorSalario,
            diaFechamento = e.diaFechamento,
            cargaHoraria = e.cargaHoraria,
            bancoHoras = e.bancoHoras,
            horaSaida = e.horarioSaida
        )
    }

    override fun provideSalario(): Double {
        return manager.provideInstance().salarios.first { it.status }.valorSalario
    }
}