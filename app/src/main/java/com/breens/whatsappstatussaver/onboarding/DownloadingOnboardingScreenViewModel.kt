package com.breens.whatsappstatussaver.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.whatsappstatussaver.data.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadingOnboardingScreenViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository) :
    ViewModel() {

    val isOnBoardingCompletedState = MutableStateFlow(false)

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
                .collect { isOnBoardingCompleted ->
                    isOnBoardingCompletedState.value = isOnBoardingCompleted
                }
        }
    }
}