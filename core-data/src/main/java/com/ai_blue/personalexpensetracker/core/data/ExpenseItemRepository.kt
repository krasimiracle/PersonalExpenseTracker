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

package com.ai_blue.personalexpensetracker.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.ai_blue.personalexpensetracker.core.database.ExpenseItem
import com.ai_blue.personalexpensetracker.core.database.ExpenseItemDao
import javax.inject.Inject

interface ExpenseItemRepository {
    val expenseItems: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultExpenseItemRepository @Inject constructor(
    private val expenseItemDao: ExpenseItemDao
) : ExpenseItemRepository {

    override val expenseItems: Flow<List<String>> =
        expenseItemDao.getExpenseItems().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        expenseItemDao.insertExpenseItem(ExpenseItem(name = name))
    }
}
