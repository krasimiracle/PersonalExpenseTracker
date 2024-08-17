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

package com.ai_blue.personalexpensetracker.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.ai_blue.personalexpensetracker.core.data.DefaultExpenseItemRepository
import com.ai_blue.personalexpensetracker.core.database.ExpenseItem
import com.ai_blue.personalexpensetracker.core.database.ExpenseItemDao

/**
 * Unit tests for [DefaultExpenseItemRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultExpenseItemRepositoryTest {

    @Test
    fun expenseItems_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultExpenseItemRepository(FakeExpenseItemDao())

        repository.add("Repository")

        assertEquals(repository.expenseItems.first().size, 1)
    }

}

private class FakeExpenseItemDao : ExpenseItemDao {

    private val data = mutableListOf<ExpenseItem>()

    override fun getExpenseItems(): Flow<List<ExpenseItem>> = flow {
        emit(data)
    }

    override suspend fun insertExpenseItem(item: ExpenseItem) {
        data.add(0, item)
    }
}
