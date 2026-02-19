package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            placeholder = { Text("Поиск групп") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        ExposedDropdownMenu(
            expanded = expanded && filteredGroups.isNotEmpty(),
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
