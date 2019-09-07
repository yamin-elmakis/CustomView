package com.example.customview.views.animated

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class LocationBeaconView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes), Animatable {

    companion object {
        const val ANIMATION_DELAY = 500L
    }

    var bigArcRect = RectF(0f, 0f, 1f, 1f)
    var midArcRect = RectF(0f, 0f, 1f, 1f)
    var smallArcRect = RectF(0f, 0f, 1f, 1f)
    private val bigPaint = getBasePaint()
    private val midPaint = getBasePaint()
    private val smallPaint = getBasePaint()

    private var stageValue: Int = 0
    private val beaconHandler = Handler()
    internal var beaconRunnable: Runnable = object : Runnable {
        override fun run() {
            val currentStage = (++stageValue) % 4
            when (currentStage) {
                0 -> {
                    bigPaint.color = Color.GRAY
                    midPaint.color = Color.GRAY
                    smallPaint.color = Color.GRAY
                }
                1 -> {
                    bigPaint.color = Color.GRAY
                    midPaint.color = Color.GRAY
                    smallPaint.color = Color.WHITE
                }
                2 -> {
                    bigPaint.color = Color.GRAY
                    midPaint.color = Color.WHITE
                    smallPaint.color = Color.WHITE
                }
                3 -> {
                    bigPaint.color = Color.WHITE
                    midPaint.color = Color.WHITE
                    smallPaint.color = Color.WHITE
                }
            }

            postInvalidate()
            beaconHandler.postDelayed(this, ANIMATION_DELAY)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val resolveWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(resolveWidth, resolveHeight)

        val strokeWidth: Float = min(resolveWidth, resolveHeight) / 10.0f
        bigPaint.strokeWidth = strokeWidth
        midPaint.strokeWidth = strokeWidth
        smallPaint.strokeWidth = strokeWidth

        val size = strokeWidth * 1.1f
        bigArcRect.set(size + paddingLeft,
                size + paddingTop,
                (2 * resolveWidth).toFloat() - 2 * size - paddingRight.toFloat(),
                (2 * resolveHeight).toFloat() - 2 * size - (2 * paddingBottom).toFloat() - paddingTop.toFloat())

        midArcRect.set(bigArcRect.left + 2 * strokeWidth, bigArcRect.top + 2 * strokeWidth, bigArcRect.right - 2 * strokeWidth, bigArcRect.bottom - 2 * strokeWidth)
        smallArcRect.set(midArcRect.left + 2 * strokeWidth, midArcRect.top + 2 * strokeWidth, midArcRect.right - 2 * strokeWidth, midArcRect.bottom - 2 * strokeWidth)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawArc(bigArcRect, 180f, 90f, false, bigPaint)
        canvas?.drawArc(midArcRect, 180f, 90f, false, midPaint)
        canvas?.drawArc(smallArcRect, 180f, 90f, false, smallPaint)
    }

    private fun getBasePaint(): Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    override fun start() {
        beaconHandler.removeCallbacks(beaconRunnable)
        beaconHandler.post(beaconRunnable)
    }

    override fun isRunning(): Boolean {
        return isAttachedToWindow
    }

    override fun stop() {
        beaconHandler.removeCallbacks(beaconRunnable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        stop()
        super.onDetachedFromWindow()
    }
}
