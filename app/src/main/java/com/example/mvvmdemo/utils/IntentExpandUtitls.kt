package com.example.mvvmdemo.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * @description
 * @author Raymond
 * @date 2023/5/25
 *
 */

inline fun <reified T : Activity> Activity.startActivity() {
    Intent(this, T::class.java).run {
        startActivity(this)
    }
}

inline fun <reified T : Activity> Fragment.startActivity() {
    Intent(requireContext(), T::class.java).run {
        startActivity(this)
    }
}