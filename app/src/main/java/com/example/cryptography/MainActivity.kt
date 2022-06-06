package com.example.cryptography

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptography.common.CommonEditText
import com.example.cryptography.ui.theme.CryptographyTheme
import com.kazakago.cryptore.CipherAlgorithm
import com.kazakago.cryptore.Cryptore

class MainActivity : ComponentActivity() {

    private enum class Alias(val value: String) {
        RSA("CIPHER_RSA"),
        AES("CIPHER_AES")
    }

    private val cryptoreRSA: Cryptore by lazy {
        val builder = Cryptore.Builder(alias = Alias.RSA.value, type = CipherAlgorithm.RSA)
        builder.context = this //Need Only RSA on below API Lv22.
//        builder.blockMode = BlockMode.ECB //If Needed.
//        builder.encryptionPadding = EncryptionPadding.RSA_PKCS1 //If Needed.
        builder.build()
    }
    private val cryptoreAES: Cryptore by lazy {
        val builder = Cryptore.Builder(alias = Alias.AES.value, type = CipherAlgorithm.AES)
//        builder.blockMode = BlockMode.CBC //If Needed.
//        builder.encryptionPadding = EncryptionPadding.PKCS7 //If Needed.
        builder.build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptographyTheme {
                val originalText = remember { mutableStateOf("")}
                val encrText = remember { mutableStateOf("")}
                val decrText = remember { mutableStateOf("")}
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn{

                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                horizontalArrangement = Arrangement.Center,
                            ){
                                Text(text = "Cryptography",color = Color.Black)
                            }
                        }

                        item {

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                            ) {

                                TextField(value = originalText.value, onValueChange = {
                                    originalText.value = it
                                },
                                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                                    placeholder = {Text("Enter text")}
                                )
                                Text(text = "Encrypted : ${encrText.value}", modifier = Modifier.padding(10.dp))
                                Text(text = "Decrypted : ${decrText.value}", modifier = Modifier.padding(10.dp))

                            }

                        }

                        item{
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp, horizontal = 20.dp)
                            ){
                                Button(onClick = {
                                    val encryptedStr = encryptRSA(originalText.value)
                                    encrText.value = encryptedStr

                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)) {
                                    Text(text = "EncryptRSAButton")
                                }

                                Button(onClick = {

                                    val encryptedStr = encryptAES(originalText.value)
                                    encrText.value = encryptedStr

                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)) {
                                    Text(text = "EncryptAESButton")
                                }

                                Button(onClick = {
                                    val decryptedStr = decryptRSA(encrText.value)
                                    decrText.value = decryptedStr
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)) {
                                    Text(text = "DecryptRSAButton")
                                }

                                Button(onClick = {
                                    val decryptedStr = decryptAES(encrText.value)
                                    decrText.value = decryptedStr
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)) {
                                    Text(text = "DecryptAESButton")
                                }
                            }
                        }


                    }
                }
            }
        }
    }

    private fun encryptRSA(plainStr: String): String {
        try {
            val plainByte = plainStr.toByteArray()
            val result = cryptoreRSA.encrypt(plainByte = plainByte)
            return Base64.encodeToString(result.bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun decryptRSA(encryptedStr: String): String {
        try {
            val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
            val result = cryptoreRSA.decrypt(encryptedByte = encryptedByte)
            return String(result.bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun encryptAES(plainStr: String): String {
        try {
            val plainByte = plainStr.toByteArray()
            val result = cryptoreAES.encrypt(plainByte = plainByte)
            cipherIV = result.cipherIV
            return Base64.encodeToString(result.bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun decryptAES(encryptedStr: String): String {
        try {
            val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
            val result = cryptoreAES.decrypt(encryptedByte = encryptedByte, cipherIV = cipherIV)
            return String(result.bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private var cipherIV: ByteArray?
        get() {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            preferences.getString("cipher_iv", null)?.let {
                return Base64.decode(it, Base64.DEFAULT)
            }
            return null
        }
        set(value) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.putString("cipher_iv", Base64.encodeToString(value, Base64.DEFAULT))
            editor.apply()
        }

}

