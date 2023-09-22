package br.com.generic.base.data.extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import br.com.generic.base.R
import br.com.generic.base.utils.Constants.Companion.BASE_URL

var serverURL = BASE_URL
var loginFailed = ""
var cookie = ""
var userExhibitionName = ""
var userConnectionCode = ""
var userCode = ""


fun createAlertDialog(context: Context, title: String, message: String, resultBoolean: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(R.string.ok_text) { _, _ -> resultBoolean(true) }
    builder.show()
}

fun createQuestionDialog(context: Context, title: String, message: String, resultBoolean: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("Sim") { _, _ -> resultBoolean(true) }
    builder.setNegativeButton("Não") { _, _ -> resultBoolean(false) }
    builder.show()
}

// Esconde o teclado ao fazer alguma ação
fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun countMatches(string: String, pattern: String): Int {
    var index = 0
    var count = 0

    while (true)
    {
        index = string.indexOf(pattern, index)
        index += if (index != -1)
        {
            count++
            pattern.length
        }
        else {
            return count
        }
    }
}

fun getUserQuery() : String {
    return "CODUSU = STP_GET_CODUSULOGADO"
}

fun loadUserLogo(server: String, userCode: String): String {
    return "${server}Usuario@FOTO@CODUSU=$userCode.dbimage"
}
