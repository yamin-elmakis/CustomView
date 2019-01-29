package com.example.customview.views.interactive

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class ColorBarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes) {

    private val colorSeeds : IntArray = intArrayOf(Color.parseColor("#000000"), Color.parseColor("#FF5252"), Color.parseColor("#FFEB3B"), Color.parseColor("#00C853"), Color.parseColor("#00B0FF"), Color.parseColor("#D500F9"), Color.parseColor("#8D6E63"))
    private lateinit var colorGradient: LinearGradient
    private var rectf: RectF = RectF()
    private var barCornerRadius: Float = 5 * Resources.getSystem().displayMetrics.density
    private var rectPaint: Paint = Paint().apply {
        isAntiAlias = true
    }

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
        Log.e("YAMIN_LOG", "ColorBarView.onSizeChanged: h: $h")
        Log.e("YAMIN_LOG", "ColorBarView.onSizeChanged: paddingBottom: $paddingBottom")
        Log.e("YAMIN_LOG", "ColorBarView.onSizeChanged: paddingTop: $paddingTop")
        Log.e("YAMIN_LOG", "ColorBarView.onSizeChanged: rect: $rectf")
        barCornerRadius = (h - paddingBottom - paddingTop) / 2f
        Log.e("YAMIN_LOG", "ColorBarView.onSizeChanged: radius: $barCornerRadius")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("YAMIN_LOG", "ColorBarView.onDraw")
        canvas?.drawRoundRect(rectf, barCornerRadius, barCornerRadius, rectPaint)
    }

}