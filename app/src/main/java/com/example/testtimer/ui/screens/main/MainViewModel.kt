package com.example.testtimer.ui.screens.main

import android.icu.util.LocaleData
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtimer.repository.PrefsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefsStore: PrefsStore
) : ViewModel() {

    private val oneSecond = 1000L

    private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private var timerJob: Job? = null


    /**
     * Field to observe via compose to get current time in string format
     */
    val currentTime = mutableStateOf("00:00:00")

    /**
     * Checks if timer is started
     */
    fun checkTimer() = flow {
        prefsStore.getIsTimerStarted().collect {
            emit(it)
        }
    }

    /**
     * Start to collect the values
     */
    fun getCurrentTime() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                prefsStore.setIsTimerStarted(true)
                prefsStore.getCurrentTime().collect {
                    currentTime.value = timeFormatter.format(Date(it))
                    delay(oneSecond)
                    prefsStore.setCurrentTime(it + oneSecond)
                }
            }
        }
    }

    /**
     * Stops the values and reset current value
     */
    fun stopTheTimer() {
        timerJob?.cancel()
        timerJob = null
        viewModelScope.launch {
            prefsStore.setCurrentTime(0L)
            currentTime.value = timeFormatter.format(Date(0L))
            prefsStore.setIsTimerStarted(false)
        }
    }
}