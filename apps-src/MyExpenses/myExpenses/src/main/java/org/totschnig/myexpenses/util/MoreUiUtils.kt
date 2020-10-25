package org.totschnig.myexpenses.util

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.ColorUtils.calculateContrast
import com.google.android.material.chip.ChipGroup
import org.totschnig.myexpenses.R
import org.totschnig.myexpenses.preference.PrefHandler
import org.totschnig.myexpenses.preference.PrefKey
import org.totschnig.myexpenses.ui.filter.ScrollingChip

fun <T> ChipGroup.addChipsBulk(chips: Iterable<T>, closeFunction: ((T) -> Unit)?) {
    removeAllViews()
    for (chip in chips) {
        addView(ScrollingChip(context).also { scrollingChip ->
            scrollingChip.text = chip.toString()
            closeFunction?.let {
                scrollingChip.isCloseIconVisible = true
                scrollingChip.setOnCloseIconClickListener {
                    removeView(scrollingChip)
                    it(chip)
                }
            }
        })
    }
}

fun setNightMode(prefHandler: PrefHandler, context: Context) {
    AppCompatDelegate.setDefaultNightMode(
            when(prefHandler.getString(PrefKey.UI_THEME_KEY, context.getString(R.string.pref_ui_theme_default))) {
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            })
}

fun getBestForeground(color: Int) = arrayOf(Color.BLACK, Color.WHITE).maxByOrNull { calculateContrast(color, it) }