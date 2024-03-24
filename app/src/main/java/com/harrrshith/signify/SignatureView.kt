package com.harrrshith.signify

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class SignatureView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    private var drawPath: CustomPath? = null
    private var canvasBitmap: Bitmap? = null
    private var canvasPaint: Paint? = null
    private var drawPaint: Paint? = null
    private var brushSize: Float = 0f
    private var mColor: Int = Color.BLACK
    private var mCanvas: Canvas? = null
    private var drawPaths = ArrayList<CustomPath>()
    private var undoDrawPaths = ArrayList<CustomPath>()


    init {
        setUpDrawing()
    }

    private fun setUpDrawing(){
        drawPaint = Paint()
        drawPath = CustomPath(mColor, brushSize)
        drawPaint!!.apply {
            color = mColor
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
        for (path in drawPaths) {
            drawPaint!!.strokeWidth = path.brushThickness
            drawPaint!!.color = path.color
            canvas.drawPath(path, drawPaint!!)
        }
        if (!drawPath!!.isEmpty){
            drawPaint!!.strokeWidth = drawPath!!.brushThickness
            drawPaint!!.color = drawPath!!.color
            canvas.drawPath(drawPath!!, drawPaint!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                drawPath!!.color = mColor
                drawPath!!.brushThickness = brushSize
                drawPath!!.reset()
                if (touchX != null && touchY != null) {
                    drawPath!!.moveTo(touchX, touchY)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(touchX != null && touchY != null){
                    drawPath!!.lineTo(touchX, touchY)
                }
            }
            MotionEvent.ACTION_UP -> {
                drawPaths.add(drawPath!!)
                drawPath = CustomPath(mColor, brushSize)

            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setBrushSize(size: Float){
        brushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, resources.displayMetrics)
    }

    fun setPenColor(newColor: String){
        mColor = Color.parseColor(newColor)
        drawPaint!!.color = mColor
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float): Path()
}

