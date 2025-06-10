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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.record.Record
import com.gowoon.model.reward.BottleType
import de.apuri.physicslayout.lib.PhysicsLayout
import de.apuri.physicslayout.lib.drag.DragConfig
import de.apuri.physicslayout.lib.physicsBody
import de.apuri.physicslayout.lib.simulation.rememberClock
import de.apuri.physicslayout.lib.simulation.rememberSimulation
import kotlinx.coroutines.delay

private const val COL_COUNT = 5

enum class StarBottleMode { Default, Preview }

@Composable
fun StarBottle(
    modifier: Modifier = Modifier,
    starBottleMode: StarBottleMode = StarBottleMode.Default,
    bottleType: BottleType,
    records: List<Record>,
    newRecord: Record? = null,
    recordAdded: Boolean = false,
    onChangeDiff: (Float) -> Unit = {},
    onClickBottle: () -> Unit
) {
    key(bottleType) {
        val resource = when (bottleType) {
            BottleType.DEFAULT -> com.gowoon.designsystem.R.drawable.bottle
            BottleType.CIRCLE -> com.gowoon.designsystem.R.drawable.bottle_circle
            BottleType.HEART -> com.gowoon.designsystem.R.drawable.bottle_heart
        }
        val shape = when (bottleType) {
            BottleType.DEFAULT -> {
                when (starBottleMode) {
                    StarBottleMode.Default -> {
                        RoundedCornerShape(65.dp)
                    }

                    StarBottleMode.Preview -> {
                        RoundedCornerShape(50.dp)
                    }
                }
            }

            BottleType.CIRCLE -> {
                CircleShape
            }

            BottleType.HEART -> {
                when (starBottleMode) {
                    StarBottleMode.Default -> {
                        CutCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 135.dp,
                            bottomEnd = 135.dp
                        )
                    }

                    StarBottleMode.Preview -> {
                        CutCornerShape(
                            topStart = 25.dp,
                            topEnd = 25.dp,
                            bottomStart = 90.dp,
                            bottomEnd = 90.dp
                        )
                    }
                }
            }
        }
        val shapeModifier = when (bottleType) {
            BottleType.DEFAULT -> {
                when (starBottleMode) {
                    StarBottleMode.Default -> {
                        Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp)
                            .padding(horizontal = 10.dp)
                            .padding(10.dp)
                    }

                    StarBottleMode.Preview -> {
                        Modifier
                            .fillMaxSize()
                            .padding(top = 30.dp)
                            .padding(horizontal = 10.dp)
                            .padding(5.dp)
                    }
                }
            }

            BottleType.CIRCLE -> {
                when (starBottleMode) {
                    StarBottleMode.Default -> {
                        Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(10.dp)
                    }

                    StarBottleMode.Preview -> {
                        Modifier
                            .padding(top = 13.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(8.dp)
                    }
                }
            }

            BottleType.HEART -> {
                when (starBottleMode) {
                    StarBottleMode.Default -> {
                        Modifier
                            .padding(top = 65.dp, bottom = 45.dp, start = 25.dp, end = 25.dp)
                            .fillMaxSize()
                    }

                    StarBottleMode.Preview -> {
                        Modifier
                            .padding(top = 45.dp, bottom = 30.dp, start = 15.dp, end = 15.dp)
                            .fillMaxSize()
                    }
                }
            }
        }
        val sizeModifier = when (starBottleMode) {
            StarBottleMode.Default -> Modifier
                .width(330.dp)
                .height(400.dp)

            StarBottleMode.Preview -> Modifier
                .width(215.dp)
                .height(260.dp)
        }
        Box(
            modifier = modifier
                .then(sizeModifier)
                .noRippleClickable { onClickBottle() }
        ) {
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(resource),
                contentDescription = null
            )
            StarBottlePhysicsBody(
                modifier = shapeModifier.then(
                    Modifier.background(
                        color = Color.Transparent,
                        shape = shape
                    )
                ),
                bottleMode = starBottleMode,
                bottleShape = shape,
                starSize = when (starBottleMode) {
                    StarBottleMode.Default -> {
                        when (bottleType) {
                            BottleType.HEART -> 35.dp
                            else -> 40.dp
                        }
                    }

                    StarBottleMode.Preview -> {
                        when (bottleType) {
                            BottleType.HEART -> 20.dp
                            else -> 23.dp
                        }
                    }
                },
                records = records,
                newRecord = if (recordAdded) newRecord else null,
                onChangeDiff = onChangeDiff
            )
        }
    }
}

@Composable
internal fun StarBottlePhysicsBody(
    modifier: Modifier = Modifier,
    bottleMode: StarBottleMode,
    bottleShape: Shape,
    starSize: Dp,
    records: List<Record>,
    newRecord: Record?,
    onChangeDiff: (Float) -> Unit
) {
    var show by remember { mutableStateOf(false) }
    val simulation = rememberSimulation(rememberClock())

    var lastHandledTime by remember { mutableStateOf(0L) }
    var preOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(true) {
        delay(1000)
        show = true
    }
    GravitySensor { (x, y) ->
        simulation.setGravity(Offset(-x, y).times(6f))
        val now = System.currentTimeMillis()
        if (now - lastHandledTime >= 1000L) {
            lastHandledTime = now
            
            val new = Offset(x, y)
            val diff = (preOffset - new).getDistance()
            preOffset = new
            onChangeDiff(diff)
        }
    }
    PhysicsLayout(
        modifier = modifier, simulation = simulation, shape = bottleShape
    ) {
        newRecord?.let {
            if (show) {
                Ball(modifier = Modifier.align(Alignment.TopCenter), record = it, size = starSize)
            }
        }
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = if (bottleShape is RoundedCornerShape) {
                        20.dp
                    } else {
                        when (bottleMode) {
                            StarBottleMode.Default -> 50.dp
                            StarBottleMode.Preview -> 30.dp
                        }
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            records.filterNot { it == newRecord }.chunked(COL_COUNT).forEach { rowItems ->
                Row {
                    rowItems.forEach { record ->
                        Ball(record = record, size = starSize)
                    }
                }
            }
        }
    }
}

@Composable
private fun Ball(
    modifier: Modifier = Modifier,
    size: Dp,
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
