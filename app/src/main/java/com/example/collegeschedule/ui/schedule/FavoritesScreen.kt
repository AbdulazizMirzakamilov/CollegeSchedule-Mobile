package com.example.collegeschedule.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.utils.FavoritesManager

@Composable
fun FavoritesScreen(
    onGroupSelected: (String) -> Unit
) {

    val context = LocalContext.current
    var favorites by remember {
        mutableStateOf(FavoritesManager.getFavorites(context).toList())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (favorites.isEmpty()) {
            Text("Нет избранных групп")
        } else {
            LazyColumn {
                items(favorites) { group ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = group,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onGroupSelected(group)
                                }
                        )

                        IconButton(
                            onClick = {
                                FavoritesManager.removeFavorite(context, group)
                                favorites =
                                    FavoritesManager.getFavorites(context).toList()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Remove"
                            )
                        }
                    }
                }
            }
        }
    }
}
