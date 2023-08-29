package com.example.myapplication

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.net.wifi.WifiManager
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var myTextView: TextView
    private lateinit var brightnessview: TextView
    private lateinit var sample: TextView
    private lateinit var batterylife:TextView
    private lateinit var totalcap:TextView
    private lateinit var clickabletext:TextView
    private lateinit var inputtext : EditText
    private lateinit var minutes : EditText
    private lateinit var calc : Button
    private lateinit var wifitoggleButton: ToggleButton
    private lateinit var btbutton:ToggleButton
    private lateinit var camerabutton:ToggleButton
    private lateinit var mobiledatabutton:ToggleButton
    private lateinit var brightnessslider: SeekBar
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var wifiManager: WifiManager
    private val savedHistory = mutableListOf<String>()
    private var perminute =1.00



    @SuppressLint("MissingInflatedId") //generate warnings




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get UsageStatsManager instance
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager




        // Register the battery info receiver


        setContentView(R.layout.activity_main)
        brightnessview = findViewById(R.id.txtview)
        sample = findViewById(R.id.textView1)









        showBatteryLevel(this,sample)
    }

    private fun getBatteryCapacity(context: Context): Long {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val chargeCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
        return (chargeCounter.toFloat()).toLong()
    }
    private fun getTimestamp(displayText: String): Long {
        val timeStampString = displayText.substring(0, 19)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(timeStampString)
        return date!!.time
    }
    private fun showBatteryLevel(context: Context, textView: TextView) {
        var wifi = ""
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        wifitoggleButton = findViewById(R.id.myButton)
        btbutton = findViewById(R.id.btbutton)
        camerabutton = findViewById(R.id.camerabutton)
        mobiledatabutton = findViewById(R.id.mobiledatabutton)
        batterylife = findViewById(R.id.batterylife)
        wifitoggleButton.isChecked=wifiManager.isWifiEnabled
        var w = wifiManager.isWifiEnabled
        val bluetoothManager: BluetoothManager? = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
        var bt = bluetoothAdapter?.isEnabled?:false
        btbutton.isChecked = bluetoothAdapter?.isEnabled?:false
        val remainingmah = getBatteryCapacity(this)/1000
//        val bt = if((bluetoothAdapter?.isEnabled)==true) "Bluetooth is on" else "Bluetooth is off"
        val isWifiEnabled = wifiManager.isWifiEnabled
        var pro = 1
        updatevalues(wifitoggleButton.isChecked,btbutton.isChecked,camerabutton.isChecked,mobiledatabutton.isChecked,remainingmah,pro)
        wifitoggleButton.setOnCheckedChangeListener { compoundButton, b ->
            mobiledatabutton.isChecked = !wifitoggleButton.isChecked
            updatevalues(wifitoggleButton.isChecked,btbutton.isChecked,camerabutton.isChecked,mobiledatabutton.isChecked,remainingmah,pro) }
        btbutton.setOnCheckedChangeListener { compoundButton, b ->
            updatevalues(wifitoggleButton.isChecked,btbutton.isChecked,camerabutton.isChecked,mobiledatabutton.isChecked,remainingmah,pro)
        }
        camerabutton.setOnCheckedChangeListener { compoundButton, b ->
            updatevalues(wifitoggleButton.isChecked,btbutton.isChecked,camerabutton.isChecked,mobiledatabutton.isChecked,remainingmah,pro)
        }
        mobiledatabutton.setOnCheckedChangeListener { compoundButton, b ->
            wifitoggleButton.isChecked = !mobiledatabutton.isChecked
            updatevalues(wifitoggleButton.isChecked,btbutton.isChecked,camerabutton.isChecked,mobiledatabutton.isChecked,remainingmah,pro)
        }


        brightnessslider = findViewById(R.id.brightness)
        brightnessslider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                brightnessview.text = if(progress==1) "Brightness: 50%" else if(progress==2)"Brightness: 100%" else "Brightness: 50% (default)"
                pro=progress
                updatevalues(wifitoggleButton.isChecked,btbutton.isChecked,camerabutton.isChecked,mobiledatabutton.isChecked,remainingmah,progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Called when the user starts tracking the seek bar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Called when the user stops tracking the seek bar
            }
        })



        totalcap = findViewById(R.id.totalcap)
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val chargeCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
        val totalCapacity = (chargeCounter / (level / 100.0)).toInt()/1000
        val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val displayText = "Approx. time left for a meeting in Google Meet:"
        totalcap.text = totalCapacity.toString() + "mAh"
        textView.text = displayText









        val clickableText = findViewById<TextView>(R.id.clickableText)
        val inputText = findViewById<EditText>(R.id.inputText)
        val calc = findViewById<Button>(R.id.calc)
        val minutes = findViewById<EditText>(R.id.minutes)
        val txtv4 = findViewById<TextView>(R.id.textView4)
        val result = findViewById<TextView>(R.id.textView7)
        inputText.visibility = View.GONE

        clickableText.setOnClickListener {
            if(clickableText.text == "Try Again"){
                clickableText.visibility=View.GONE
                txtv4.text="If the above value is incorrect, Enter Battery Capacity"
                calc.visibility=View.VISIBLE
            }
            inputText.visibility = View.VISIBLE
            minutes.visibility=View.VISIBLE
            calc.visibility=View.VISIBLE
            result.text="Enter the duration of meeting(in minutes):"
        }
        calc.setOnClickListener{
            if(inputText.text.isEmpty() && minutes.text.isEmpty() && inputText.visibility == View.VISIBLE){
                clickableText.visibility=View.GONE
                txtv4.text ="Please enter Battery capacity and duration "
            }
            else if(inputText.text.isEmpty() && minutes.text.isEmpty() && inputText.visibility == View.GONE){
                clickableText.visibility=View.GONE
                txtv4.text = "Please enter duration "
            }
            else if(inputText.text.isEmpty() && minutes.text.isNotEmpty()){
                clickableText.text = "Try Again"
                inputText.visibility = View.GONE
                minutes.visibility=View.GONE
                calc.visibility=View.GONE
                txtv4.text = "Using calculated Battery Capacity as default "
                val min= minutes.text.toString()
                result.text = "After the meeting, your battery level will be at " + (((remainingmah-(min.toInt()*perminute))/totalCapacity)*100).toString() + "%"


            }
            else if(inputText.text.isNotEmpty() && minutes.text.isNotEmpty()){
                clickableText.text = "Try Again"
                clickableText.visibility=View.VISIBLE
                inputText.visibility = View.GONE
                minutes.visibility=View.GONE
                calc.visibility=View.GONE
                val min= minutes.text.toString()
                val cap = inputText.text.toString()
                result.text = "After the meeting, your battery level will be at " + (((remainingmah-(min.toInt()*perminute))/cap.toInt())*100).toString() + "%"

            }

        }










    }
    private fun updatevalues(w:Boolean,bt:Boolean,camera:Boolean,mobiledata:Boolean,remainingmah:Long,progress:Int){
        if(w){
            if(bt){
                if(camera){
                    if(progress==2){
                        calculateRemainingTime(remainingmah,15.8)
                    }
                    else{
                        calculateRemainingTime(remainingmah,10.0)
                    }
                }
                else{
                    if(progress==2){
                        calculateRemainingTime(remainingmah,11.6)
                    }
                    else{
                        calculateRemainingTime(remainingmah,9.8)
                    }
                }
            }
            else{
                if(camera){
                    if(progress==2){
                        calculateRemainingTime(remainingmah,24.2)
                    }
                    else{
                        calculateRemainingTime(remainingmah,17.6)
                    }
                }
                else{
                    if(progress==2){
                        calculateRemainingTime(remainingmah,11.4)
                    }
                    else{
                        calculateRemainingTime(remainingmah,8.1)
                    }
                }
            }
        }
        else{
            if(mobiledata){
                if(bt){
                    if(camera){
                        if(progress==2){
                            calculateRemainingTime(remainingmah,28.8)
                        }
                        else{
                            calculateRemainingTime(remainingmah,22.6)
                        }
                    }
                    else{
                        if(progress==2){
                            calculateRemainingTime(remainingmah,20.2)
                        }
                        else{
                            calculateRemainingTime(remainingmah,15.7)
                        }
                    }
                }
                else{
                    if(camera){
                        if(progress==2){
                            calculateRemainingTime(remainingmah,23.2)
                        }
                        else{
                            calculateRemainingTime(remainingmah,21.6)
                        }
                    }
                    else{
                        if(progress==2){
                            calculateRemainingTime(remainingmah,15.2)
                        }
                        else{
                            calculateRemainingTime(remainingmah,12.1)
                        }
                    }
                }
            }

        }
    }
    private fun calculateRemainingTime(capacity: Long, mahconsumed: Double){

        batterylife.text = (((capacity)/mahconsumed)/60).toInt().toString() + " Hours " + (((((capacity)/mahconsumed)/60)-(((capacity)/mahconsumed)/60).toInt())*60).toInt() + " Minutes"
        perminute= mahconsumed
    }

}