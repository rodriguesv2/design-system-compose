package br.com.dimensa.mask.components.utils.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.com.dimensa.mask.components.utils.extensions.mask

class MaskTransformation(
    private val mask: String,
    private val wildcardChar: Char = '#',
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val maskTrimmedLength = mask.replace(
            regex = "[^${wildcardChar}]".toRegex(),
            replacement = ""
        ).length

        val originalText = text.text.take(maskTrimmedLength)

        val masked = if (originalText.isNotEmpty()) originalText.mask(mask = mask)
        else ""

        return TransformedText(
            text = AnnotatedString(text = masked),
            offsetMapping = MaskOffsetMapping(
                mask = mask,
                wildcardChar = wildcardChar,
                originalText = originalText,
            )
        )
    }
}