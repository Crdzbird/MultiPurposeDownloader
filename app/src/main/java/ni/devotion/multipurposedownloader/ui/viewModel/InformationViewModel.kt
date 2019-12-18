package ni.devotion.multipurposedownloader.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ni.devotion.multipurposedownloader.R
import ni.devotion.multipurposedownloader.models.Information
import ni.devotion.multipurposedownloader.repositories.InformationRepository
import ni.devotion.multipurposedownloader.ui.Event

class InformationViewModel (private val informationRepository: InformationRepository): ViewModel() {

    private val _uiState = MutableLiveData<CountryDataState>()

    val uiState: LiveData<CountryDataState>
        get() = _uiState

    fun requestInformation() {
        viewModelScope.launch {
            runCatching {
                emitUiState(showProgress = true)
                informationRepository.obtainInformation()
            }.onSuccess {
                emitUiState(result = Event(it))
            }.onFailure {
                emitUiState(error = Event(R.string.internet_connection_error))
            }
        }
    }

    fun emitUiState(showProgress: Boolean = false, result: Event<List<Information>>? = null, error: Event<Int>? = null){
        val dataState = CountryDataState(showProgress, result, error)
        _uiState.value = dataState
    }

    data class CountryDataState(val showProgress: Boolean, val result: Event<List<Information>>?, val error: Event<Int>?)
}