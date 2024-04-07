package com.breens.whatsappstatussaver.onboarding

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.whatsappstatussaver.preferences.domain.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class DownloadingOnboardingScreenViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository) :
    ViewModel() {

    private val _isOnBoardingCompletedState = mutableStateOf(false)
    val isOnBoardingCompletedState = _isOnBoardingCompletedState

    fun setIsOnboardingCompleted(isOnBoardingCompleted: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setIsOnboardingCompleted(
                isOnBoardingCompleted = isOnBoardingCompleted,
            )
        }
    }

    fun getIsOnboardingCompleted() {
        viewModelScope.launch {
            preferencesRepository.isOnBoardingCompleted()
                .collectLatest { isOnBoardingCompleted ->
                    Log.e("DownloadingScreen", "isOnBoardingCompleted: $isOnBoardingCompleted")
                    isOnBoardingCompletedState.value = isOnBoardingCompleted
                }
        }
    }
}
