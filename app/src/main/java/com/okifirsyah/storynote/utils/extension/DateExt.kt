package com.okifirsyah.storynote.utils.extension

import com.okifirsyah.data.local.preference.StoragePreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val pref: StoragePreference by inject(StoragePreference::class.java)

fun Date.formatDate(): String {
    val localeTag = runBlocking {
        pref.getLocaleSetting().first()
    }

    val locale = localeTag.let {
        if (it.isNullOrEmpty()) {
            Locale.getDefault()
        } else {
            Locale.forLanguageTag(it)
        }
    }

    val formatter = SimpleDateFormat("EEEE, dd MMM yyyy", locale)

    return formatter.format(this)
}