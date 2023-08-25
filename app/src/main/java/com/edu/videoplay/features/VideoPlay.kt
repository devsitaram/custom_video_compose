package com.edu.videoplay.features

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.ClosedCaptionOff
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MotionPhotosPause
import androidx.compose.material.icons.filled.MotionPhotosPaused
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsInputComposite
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VideoPlayViewScreen() {

    val context = LocalContext.current
    val videoView = rememberVideoViews(context)
    var buttonVisibility by remember { mutableStateOf(false) }
    val videoScope = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(true) }

    val videoStartAndPause: () -> Unit = {
        videoScope.launch {
            if (isPlaying) {
                videoView.pause()
            } else {
                videoView.start()
            }
            isPlaying = !isPlaying
        }
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AndroidView(
                    factory = { videoView },
                    modifier = Modifier
                        .clickable { buttonVisibility = !buttonVisibility },
                )

                if (buttonVisibility) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
//                                contentAlignment = Alignment.TopCenter
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Cyan),
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
                                // switch button
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
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
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.ClosedCaptionOff,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                // setting icon
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                            }
                        }
//                    }

//                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        // back, play, and skip button
                        var playVideo by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Red),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(25.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    playVideo = !playVideo
                                    videoStartAndPause() // play and stop
                                }
                            ) {
                                if (playVideo) {
                                    Icon(
                                        imageVector = Icons.Default.MotionPhotosPause,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircleOutline,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
//                    }

//                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter){
                        // slider
                        var sliderPosition by remember { mutableFloatStateOf(0f) } // Corrected the state type to Float
                        val interactionSource = remember { MutableInteractionSource() }
                        val colors = SliderDefaults.colors(
                            thumbColor = Color.Red,
                            activeTrackColor = Color.Red
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Blue)
                        ) {
                            // show Full screen and Exit screen
                            var convertScreen by remember { mutableStateOf(false) }
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
                                IconButton(
                                    onClick = { convertScreen = !convertScreen }
                                ) {
                                    if (convertScreen) {
                                        Icon(
                                            imageVector = Icons.Default.Fullscreen,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.FullscreenExit,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
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
                                    .padding(top = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberVideoViews(context: Context): VideoView {
    // Online video path
    val onlinePath = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
//        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
    val onlineVideo = Uri.parse(onlinePath)
    val videoView = VideoView(context)
    // Set the video URI and start playback
    videoView.setVideoURI(onlineVideo)
//    videoView.layoutParams = FrameLayout.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.WRAP_CONTENT
//    )
    videoView.start()
    return videoView
}

@Composable
private fun rememberMediaController(
    videoView: VideoView,
    context: Context
): MediaController {
    val mediaController = remember { MediaController(context) }
    mediaController.setAnchorView(videoView)
    return mediaController
}

//@Composable
//fun VideoPlayer(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val exoPlayer = remember {
//        ExoPlayer.Builder(context).build().apply {
//            setMediaItem(
//                MediaItem.fromUri(
//                    "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
//                )
//            )
//            prepare()
//            playWhenReady = true
//        }
//    }
//
//    Box(modifier = modifier) {
//        DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }
//        AndroidView(
//            factory = {
//                PlayerView(context).apply {
//                    player = exoPlayer
//                    layoutParams = FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                }
//            },
//            modifier = Modifier
//        )
//    }
//}







//
//@Composable
//fun VideoPlayer(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val exoPlayer = remember {
//        ExoPlayer.Builder(context).build().apply {
//            setMediaItem(
//                MediaItem.fromUri(
//                    "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
//                )
//            )
//            prepare()
//            playWhenReady = true
//        }
//    }
//
//    var isPlaying by remember { mutableStateOf(true) } // Start playing by default
//    var playbackState by remember { mutableIntStateOf(Player.STATE_IDLE) }
//    var isLoading by remember { mutableStateOf(false) } // To indicate if the video is loading
//    var progress by remember { mutableFloatStateOf(0f) } // Current playback progress
//    val duration = exoPlayer.duration
//
////    exoPlayer.addListener(
////        object : Player.Listener {
////            override fun onIsPlayingChanged(isPlayingValue: Boolean) {
////                isPlaying = isPlayingValue
////            }
//
////            override fun onPlaybackStateChanged(state: Int) {
////                playbackState = state
////                isLoading = state == Player.STATE_BUFFERING
////                if (state == Player.STATE_ENDED) {
////                    isPlaying = false
////                }
////            }
//
////            override fun onPositionDiscontinuity(
////                oldPosition: Player.PositionInfo,
////                newPosition: Player.PositionInfo,
////                reason: Int
////            ) {
////                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
////            }
////        }
////    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
////            .clickable {
////                if (isPlaying) {
////                    exoPlayer.pause()
////                } else {
////                    exoPlayer.play()
////                }
////            }
//    ) {
//        DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }
//        Box(contentAlignment = Alignment.Center) {
//            AndroidView(
//                factory = {
//                    PlayerView(context).apply {
//                        player = exoPlayer
//                        useController = false // default icon buttons off
//                        layoutParams = FrameLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT
//                        )
//                    }
//                },
//                modifier = modifier
//            )
//
//            // video play and pause
//            VideoLayout(
//                isPlaying = isPlaying,
//                onPlayAndPause = {
//                    isPlaying = if (isPlaying) {
//                        exoPlayer.pause()
//                        !isPlaying
//                    } else {
//                        exoPlayer.play()
//                        !isPlaying
//                    }
//                },
////                onPlay = { exoPlayer.play() },
////                onPause = { exoPlayer.pause() },
//                onSkipPrevious = {},
//                onSkipNext = {},
//                onReplay = {
//                    exoPlayer.seekTo(0)
//                    exoPlayer.play()
//                }
//            )
//        }
//    }
//}

// pause and stop
//@Composable
//fun VideoLayout(
//    isPlaying: Boolean,
//    onPlayAndPause: () -> Unit,
////    onPause: () -> Unit,
//    onSkipPrevious: () -> Unit,
//    onSkipNext: () -> Unit,
//    onReplay: () -> Unit,
//) {
//    Row(
//        horizontalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        // back or Skip Previous
//        IconButton(
//            onClick = {
//                onSkipPrevious()
//            }
//        ) {
//            Icon(
//                imageVector = Icons.Default.SkipPrevious,
//                contentDescription = "Skip Previous",
//                tint = Color.White,
//                modifier = Modifier.size(25.dp)
//            )
//        }
//        // play and pause
//        IconButton(
//            onClick = {
//                onPlayAndPause()
////                if (isPlaying) {
////                    onPause()
////                } else {
////                    onPlay()
////                }
//            }
//        ) {
//            Icon(
//                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
//                contentDescription = if (isPlaying) "Pause" else "Play",
//                tint = Color.White,
//                modifier = Modifier.size(30.dp)
//            )
//        }
//        // Skip Next video
//        IconButton(
//            onClick = {
//                onSkipNext()
//            }
//        ) {
//            Icon(
//                imageVector = Icons.Default.SkipNext,
//                contentDescription = "Skip Next",
//                tint = Color.White,
//                modifier = Modifier.size(25.dp)
//            )
//        }
//        // replay icon
//        IconButton(
//            onClick = {
//                onReplay()
//            }
//        ) {
//            Icon(
//                imageVector = Icons.Default.Replay,
//                contentDescription = "Replay",
//                tint = Color.White,
//                modifier = Modifier.size(25.dp)
//            )
//        }
//    }
//}