package com.isoftinc.film.util

import android.os.Build
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import androidx.annotation.RequiresApi

class TransitionUtil {

    private val duration: Long = 200

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun slide(gravity: Int): Transition {
        val slideTransition = Slide(gravity)
        slideTransition.duration = duration
        return slideTransition
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun fade(mode: Int): Transition {
        val fadeTransition = Fade(mode)
        fadeTransition.duration = duration
        return fadeTransition
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun explode(): Transition {
        val enterTransition = Explode()
        enterTransition.duration = 500
        return enterTransition
    }

}
