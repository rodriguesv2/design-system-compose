package br.com.dimensa.mask.components.utils.transformations

import androidx.compose.ui.text.input.OffsetMapping

class MaskOffsetMapping(
    private val mask: String,
    private val wildcardChar: Char,
    private val originalText: String,
) : OffsetMapping {
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