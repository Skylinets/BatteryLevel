package com.skyline.battery.viewmodels

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyline.battery.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class BatteryViewModel @Inject constructor() : ViewModel() {

    private val context: Context = MyApplication.applicationContext()

    private val _isCharging = MutableStateFlow(false)
    val isCharging: StateFlow<Boolean> = _isCharging

    private val _isFastCharging = MutableStateFlow(false)
    val isFastCharging: StateFlow<Boolean> = _isFastCharging
    private var previousBatteryLevel: Float = 0f

    private val _batteryLevel = MutableStateFlow(0f)
    val batteryLevel: StateFlow<Float> = _batteryLevel

    fun updateBatteryLevel(level: Float) {
        _batteryLevel.value = level
      //  checkFastCharging(level)
        previousBatteryLevel = level
    }

    fun updateChargingState(isCharging: Boolean) {
        _isCharging.value = isCharging
    }

    private val chargingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            val level: Int = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale: Int = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryLevel = level / scale.toFloat()

            viewModelScope.launch {
                _isCharging.emit(isCharging)
                _batteryLevel.emit(batteryLevel)
                checkFastCharging2(batteryLevel)
            }
        }
    }
    init {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(chargingReceiver, intentFilter)
    }
    private fun checkFastCharging(currentLevel: Float) {
        val levelDifference = currentLevel - previousBatteryLevel
        val isFastCharging = levelDifference > 0.4f
        _isFastCharging.value = isFastCharging
    }
    private fun checkFastCharging2(currentLevel: Float) {
        val voltage = 9.0
        val powerFastCharging = 25.0
        val powerSlowCharging = 7.75

        val currentFastCharging = powerFastCharging / voltage
        val currentSlowCharging = powerSlowCharging / voltage

        val levelDifference = currentLevel - previousBatteryLevel

        _isFastCharging.value = levelDifference * voltage > currentFastCharging
        previousBatteryLevel = currentLevel
    }
    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(chargingReceiver)
    }
}
