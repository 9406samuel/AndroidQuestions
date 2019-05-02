package com.samuelcabezas.compasscustomviewapp

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.Random;

class CompassView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val LTBROWN_COLOR = Color.rgb(210,105,30)
    private val DRBROWN_COLOR = Color.rgb(139,69,19)

    private var externalPadding = 30f
    private var baseTextSize = 100f
    private var borderWidth = 0f
    private var baseRadius = 0f
    private var brownRingRadius = 0f
    private var ltGrayRingRadius = 0f
    private var whiteCircleRadius = 0f
    private var blackCenterPointRadius = 0f
    private var gradesRadius = 0f
    private var cardinalPointsRadius = 0f
    private var gradesTextSize = 0f
    private var needleLineWidth = 0f
    private var redNeedleAngle = Random().nextDouble()
    private var blackNeedleAngle = 0.0
    private var needleLength = 0f
    private var cardinalPointsTextSize = 0f
    private var brownRingWidth = 0
    private var ltGrayRingWidth = 0
    private var whiteCircleWidth = 0
    private var centerX = 0f
    private var centerY = 0f
    private var rect = Rect()

    private val gradesList = Array(18) { i -> i * 20}
    private val cardinalPoints = arrayOf("S", "W", "N", "E")

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initializeCompass()

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        /*BROWN RING*/
        paint.color = LTBROWN_COLOR
        paint.style = Paint.Style.FILL
        drawCircle(canvas, brownRingRadius, paint)

        //External border
        paint.color = DRBROWN_COLOR
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        drawCircle(canvas, brownRingRadius, paint)

        /*LITE GRAY RING*/
        paint.color = Color.LTGRAY
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f
        drawCircle(canvas, ltGrayRingRadius, paint)

        //External border
        paint.color = DRBROWN_COLOR
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        drawCircle(canvas, ltGrayRingRadius, paint)

        /*WHITE INTERNAL CIRCLE*/
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f
        drawCircle(canvas, whiteCircleRadius, paint)

        /*BLACK CENTER POINT*/
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        drawCircle(canvas, blackCenterPointRadius, paint)

        /*GRADES*/
        paint.textSize = gradesTextSize
        drawGrades(canvas, gradesRadius, paint)

        /*NEEDLE*/
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        paint.strokeWidth = needleLineWidth
        drawNeedle(canvas, redNeedleAngle, paint)

        paint.color = Color.BLACK
        drawNeedle(canvas, blackNeedleAngle, paint)

        /*CARDINAL POINTS*/
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.textSize = cardinalPointsTextSize
        drawCardinalPoints(canvas, cardinalPointsRadius, paint)

    }

    private fun initializeCompass(){
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            centerY = Math.min(width, height) / 2f
            centerX = Math.max(width, height) / 2f
            baseRadius =  centerY
            gradesTextSize = baseTextSize * 0.3f
            cardinalPointsTextSize = baseTextSize * 0.4f
        } else {
            baseRadius = Math.min(width, height) / 2f
            centerY = Math.max(width, height) / 2f
            centerX = baseRadius
            gradesTextSize = baseTextSize * 0.4f
            cardinalPointsTextSize = baseTextSize * 0.6f
        }

        needleLineWidth = baseRadius * 0.05f
        borderWidth = baseRadius * 0.01f
        redNeedleAngle *= Math.PI
        blackNeedleAngle = redNeedleAngle  + Math.PI
        blackCenterPointRadius = baseRadius * 0.05f
        needleLength = baseRadius * 0.75f
        brownRingWidth = (baseRadius * 0.1f).toInt()
        ltGrayRingWidth = (baseRadius * 0.15f).toInt()
        whiteCircleWidth = (baseRadius * 0.75f).toInt()

        brownRingRadius = baseRadius - externalPadding
        ltGrayRingRadius = brownRingRadius - brownRingWidth
        whiteCircleRadius = ltGrayRingRadius - ltGrayRingWidth
        gradesRadius = ltGrayRingRadius - ltGrayRingWidth / 2
        cardinalPointsRadius = whiteCircleRadius * 0.7f
    }

    private fun drawCircle(canvas: Canvas, r: Float, paint: Paint) {
        canvas.drawCircle(centerX, centerY, r, paint)
    }

    private fun drawGrades(canvas: Canvas, r: Float, paint: Paint){

        for(i in gradesList.indices){
            val tmp = gradesList[i].toString()
            paint.getTextBounds(tmp,0, tmp.length, rect)
            val angle = Math.PI / (gradesList.size / 2) * ((i + 1) + 12.5)
            val x = (width / 2 + Math.cos(angle) * r - rect.width() / 2).toFloat()
            val y = ((height + 50) / 2 + Math.sin(angle) * r - rect.height() / 2).toFloat()
            canvas.drawText(tmp, x, y, paint)
        }
    }

    fun drawCardinalPoints(canvas: Canvas, r: Float, paint: Paint){

        for(i in cardinalPoints.indices){
            val tmp = cardinalPoints[i]
            paint.getTextBounds(tmp,0,tmp.length,rect)
            val angle = Math.PI/(cardinalPoints.size/2)*(i + 1)
            val x = (width / 2 + Math.cos(angle) * r - rect.width() / 2).toFloat()
            val y = ((height + 50)/ 2 + Math.sin(angle) * r - rect.height() / 2).toFloat()
            canvas.drawText(tmp, x, y, paint)
        }
    }

    private fun drawNeedle(canvas: Canvas, angle: Double, paint: Paint){
        canvas.drawLine(width / 2f, height / 2f, (width / 2f + Math.cos(angle) * needleLength).toFloat(),
                    (height / 2 + Math.sin(angle) * needleLength).toFloat(), paint)
    }
    

    }
