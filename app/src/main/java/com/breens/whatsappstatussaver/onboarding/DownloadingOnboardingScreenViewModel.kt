package com.breens.whatsappstatussaver.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.whatsappstatussaver.preferences.domain.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DownloadingOnboardingScreenViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository) :
    ViewModel() {

    private val _isOnBoardingCompletedState = MutableStateFlow(false)
    val isOnBoardingCompletedState = _isOnBoardingCompletedState.asStateFlow()

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
                    _isOnBoardingCompletedState.value = isOnBoardingCompleted
                }
        }
    }
}
