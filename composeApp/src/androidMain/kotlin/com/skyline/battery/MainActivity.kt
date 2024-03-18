package com.skyline.battery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.skyline.battery.ui.BatteryScreen
import com.skyline.battery.viewmodels.BatteryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val batteryViewModel: BatteryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BatteryScreen(viewModel = batteryViewModel)
        }

        observeChargingState()
        observeBatteryLevel()
    }

    private fun observeChargingState() {
        lifecycleScope.launchWhenStarted {
            batteryViewModel.isCharging.collect { isCharging ->
                batteryViewModel.updateChargingState(isCharging)
            }
        }
    }

    private fun observeBatteryLevel() {
        lifecycleScope.launchWhenStarted {
            batteryViewModel.batteryLevel.collect { level ->
                batteryViewModel.updateBatteryLevel(level)
            }
        }
    }
}
