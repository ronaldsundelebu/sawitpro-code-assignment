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

package android.template.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import android.template.core.data.DefaultMyModelRepository
import android.template.core.data.MyModelRepository
import android.template.core.data.model.TicketData
import android.template.core.database.MyModel
import android.template.core.database.MyModelDao
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.setMain
import org.junit.Before

/**
 * Unit tests for [DefaultMyModelRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultMyModelRepositoryTest {

    private lateinit var repository: MyModelRepository

    @Before
    fun beforeTest() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = DefaultMyModelRepository(FakeMyModelDao())
    }

    @Test
    fun `test for add repository`() = runTest {
        repository.add(TicketData())
        assertEquals(repository.getMyModels().size, 1)
    }

    @Test
    fun `test for update repository`() = runTest {
        repository.add(TicketData(1, driverName = "test1"))
        repository.add(TicketData(2, driverName = "test1"))
        repository.add(TicketData(3, driverName = "test1"))
        repository.add(TicketData(4, driverName = "test1"))

        repository.update(TicketData(2, driverName = "test2"))

        assertEquals(repository.getMyModels().size, 4)
        assertEquals(repository.findMyModel(2).driverName, "test2")
    }

}

private class FakeMyModelDao: MyModelDao {
    val dataList = mutableListOf<MyModel>()

    override suspend fun getMyModels(): List<MyModel> {
        return dataList
    }

    override suspend fun findMyModel(id: Int): MyModel? {
        return dataList.find { it.id == id }
    }

    override suspend fun insertMyModel(item: MyModel) {
        dataList.add(item.copy(id = dataList.size))
    }

    override suspend fun updateMyModel(item: MyModel) {
        val index = dataList.indexOfFirst { it.id == item.id }
        dataList[index] = item
    }
}
