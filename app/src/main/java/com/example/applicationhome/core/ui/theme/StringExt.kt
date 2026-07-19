package com.example.applicationhome.core.ui.theme

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
فانكشن بتفلتر النص بتخلي جزء حروف تخينة و جزء حروف رفيعة
الString لو بادئ بحروف في باراميتر searchText الحروف دي هتبقى رفيعة و باقي النص هيبقى زي ما هو
 */
fun String.formatSingleWordInSearch(searchText : String): AnnotatedString{
    return buildAnnotatedString {
        if(searchText.isNotEmpty() && this@formatSingleWordInSearch.startsWith(searchText, ignoreCase = true)){
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)){
                append(this@formatSingleWordInSearch.substring(0, searchText.length))
            }
            append(this@formatSingleWordInSearch.substring(searchText.length))
        }else{
            append(this@formatSingleWordInSearch)
        }
    }
}