package android.template.feature.weighbridge.ui

import android.template.core.data.model.TicketData
import android.template.core.ui.MyApplicationTheme
import android.template.core.ui.timestampToDate
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlin.math.abs

@Composable
fun WeightBridgeListScreen(
    onNavigateToAddEditScreen: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeighBridgeListViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val items by produceState<WeighBridgeListUiState>(
        initialValue = WeighBridgeListUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadTicketData()
    }

    if (items is WeighBridgeListUiState.Success) {
        WeightBridgeListScreen(
            items = (items as WeighBridgeListUiState.Success).data,
            onAddTicketClick = {
                onNavigateToAddEditScreen(0)
            },
            onEditTicketClick = {
                onNavigateToAddEditScreen(it.id)
            },
            onFilterChanged = { query, isAsc ->
                Log.d("DATA_DATA", "$query + $isAsc")
                viewModel.filterData(query, isAsc)
            },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun WeightBridgeListScreen(
    items: List<TicketData>,
    onAddTicketClick: () -> Unit,
    onEditTicketClick: (TicketData) -> Unit,
    onFilterChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    var isAscending by remember { mutableStateOf(false) }
    val rotationUp = 90f
    val rotationDown = -90f

    var showTicketDetail by remember { mutableStateOf(false) }
    var selectedTicket: TicketData by remember { mutableStateOf(TicketData()) }

    if (showTicketDetail) {
        Dialog(
            onDismissRequest = { showTicketDetail = false }
        ) {
            DetailTicketCard(selectedTicket)
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Ticket List")
                },
                actions = {
                    IconButton(
                        onClick = onAddTicketClick,
                        content = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add",
                                tint = Color.Black,
                            )
                        }
                    )
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            stickyHeader {
                Row {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search",
                                tint = Color.Black,
                            )
                        },
                        singleLine = true,
                        value = query,
                        onValueChange = {
                            query = it
                            onFilterChanged(it, isAscending)
                        }
                    )
                    IconButton(
                        onClick = {
                            isAscending = !isAscending
                            onFilterChanged(query, isAscending)
                        },
                        content = {
                            Icon(
                                modifier = Modifier.rotate(
                                    if (isAscending) rotationUp
                                    else rotationDown
                                ),
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "sort",
                                tint = Color.Black,
                            )
                        }
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
            items(items) { ticket ->
                Spacer(Modifier.height(8.dp))
                SmallTicketCard(
                    ticket = ticket,
                    onCardClick = {
                        selectedTicket = ticket
                        showTicketDetail = true
                    },
                    onEditTicketClick = { onEditTicketClick(ticket) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SmallTicketCard(
    ticket: TicketData,
    onCardClick: () -> Unit,
    onEditTicketClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White,
        ),
        onClick = onCardClick,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "${ticket.licenseNumber} | ${ticket.driverName}",
                    color = Color.Black,
                    fontWeight = FontWeight(800),
                    fontSize = 16.sp,
                )
                Spacer(Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "datetime",
                        tint = Color.Black,
                    )
                    Text(
                        text = timestampToDate(ticket.timestamp),
                        color = Color.Black,
                    )
                }
            }
            IconButton(
                modifier = Modifier.size(18.dp),
                onClick = onEditTicketClick,
                content = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "edit",
                        tint = Color.Black,
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailTicketCard(
    ticket: TicketData,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Date: ${timestampToDate(ticket.timestamp)}",
                    color = Color.Black,
                )
                Text(
                    text = "Driver Name: ${ticket.driverName}",
                    color = Color.Black,
                )
                Text(
                    text = "License Number: ${ticket.licenseNumber}",
                    color = Color.Black,
                )
                Text(
                    text = "Inbound Weight: ${ticket.inWeight}",
                    color = Color.Black,
                )
                Text(
                    text = "Outbound Weight: ${ticket.outWeight}",
                    color = Color.Black,
                )
                Text(
                    text = "Net Weight: ${abs(ticket.inWeight - ticket.outWeight)}",
                    color = Color.Black,
                )
            }
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
private fun DetailTicketCardPreview() {
    MyApplicationTheme {
        SmallTicketCard(
            ticket = TicketData(
                timestamp = 1695252688,
                licenseNumber = "B 1278 AAR",
                driverName = "John Doe",
                inWeight = 6852,
                outWeight = 1200,
            ),
            onCardClick = { /*...*/ },
            onEditTicketClick = { /*...*/ },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SmallTicketCardPreview() {
    MyApplicationTheme {
        DetailTicketCard(
            ticket = TicketData(
                timestamp = 1695252688,
                licenseNumber = "B 1278 AAR",
                driverName = "John Doe",
                inWeight = 6852,
                outWeight = 1200,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        WeightBridgeListScreen(
            getTicketDataList(),
            onAddTicketClick = { /*...*/ },
            onEditTicketClick = { /*...*/ },
            onFilterChanged = { _, _ -> /*...*/ },
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        WeightBridgeListScreen(
            getTicketDataList(),
            onAddTicketClick = { /*...*/ },
            onEditTicketClick = { /*...*/ },
            onFilterChanged = { _, _ -> /*...*/ },
        )
    }
}

private fun getTicketDataList(): List<TicketData> {
    val list = mutableListOf<TicketData>()
    repeat(15) {
        list.add(
            TicketData(
                timestamp = 1695252688,
                licenseNumber = "B 1278 AAR",
                driverName = "John Doe",
                inWeight = 6852,
                outWeight = 1200,
            )
        )
    }
    return list
}