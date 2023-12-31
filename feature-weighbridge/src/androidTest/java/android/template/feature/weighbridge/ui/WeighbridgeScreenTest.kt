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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [MyModelScreen].
 */
@RunWith(AndroidJUnit4::class)
class MyModelScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            MyModelScreen(
                ticket = TicketData(),
                onSaveClick = { /*...*/ },
            )
        }
    }

    @Test
    fun check_if_inbound_time_exist() {
        composeTestRule.onNodeWithText("Inbound Time").assertExists().performClick()
    }

    @Test
    fun check_if_license_number_exist() {
        composeTestRule.onNodeWithText("License Number").assertExists().performClick()
    }

    @Test
    fun check_if_driver_name_exist() {
        composeTestRule.onNodeWithText("Driver Name").assertExists().performClick()
    }

    @Test
    fun check_if_inbound_weight_exist() {
        composeTestRule.onNodeWithText("Inbound Weight").assertExists().performClick()
    }

    @Test
    fun check_if_outbound_weight_exist() {
        composeTestRule.onNodeWithText("Outbound Weight").assertExists().performClick()
    }

    @Test
    fun check_if_net_weight_exist() {
        composeTestRule.onNodeWithText("Net Weight").assertExists().performClick()
    }
}
