package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.dto.LessonGroupPart
import com.example.collegeschedule.utils.formatDate
import com.example.collegeschedule.utils.formatTimeRange

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {

        items(data) { day ->

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = day.weekday,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = formatDate(day.lessonDate),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            day.lessons.forEach { lesson ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Время
                        val (startTime, endTime) = formatTimeRange(lesson.time)

                        Column(
                            modifier = Modifier.width(58.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = startTime,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = endTime,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .heightIn(min = 40.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            // Берём первый предмет как основной
                            val mainInfo = lesson.groupParts.values.firstOrNull { it != null }

                            if (mainInfo != null) {
                                Text(
                                    text = mainInfo.subject,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Подгруппы
                            lesson.groupParts.forEach { (part, info) ->
                                if (info != null) {

                                    val partLabel = when (part) {
                                        LessonGroupPart.FULL -> ""
                                        LessonGroupPart.SUB1 -> "1 подгр."
                                        LessonGroupPart.SUB2 -> "2 подгр."
                                    }

                                    if (part != LessonGroupPart.FULL) {
                                        Text(
                                            text = partLabel,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Text(
                                        text = info.teacher,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    // Выделенный кабинет
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Text(
                                            text = "${info.building}, ${info.classroom}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(
                                                horizontal = 6.dp,
                                                vertical = 2.dp
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
