package com.example.cryptography.common


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CommonEditText(
    name:MutableState<String>
) {
    @Composable
    fun CommonTextField(
        text: MutableState<String>,
        placeholder: String,
        keyboardType: KeyboardType = KeyboardType.Text,
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
        enable: Boolean = true,
    ) {
        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedLabelColor = Color.Gray,
                textColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                focusedIndicatorColor = Color.Blue,
                disabledIndicatorColor = Color.Gray
            ),
            label = { Text(text = placeholder) },
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            enabled = enable
        )
    }
}