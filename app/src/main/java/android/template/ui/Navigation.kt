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

package android.template.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.template.feature.weighbridge.ui.MyModelScreen
import android.template.feature.weighbridge.ui.WeightBridgeListScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MAIN_SCREEN_ROUTE
    ) {
        composable(route = MAIN_SCREEN_ROUTE) { _ ->
            WeightBridgeListScreen(
                onNavigateToAddEditScreen = { navController.navigate(ADD_EDIT_SCREEN_ARGS + it) }
            )
        }
        composable(
            route = ADD_EDIT_SCREEN_ROUTE,
            arguments = listOf(navArgument("ticketId") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) { backStackEntry ->
            MyModelScreen(
                onNavigateBack = { navController.navigateUp() },
                ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 0
            )
        }
    }
}

const val MAIN_SCREEN_ROUTE = "main_screen"
const val ADD_EDIT_SCREEN_ROUTE = "add_edit_screen?ticketId={ticketId}"
const val ADD_EDIT_SCREEN_ARGS = "add_edit_screen?ticketId="
