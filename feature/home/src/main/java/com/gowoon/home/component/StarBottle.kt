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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gowoon.model.record.BadCategory
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.model.record.Record
import com.gowoon.ui.component.Star
import de.apuri.physicslayout.lib.PhysicsLayout
import de.apuri.physicslayout.lib.drag.DragConfig
import de.apuri.physicslayout.lib.physicsBody
import de.apuri.physicslayout.lib.simulation.rememberClock
import de.apuri.physicslayout.lib.simulation.rememberSimulation

@Composable
internal fun StarBottle(
    modifier: Modifier = Modifier,
    bottleShape: Shape = RoundedCornerShape(65.dp),
    records: List<Record>
) {
    val simulation = rememberSimulation(rememberClock())
    GravitySensor { (x, y) ->
        simulation.setGravity(Offset(-x, y).times(3f))
    }
    val columns = 5
    PhysicsLayout(
        modifier = modifier,
        simulation = simulation,
        shape = bottleShape
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            records.chunked(columns).forEach { rowItems ->
                Row {
                    rowItems.forEach { item ->
                        Ball(record = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun Ball(
    size: Dp = 50.dp,
    record: Record
) {
    Box(
        modifier = Modifier
            .physicsBody(
                shape = CircleShape,
                dragConfig = DragConfig(),
            )
            .size(size)
            .background(Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Star(
            size = size,
            record = record
        )
    }
}

@Preview
@Composable
private fun StarBottlePreview() {
    StarBottle(
        modifier = Modifier.fillMaxSize(),
        records = listOf(
            Record.NoConsumption(),
            Record.ConsumptionRecord(
                goodRecord = Consumption(ConsumptionType.GOOD, GoodCategory.ENERGY, "아아"),
                badRecord = Consumption(ConsumptionType.BAD, BadCategory.LAZINESS, "아아")
            ),
            Record.ConsumptionRecord(
                goodRecord = Consumption(ConsumptionType.GOOD, GoodCategory.FLEX, "아아"),
                badRecord = Consumption(ConsumptionType.BAD, BadCategory.ADDICTION, "아아")
            ),
            Record.ConsumptionRecord(
                goodRecord = Consumption(ConsumptionType.GOOD, GoodCategory.HEALTH, "아아"),
                badRecord = Consumption(ConsumptionType.BAD, BadCategory.OVERFRUGALITY, "아아")
            ),
            Record.ConsumptionRecord(
                goodRecord = Consumption(ConsumptionType.GOOD, GoodCategory.DIGNITY, "아아"),
                badRecord = Consumption(ConsumptionType.BAD, BadCategory.BOASTFULNESS, "아아")
            ),
            Record.NoConsumption(),
            Record.ConsumptionRecord(
                goodRecord = Consumption(ConsumptionType.GOOD, GoodCategory.GROWTH, "아아"),
                badRecord = Consumption(ConsumptionType.BAD, BadCategory.HABIT, "아아")
            ),
        )
    )
}