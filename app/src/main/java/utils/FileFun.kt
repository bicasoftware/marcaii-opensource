package utils

import android.os.Environment
import java.io.*

object FileFun {

    private const val SYSFOLDER = "Marcaii"

    fun generateFileOnSDAndReturnDir(texto: String, fileName: String): String {
        try {
            val folder = File(Environment.getExternalStorageDirectory().absolutePath, FileFun.SYSFOLDER)
            if(!folder.exists()) {
                folder.mkdirs()
            }

            val file = File(folder, fileName)
            val fileWriter = FileWriter(file)
            fileWriter.append(texto)
            fileWriter.flush()
            fileWriter.close()

        } catch(ex: IOException) {
            E(ex.localizedMessage ?: ex.message ?: ex.toString())
        }

        return Environment.getExternalStorageDirectory().toString() + "/" + FileFun.SYSFOLDER + "/" + fileName
    }
}