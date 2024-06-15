package com.okifirsyah.customview.utils.enums

enum class PasswordType(val value: Int) {
    MAIN(0),
    CONFIRMATION(1);

    companion object {
        fun from(value: Int): PasswordType = entries.first { it.value == value }
    }
}