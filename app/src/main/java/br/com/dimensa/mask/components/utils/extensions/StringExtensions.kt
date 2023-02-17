package br.com.dimensa.mask.components.utils.extensions

fun String.unmask(): String {
    return this.replace(
        regex = "[^a-zA-Z\\d]".toRegex(),
        replacement = ""
    )
}

fun String.mask(
    mask: String,
    wildcard: Char = '#'
): String {
    val maskTrimmedLength = mask.replace(
        regex = "[^${wildcard}]".toRegex(),
        replacement = ""
    ).length

    val originalTextSliced = this.take(maskTrimmedLength)

    var maskedText = ""
    var specialCharCount = 0

    kotlin.run {
        mask.forEachIndexed() { index, maskChar ->
            try {
                if (maskChar != wildcard) {
                    specialCharCount += 1
                    maskedText += maskChar
                    return@forEachIndexed
                }

                maskedText += originalTextSliced[index - specialCharCount]
            } catch (ex: StringIndexOutOfBoundsException) {
                return@run
            }
        }
    }

    return maskedText
}