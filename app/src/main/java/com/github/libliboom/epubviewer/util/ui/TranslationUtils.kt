package com.github.libliboom.epubviewer.util.ui

import android.view.View
import kotlin.math.abs

object TranslationUtils {

    const val EFFECT_NONE = 1
    const val EFFECT_CUBE_OUT_DEPTH = 2
    const val EFFECT_ZOOM_OUT_PAGE = 3
    const val EFFECT_GEO = 4
    const val EFFECT_FADE_OUT = 5

    fun effectNone(): (page: View, position: Float) -> Unit {
        return { _, _ -> null }
    }

    fun effectCubeOutDepth(): (page: View, position: Float) -> Unit {
        return { page, position ->
            when {
                position < -1 -> {
                    page.alpha = 0f
                }
                position <= 0 -> {
                    page.apply {
                        alpha = 1f
                        pivotX = page.width.toFloat()
                        rotationY = -90 * abs(position)
                    }
                }
                position <= 1 -> {
                    page.apply {
                        alpha = 1f
                        pivotX = 0f
                        rotationY = 90 * abs(position)
                    }
                }
                else -> {
                    page.alpha = 0f
                }
            }

            if (abs(position) <= 0.5) {
                page.scaleY = 0.4f.coerceAtLeast(1 - abs(position))
            } else if (abs(position) <= 1) {
                page.scaleY = 0.4f.coerceAtLeast(1 - abs(position))
            }
        }
    }

    fun effectZoomOutPageEffect(): (page: View, position: Float) -> Unit {
        return { page, position ->
            page.translationX = -position * page.width

            if (abs(position) < 0.5) {
                page.apply {
                    visibility = View.VISIBLE
                    scaleX = 1 - abs(position)
                    scaleY = 1 - abs(position)
                }
            } else if (abs(position) > 0.5) {
                page.visibility = View.GONE
            }

            when {
                position < -1 -> {
                    page.alpha = 0f
                }
                position <= 0 -> {
                    page.alpha = 1f
                    page.rotation = 360 * (1 - abs(position))
                }
                position <= 1 -> {
                    page.alpha = 1f
                    page.rotation = -360 * (1 - abs(position))
                }
                else -> {
                    page.alpha = 0f
                }
            }
        }
    }

    fun effectGeo(): (page: View, position: Float) -> Unit {
        return { page, position ->
            page.translationX = -position * page.width

            when {
                position < -1 -> {
                    page.alpha = 0f
                }
                position <= 0 -> {
                    page.alpha = 1f
                    page.pivotX = 0f
                    page.rotationY = 90 * abs(position)
                }
                position <= 1 -> {
                    page.alpha = 1f
                    page.pivotX = page.width.toFloat()
                    page.rotationY = -90 * abs(position)
                }
                else -> {
                    page.alpha = 0f
                }
            }
        }
    }

    fun effectFadeOut(): (page: View, position: Float) -> Unit {
        return { page, position ->
            page.apply {
                translationX = -position * page.width
                alpha = 1 - abs(position)
            }
        }
    }
}
