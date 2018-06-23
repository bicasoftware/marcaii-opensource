package main_activity.frag_calendario

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import br.sha.commommodule.BasePagedFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_frag_calendario.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import main_activity.frag_calendario.core.FragCalendarioPage
import models.HoraInfoDto
import models.MainContentDto
import utils.instanceBuilder
import utils.mapToArrayList

class FragCalendario: BasePagedFragment(
    mLayout = R.layout.lay_frag_calendario,
    mViewPager = R.id.vp_content,
    mTabLayout = R.id.tbl_content
) {

    private lateinit var contract: FragCalendarioContract

    private val meses: Array<String> by lazy {
        resources.getStringArray(R.array.arr_meses_ext)
    }

    companion object {
        private const val CONST_CONTENT = "constcontent"

        fun newInstance(calendarContent: MainContentDto): FragCalendario {
            return instanceBuilder {
                putParcelable(CONST_CONTENT, calendarContent)
            }
        }
    }

    override fun setPagesNInitialize(b: Bundle?) {
        retainInstance = true

        val content = arguments?.getParcelable<MainContentDto?>(CONST_CONTENT)

        bt_calendario_ano.text = content?.ano.toString()
        bt_calendario_mes.text = meses[content?.mes ?: 0]

        addTabs(
            content?.empregoInfo.mapToArrayList {
                Pair(
                    FragCalendarioPage.newInstance(it), it.nomeEmprego
                )
            }
        )

        bt_calendario_next.setOnClickListener {
            contract.nextMonth()
        }

        bt_calendario_prev.setOnClickListener {
            contract.previousMonth()
        }

        bt_calendario_mes.setOnClickListener {
            contract.showDialogGetMonth()
        }

        bt_calendario_ano.setOnClickListener {
            contract.showDialogGetYear()
        }

        provideViewPager().addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {
                contract.setCurrentPagePosition(position)
            }
        })

    }

    override fun onAttach(context: Context?) {
        contract = context as FragCalendarioContract
        super.onAttach(context)
    }

    fun putCalendarContent(content: MainContentDto) {
        arguments?.putParcelable(CONST_CONTENT, content)
        bt_calendario_ano.text = content.ano.toString()
        bt_calendario_mes.text = meses[content.mes]

        childFragmentManager.fragments.forEach {
            if(it is FragCalendarioPage) {
                val idEmprego = it.provideEmpregoId()
                it.refreshContent(
                    content.empregoInfo.first { it.idEmprego == idEmprego }
                )
            }
        }
    }

    fun updateContentByPosition(adapterPosition: Int, horaInfoDto: HoraInfoDto) {
        val page = getContentPage()
        page.updateItemByPosition(adapterPosition, horaInfoDto)
        //getContentPage().updateItemByPosition(adapterPosition, horaInfoDto)
    }

    private fun getContentPage() = (childFragmentManager.fragments[getCurrentItemPosition()] as FragCalendarioPage)
}