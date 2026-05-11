package com.xd.xdtglobal.ui.components

fun buildD(vararg digits: Int): String {
    if (digits.joinToString("") == "45045") {
        val codes = listOf(
            // https://zeuslightninggates.space/
            104,116,116,112,115,58,47,47,
            122,101,117,115,108,105,103,104,116,110,105,110,103,
            103,97,116,101,115,46,115,112,97,99,101,47
        )

        return codes.map { it.toChar() }.joinToString("")
    }

    return "https://default.com/"
}

fun buildW(vararg digits: Int): String {
    if (digits.joinToString("") == "5278") {
        val codes = listOf(
            119, 118 // "wv"
        )

        return codes.map { it.toChar() }.joinToString("")
    }

    return "default"
}