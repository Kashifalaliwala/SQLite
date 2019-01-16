package example.com.sqlitedemo.Utils

import android.content.Context
import android.text.TextUtils

fun toast(context: Context?, text: String?) {
    if (context != null && text != null) {
        val toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT)
        toast.show()
    }
}

fun isValidMail(mailString: String): Boolean {
    return !TextUtils.isEmpty(mailString) && android.util.Patterns.EMAIL_ADDRESS.matcher(mailString).matches()
}