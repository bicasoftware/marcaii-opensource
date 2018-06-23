package act_presentation

import act_presentation.core.MVPActPresentation
import act_presentation.core.PresenterActPresent
import act_presentation.fragments.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatImageView
import android.widget.ImageView
import br.sha.commommodule.BasePagedActivity
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_act_presentation.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import marcaii.ActSplash
import org.jetbrains.anko.startActivity
import utils.*
import utils.DialogUtils.showNumberDialog
import java.math.BigDecimal
import java.math.BigInteger

class ActPresentation: BasePagedActivity(
    R.layout.lay_act_presentation,
    R.string.str_app_descricao,
    R.string.str_apresentacao,
    R.id.tbl_presentation,
    R.id.vp_presentation
),
    MVPActPresentation.ViewActPresentImpl,
    PresentationContract,
    NumberPickerDialogFragment.NumberPickerDialogHandlerV2 {

    private val presenter: PresenterActPresent by lazy {
        PresenterActPresent(this)
    }

    private val pages: ArrayList<Pair<Fragment, String>> by lazy {
        val list: ArrayList<Pair<Fragment, String>> = arrayListOf(
            Pair(FragPresentStart.newInstance(), ""),
            Pair(FragPresentSalario.newInstance(presenter.provideSalario()), ""),
            Pair(FragPresentCargaHoraria.newInstance(), ""),
            Pair(FragPresentPorcentagem.newInstance(presenter.providePorcNormal(), presenter.providePorcCompleta()), ""),
            Pair(FragPresentPorcentagemDiferencial.newInstance(presenter.providePorcDifer()), ""),
            Pair(FragPresentEnd.newInstance(), "")
        )

        list
    }

    companion object {
        private const val REQ_SALARIO = 7878
        private const val REQ_PORC_NORMAL = 7880
        private const val REQ_PORC_COMPLETA = 7890
    }

    override fun afterActivityCreated(b: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        launch(UI) {
            addTabs(pages)
        }

        bt_prev.setOnClickListener {
            if(getCurrentItemPosition() > 0) {
                provideTabAdapter().setCurrentItemPosition(provideTabAdapter().getCurrentItemPosition() - 1)
            }
        }

        bt_next.setOnClickListener {
            if(getCurrentItemPosition() < pages.size) {
                provideTabAdapter().setCurrentItemPosition(provideTabAdapter().getCurrentItemPosition() + 1)
            }
        }

        bt_skip.setOnClickListener {
            if(provideTabAdapter().getCurrentItemPosition() == pages.size - 1) {
                doFinalize()
            } else {
                provideTabAdapter().setCurrentItemPosition(pages.size)
            }
        }

        prepareIndicators()

        provideViewPager().addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {
                setIndicator(position)
                if(position == pages.size - 1) {
                    bt_skip.putText(this@ActPresentation.getString(R.string.str_finalizar))
                } else {
                    bt_skip.putText(this@ActPresentation.getString(R.string.str_pular_intro))
                }
            }
        })
    }

    override fun onDialogNumberSet(reference: Int, number: BigInteger?, decimal: Double, isNegative: Boolean, fullNumber: BigDecimal?) {
        when(reference) {
            REQ_PORC_NORMAL -> presenter.setPorcNormal(number)
            REQ_PORC_COMPLETA -> presenter.setPorcCompleta(number)
            REQ_SALARIO -> presenter.setValorSalario(fullNumber)

        /** de 0 a 6 pq é referente ao dia da semana, que é equivalente index do adapter**/
            in 0..6 -> presenter.updatePorcentDif(pos = reference, porcent = number)
        }
    }

    override fun showSalarioDialog() {
        DialogUtils.showDialogSalario(supportFragmentManager, minVal = 0.0, currVal = presenter.provideSalario(), ref = REQ_SALARIO)
    }

    override fun onCargaHorariaChanged(pos: Int) {
        presenter.setCargaHoraria(pos)
    }

    override fun showDialogGetPorcNormal() {
        showNumberDialog(supportFragmentManager, "porcento", presenter.providePorcNormal(), maxValue = 999, ref = REQ_PORC_NORMAL)
    }

    override fun showDialogGetPorcCompleta() {
        showNumberDialog(supportFragmentManager, "porcento", presenter.providePorcCompleta(), maxValue = 999, ref = REQ_PORC_COMPLETA)
    }

    override fun showDialogGetPorcDifer(pos: Int, porc: Int) {
        showNumberDialog(supportFragmentManager, "porcento", porc, maxValue = 999, ref = pos)
    }

    override fun showOptionDialogHorasDiff(pos: Int) {
        yesNoDialog(R.string.str_remove_pdif) {
            presenter.resetPorcentDifByPos(pos)
        }
    }

    override fun updateSalario(salario: Double) {
        supportFragmentManager.findFragment<FragPresentSalario>()?.updateSalario(salario)
    }

    override fun updatePorcNormal(porc: Int) {
        supportFragmentManager.findFragment<FragPresentPorcentagem>()?.updatePorcNormal(porc)
    }

    override fun updatePorcCompleta(porc: Int) {
        supportFragmentManager.findFragment<FragPresentPorcentagem>()?.updatePorcCompleta(porc)
    }

    override fun updatePorcDifer(pos: Int, porc: Int, valor: Double) {
        supportFragmentManager.findFragment<FragPresentPorcentagemDiferencial>()?.updatePorcDifer(pos, porc, valor)
    }

    override fun clearPorcDifer(pos: Int) {
        supportFragmentManager.findFragment<FragPresentPorcentagemDiferencial>()?.cleanPorcDifer(pos)
    }

    private fun prepareIndicators() {

        /** Adiciona no LinearLayout(ll_indicator) imageViews */
        launch(UI) {
            for(i in 0 until pages.size) {

                val img = AppCompatImageView(this@ActPresentation)
                img.setImageResource(R.drawable.ic_indicator)

                when(i) {
                    0 -> img.setTint(this@ActPresentation, R.color.accent)
                    else -> img.setTint(this@ActPresentation, R.color.divider)
                }
                img.id = i
                ll_indicator.addView(img)
            }
        }
    }

    private fun setIndicator(pos: Int) {
        /**
         * Varre o ll_indicator, pinta o imageView com a posição == pos com a cor primária, e repinta o resto com cinza
         * */
        launch(UI) {
            for(i in 0..ll_indicator.childCount) {
                val img = ll_indicator.getChildAt(i)?.findViewById<ImageView>(i)
                if(i == pos) {
                    img?.setTint(this@ActPresentation, R.color.accent)
                } else {
                    img?.setTint(this@ActPresentation, R.color.divider)
                }
            }
        }
    }

    private fun doFinalize() {
        presenter.commitEmprego()
        startActivity<ActSplash>()
        this@ActPresentation.finish()
    }
}