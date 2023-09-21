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

import android.template.core.data.model.TicketData
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [WeighbridgeListScreenTest].
 */
@RunWith(AndroidJUnit4::class)
class WeighbridgeListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            WeightBridgeListScreen(
                items = listOf(
                    TicketData(licenseNumber = "123", driverName = "test1"),
                    TicketData(licenseNumber = "123", driverName = "test2"),
                    TicketData(licenseNumber = "123", driverName = "test3"),
                    TicketData(licenseNumber = "123", driverName = "test4"),
                    TicketData(licenseNumber = "123", driverName = "test5"),
                ),
                onAddTicketClick = { /*...*/ },
                onEditTicketClick = { /*...*/ },
                onFilterChanged = { _, _ ->  /*...*/ },
            )
        }
    }

    @Test
    fun check_if_component_edit_exist() {
        composeTestRule.onAllNodesWithContentDescription("edit").assertCountEquals(5)
    }

    @Test
    fun check_if_component_add_exist() {
        composeTestRule.onAllNodesWithContentDescription("add").assertCountEquals(1)
    }

    @Test
    fun check_if_component_sort_exist() {
        composeTestRule.onAllNodesWithContentDescription("sort").assertCountEquals(1)
    }

}
