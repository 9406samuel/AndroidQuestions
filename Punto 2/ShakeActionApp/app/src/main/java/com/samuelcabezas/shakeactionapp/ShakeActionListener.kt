package com.samuelcabezas.shakeactionapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeEventListener : SensorEventListener {

    //Total shakes
    private var totalShakes: Int = 0

    // The gForce that is necessary to register as shake. Must be greater than 1G (one earth gravity unit)
    private val SHAKE_THRESHOLD_GRAVITY = 2.7f
    private val SHAKE_SLOP_TIME_MS = 500

    private var currentShakeTimestamp: Long = 0

    //OnShakeListener that is called when shake is detected
    private var mShakeListener: OnShakeListener? = null

    fun setOnShakeListener(listener: OnShakeListener) {
        mShakeListener = listener
    }

    override fun onSensorChanged(se: SensorEvent) {
        //get current data
        val currentX = se.values[0]
        val currentY = se.values[1]
        val currentZ = se.values[2]

        val gX = (currentX / SensorManager.GRAVITY_EARTH).toDouble()
        val gY = (currentY / SensorManager.GRAVITY_EARTH).toDouble()
        val gZ = (currentZ / SensorManager.GRAVITY_EARTH).toDouble()

        // gForce will be close to 1 when there is no movement.
        val gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ)

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis()
            // ignore shake events too close to each other (500ms)
            if (currentShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return
            }

            currentShakeTimestamp = now
            mShakeListener?.onShake(++totalShakes)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}

// Interface for shake action
interface OnShakeListener {
    //Called when shake action is detected
    fun onShake(totalShakes: Int)
}
