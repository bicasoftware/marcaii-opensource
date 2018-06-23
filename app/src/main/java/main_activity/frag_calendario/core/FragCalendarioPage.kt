package main_activity.frag_calendario.core

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import br.sha.commommodule.BaseFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_calendar_page_page.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import main_activity.frag_calendario.CalendarioPageContract
import main_activity.frag_calendario.calendario.RecAdapterCalendario
import main_activity.frag_calendario.calendario.RecAdapterHeader
import models.EmpregoInfoDto
import models.HoraInfoDto
import utils.ItemOffsetDecoration
import utils.instanceBuilder
import java.lang.ref.WeakReference

class FragCalendarioPage: BaseFragment(R.layout.lay_frag_calendar_page_page),
    MVPCalendarioPage.ViewContentPageImpl {

    private lateinit var presenter: PresenterCalendarPage
    private lateinit var calendarioPageContract: CalendarioPageContract

    private val offSetDecoration: ItemOffsetDecoration by lazy {
        ItemOffsetDecoration(resources.getDimensionPixelOffset(R.dimen.small_calendar_dimen))
    }

    companion object {

        private const val CONST_PAGECALENDARIO = "pagecalendario"

        fun newInstance(pageContentDto: EmpregoInfoDto) = instanceBuilder<FragCalendarioPage> {
            putParcelable(CONST_PAGECALENDARIO, pageContentDto)
        }
    }

    override fun afterViewCreated(view: View?, b: Bundle?) {
        presenter = PresenterCalendarPage(
            this,
            arguments?.getParcelable(CONST_PAGECALENDARIO) as EmpregoInfoDto
        )

        async(UI) {
            rv_calendario_header.layoutManager = GridLayoutManager(this@FragCalendarioPage.context, 7)
            rv_calendario_header.adapter = RecAdapterHeader(this@FragCalendarioPage.context!!)

            rv_calendario.layoutManager = GridLayoutManager(this@FragCalendarioPage.context, 7)
            rv_calendario.addItemDecoration(offSetDecoration)
            rv_calendario.adapter = presenter.provideCalendarAdapter()
        }

    }

    override fun onAttach(context: Context?) {
        calendarioPageContract = context as CalendarioPageContract
        super.onAttach(context)
    }

    override fun provideContext(): Context {
        return context!!
    }

    override fun provideWeakReference(): WeakReference<Context> {
        return WeakReference(context!!)
    }

    override fun setAdapter(adapter: RecAdapterCalendario) {
        launch(UI) {
            rv_calendario.adapter = adapter
        }
    }

    override fun showActGetHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int) {
        calendarioPageContract.showActGetHoras(idEmprego, itemCalendario, adapterPosition)
    }

    override fun showBtsInfoHoras(idEmprego: String, itemCalendario: HoraInfoDto, adapterPosition: Int) {
        calendarioPageContract.showBtsInfoHoras(idEmprego, itemCalendario, adapterPosition)
    }

    override fun setCalendarAdapter(adapterCalendar: RecAdapterCalendario) {
        launch(UI) {
            rv_calendario.adapter = adapterCalendar
        }
    }

    fun updateItemByPosition(pos: Int, horaInfoDto: HoraInfoDto) {
        presenter.updateItemByPos(pos, horaInfoDto)
    }

    fun refreshContent(content: EmpregoInfoDto){
        arguments?.putParcelable(CONST_PAGECALENDARIO, content)
        presenter.refreshContent(content)
    }

    fun provideEmpregoId(): String{
        return presenter.getEmpregoId()
    }
}