package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.utils.getWeekDateRange

@Composable
fun ScheduleScreen() {

    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val (start, end) = getWeekDateRange()

    // Загрузка списков групп при старте
    LaunchedEffect(Unit) {
        try {
            groups = RetrofitInstance.api.getGroups()
        } catch (e: Exception) {
            error = e.message
        }
    }

    // Загрузка расписания при выборе группы
    LaunchedEffect(selectedGroup) {
        selectedGroup?.let { group ->
            loading = true
            error = null
            try {
                schedule = RetrofitInstance.api.getSchedule(group, start, end)
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    Column {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                expanded = true
            },
            label = { Text("Поиск группы") }
        )

        val filteredGroups = groups.filter {
            it.contains(searchQuery, ignoreCase = true)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredGroups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group) },
                    onClick = {
                        selectedGroup = group
                        searchQuery = group
                        expanded = false
                    }
                )
            }
        }

        when {
            loading -> CircularProgressIndicator()
            error != null -> Text("Ошибка: $error")
            selectedGroup != null -> ScheduleList(schedule)
        }
    }
}