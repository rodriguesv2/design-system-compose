package br.com.dimensa.mask

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.dimensa.mask.ui.theme.MaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaskTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var text by remember { mutableStateOf("") }

        CustomTextField(
            text = text,
            onValueChange = {
                text = it.take(11)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text, fontSize = 24.sp)
    }
}

@Composable
fun CustomTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(text = "Campo de texto")
        },
        visualTransformation = MaskTransformation(mask = "###.###.###-##"),
        modifier = modifier,
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    MaskTheme {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            CustomTextField("", {})
        }
    }
}

class MaskTransformation(
    private val mask: String,
    private val wildCardChar: Char = '#',
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val maskTrimmedLength = mask.replace(
            regex = "[^${wildCardChar}]".toRegex(),
            replacement = ""
        ).length

        val originalText = text.text.take(maskTrimmedLength)
        var maskedText = ""
        var specialCharCount = 0

        if (originalText.isNotEmpty())
            kotlin.run {
                mask.forEachIndexed() { index, maskChar ->
                    try {
                        if (maskChar != wildCardChar) {
                            specialCharCount += 1
                            maskedText += maskChar
                            return@forEachIndexed
                        }

                        maskedText += originalText[index - specialCharCount]
                    } catch (ex: StringIndexOutOfBoundsException) {
                        return@run
                    }
                }
            }

        val offsetTranslator = object : OffsetMapping {
            private fun getSpecialCharPositions(): List<Int> {
                val specialCharPositions = mutableListOf<Int>()
                mask.forEachIndexed { index, char ->
                    if (char != wildCardChar) specialCharPositions.add(index)
                }

                return specialCharPositions
            }

            override fun originalToTransformed(offset: Int): Int {
                if (
                    originalText.isEmpty() ||
                    getSpecialCharPositions().isEmpty()
                ) return offset

                getSpecialCharPositions().forEachIndexed { index, position ->
                    val transformedOffset = offset + index
                    if (transformedOffset < position) {
                        return transformedOffset
                    }
                }

                val transformedOffset = offset + getSpecialCharPositions().size

                return if (transformedOffset <= mask.length) transformedOffset
                else mask.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (
                    originalText.isEmpty() ||
                    getSpecialCharPositions().isEmpty()
                ) return offset

                getSpecialCharPositions().forEachIndexed { index, position ->
                    val originalOffset = offset - index
                    if (originalOffset < position) {
                        return originalOffset
                    }
                }

                val originalOffset = offset - getSpecialCharPositions().size

                return if (originalOffset <= originalText.length) originalOffset
                else originalText.length
            }
        }

        return TransformedText(
            text = AnnotatedString(text = maskedText),
            offsetMapping = offsetTranslator
        )
    }
}

