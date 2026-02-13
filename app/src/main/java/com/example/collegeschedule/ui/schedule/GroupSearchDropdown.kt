package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSearchDropdown(
    groups: List<String>,
    selectedGroup: String?,
    onGroupSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(selectedGroup ?: "") }

    val filteredGroups = groups.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                expanded = true
            },
            label = { Text("Выберите группу") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredGroups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group) },
                    onClick = {
                        searchQuery = group
                        onGroupSelected(group)
                        expanded = false
                    }
                )
            }
        }
    }
}
