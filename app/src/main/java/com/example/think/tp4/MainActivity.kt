package com.example.think.tp4

import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.Time
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var async: MainActivity.HumidityAsync

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun clickStart(v: View) {
        v.isEnabled = false
        stopBtn.isEnabled = true
        async = HumidityAsync(statusTv,progressBar)
        async.execute()

    }

    fun clickStop(v: View) {
        v.isEnabled = false
        startBtn.isEnabled = true
        async.cancel(true)


    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        clickStop(stopBtn)

    }

    private class HumidityAsync(val tv: TextView, val progress: ProgressBar) : AsyncTask<Void, Float, Int>() {
        val status = tv
        val prog = progress

        override fun doInBackground(vararg params: Void?): Int? {
            while (!isCancelled) {

                val ds2438: HumiditySensorAbstract = HTTPHumiditySensor("http://lmi92.cnam.fr/ds2438/ds2438/")
                publishProgress(ds2438.value())
                Thread.sleep(ds2438.minimalPeriod())
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Float?) {
            super.onProgressUpdate(*values)
            val currentTime = Time()
            currentTime.setToNow()
            val date = currentTime.format("%d.%m.%Y %H:%M:%S")
            status.text = "[" + date + "] ds2438 : " + values[0];
            prog.setProgress(values[0]!!.toInt());
        }


    }
}
