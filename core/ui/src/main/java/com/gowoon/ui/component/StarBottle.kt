package com.gowoon.ui.component

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.record.Record
import de.apuri.physicslayout.lib.PhysicsLayout
import de.apuri.physicslayout.lib.drag.DragConfig
import de.apuri.physicslayout.lib.physicsBody
import de.apuri.physicslayout.lib.simulation.rememberClock
import de.apuri.physicslayout.lib.simulation.rememberSimulation
import kotlinx.coroutines.delay

private const val COL_COUNT = 5
private const val STAR_SIZE_DP = 45

@Composable
fun StarBottle(
    modifier: Modifier = Modifier,
    records: List<Record>,
    newRecord: Record? = null,
    recordAdded: Boolean = false,
    onClickBottle: () -> Unit
) {
    Box(
        modifier = modifier
            .width(300.dp)
            .height(400.dp)
            .noRippleClickable { onClickBottle() }
    ) {
//        Image(
//            modifier = Modifier
//                .fillMaxSize()
//                .align(Alignment.Center)
//                .clip(RoundedCornerShape(65.dp)),
//            painter = painterResource(com.gowoon.designsystem.R.drawable.bottle_background),
//            contentDescription = null
//        )
        StarBottlePhysicsBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .padding(10.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(65.dp)),
            records = records,
            newRecord = if (recordAdded) newRecord else null
        )
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer { alpha = 0.7f },
            painter = painterResource(com.gowoon.designsystem.R.drawable.bottle),
            contentDescription = null
        )
    }
}

@Composable
internal fun StarBottlePhysicsBody(
    modifier: Modifier = Modifier,
    bottleShape: Shape = RoundedCornerShape(65.dp),
    records: List<Record>,
    newRecord: Record?
) {
    var show by remember { mutableStateOf(false) }
    val simulation = rememberSimulation(rememberClock())

    LaunchedEffect(true) {
        delay(1000)
        show = true
    }
    GravitySensor { (x, y) ->
        simulation.setGravity(Offset(-x, y).times(6f))
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
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
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
    size: Dp = STAR_SIZE_DP.dp,
    record: Record
) {
    Box(
        modifier = modifier
            .physicsBody(
                shape = CircleShape,
                dragConfig = DragConfig(),
            )
            .size(size)
            .background(Color.Transparent, CircleShape)
            .padding(0.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Star(
            size = size, record = record
        )
    }
}

@Composable
fun GravitySensor(
    onGravityChanged: (List<Float>) -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService<SensorManager>()
        val gravitySensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)

        val gravityListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val (x, y, z) = event.values
                onGravityChanged(listOf(x, y, z))
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }
        }

        sensorManager?.registerListener(
            gravityListener,
            gravitySensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager?.unregisterListener(gravityListener)
        }
    }
}