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
            offsetMapping = MaskOffsetTransformation(
                mask = mask,
                wildcardChar = wildcardChar,
                originalText = originalText,
            )
        )
    }
}

class MaskOffsetTransformation(
    private val mask: String,
    private val wildcardChar: Char,
    private val originalText: String,

    ): OffsetMapping {
    private fun getSpecialCharPositions(): List<Int> {
        val specialCharPositions = mutableListOf<Int>()
        mask.forEachIndexed { index, char ->
            if (char != wildcardChar) specialCharPositions.add(index)
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