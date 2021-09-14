package com.example.calendarandtasks.util

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import com.example.calendarandtasks.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class IconButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.buttonIconOnly
) : Chip(context, attrs, defStyleAttr)
