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
class WeighbridgeListScreenViewModelTest {

    private lateinit var viewModel: WeighBridgeListViewModel

    @Before
    fun beforeTest() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = WeighBridgeListViewModel(FakeWeighbridgeListScreenViewModelRepository())
    }

    @Test
    fun `test initial uiState`() = runTest {
        assertEquals(viewModel.uiState.value, WeighBridgeListUiState.Loading)
    }

    @Test
    fun `test loadTicketData with found data`() = runTest {
        viewModel.loadTicketData()

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is WeighBridgeListUiState.Success)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.count(), 5)
    }

    @Test
    fun `test filterData with empty query and descending`() = runTest {
        viewModel.loadTicketData()
        viewModel.filterData("", false)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is WeighBridgeListUiState.Success)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.count(), 5)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.first().timestamp, 5)
    }

    @Test
    fun `test filterData with empty query and ascending`() = runTest {
        viewModel.loadTicketData()
        viewModel.filterData("", true)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is WeighBridgeListUiState.Success)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.count(), 5)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.first().timestamp, 1)
    }

    @Test
    fun `test filterData with 'a' query`() = runTest {
        viewModel.loadTicketData()
        viewModel.filterData("a", false)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is WeighBridgeListUiState.Success)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.count(), 0)
    }

    @Test
    fun `test filterData with '1' query`() = runTest {
        viewModel.loadTicketData()
        viewModel.filterData("1", false)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is WeighBridgeListUiState.Success)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.count(), 2)
    }

    @Test
    fun `test filterData with 'e' query`() = runTest {
        viewModel.loadTicketData()
        viewModel.filterData("e", false)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is WeighBridgeListUiState.Success)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.count(), 5)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.first().timestamp, 5)
    }

    @Test
    fun `test filterData with '5' query and ascending`() = runTest {
        viewModel.loadTicketData()
        viewModel.filterData("5", true)

        val expectedResult = viewModel.uiState.value
        assertTrue(expectedResult is WeighBridgeListUiState.Success)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.count(), 3)
        assertEquals((expectedResult as WeighBridgeListUiState.Success).data.first().timestamp, 3)
    }

}

private class FakeWeighbridgeListScreenViewModelRepository : MyModelRepository {
    val dataList = mutableListOf(
        TicketData(id = 5, timestamp = 5, driverName = "test5", licenseNumber = "456"),
        TicketData(id = 4, timestamp = 4, driverName = "test4", licenseNumber = "456"),
        TicketData(id = 3, timestamp = 3, driverName = "test3", licenseNumber = "456"),
        TicketData(id = 2, timestamp = 2, driverName = "test2", licenseNumber = "123"),
        TicketData(id = 1, timestamp = 1, driverName = "test1", licenseNumber = "123"),
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
