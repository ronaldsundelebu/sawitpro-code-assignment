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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import android.template.feature.weighbridge.ui.MyModelUiState.Success
import android.template.core.ui.MyApplicationTheme
import android.template.core.ui.timestampToDate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import kotlin.math.abs

@Composable
fun MyModelScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyModelViewModel = hiltViewModel(),
    ticketId: Int = 0,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val item by produceState<MyModelUiState>(
        initialValue = MyModelUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getTicketData(ticketId)
    }

    MyModelScreen(
        modifier = modifier,
        ticket = if (item is Success) {
            (item as Success).data
        } else {
            TicketData()
        },
        onSaveClick = {
            viewModel.onSaveClick(it)
            onNavigateBack()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyModelScreen(
    ticket: TicketData,
    onSaveClick: (TicketData) -> Unit,
    modifier: Modifier = Modifier
) {
    var timestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        timestamp = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    },
                ) {
                    Text("OK")
                }
            },
            content = { DatePicker(datePickerState) }
        )
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create Inbound Ticket")
                }
            )
        }
    ) {
        var license by remember { mutableStateOf(ticket.licenseNumber) }
        var driverName by remember { mutableStateOf(ticket.driverName) }
        var inWeight by remember { mutableStateOf(ticket.inWeight) }
        var outWeight by remember { mutableStateOf(ticket.outWeight) }
        val netWeight = abs(inWeight - outWeight)

        LaunchedEffect(ticket) {
            timestamp = ticket.timestamp
            license = ticket.licenseNumber
            driverName = ticket.driverName
            inWeight = ticket.inWeight
            outWeight = ticket.outWeight
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if (it.hasFocus) showDatePicker = true },
                readOnly = true,
                label = { Text("Inbound Time") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "calendar icon",
                    )
                },
                value = timestampToDate(timestamp),
                onValueChange = { /*...*/ },
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("License Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "calendar icon",
                    )
                },
                value = license,
                onValueChange = { license = it },
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Driver Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "calendar icon",
                    )
                },
                value = driverName,
                onValueChange = { driverName = it },
            )
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    label = { Text("Inbound Weight") },
                    value = inWeight.toString(),
                    onValueChange = { value ->
                        val result = value.filter { it.isDigit() }.toIntOrNull()
                        inWeight = result ?: 0
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    label = { Text("Outbound Weight") },
                    value = outWeight.toString(),
                    onValueChange = { value ->
                        val result = value.filter { it.isDigit() }.toIntOrNull()
                        outWeight = result ?: 0
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text("Net Weight") },
                value = netWeight.toString(),
                onValueChange = { /*...*/ },
            )
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                onClick = {
                    onSaveClick(
                        TicketData(
                            id = ticket.id,
                            timestamp = timestamp,
                            licenseNumber = license,
                            driverName = driverName,
                            inWeight = inWeight,
                            outWeight = outWeight,
                        )
                    )
                },
            ) {
                Text("Save")
            }
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MyModelScreen(
            ticket = TicketData(),
            onSaveClick = { /*...*/ },
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        MyModelScreen(
            ticket = TicketData(),
            onSaveClick = { /*...*/ },
        )
    }
}
