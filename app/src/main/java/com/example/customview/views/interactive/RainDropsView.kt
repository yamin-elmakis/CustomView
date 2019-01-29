package com.example.customview.views.interactive

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.customview.R
import com.example.customview.utils.nextColor
import kotlin.random.Random

class RainDropsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 2f
    }

    private val random : Random= Random(System.currentTimeMillis())
    private var rainDropList: List<RainDrop> = listOf()
    private val maxRadius by lazy {
        context.resources.getDimension(R.dimen.rain_drop_radius)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec),
                resolveSize(desiredHeight, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rainDropList.forEach { it.draw(canvas, paint) }
        rainDropList = rainDropList.filter { it.isValid() }
        if (rainDropList.isNotEmpty() && isAttachedToWindow) invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                rainDropList += RainDrop(event.getX(pointerIndex), event.getY(pointerIndex), maxRadius, random.nextColor())
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                rainDropList += RainDrop(event.getX(pointerIndex), event.getY(pointerIndex), maxRadius, random.nextColor())
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}

data class RainDrop(private val centerX: Float, private val centerY: Float, private val maxRadius: Float, private val dropColor: Int) : Parcelable {

    var currentRadius = 1f

    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt()) {
        currentRadius = parcel.readFloat()
    }

fun draw(canvas: Canvas, paint: Paint) {
        paint.apply {
            this.color = dropColor
            alpha = ((maxRadius - currentRadius) / maxRadius * MAX_ALPHA).toInt()
        }
        canvas.drawCircle(centerX, centerY, currentRadius++, paint)
    }

    fun isValid() = currentRadius < maxRadius

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(centerX)
        parcel.writeFloat(centerY)
        parcel.writeFloat(maxRadius)
        parcel.writeInt(dropColor)
        parcel.writeFloat(currentRadius)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RainDrop> {
        private const val MAX_ALPHA = 255

        override fun createFromParcel(parcel: Parcel): RainDrop {
            return RainDrop(parcel)
        }

        override fun newArray(size: Int): Array<RainDrop?> {
            return arrayOfNulls(size)
        }
    }
}
