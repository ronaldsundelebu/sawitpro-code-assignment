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

package android.template.core.data

import android.template.core.data.model.TicketData
import android.template.core.database.MyModel
import android.template.core.database.MyModelDao
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MyModelRepository {
    suspend fun getMyModels(): List<TicketData>

    suspend fun findMyModel(id: Int): TicketData

    suspend fun add(ticket: TicketData)

    suspend fun update(ticket: TicketData)
}

class DefaultMyModelRepository @Inject constructor(
    private val myModelDao: MyModelDao
) : MyModelRepository {

    override suspend fun getMyModels(): List<TicketData> =
        myModelDao.getMyModels().map {
            TicketData(
                id = it.id,
                timestamp = it.timestamp,
                licenseNumber = it.licenseNumber,
                driverName = it.driverName,
                inWeight = it.inWeight,
                outWeight = it.outWeight
            )
        }

    override suspend fun findMyModel(id: Int): TicketData {
        val result = myModelDao.findMyModel(id)
            ?: return TicketData()
        return TicketData(
            id = result.id,
            timestamp = result.timestamp,
            licenseNumber = result.licenseNumber,
            driverName = result.driverName,
            inWeight = result.inWeight,
            outWeight = result.outWeight
        )
    }

    override suspend fun add(ticket: TicketData) {
        myModelDao.insertMyModel(
            MyModel(
                timestamp = ticket.timestamp,
                licenseNumber = ticket.licenseNumber,
                driverName = ticket.driverName,
                inWeight = ticket.inWeight,
                outWeight = ticket.outWeight,
            )
        )
    }

    override suspend fun update(ticket: TicketData) {
        myModelDao.updateMyModel(
            MyModel(
                id = ticket.id,
                timestamp = ticket.timestamp,
                licenseNumber = ticket.licenseNumber,
                driverName = ticket.driverName,
                inWeight = ticket.inWeight,
                outWeight = ticket.outWeight,
            )
        )
    }
}
