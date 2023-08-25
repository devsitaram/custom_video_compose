package com.edu.videoplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.edu.videoplay.features.Media3Play
import com.edu.videoplay.ui.theme.VideoPlayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoPlayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Media3Play()
//                    VideoPlayViewScreen()
                }
            }
        }
    }
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
//    var isPlaying by remember { mutableStateOf(true) } // Start playing by default
//    exoPlayer.addListener(
//        object : Player.Listener {
//            override fun onIsPlayingChanged(isPlayingValue: Boolean) {
//                isPlaying = isPlayingValue
//            }
//        }
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .clickable {
//                if (isPlaying) {
//                    exoPlayer.pause() // Corrected function name
//                } else {
//                    exoPlayer.play() // Corrected function name
//                }
//            }
//    ) {
//        DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }
//
//        Box {
//            AndroidView(
//                factory = {
//                    PlayerView(context).apply {
//                        // Resizes the video in order to be able to fill the whole screen
//                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
//                        player = exoPlayer
//                        // Hides the default Player Controller
//                        useController = false
//                        // Fills the whole screen
//                        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
//                    }
//                }
//            )
//
//            VideoLayout(
//                isPlaying = isPlaying,
//                onPlay = {
//                    exoPlayer.play()
//                },
//                onPause = {
//                    exoPlayer.pause()
//                }
//            )
//        }
//    }
//}

//@Composable
//fun VideoPlayViewScreen() {
//    val context = LocalContext.current
//    val selectedUri =
//        "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
//
//    val player = ExoPlayer.Builder(context).build()
//
//    // Initialize the video player with the selected URI
//    val mediaItem = MediaItem.fromUri(selectedUri)
//    player.setMediaItem(mediaItem)
//    player.prepare()
//
//    // Release the player when the Composable is disposed
//    DisposableEffect(player) {
//        onDispose {
//            player.release()
//        }
//    }
//
//    Surface(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            AndroidView(
//                factory = { ctx ->
//                    PlayerView(ctx).apply {
//                        this.player = player
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16 / 9f)
//            )
//        }
//    }
//}

//@Composable
//fun VideoPlayViewScreen() {
//    val context = LocalContext.current
//    Surface(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            val selectedUri = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
//            val selectVideoLuncher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.GetContent(),
//                onResult = { uri ->
//                    uri?.let {
//                        // Initialize the video player with the selected URI
//                        val player = ExoPlayer.Builder(context).build()
//                        val mediaItem = MediaItem.fromUri(selectedUri)
//                        player.setMediaItem(mediaItem)
//                        player.prepare()
//
//                        // Release the player when the Composable is disposed
////                        DisposableEffect(player) {
////                            onDispose {
////                                player.release()
////                            }
////                        }
//                    }
//                }
//            )
//
//            AndroidView(
//                factory = { context ->
//                    PlayerView(context).apply {
//                        player = ExoPlayer.Builder(context).build()
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16 / 9f)
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//        }
//    }
//}

//@Composable
//fun VideoPlayViewScreen() {
//    val context = LocalContext.current
//    val videoView = rememberVideoView(context)
//    val mediaController = rememberMediaController(videoView, context)
//
//    Surface(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            AndroidView(
//                factory = { videoView },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16 / 15f)
//            ) { view ->
//                view.layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//                videoView.setMediaController(mediaController)
//            }
//        }
//    }
//
//    // Local video path
//    // val vPath = "android.resource://" + context.packageName + "/raw/neymar"
//    // val videoPath = Uri.parse(vPath)
//
//    // Online video path
//    val onlinePath =
//        "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
//    val onlineVideo = Uri.parse(onlinePath)
//
//    // Set the video URI and start playback
//    videoView.setVideoURI(onlineVideo)
//    // videoView.setVideoURI(videoPath)
//    videoView.start()
//
//    // if video is played for less than 60 seconds
//    videoView.setOnPreparedListener { mp ->
//        val duration = mp.duration // Duration of the video in milliseconds
//        val maxDuration = 60000 // Maximum duration of 1 minute in milliseconds
//
//        if (duration > maxDuration) {
//            videoView.seekTo(0) // Start from the beginning
//            videoView.start()   // Start playback
//            videoView.postDelayed(
//                { videoView.pause() },
//                maxDuration.toLong()
//            ) // Pause after 1 minute
//            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//        } else {
//            videoView.start() // Start playback normally
//        }
//    }
//}
//
//
//
//@Composable
//private fun rememberVideoView(context: Context): VideoView {
//    val videoView = VideoView(context)
//    videoView.layoutParams = ViewGroup.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.MATCH_PARENT
//    )
//    return videoView
//}
//
////@Composable
////private fun rememberMediaController(videoView: VideoView, context: Context): MediaController {
////    val mediaController = remember { android.widget.MediaController(context) }
////    mediaController.setAnchorView(videoView)
////    return mediaController
////}
//
//
//@Preview
//@Composable
//fun VideoPlayer() {
//
//    val context = LocalContext.current
//    val videoView = rememberVideoView(context)
////    val mediaController = rememberMediaController(videoView, context)
//
//
//    // Local video path
//    // val vPath = "android.resource://" + context.packageName + "/raw/neymar"
//    // val videoPath = Uri.parse(vPath)
//
//    // Online video path
//    val onlinePath =
//        "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
//    val onlineVideo = Uri.parse(onlinePath)
//
//    // Set the video URI and start playback
//    videoView.setVideoURI(onlineVideo)
//    // videoView.setVideoURI(videoPath)
//    videoView.start()
//
//    Surface(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            AndroidView(
//                factory = {
//                    videoView
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16 / 9f),
//            )
////            videoView.setMediaController(mediaController)
//            videoView.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//        }
//    }
//}