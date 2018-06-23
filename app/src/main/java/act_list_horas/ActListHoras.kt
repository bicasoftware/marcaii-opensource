package act_list_horas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import asCurrency
import br.sha.commommodule.BaseActivityBackButton
import br.sha.commommodule.BaseRecyclerViewDivider
import exemple.sha.horas.BuildConfig
import exemple.sha.horas.R
import kotlinx.android.synthetic.main.lay_bts_list_horas.*
import kotlinx.android.synthetic.main.lay_bts_list_horas_totais_item.view.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import models.RelatorioInfoDto
import utils.*
import java.io.File

class ActListHoras: BaseActivityBackButton(R.layout.lay_bts_list_horas, R.string.str_relacao) {

    private val meses: Array<String> by lazy {
        resources.getStringArray(R.array.arr_meses_ext)
    }

    private lateinit var content: RelatorioInfoDto
    private var mes = 0
    private var ano = 2010
    private var idEmprego = ""

    companion object {
        private const val REQUEST_WRITE = 123
        const val CONST_MES = "CONSTMES"
        const val CONST_ANO = "CONSTANO"
        const val CONST_IDEMPREGO = "CONST_ID_EMPREGO"
    }

    override fun doAfterCreated(b: Bundle?) {
        if(b == null) throw Exception("bundle can't be null")

        launch(UI) {
            mes = b.getInt(CONST_MES)
            ano = b.getInt(CONST_ANO)
            idEmprego = b.getString(CONST_IDEMPREGO)
            content = async(CommonPool) { RelatorioBuilder.buildRelatorio(idEmprego, ano, mes) }.await()
            tv_periodo_horas.text =
                getString(R.string.pholder_periodo_mes, dateToString(content.inicio, DateFormats.BR), dateToString(content.termino, DateFormats.BR))
            supportActionBar?.title = getString(R.string.pholder_horas_mes, meses[mes])

            rv_list_horas.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(BaseRecyclerViewDivider(this@ActListHoras))
                adapter = RecAdapterListHoras(context, content.horasInRange)
            }

            lay_totais_normal.apply {
                tv_list_totais_tipo.text = getString(R.string.str_hora_normal)
                tv_list_totais_tipo.setTextColor(ContextCompat.getColor(this@ActListHoras, R.color.horanormal))
                tv_list_totais_quantidade.text = getString(R.string.pholder_horas, content.totalNormal.quantidade.minutesToHours())
                tv_list_totais_valor.text = content.totalNormal.total.asCurrency()
            }
            lay_totais_completo.apply {
                tv_list_totais_tipo.text = getString(R.string.str_hora_completa)
                tv_list_totais_tipo.setTextColor(ContextCompat.getColor(this@ActListHoras, R.color.horaferiado))
                tv_list_totais_quantidade.text = getString(R.string.pholder_horas, content.totalCompleto.quantidade.minutesToHours())
                tv_list_totais_valor.text = content.totalCompleto.total.asCurrency()
            }
            lay_totais_difer.apply {
                tv_list_totais_tipo.text = getString(R.string.str_hora_diferencial)
                tv_list_totais_tipo.setTextColor(ContextCompat.getColor(this@ActListHoras, R.color.horadiff))
                tv_list_totais_quantidade.text = getString(R.string.pholder_horas, content.totalDifer.quantidade.minutesToHours())
                tv_list_totais_valor.text = content.totalDifer.total.asCurrency()
            }

            tv_totais_total.text = (content.totalNormal.total + content.totalCompleto.total + content.totalDifer.total).asCurrency()
        }

    }

    override fun doOnBackPressed() {
        this@ActListHoras.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_print, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        if(item?.itemId == R.id.menu_print) {
            if(hasWritePermission()) {
                showDialogFileType()
            } else {
                requestWritePermission()
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_WRITE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogFileType()
            }
        }
    }

    private fun hasWritePermission(): Boolean {
        return ContextCompat
            .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWritePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE)
    }

    private fun showDialogFileType() {
        selectorDialog(R.string.str_confirmar_gerar_arquivo, R.array.arr_opcoes_relatorios) {
            generateFile(it)
        }
    }

    private fun showDialogViewFile(dir: String, fileType: Int) {
        yesNoDialog(R.string.str_generated_file) {
            val intent = Intent()
            val fileExt = if(fileType == 0) "text/csv" else "text/plain"
            val uri = if(Build.VERSION.SDK_INT >= 24) {
                FileProvider
                    .getUriForFile(
                        this,
                        "${BuildConfig.APPLICATION_ID}.provider",
                        File(dir)
                    )
            } else {
                Uri.fromFile(File(dir))
            }

            intent.action = android.content.Intent.ACTION_VIEW
            intent.setDataAndType(uri, fileExt)
            startActivity(intent)
        }
    }

    private fun generateFile(selected: Int) {
        launch(UI) {
            val dialog = buildIndeterminateProgressDialog()
            dialog.show()

            val dir = async(CommonPool) {
                when(selected) {
                    0 -> RelatorioFileBuilder.genCSV(this@ActListHoras, content)
                    1 -> RelatorioFileBuilder.genTXT(this@ActListHoras, content)
                    else -> ""
                }
            }.await()

            dialog.dismiss()

            showDialogViewFile(dir, selected)
        }
    }
}