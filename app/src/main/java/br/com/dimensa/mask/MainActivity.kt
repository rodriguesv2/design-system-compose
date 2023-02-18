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
import br.com.dimensa.mask.components.utils.transformations.MaskDynamicTransformation
import br.com.dimensa.mask.components.utils.transformations.MaskTransformation
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
                text = it
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
        visualTransformation = MaskDynamicTransformation(
            mask1 = "###.###.###-##",
            mask2 = "##.###.###/####-##"
        ),
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

