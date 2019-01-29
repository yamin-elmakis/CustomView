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

class DrawingBoardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes) {

    private val colorFlipper: ColorFlipper = ColorFlipper(0f, 0f, 0f)
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        isAntiAlias = true
        strokeWidth = 11f
    }
    var drawingList: ArrayList<ArrayList<BoardPoint>> = arrayListOf()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec), resolveSize(desiredHeight, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var boardPoint: BoardPoint
        var prevPoint: BoardPoint
        drawingList.forEach { innerList ->
            innerList.forEach { point ->
                point.currentAlpha -= 2
            }
        }
        drawingList.forEach { innerList ->
            for (i in 1 until innerList.size) {
                prevPoint = innerList[i - 1]
                boardPoint = innerList[i]
                paint.apply {
                    color = boardPoint.color
                    alpha = boardPoint.currentAlpha
                }
                canvas.drawLine(prevPoint.centerX, prevPoint.centerY, boardPoint.centerX, boardPoint.centerY, paint)
            }
        }
        drawingList.forEach { innerList ->
            innerList.removeIf { !it.isValid() }
        }
        drawingList.removeIf { it.isEmpty() }
        if (drawingList.isNotEmpty() && isAttachedToWindow) invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                drawingList.add(arrayListOf())
                addBoardPoint(event.getX(pointerIndex), event.getY(pointerIndex))
                return true
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_MOVE -> {
                addBoardPoint(event.getX(pointerIndex), event.getY(pointerIndex))
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun addBoardPoint(x: Float, y: Float) {
        val boardPoint = BoardPoint(x, y, colorFlipper.getNextColor())
        drawingList.last {
            it.add(boardPoint)
        }
    }
}

class BoardPoint(val centerX: Float, val centerY: Float, val color: Int) : Parcelable {
    var currentAlpha = 255

    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt()) {
        currentAlpha = parcel.readInt()
    }

    fun isValid() = currentAlpha > 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(centerX)
        parcel.writeFloat(centerY)
        parcel.writeInt(color)
        parcel.writeInt(currentAlpha)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardPoint> {

        override fun createFromParcel(parcel: Parcel): BoardPoint {
            return BoardPoint(parcel)
        }

        override fun newArray(size: Int): Array<BoardPoint?> {
            return arrayOfNulls(size)
        }
    }
}

data class ColorFlipper(private var red: Float, private var green: Float, private var blue: Float) {

    enum class Color {
        RED, GREEN, BLUE
    }

    private var destinationColor: Color = Color.BLUE

    fun getNextColor(): Int {
        when (destinationColor) {
            ColorFlipper.Color.RED -> {
                if (red > 254) {
                    destinationColor = Color.GREEN
                } else {
                    red += 1
                }
                if (green > 2)
                    green -= 2
                if (blue > 2)
                    blue -= 2
            }
            ColorFlipper.Color.GREEN -> {
                if (green > 254) {
                    destinationColor = Color.BLUE
                } else {
                    green += 1
                }
                if (red > 2)
                    red -= 2
                if (blue > 2)
                    blue -= 2
            }
            ColorFlipper.Color.BLUE -> {
                if (blue > 254) {
                    destinationColor = Color.RED
                } else {
                    blue += 1
                }
                if (red > 2)
                    red -= 2
                if (green > 2)
                    green -= 2
            }
        }
        val color = android.graphics.Color.valueOf(red / 256, green / 256, blue / 256, 1f)
        return color.toArgb()
    }

}