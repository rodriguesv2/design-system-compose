package br.com.dimensa.mask.components.utils.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.com.dimensa.mask.components.utils.extensions.mask
import br.com.dimensa.mask.components.utils.functions.max
import br.com.dimensa.mask.components.utils.functions.min

class MaskDynamicTransformation(
    private val mask1: String,
    private val mask2: String,
    private val wildcardChar: Char = '#',
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        val masked = if (originalText.isNotEmpty()) originalText.mask(mask = getMaskByOriginalText(text.text))
        else ""
        return TransformedText(
            text = AnnotatedString(text = masked),
            offsetMapping = MaskOffsetMapping(
                mask = getMaskByOriginalText(text.text),
                wildcardChar = wildcardChar,
                originalText = originalText.take(getTrimmedMaskLength(getMaskByOriginalText(text.text))),
            )
        )
    }

    private fun getTrimmedMaskLength(mask: String): Int {
        return mask.replace(
            regex = "[^${wildcardChar}]".toRegex(),
            replacement = ""
        ).length
    }

    private fun getMaskByOriginalText(text: String): String {
        return if (text.length <= getTrimmedMaskLength(min(mask1, mask2))) min(mask1, mask2)
        else max(mask1, mask2)
    }
}