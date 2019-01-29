package com.example.customview.views.interactive

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import lib.yamin.easylog.EasyLog


class ColorPickerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes) {

    private val colorSeeds : IntArray = intArrayOf(Color.parseColor("#000000"), Color.parseColor("#FF5252"), Color.parseColor("#FFEB3B"), Color.parseColor("#00C853"), Color.parseColor("#00B0FF"), Color.parseColor("#D500F9"), Color.parseColor("#8D6E63"))
    private val pickerBar: PickerBar = PickerBar(colorSeeds)
    private val thumbIcon: ThumbIcon = ThumbIcon(PointF(0f, 0f), colorSeeds)

    init {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //color bar position
        val barHeight = (h - paddingTop) / 4f
        val barLeft: Float = paddingStart.toFloat()
        val barRight: Float = width.toFloat() - paddingEnd
        val barBottom: Float = h - paddingBottom.toFloat()
        val barTop: Float = barBottom - barHeight

        val thumbHeight = 2.5f * (h - paddingTop) / 4f
        val thumbBottom: Float = barTop
        val thumbTop: Float = thumbBottom - thumbHeight

        pickerBar.setSize(barLeft, barTop, barRight, barBottom)
        thumbIcon.setSize(barLeft, thumbTop, barRight, thumbBottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minHeight = if (suggestedMinimumHeight > 0)
            suggestedMinimumHeight
        else
            (80 * Resources.getSystem().displayMetrics.density).toInt()
        val minWidth = if (suggestedMinimumWidth > 0)
            suggestedMinimumWidth
        else
            (150 * Resources.getSystem().displayMetrics.density).toInt()
        val desiredWidth = minWidth + paddingLeft + paddingRight
        val desiredHeight = minHeight + paddingTop + paddingBottom
        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec), resolveSize(desiredHeight, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        pickerBar.draw(canvas)
        thumbIcon.draw(canvas)
//        if (thumbX < rectf.left) {
//            thumbX = rectf.left
//        } else if (thumbX > rectf.right) {
//            thumbX = rectf.right
//        }
//        val color = pickColor(thumbX, width)
//        thumbPaint.color = color

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                EasyLog.e("DOWN")
                thumbIcon.center.x = event.x
//                colorChangeListener?.onColorChangeListener(getColor())
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                EasyLog.e("MOVE")
                parent.requestDisallowInterceptTouchEvent(true)
                thumbIcon.center.x = event.x
                invalidate()
//                colorChangeListener?.onColorChangeListener(getColor())
            }
            MotionEvent.ACTION_UP -> {
                invalidate()
            }
        }
        return true
    }
}

private data class ThumbIcon(val center: PointF, val colorSeeds: IntArray) {
//    data class
    var thumbState:ThumbState = ThumbState.StateClose(1)
    private var canvasWidth: Float = 0f
    private var thumbRadius: Float = 16f
    private var thumbBorderRadius: Float = 16f
    private var thumbBorderPaint: Paint = Paint().apply {
        color = Color.YELLOW
        alpha = 150
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 2 * Resources.getSystem().displayMetrics.density
    }
    private var thumbPaint: Paint = Paint().apply {
        isAntiAlias = true
    }

    fun setSize(left: Float, top: Float, right: Float, bottom: Float) {
        thumbBorderRadius = (bottom - top) / 2
        thumbRadius = thumbBorderRadius - thumbBorderPaint.strokeWidth / 2
        center.y = (bottom - top) / 2 + top
        canvasWidth = right - left
    }

    fun draw(canvas: Canvas?) {
        EasyLog.e(center)
        canvas?.drawCircle(center.x, center.y, thumbBorderRadius, thumbBorderPaint)
        thumbPaint.let {
            it.color = getColor(center.x)
        }
        canvas?.drawCircle(center.x, center.y, thumbRadius, thumbPaint)
    }

    private fun getColor(position: Float): Int {

//        val value = (position - paddingStart) / (canvasWidth - (paddingStart + paddingEnd))
        val value = (position) / (canvasWidth)
        when {
            value <= 0.0 -> return colorSeeds[0]
            value >= 1 -> return colorSeeds[colorSeeds.size - 1]
            else -> {
                var colorPosition = value * (colorSeeds.size - 1)
                val i = colorPosition.toInt()
                colorPosition -= i
                val c0 = colorSeeds[i]
                val c1 = colorSeeds[i + 1]

                val red = mix(Color.red(c0), Color.red(c1), colorPosition)
                val green = mix(Color.green(c0), Color.green(c1), colorPosition)
                val blue = mix(Color.blue(c0), Color.blue(c1), colorPosition)
                return Color.rgb( red, green, blue)
            }
        }

    }

    fun mix(start: Int, end: Int, position: Float): Int {
        return start + Math.round(position * (end - start))
    }

    sealed class ThumbState {

        data class StateClose(val size :Int) : ThumbState()
        data class StateOpen(val size :Int) : ThumbState()
    }

}

private data class PickerBar(val colorSeeds: IntArray) {
    private lateinit var colorGradient: LinearGradient
    private var rectf: RectF = RectF()
    private var barCornerRadius: Float = 5 * Resources.getSystem().displayMetrics.density

    private var rectPaint: Paint = Paint().apply {
        isAntiAlias = true
    }

    fun setSize(left: Float, top: Float, right: Float, bottom: Float) {
        colorGradient = LinearGradient(left, 0f, right - left, 0f, colorSeeds, null, Shader.TileMode.CLAMP)
        rectPaint.shader = colorGradient

        rectf.set(left, top, right, bottom)
        barCornerRadius = top / 2f
    }

    fun draw(canvas: Canvas?) {
        EasyLog.e(rectf)
        canvas?.drawRoundRect(rectf, barCornerRadius, barCornerRadius, rectPaint)
    }

}