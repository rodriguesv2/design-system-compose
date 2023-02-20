package br.com.dimensa.mask.components.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    errorMessage: String? = null,
    maxLines: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    singleLine: Boolean = false,


) {
    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = { text ->
                onValueChange(text.take(maxLength))
            },
            modifier = modifier,
            label = {
                Text(text = label)
            },
            enabled = isEnabled,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
            isError = !errorMessage.isNullOrBlank(),
            maxLines = maxLines,
            readOnly = readOnly,
            singleLine = singleLine,


        )
        errorMessage?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = it,
                modifier = Modifier.padding(start = 16.5.dp),
                fontSize = 12.sp,
                color = Color.Red
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showBackground = true)
@Composable
fun BaseTextFieldPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        BaseTextField(
            value = "Beltrano",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = "Nome",
            isEnabled = false
        )
    }
}