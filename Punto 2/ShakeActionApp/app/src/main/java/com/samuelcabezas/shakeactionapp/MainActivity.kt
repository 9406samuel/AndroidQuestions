package com.samuelcabezas.shakeactionapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), OnShakeListener{

    private var sensorManager: SensorManager? = null
    private var sensorListener: ShakeEventListener? = null
    private lateinit var tvTotalShakes : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorListener = ShakeEventListener()
        sensorListener?.setOnShakeListener(this)
        tvTotalShakes = findViewById(R.id.tv_total_shakes)
        tvTotalShakes.text = String.format(getString(R.string.total_shakes), 0)
    }

    override fun onResume(){
        super.onResume()
        sensorManager?.registerListener(sensorListener,
                sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause(){
        super.onPause()
        sensorManager?.unregisterListener(sensorListener)
    }

    override fun onShake(totalShakes : Int) {
        tvTotalShakes.text = String.format(getString(R.string.total_shakes), totalShakes)
        Toast.makeText(this, R.string.shake, Toast.LENGTH_SHORT).show()
    }
}
