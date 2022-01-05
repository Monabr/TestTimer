package com.example.testtimer.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val isTimerStarted by viewModel.checkTimer().collectAsState(initial = null)

    Surface(color = MaterialTheme.colors.background) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = viewModel.currentTime.value, fontSize = MaterialTheme.typography.h3.fontSize)
            Spacer(modifier = Modifier.height(128.dp))

            when (isTimerStarted) {
                true -> {

                    rememberSaveable("started") {
                        viewModel.getCurrentTime()
                        true
                    }

                    Button(onClick = { viewModel.stopTheTimer() }, modifier = Modifier.padding(horizontal = 24.dp)) {
                        Text(text = "Stop", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(16.dp))
                    }
                }
                false -> {
                    Button(onClick = { viewModel.getCurrentTime() }, modifier = Modifier.padding(horizontal = 24.dp)) {
                        Text(text = "Start", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(16.dp))
                    }
                }
                else -> {}
            }
        }
    }
}