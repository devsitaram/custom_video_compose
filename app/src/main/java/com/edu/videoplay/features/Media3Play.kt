@file:Suppress("NAME_SHADOWING")

package com.edu.videoplay.features

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.Space
import android.widget.Toast
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.ClosedCaptionOff
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun Media3Play() {
    VideoPlayer()
}

@Composable
fun VideoPlayer(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity

    var iconVisible by remember { mutableStateOf(false) }

    val videoUrls = mutableListOf(
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        // Add more video URLs here
    )

    var currentVideoIndex by remember { mutableIntStateOf(0) }
    val currentVideoUrl = videoUrls[currentVideoIndex]
    // exoplayer
    val exoPlayer = remember {
        val currentVideoUrl = Uri.parse(currentVideoUrl)
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
                MediaItem.fromUri(currentVideoUrl)
            )
            prepare()
            playWhenReady = true
        }
    }

    // Fullscreen state
    var convertScreen by remember { mutableStateOf(false) }
    val landScape: () -> Unit = {
        if (convertScreen) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        convertScreen = !convertScreen
    }


    // setting
    var showSettingItem by remember { mutableStateOf(false) }
    var settingAction by remember { mutableStateOf(false) }

    // Start playing by default
    var isPlaying by remember { mutableStateOf(true) }
    var playbackState by remember { mutableIntStateOf(0) }

    var isSwitch by remember { mutableStateOf(true) }
    if (playbackState == Player.STATE_ENDED) {
        if (isSwitch) {
            if (currentVideoIndex == videoUrls.size-1) {
                isSwitch = false
            } else {
                currentVideoIndex++
                exoPlayer.setMediaItem(MediaItem.fromUri(videoUrls[currentVideoIndex]))
                exoPlayer.prepare()
                exoPlayer.play()
                Toast.makeText(context, "Next Video Play", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var isLoading by remember { mutableStateOf(false) } // To indicate if the video is loading
    val durationSecond = exoPlayer.duration // Total duration in seconds
    val currentPositions = exoPlayer.currentPosition // Current playback position in seconds

    exoPlayer.addListener(
        object : Player.Listener {
//            override fun onIsPlayingChanged(isPlayingValue: Boolean) {
//                isPlaying = isPlayingValue
//            }

            override fun onPlaybackStateChanged(state: Int) {
                playbackState = state
//                isLoading = state == Player.STATE_BUFFERING
            }

            @SuppressLint("UnsafeOptInUsageError")
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE) {
                    // Video playback has ended or couldn't start
                    // Implement your logic here to show the indicator
                    Toast.makeText(context, "Hello Hello", Toast.LENGTH_LONG).show()
                }
            }
        }
    )

    LaunchedEffect(iconVisible, showSettingItem) {
        if (iconVisible) {
            delay(4000) // Delay for 5 seconds
            iconVisible = false // Hide the icon after the delay
        }
        if (showSettingItem) {
            delay(4000) // Delay for 5 seconds
            showSettingItem = false // hide the setting items
        }
    }

    DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }

    // snackBar show message
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(275.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier//.height(275.dp)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false // default icon buttons off
                        layoutParams = FrameLayout.LayoutParams(
                            MATCH_PARENT, WRAP_CONTENT,
                        )
                    }
                },
                modifier = modifier
                    .clickable {
                        iconVisible = !iconVisible
                    }
            )

            // action icons visibility
            if (iconVisible) {
                Column(
                    modifier = Modifier.height(275.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // top action icons
                    TopIconsActionView(
                        // auto play action
                        imageVector = if (isSwitch) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        autoPlay = {
                            isSwitch = !isSwitch
                        },
                        // caption
                        onCaption = {
                            scope.launch {
                                snackBarHostState.showSnackbar("Hello SnackBar")
                            }
                        },
                        // settings
                        onSettingClick = {
                            settingAction = !settingAction
                            showSettingItem = true
                        },
                    )
                    // pause, stop, back, skip, replay
                    CenterPlayActionView(
                        // play and stop
                        isPlaying = isPlaying,
                        onPlayAndPause = {
                            isPlaying = if (isPlaying) {
                                exoPlayer.pause()
                                !isPlaying
                            } else {
                                exoPlayer.play()
                                !isPlaying
                            }
                        },
                        // back to previous
                        onSkipPrevious = {
                            if (currentVideoIndex > 0) {
                                currentVideoIndex--
                                exoPlayer.setMediaItem(MediaItem.fromUri(videoUrls[currentVideoIndex]))
                                exoPlayer.prepare()
                                exoPlayer.play()
                                isPlaying = true
                            }
                        },
                        // next video play
                        onSkipNext = {
                            if (currentVideoIndex < videoUrls.size - 1) {
                                currentVideoIndex++
                                exoPlayer.setMediaItem(MediaItem.fromUri(videoUrls[currentVideoIndex]))
                                exoPlayer.prepare()
                                exoPlayer.play()
                                isPlaying = true
                            }
                        },
                        // replay the same video
                        onReplay = {
                            exoPlayer.seekTo(0)
                            exoPlayer.play()
                            isPlaying = true
                        },
                        playState = playbackState
                    )
                    // button slider landscape
                    BottomIconsActionView(
                        // landscape
                        onConvertLandScape = {
                            landScape()
                            Toast.makeText(context, "$convertScreen", Toast.LENGTH_SHORT).show()
                        },
                        imageVector = if (convertScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen
                    )
                }
            }

            if (showSettingItem) {
                Row(
                    modifier = Modifier.height(275.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.fillMaxWidth())
                    SettingMenuItem(
                        settingAction = settingAction,
                        onSettingDismiss = { settingAction = false }
                    )
                }
            }
        }
    }
}

// top icon bar
@Composable
fun TopIconsActionView(
    imageVector: ImageVector,
    autoPlay: () -> Unit,
    onCaption: () -> Unit,
    onSettingClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            // switch auto play icon
            IconButton(onClick = { autoPlay() }, modifier = Modifier) {
//                Switch(
//                    modifier = Modifier.height(5.dp), // Set the desired height for the Switch
//                    checked = switch,
//                    onCheckedChange = { switch = it },
//                    thumbContent = {}
//                )
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(25.dp), // Adjust the size of the thumb
                )
            }
            // CastConnected icon
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.CastConnected,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            // ClosedCaptionOff icon
            IconButton(onClick = { onCaption() }) {
                Icon(
                    imageVector = Icons.Default.ClosedCaptionOff,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
            // setting icon
            IconButton(onClick = { onSettingClick() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

// pause and stop
@Composable
fun CenterPlayActionView(
    isPlaying: Boolean,
    onPlayAndPause: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onReplay: () -> Unit,
    playState: Int
) {
    Log.e("Status", "$playState")
//    AnimatedVisibility(
//        visible = isPlaying,
//        enter = fadeIn(tween(200)),
//        exit = fadeOut(tween(200))
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.5f)),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Rounded.Pause,
//                contentDescription = null,
//                tint = Color.White
//            )
//        }
//    }
    // video play, pause, back and skip
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // back or Skip Previous
            IconButton(onClick = { onSkipPrevious() }) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Skip Previous",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
            // play, pause and replay
            IconButton(
                onClick = {
                    if (playState == Player.STATE_ENDED) {
                        onReplay()
                    } else {
                        onPlayAndPause()
                    }
                }
            ) {
                if (playState == Player.STATE_ENDED) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Replay",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            // Skip Next video
            IconButton(onClick = { onSkipNext() }) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Skip Next",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomIconsActionView(
    onConvertLandScape: () -> Unit = {},
    imageVector: ImageVector
) {
    // duration slider and zoom
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        var sliderPosition by remember { mutableFloatStateOf(0f) } // Corrected the state type to Float
        val interactionSource = remember { MutableInteractionSource() }
        val colors = SliderDefaults.colors(
            thumbColor = Color.Red,
            activeTrackColor = Color.Red
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // show Full screen and Exit screen
//            var convertScreen by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sliderPosition.toString(),
                    color = Color.White,
                    modifier = Modifier.padding(start = 5.dp)
                ) // show the text
                IconButton(onClick = { onConvertLandScape() }) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            // slider
            Slider(
                value = sliderPosition, // Changed 'state' to 'value'
                onValueChange = {
                    sliderPosition = it
                }, // Added 'onValueChange' to update the value
                interactionSource = interactionSource,
                valueRange = 0f..10f,
                colors = colors,
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        colors = colors,
                        modifier = Modifier.size(15.dp)
                    )
                },
                modifier = Modifier
                    .height(5.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun SettingMenuItem(settingAction: Boolean, onSettingDismiss: () -> Unit) {
    // dropdown menu and inside have more item
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
    ) {
        DropdownMenu(
            expanded = settingAction,
            onDismissRequest = {
                onSettingDismiss()
            },
            modifier = Modifier.wrapContentWidth()
        ) {
            DropdownMenuItem(onClick = { /** onSettingDismiss() */ }) {
                MenusItemView(text = "Quality", imageVector = Icons.Default.Settings)
            }
            DropdownMenuItem(onClick = { /** onSettingDismiss() */ }) {
                MenusItemView(text = "Loop video", imageVector = Icons.Default.RepeatOne)
            }
            DropdownMenuItem(onClick = { /** onSettingDismiss() */ }) {
                MenusItemView(text = "Report", imageVector = Icons.Default.Flag)
            }
            DropdownMenuItem(onClick = { /** onSettingDismiss() */ }) {
                MenusItemView(
                    text = "Help & feedback",
                    imageVector = Icons.Default.HelpOutline
                )
            }
            DropdownMenuItem(onClick = { /** onSettingDismiss() */ }) {
                MenusItemView(
                    text = "Playback speed",
                    imageVector = Icons.Default.PlayCircleOutline
                )
            }
            DropdownMenuItem(onClick = { /** onSettingDismiss() */ }) {
                MenusItemView(
                    text = "Download",
                    imageVector = Icons.Default.Download
                )
            }
        }
    }
}

@Composable
fun MenusItemView(text: String, imageVector: ImageVector) {
    Row(Modifier.fillMaxWidth()) {
        Icon(
            imageVector = imageVector, contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(20.dp)
        )
        TextView(
            text = text,
            style = TextStyle(
                color = Color.Gray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TextView(text: String, style: TextStyle, modifier: Modifier) {
    Text(text = text, style = style, modifier = modifier)
}

@Composable
fun SwitchWithThumbIconView() {

    var switch by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Switch(
            modifier = Modifier.semantics { contentDescription = "SwitchIcon" },
            checked = switch,
            onCheckedChange = { switch = it },
            thumbContent = {
                Icon(
                    imageVector = if (switch) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        )
    }
}

//Box(
//modifier = Modifier
//.fillMaxSize()
//.padding(vertical = 15.dp, horizontal = 20.dp),
//contentAlignment = Alignment.BottomEnd
//) {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(15.dp)
//    ) {
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(
//                imageVector = Icons.Rounded.Favorite,
//                contentDescription = null,
//                tint = Color.White
//            )
//        }
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(
//                imageVector = Icons.Rounded.Comment,
//                contentDescription = null,
//                tint = Color.White
//            )
//        }
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(
//                imageVector = Icons.Rounded.IosShare,
//                contentDescription = null,
//                tint = Color.White
//            )
//        }
//    }
//}

@Composable
fun SimpleSnackBarView() {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            "SnackBar"
                        )
                    }
                }
            ) {
                Text("Show snackBar")
            }
        },
        content = { innerPadding ->
            Text(
                text = "Body content",
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }
    )
}




//Log.e("Player.STATE_BUFFERING", Player.STATE_BUFFERING.toString())
//Log.e("playbackState is :", "$playbackState")
//Log.e("Switch is :", "$isSwitch")
//Log.e("currentVideoIndex is :", "$currentVideoIndex")
//Log.e("Total number of List :", videoUrls.size.toString())