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

package com.ai_blue.personalexpensetracker.feature.expenseitem.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.ai_blue.personalexpensetracker.core.data.ExpenseItemRepository
import com.ai_blue.personalexpensetracker.feature.expenseitem.ui.ExpenseItemUiState.Error
import com.ai_blue.personalexpensetracker.feature.expenseitem.ui.ExpenseItemUiState.Loading
import com.ai_blue.personalexpensetracker.feature.expenseitem.ui.ExpenseItemUiState.Success
import javax.inject.Inject

@HiltViewModel
class ExpenseItemViewModel @Inject constructor(
    private val expenseItemRepository: ExpenseItemRepository
) : ViewModel() {

    val uiState: StateFlow<ExpenseItemUiState> = expenseItemRepository
        .expenseItems.map<List<String>, ExpenseItemUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addExpenseItem(name: String) {
        viewModelScope.launch {
            expenseItemRepository.add(name)
        }
    }
}

sealed interface ExpenseItemUiState {
    object Loading : ExpenseItemUiState
    data class Error(val throwable: Throwable) : ExpenseItemUiState
    data class Success(val data: List<String>) : ExpenseItemUiState
}
