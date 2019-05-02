package com.samuelcabezas.compasscustomviewapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CompassView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val LTBROWN_COLOR = Color.rgb(210,105,30)
    private val DRBROWN_COLOR = Color.rgb(139,69,19)

    private var externalPadding = 50f
    private var externalBorderWidth = 10f
    private var internalBorderWidth = 10f
    private var internalPadding = 125f

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var size = 0
    private var rect = Rect()

    private val gradesList = Array(18, {i -> i*20})

    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        initializeCompass()
        drawCircle(canvas)
        drawCenter(canvas)
        drawGrades(canvas)
        drawNeddle(canvas, -1*Math.PI * 1/4, Color.RED)
        drawNeddle(canvas, Math.PI * 3/4, Color.BLACK)


        invalidate()
    }

    private fun initializeCompass(){
        radius = Math.min(width, height) / 2f
        centerX = radius
        centerY = Math.max(width, height) / 2f
    }

    private fun drawCircle(canvas: Canvas) {

        //External brown ring
        paint.color = LTBROWN_COLOR
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius - externalPadding, paint)

        paint.color = DRBROWN_COLOR
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = externalBorderWidth
        canvas.drawCircle(centerX, centerY, radius - externalPadding - externalBorderWidth, paint)

        //Internal lite gray circle
        paint.color = Color.LTGRAY
        paint.style = Paint.Style.FILL

        canvas.drawCircle(centerX, centerY, radius - internalPadding, paint)

        paint.color = DRBROWN_COLOR
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = externalBorderWidth
        canvas.drawCircle(centerX, centerY, radius - internalPadding - internalBorderWidth / 2f, paint)
    }

    private fun drawCenter(canvas: Canvas){

        //Compass center
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, 20f, paint)
    }

    private fun drawGrades(canvas: Canvas){
        paint.textSize = 40f
        for(i in gradesList.indices){
            val tmp = gradesList[i].toString()
            paint.getTextBounds(tmp,0,tmp.length,rect)
            var angle = Math.PI/(gradesList.size/2)*((i + 1) + 12.5)
            var x = ((width)/ 2 + Math.cos(angle)*(radius - internalPadding - internalBorderWidth - 50) - rect.width()/2).toFloat()
            var y = ((height+50)/ 2 + Math.sin(angle)*(radius - internalPadding - internalBorderWidth -50)- rect.height()/2).toFloat()
            canvas.drawText(tmp, x, y, paint)

        }
    }

    private fun drawNeddle(canvas: Canvas, angle: Double, color: Int){
        //External brown ring
        paint.color = color
        paint.style = Paint.Style.FILL


        canvas.drawLine(width/2f, height/2f, (width/ 2 + Math.cos(angle)*centerX/2).toFloat(),
                (height/2 + Math.sin(angle)*centerY/2).toFloat(),paint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 1
        size = Math.min(measuredWidth, measuredHeight)
// 2
        setMeasuredDimension(size, size)

    }
}