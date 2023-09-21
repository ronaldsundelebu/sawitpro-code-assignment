package android.template.feature.weighbridge.ui

import android.template.core.data.MyModelRepository
import android.template.core.data.model.TicketData
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeighBridgeListViewModel @Inject constructor(
    private val myModelRepository: MyModelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeighBridgeListUiState>(WeighBridgeListUiState.Loading)
    val uiState: StateFlow<WeighBridgeListUiState> = _uiState

    internal var ticketList: List<TicketData> = emptyList()

    fun loadTicketData() {
        viewModelScope.launch {
            ticketList = myModelRepository.getMyModels()
            _uiState.value = WeighBridgeListUiState.Success(ticketList)
        }
    }

    fun filterData(query: String, isAscending: Boolean) {
        val newList = if (isAscending) {
            ticketList.reversed()
        } else {
            ticketList
        }

        if (query.isBlank()) {
            _uiState.value = WeighBridgeListUiState.Success(newList)
            return
        }

        val filteredList = newList.filter {
            it.driverName.contains(query, ignoreCase = true) ||
                it.licenseNumber.contains(query, ignoreCase = true)
        }

        _uiState.value = WeighBridgeListUiState.Success(filteredList)
    }
}

sealed interface WeighBridgeListUiState {
    object Loading : WeighBridgeListUiState
    data class Error(val throwable: Throwable) : WeighBridgeListUiState
    data class Success(val data: List<TicketData>) : WeighBridgeListUiState
}