/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.feature.weighbridge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.template.core.data.MyModelRepository
import android.template.core.data.model.TicketData
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MyModelViewModel @Inject constructor(
    private val myModelRepository: MyModelRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow<MyModelUiState>(MyModelUiState.Loading)
    val uiState: StateFlow<MyModelUiState> = _uiState

    fun getTicketData(id: Int) {
        viewModelScope.launch {
            _uiState.value = MyModelUiState.Success(myModelRepository.findMyModel(id))
        }
    }

    fun onSaveClick(ticket: TicketData) {
        viewModelScope.launch {
            if (ticket.id > 0) {
                myModelRepository.update(ticket)
            } else {
                myModelRepository.add(ticket)
            }
        }
    }
}

sealed interface MyModelUiState {
    object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: TicketData) : MyModelUiState
}
