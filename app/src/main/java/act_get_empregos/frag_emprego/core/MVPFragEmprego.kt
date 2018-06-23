package act_get_empregos.frag_emprego.core

import android.content.Context
import br.sha.commommodule.BaseSpinnerAdapter

interface MVPFragEmprego {

    interface ModelFragEmpregoImpl {
        fun getBancoHora(): Boolean
        fun getCargaHoraria(): String
        fun getDiaFechamento(): Int
        fun getHorarioSaida(): String
        fun getNomeEmprego(): String
        fun getValorSalario(): Double
    }

    interface PresenterFragEmpregoImpl {
        fun getBancoHora(): Boolean
        fun getCargaHoraria(): String
        fun getPosByCargaHoraria(): Int
        fun getDiaFechamento(): Int
        fun getHorarioSaida(): String
        fun getSplitHorarioSaida(): Pair<Int, Int>
        fun getNomeEmprego(): String
        fun getValorSalario(): Double
        fun provideCargaHorariaAdapter(): BaseSpinnerAdapter
    }

    interface ViewFragEmpregoImpl {
        fun provideContext(): Context
    }
}