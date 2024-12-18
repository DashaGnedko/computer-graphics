import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import java.awt.Toolkit

@Composable
fun App() {
    var red by remember { mutableStateOf(255f) }
    var green by remember { mutableStateOf(255f) }
    var blue by remember { mutableStateOf(255f) }

    var cyan by remember { mutableStateOf(0f) }
    var magenta by remember { mutableStateOf(0f) }
    var yellow by remember { mutableStateOf(0f) }
    var black by remember { mutableStateOf(0f) }

    var hue by remember { mutableStateOf(0f) }
    var saturation by remember { mutableStateOf(0f) }
    var lightness by remember { mutableStateOf(0f) }

    var isUserUpdatingRgb by remember { mutableStateOf(false) }
    var isUserUpdatingCmyk by remember { mutableStateOf(true) }
    var isUserUpdatingHsl by remember { mutableStateOf(false) }

    LaunchedEffect(red, green, blue) {
        if (isUserUpdatingRgb) {
            isUserUpdatingRgb = false
            isUserUpdatingCmyk = false
            isUserUpdatingHsl = false

            val cmyk = rgbToCmyk(red.toInt(), green.toInt(), blue.toInt())
            cyan = cmyk[0] * 100
            magenta = cmyk[1] * 100
            yellow = cmyk[2] * 100
            black = cmyk[3] * 100

            val hsl = rgbToHsl(red, green, blue)
            hue = hsl[0] * 100
            saturation = hsl[1] * 100
            lightness = hsl[2] * 100
        }
    }

    LaunchedEffect(cyan, magenta, yellow, black) {
        if (isUserUpdatingCmyk) {
            isUserUpdatingRgb = false
            isUserUpdatingCmyk = false
            isUserUpdatingHsl = false

            val rgb = cmykToRgb(cyan / 100, magenta / 100, yellow / 100, black / 100)
            red = rgb[0].toFloat()
            green = rgb[1].toFloat()
            blue = rgb[2].toFloat()

            val hsl = rgbToHsl(red, green, blue)
            hue = hsl[0] * 100
            saturation = hsl[1] * 100
            lightness = hsl[2] * 100
        }
    }

    LaunchedEffect(hue, saturation, lightness) {
        if (isUserUpdatingHsl) {
            isUserUpdatingRgb = false
            isUserUpdatingCmyk = false
            isUserUpdatingHsl = false

            val rgb = hslToRgb(hue / 100f, saturation / 100f, lightness / 100f)
            red = rgb[0].toFloat()
            green = rgb[1].toFloat()
            blue = rgb[2].toFloat()

            val cmyk = rgbToCmyk(red.toInt(), green.toInt(), blue.toInt())
            cyan = cmyk[0] * 100
            magenta = cmyk[1] * 100
            yellow = cmyk[2] * 100
            black = cmyk[3] * 100
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Red:")
                TextField(
                    value = red.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingRgb = true; red = updateValue(red, newValue, 0, 255)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 41.dp).width(60.dp)
                )
                Slider(
                    value = red, onValueChange = { newvalue -> isUserUpdatingRgb = true; red = newvalue },
                    valueRange = 0f..255f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Red,
                        activeTrackColor = Color.Red,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }

        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Green:")
                TextField(
                    value = green.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingRgb = true; green = updateValue(green, newValue, 0, 255)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 28.dp).width(60.dp)
                )
                Slider(
                    value = green, onValueChange = { newvalue -> isUserUpdatingRgb = true; green = newvalue },
                    valueRange = 0f..255f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Green,
                        activeTrackColor = Color.Green,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }

        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Blue:")
                TextField(
                    value = blue.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingRgb = true; blue = updateValue(blue, newValue, 0, 255)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 38.dp).width(60.dp)
                )
                Slider(
                    value = blue, onValueChange = { newvalue -> isUserUpdatingRgb = true; blue = newvalue },
                    valueRange = 0f..255f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Blue,
                        activeTrackColor = Color.Blue,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Cyan:")
                TextField(
                    value = cyan.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingCmyk = true; cyan = updateValue(cyan, newValue, 0, 100)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 35.dp).width(60.dp)
                )
                Slider(
                    value = cyan, onValueChange = { newvalue -> isUserUpdatingCmyk = true; cyan = newvalue },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Cyan,
                        activeTrackColor = Color.Cyan,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }
        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Magenta:")
                TextField(
                    value = magenta.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingCmyk = true; magenta = updateValue(magenta, newValue, 0, 100)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 10.dp).width(60.dp)
                )
                Slider(
                    value = magenta, onValueChange = { newvalue -> isUserUpdatingCmyk = true; magenta = newvalue },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Magenta,
                        activeTrackColor = Color.Magenta,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }
        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Yellow:")
                TextField(
                    value = yellow.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingCmyk = true; yellow = updateValue(yellow, newValue, 0, 100)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 26.dp).width(60.dp)
                )
                Slider(
                    value = yellow, onValueChange = { newvalue -> isUserUpdatingCmyk = true; yellow = newvalue },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Yellow,
                        activeTrackColor = Color.Yellow,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }
        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Black:")
                TextField(
                    value = black.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingCmyk = true; black = updateValue(black, newValue, 0, 100)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 33.dp).width(60.dp)
                )
                Slider(
                    value = black, onValueChange = { newvalue -> isUserUpdatingCmyk = true; black = newvalue },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.Gray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Hue:")
                TextField(
                    value = hue.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingHsl = true; hue = updateValue(hue, newValue, 0, 100)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 40.dp).width(60.dp)
                )
                Slider(
                    value = hue, onValueChange = { newvalue -> isUserUpdatingHsl = true; hue = newvalue },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(inactiveTrackColor = Color.Gray)
                )
            }
        }
        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Lightness:")
                TextField(
                    value = lightness.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingHsl = true; lightness = updateValue(lightness, newValue, 0, 100)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 8.dp).width(60.dp)
                )
                Slider(
                    value = lightness, onValueChange = { newvalue -> isUserUpdatingHsl = true; lightness = newvalue },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(inactiveTrackColor = Color.Gray)
                )
            }
        }
        Column(modifier = Modifier.padding(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Saturation:")
                TextField(
                    value = saturation.toInt().toString(), singleLine = true,
                    onValueChange = { newValue: String ->
                        isUserUpdatingHsl = true; saturation = updateValue(saturation, newValue, 0, 100)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(start = 3.dp).width(60.dp)
                )
                Slider(
                    value = saturation, onValueChange = { newvalue -> isUserUpdatingHsl = true; saturation = newvalue },
                    valueRange = 0f..100f, modifier = Modifier.fillMaxWidth(0.5f),
                    colors = SliderDefaults.colors(inactiveTrackColor = Color.Gray)
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(end = 250.dp)
                .size(150.dp)
                .background(color = Color(red.toInt(), green.toInt(), blue.toInt()))
                .align(Alignment.CenterEnd)
                .border(BorderStroke(1.dp, Color.Black))
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp, end = 70.dp), horizontalArrangement = Arrangement.End) {
            var r = 0
            val step = 25
            for (i in 1..10) {
                r += step
                ColorBox(color = Color(r, 0, 0)) { color ->
                    red = color.red * 255
                    green = color.green * 255
                    blue = color.blue * 255
                    isUserUpdatingRgb = true
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(end = 70.dp), horizontalArrangement = Arrangement.End) {
            var g = 0
            val step = 25
            for (i in 1..10) {
                g += step
                ColorBox(color = Color(0, g, 0)) { color ->
                    red = color.red * 255
                    green = color.green * 255
                    blue = color.blue * 255
                    isUserUpdatingRgb = true
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(end = 70.dp), horizontalArrangement = Arrangement.End) {
            var b = 0
            val step = 25
            for (i in 1..10) {
                b += step
                ColorBox(color = Color(0, 0, b)) { color ->
                    red = color.red * 255
                    green = color.green * 255
                    blue = color.blue * 255
                    isUserUpdatingRgb = true
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(end = 70.dp), horizontalArrangement = Arrangement.End) {
                var b = 0
                val step = 25
                for (i in 1..10) {
                    b += step
                    ColorBox(color = Color(255, 255, b)) { color ->
                        red = color.red * 255
                        green = color.green * 255
                        blue = color.blue * 255
                        isUserUpdatingRgb = true
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(end = 70.dp), horizontalArrangement = Arrangement.End) {
                var b = 0
                val step = 25
                for (i in 1..10) {
                    b += step
                    ColorBox(color = Color(0, 255, b)) { color ->
                        red = color.red * 255
                        green = color.green * 255
                        blue = color.blue * 255
                        isUserUpdatingRgb = true
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(end = 70.dp), horizontalArrangement = Arrangement.End) {
                var b = 0
                val step = 25
                for (i in 1..10) {
                    b += step
                    ColorBox(color = Color(b, 0, 255)) { color ->
                        red = color.red * 255
                        green = color.green * 255
                        blue = color.blue * 255
                        isUserUpdatingRgb = true
                    }
                }
            }
        }
    }
}

@Composable
fun ColorBox(color: Color, onClick: (Color) -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color)
            .border(BorderStroke(0.5.dp, Color.Black))
            .clickable {
                onClick(color)
            }
    )
}

fun rgbToCmyk(r: Int, g: Int, b: Int): FloatArray {
    val rNorm = r / 255.0f
    val gNorm = g / 255.0f
    val bNorm = b / 255.0f

    val k = 1 - maxOf(rNorm, gNorm, bNorm)
    val c = if (k == 1f) 0f else (1 - rNorm - k) / (1 - k)
    val m = if (k == 1f) 0f else (1 - gNorm - k) / (1 - k)
    val y = if (k == 1f) 0f else (1 - bNorm - k) / (1 - k)

    return floatArrayOf(c, m, y, k)
}

fun cmykToRgb(c: Float, m: Float, y: Float, k: Float): IntArray {
    val r = (255 * (1 - c) * (1 - k)).toInt()
    val g = (255 * (1 - m) * (1 - k)).toInt()
    val b = (255 * (1 - y) * (1 - k)).toInt()
    return intArrayOf(r, g, b)
}

fun hueToRgb(p: Float, q: Float, t1: Float) : Float {
    var t = t1
    if (t < 0f) t += 1
    if (t > 1f) t -= 1
    if (t < 1f/6f) return p + (q - p) * 6 * t
    if (t < 1f/2f) return q
    if (t < 2f/3f) return p + (q - p) * (2f/3f - t) * 6
    return p
}

fun hslToRgb(h: Float, s: Float, l: Float): IntArray {
    var r: Float = 0f
    var g: Float = 0f
    var b: Float = 0f

    if (s == 0f) {
        r = l
        g = l
        b = l
    } else {
        val q = if (l < 0.5) l * (1 + s) else l + s - l * s
        val p = 2 * l - q
        r = hueToRgb(p, q, h + 1f/3f)
        g = hueToRgb(p, q, h)
        b = hueToRgb(p, q, h - 1f/3f)
    }

    return intArrayOf((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())
}

fun rgbToHsl(red: Float, green: Float, blue: Float): FloatArray {
    val r: Float = red / 255f
    val g: Float = green / 255f
    val b: Float = blue / 255f
    val vmax: Float = maxOf(r, g, b)
    val vmin: Float = minOf(r, g, b)
    var h: Float = 0f
    var s: Float = 0f
    val l: Float = (vmax + vmin) / 2f

    if (vmax == vmin) {
        return floatArrayOf(0f, 0f, l)
    }

    val d: Float = vmax - vmin;
    s = if (l > 0.5f) d / (2 - vmax - vmin) else d / (vmax + vmin)
    if (vmax == r) h = (g - b) / d + (if (g < b) 6 else 0)
    if (vmax == g) h = (b - r) / d + 2
    if (vmax == b) h = (r - g) / d + 4
    h /= 6f

    return floatArrayOf(h, s, l)
}

fun updateValue(was: Float, newValue: String, left: Int, right: Int): Float {
    var result = was
    try {
        val num = newValue.toFloat()
        if (left <= num && num <= right && num - num.toInt() == 0f) {
            result = num
        } else {
            throw IllegalArgumentException("Wrong value")
        }
    } catch (e: Exception) {
    }
    return result
}


fun main() = application {
    val screenSize = Toolkit.getDefaultToolkit().screenSize

    val windowState = rememberWindowState(
        width = 1320.dp,
        height = 750.dp
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Color Picker"
    ) {
        val density = LocalDensity.current

        val minWidthPx = with(density) { 1320.dp.toPx().toInt() }
        val minHeightPx = with(density) { 750.dp.toPx().toInt() }
        window.minimumSize = Dimension(minWidthPx, minHeightPx)

        val maxWidthPx = with(density) { 1520.dp.toPx().toInt() }
        val maxHeightPx = with(density) { 850.dp.toPx().toInt() }
        window.maximumSize = Dimension(maxWidthPx, maxHeightPx)

        App()
    }
}
