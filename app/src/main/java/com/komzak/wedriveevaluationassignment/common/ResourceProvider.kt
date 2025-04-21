package com.komzak.wedriveevaluationassignment.common

import android.content.Context
import androidx.annotation.StringRes
import org.koin.core.annotation.Single

@Single
class ResourceProvider(private val context: Context) {
    fun getString(@StringRes stringId: Int): String = context.getString(stringId)
    fun getString(@StringRes stringId: Int, vararg formatParams: Any): String =
        context.getString(stringId, *formatParams)
}