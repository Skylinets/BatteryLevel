package com.skyline.battery.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skyline.battery.ui.composable.AnimatedWaterLevel
import com.skyline.battery.viewmodels.BatteryViewModel

@Composable
fun BatteryScreen(viewModel: BatteryViewModel) {
    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val isCharging by viewModel.isCharging.collectAsState()
    val isFastCharging by viewModel.isFastCharging.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Battery Level: ${batteryLevel * 100}%")
        Text(text = if (isFastCharging) "HyperCharging" else if (isCharging) "Charging" else "Discharging")
        AnimatedWaterLevel(progress = batteryLevel * 100, isCharging = isCharging, isFastCharging = isFastCharging)
    }
}
