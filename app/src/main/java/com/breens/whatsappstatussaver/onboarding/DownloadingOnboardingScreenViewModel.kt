package com.breens.whatsappstatussaver.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.whatsappstatussaver.preferences.domain.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
                    isOnBoardingCompletedState.value = isOnBoardingCompleted
                }
        }
    }
}
