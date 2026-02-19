package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.utils.getWeekDateRange
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.collegeschedule.utils.FavoritesManager


@Composable
fun ScheduleScreen(initialGroup: String? = null) {

    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedGroup by rememberSaveable { mutableStateOf(initialGroup) }

    val context = LocalContext.current
    val (start, end) = getWeekDateRange()

    LaunchedEffect(Unit) {
        try {
            groups = RetrofitInstance.api.getGroups()
        } catch (e: Exception) {
            error = e.message
        }
    }

    LaunchedEffect(initialGroup) {
        selectedGroup = initialGroup
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // Поиск групп
        GroupSearchDropdown(
            groups = groups,
            selectedGroup = selectedGroup,
            onGroupSelected = { group ->
                selectedGroup = group
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Название группы
        selectedGroup?.let { group ->

            var isFav by remember(group) {
                mutableStateOf(FavoritesManager.isFavorite(context, group))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = group,
                    style = MaterialTheme.typography.headlineMedium
                )

                IconButton(
                    onClick = {
                        if (isFav) {
                            FavoritesManager.removeFavorite(context, group)
                        } else {
                            FavoritesManager.addFavorite(context, group)
                        }
                        isFav = !isFav
                    }
                ) {
                    Icon(
                        imageVector = if (isFav)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Text(
                    text = "Ошибка: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            selectedGroup != null -> {
                ScheduleList(schedule)
            }
        }
    }
}


