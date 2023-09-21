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

package android.template.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import android.template.core.data.MyModelRepository
import android.template.core.data.DefaultMyModelRepository
import android.template.core.data.model.TicketData
import android.template.core.database.MyModel
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsMyModelRepository(
        myModelRepository: DefaultMyModelRepository
    ): MyModelRepository
}

class FakeMyModelRepository @Inject constructor() : MyModelRepository {
    override suspend fun getMyModels(): List<TicketData> = fakeMyModels

    override suspend fun findMyModel(id: Int): TicketData = TicketData()

    override suspend fun add(ticket: TicketData) {
        throw NotImplementedError()
    }

    override suspend fun update(ticket: TicketData) {
        throw NotImplementedError()
    }
}

val fakeMyModels = emptyList<TicketData>()
