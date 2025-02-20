package com.gowoon.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gowoon.model.record.Record
import com.gowoon.ui.component.Star
import de.apuri.physicslayout.lib.PhysicsLayout
import de.apuri.physicslayout.lib.drag.DragConfig
import de.apuri.physicslayout.lib.physicsBody
import de.apuri.physicslayout.lib.simulation.rememberClock
import de.apuri.physicslayout.lib.simulation.rememberSimulation
import kotlinx.coroutines.delay

private const val COL_COUNT = 5

@Composable
internal fun StarBottle(
    modifier: Modifier = Modifier,
    bottleShape: Shape = RoundedCornerShape(65.dp),
    records: List<Record>,
    newRecord: Record?
) {
    var show by remember { mutableStateOf(false) }
    val simulation = rememberSimulation(rememberClock())

    LaunchedEffect(true) {
        delay(1500)
        show = true
    }
    GravitySensor { (x, y) ->
        simulation.setGravity(Offset(-x, y).times(3f))
    }
    PhysicsLayout(
        modifier = modifier, simulation = simulation, shape = bottleShape
    ) {
        newRecord?.let {
            if (show) {
                Ball(modifier = Modifier.align(Alignment.TopCenter), record = it)
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            records.filterNot { it == newRecord }.chunked(COL_COUNT).forEach { rowItems ->
                Row {
                    rowItems.forEach { record ->
                        Ball(record = record)
                    }
                }
            }
        }
    }
}

@Composable
private fun Ball(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    record: Record
) {
    Box(
        modifier = modifier
            .physicsBody(
                shape = CircleShape,
                dragConfig = DragConfig(),
            )
            .size(size)
            .background(Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Star(
            size = size, record = record
        )
    }
}