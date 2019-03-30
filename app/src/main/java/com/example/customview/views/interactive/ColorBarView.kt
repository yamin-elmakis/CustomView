package com.example.customview.views.interactive

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import lib.yamin.easylog.EasyLog

interface ColorChangeListener {
    fun onColorChanged(color: Int)
}

class ColorBarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes) {

    private val colorSeeds: IntArray = intArrayOf(Color.parseColor("#000000"), Color.parseColor("#FF5252"), Color.parseColor("#FFEB3B"), Color.parseColor("#00C853"), Color.parseColor("#00B0FF"), Color.parseColor("#D500F9"), Color.parseColor("#8D6E63"))
    private lateinit var colorGradient: LinearGradient
    private var lastSelectedColor :Int = Color.TRANSPARENT
    private var rectf: RectF = RectF()
    private var barCornerRadius: Float = 5 * Resources.getSystem().displayMetrics.density
    private var rectPaint: Paint = Paint().apply {
        isAntiAlias = true
    }
    var colorChangeListener: ColorChangeListener? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = if (suggestedMinimumHeight > 0)
            suggestedMinimumHeight
        else
            (25 * Resources.getSystem().displayMetrics.density).toInt()
        val width = if (suggestedMinimumWidth > 0)
            suggestedMinimumWidth
        else
            (300 * Resources.getSystem().displayMetrics.density).toInt()
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val left: Float = paddingStart.toFloat()
        val right: Float = width.toFloat() - paddingEnd
        val bottom: Float = h - paddingBottom.toFloat()
        val top: Float = paddingTop.toFloat()

        colorGradient = LinearGradient(left, 0f, right - left, 0f, colorSeeds, null, Shader.TileMode.CLAMP)
        rectPaint.shader = colorGradient

        rectf.set(left, top, right, bottom)
        barCornerRadius = (h - paddingBottom - paddingTop) / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundRect(rectf, barCornerRadius, barCornerRadius, rectPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                EasyLog.e("DOWN")
//                thumbIcon.center.x = event.x
//                colorChangeListener?.onColorChangeListener(getColor())
//                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                EasyLog.e("MOVE")
                parent.requestDisallowInterceptTouchEvent(true)
//                thumbIcon.center.x = event.x
//                invalidate()
//                colorChangeListener?.onColorChangeListener(getColor())
            }
            MotionEvent.ACTION_UP -> {
                lastSelectedColor = getColor(event.x)
                EasyLog.e("UP")
                colorChangeListener?.onColorChanged(lastSelectedColor)
//                invalidate()
            }
        }
        return true
    }

    private fun getColor(position: Float): Int {
        if (position < paddingStart || position > (paddingStart + rectf.width())) return lastSelectedColor
//        val value = (position - paddingStart) / (canvasWidth - (paddingStart + paddingEnd))
        val value = (position - paddingStart) / (rectf.width())
        EasyLog.e("value: $value")
        when {
            value <= 0.0 -> return colorSeeds[0]
            value >= 1 -> return colorSeeds[colorSeeds.size - 1]
            else -> {
                EasyLog.e("colorSeeds.size : ${colorSeeds.size}")
                var colorPosition = value * (colorSeeds.size - 1)
                val i = colorPosition.toInt()
                colorPosition -= i
                val c0 = colorSeeds[i]
                val c1 = colorSeeds[i + 1]

                val red = mix(Color.red(c0), Color.red(c1), colorPosition)
                val green = mix(Color.green(c0), Color.green(c1), colorPosition)
                val blue = mix(Color.blue(c0), Color.blue(c1), colorPosition)
                return Color.rgb(red, green, blue)
            }
        }
    }

    fun mix(start: Int, end: Int, position: Float): Int {
        return start + Math.round(position * (end - start))
    }

}