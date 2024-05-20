import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ScrollableFeed(
    topAppBarContent: @Composable () -> Unit,
    bottomNavBarContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    // State för att hålla koll på scrollstatus
    val listState = rememberLazyListState()
    val isUserScrollingDown = remember { mutableStateOf(false) }
    var previousIndex by remember { mutableIntStateOf(0) }
    var previousOffset by remember { mutableIntStateOf(0) }

    // Övervaka rullningsstatus för att avgöra om användaren rullar ner
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                isUserScrollingDown.value = if (index != previousIndex) {
                    index > previousIndex
                } else {
                    offset > previousOffset
                }
                previousIndex = index
                previousOffset = offset
            }
    }

    // Layoutcontainer som fyller hela skärmen och anpassar sig till systemfält
    Box(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Lista som fyller hela skärmen och justerar padding baserat på scrollstatus
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = if (isUserScrollingDown.value) 0.dp else 56.dp,
                    bottom = if (isUserScrollingDown.value) 56.dp else 0.dp
                )
        ) {
            items(100) {
                content()
            }
        }
        // Anpassningsbar TopAppBar
        ScrollableTopAppBar(
            isUserScrollingDown = isUserScrollingDown.value,
            content = topAppBarContent
        )
        // Anpassningsbar BottomNavBar
        ScrollableBottomNavBar(
            isUserScrollingDown = isUserScrollingDown.value,
            content = bottomNavBarContent,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun topAppBarVisibility(isUserScrollingDown: Boolean): Pair<Float, Float> {
    // Animera transparens och översättning för topp-appen
    val alpha by animateFloatAsState(
        targetValue = if (isUserScrollingDown) 0f else 1f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing), label = ""
    )
    val translationY by animateFloatAsState(
        targetValue = if (isUserScrollingDown) -100f else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing), label = ""
    )

    return alpha to translationY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableTopAppBar(
    isUserScrollingDown: Boolean,
    content: @Composable () -> Unit
) {
    val (alpha, translationY) = topAppBarVisibility(isUserScrollingDown)

    // TopAppBar med dynamisk transparens och översättning
    TopAppBar(
        title = { content() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
        ),
        modifier = Modifier
            .graphicsLayer {
                this.alpha = alpha
                this.translationY = translationY
            }
            .shadow(8.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFF3F51B5), Color(0xFF2196F3))))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    )
}

@Composable
fun bottomNavBarVisibility(isUserScrollingDown: Boolean): Pair<Float, Float> {
    // Animera transparens och översättning för bottennavigeringsfältet
    val alpha by animateFloatAsState(
        targetValue = if (isUserScrollingDown) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing), label = ""
    )
    val translationY by animateFloatAsState(
        targetValue = if (isUserScrollingDown) 0f else 100f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing), label = ""
    )

    return alpha to translationY
}

@Composable
fun ScrollableBottomNavBar(
    modifier: Modifier = Modifier,
    isUserScrollingDown: Boolean,
    content: @Composable () -> Unit
) {
    val (alpha, translationY) = bottomNavBarVisibility(isUserScrollingDown)

    // BottomNavigation med dynamisk transparens och översättning
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                this.translationY = translationY
            }
            .shadow(8.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFF3F51B5), Color(0xFF2196F3))))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        content()
    }
}

@Composable
fun MainScreen() {
    ScrollableFeed(
        topAppBarContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* TODO: Hantera tillbaka-knappen */ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tillbaka", tint = Color.White)
                }
                Text("Top App Bar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { /* TODO: Hantera menyn */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Meny", tint = Color.White)
                }
            }
        },
        bottomNavBarContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { /* TODO: Hantera hem-knappen */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Hem", tint = Color.White)
                }
                IconButton(onClick = { /* TODO: Hantera sökning */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Sök", tint = Color.White)
                }
                IconButton(onClick = { /* TODO: Hantera inställningar */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Inställningar", tint = Color.White)
                }
            }
        },
        content = {
            // Innehållet i flödet
            repeat(100) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFF3F4F6),
                                    Color(0xFFE0E0E0)
                                )
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Ikon",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Item $index",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun ScrollPreview() {
    MainScreen()
}
