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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import android.template.core.data.MyModelRepository
import android.template.core.data.model.TicketData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class MyModelViewModelTest {

    private lateinit var viewModel: MyModelViewModel

    @Before
    fun beforeTest() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MyModelViewModel(FakeMyModelViewModelRepository())
    }

    @Test
    fun `test initial uiState`() = runTest {
        assertEquals(viewModel.uiState.first(), MyModelUiState.Loading)
    }

    @Test
    fun `test getTicketData with found data`() = runTest {
        viewModel.getTicketData(1)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is MyModelUiState.Success)
        assertEquals((expectedResult as MyModelUiState.Success).data.driverName, "test")
    }

    @Test
    fun `test getTicketData without found data`() = runTest {
        viewModel.getTicketData(8)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is MyModelUiState.Success)
        assertEquals((expectedResult as MyModelUiState.Success).data.driverName, "")
    }

    @Test
    fun `test onSaveClick with add data`() = runTest {
        viewModel.onSaveClick(TicketData(id = 0, driverName = "johndoe"))
        viewModel.getTicketData(0)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is MyModelUiState.Success)
        assertEquals((expectedResult as MyModelUiState.Success).data.driverName, "johndoe")
    }

    @Test
    fun `test onSaveClick with update data`() = runTest {
        viewModel.onSaveClick(TicketData(id = 1, driverName = "test2"))
        viewModel.getTicketData(1)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is MyModelUiState.Success)
        assertEquals((expectedResult as MyModelUiState.Success).data.driverName, "test2")
    }
}

private class FakeMyModelViewModelRepository : MyModelRepository {
    val dataList = mutableListOf(
        TicketData(
            id = 1,
            driverName = "test"
        )
    )

    override suspend fun getMyModels(): List<TicketData> {
        return dataList
    }

    override suspend fun findMyModel(id: Int): TicketData {
        return dataList.find { it.id == id } ?: TicketData()
    }

    override suspend fun add(ticket: TicketData) {
        dataList.add(ticket)
    }

    override suspend fun update(ticket: TicketData) {
        val index = dataList.indexOfFirst { it.id == ticket.id }
        dataList[index] = ticket
    }
}
