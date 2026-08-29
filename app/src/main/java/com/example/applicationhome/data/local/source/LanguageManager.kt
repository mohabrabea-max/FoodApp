package com.example.applicationhome.data.local.source

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(){
    fun setAppLanguage(languageCode : String){
        val appLocales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocales)
    }

    fun getCurrentLanguage(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        return if(!locales.isEmpty){
            locales[0]?.language?: "en"
        }else{
            Locale.getDefault().language
        }
    }
}