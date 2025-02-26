package com.ssafy.workalone.presentation.ui.screen.exercise

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ssafy.workalone.R
import com.ssafy.workalone.data.local.ExerciseInfoPreferenceManager
import com.ssafy.workalone.mlkit.java.CameraXLivePreviewActivity
import com.ssafy.workalone.presentation.ui.component.bottombar.NavigationButtons
import com.ssafy.workalone.presentation.ui.component.exercise.YouTubePlayer
import com.ssafy.workalone.presentation.ui.component.topbar.AppBarView
import com.ssafy.workalone.presentation.ui.theme.LocalWorkAloneTypography
import com.ssafy.workalone.presentation.ui.theme.WalkOneBlue500
import com.ssafy.workalone.presentation.ui.theme.WalkOneGray300
import com.ssafy.workalone.presentation.ui.theme.WalkOneGray50
import com.ssafy.workalone.presentation.ui.theme.WorkAloneTheme
import com.ssafy.workalone.presentation.viewmodels.exercise.ExerciseViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExerciseView(
    navController: NavController,
    viewModel: ExerciseViewModel = ExerciseViewModel(context = LocalContext.current),
    id: Long,
    seq: Int
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val exercises = viewModel.getExerciseById(id).collectAsState(initial = listOf())

    val currentIndex = remember { mutableStateOf(seq - 1) }
    val currentExercise = exercises.value.getOrNull(currentIndex.value)

    val scrollState = rememberScrollState()
    val preferenceManager = ExerciseInfoPreferenceManager(context)
    val intent = Intent(context, CameraXLivePreviewActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    val cameraPermissionCheck = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.CAMERA
    )
    val audioPermissionCheck = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.RECORD_AUDIO
    )

    val storagePermissionCheck = ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // 권한 요청 런처
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[android.Manifest.permission.CAMERA] ?: false
            val audioGranted = permissions[android.Manifest.permission.RECORD_AUDIO] ?: false

            if (cameraGranted) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "카메라 권한이 필요합니다", Toast.LENGTH_LONG).show()
            }
            if (!audioGranted) {
                Toast.makeText(context, "음성 기능은 오디오 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            }
        }

    WorkAloneTheme {
        val typography = LocalWorkAloneTypography.current
        Scaffold(
            topBar = {
                AppBarView(
                    title = currentExercise?.title ?: "Loading...",
                    navController = navController
                )
            },
            scaffoldState = scaffoldState,
        ) { paddingValue ->
            currentExercise?.let { exerciseData ->
                Column(
                    modifier = Modifier
                        .padding(paddingValue)
                        .fillMaxSize()
                        .background(WalkOneGray300),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(WalkOneGray50)
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.SpaceBetween

                    ) {
                        // 제목 및 부제목
                        Text(
                            text = exerciseData.title,
                            style = typography.Heading02,
                        )
                        Text(
                            text = exerciseData.subTitle,
                            style = typography.Heading04,
                            color = WalkOneBlue500,
                            modifier = Modifier.padding(8.dp)
                        )
                        YouTubePlayer(
                            youtubeId =
                            when (exerciseData.title) {
                                "푸쉬업" -> context.getString(R.string.pushup)
                                "스쿼트" -> context.getString(R.string.squat)
                                "플랭크" -> context.getString(R.string.plank)
                                else -> context.getString(R.string.situp)
                            },
                            lifecycleOwner = LocalLifecycleOwner.current
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WalkOneGray50)
                            .padding(top = 10.dp)
                            .padding(bottom = 3.dp)
                            .padding(horizontal = 30.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        // 운동 방법
                        Text(
                            text = "운동 방법",
                            style = WorkAloneTheme.typography.Heading03,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        SectionItem(title = "기본 자세", description = exerciseData.basicPose)
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionItem(title = "동작 수행", description = exerciseData.movement)
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionItem(title = "호흡", description = exerciseData.breath)
                        Spacer(modifier = Modifier.height(32.dp))

                        val isLast = currentExercise.seq == exercises.value.lastOrNull()?.seq
                        val isFirst = seq == 1

                        NavigationButtons(
                            navController = navController,
                            id = id,
                            seq = seq,
                            isLast = isLast,
                            isFirst = isFirst,
                            onStartExercise = {
                                viewModel.saveExercisesPreferences(exercises.value)
                                Log.d("Get Exercise", preferenceManager.getExerciseList().toString())
                                if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED ||
                                    audioPermissionCheck != PackageManager.PERMISSION_GRANTED ||
                                    storagePermissionCheck != PackageManager.PERMISSION_GRANTED
                                ) {
                                    requestPermissionLauncher.launch(
                                        arrayOf(
                                            android.Manifest.permission.CAMERA,
                                            android.Manifest.permission.RECORD_AUDIO,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        )
                                    )
                                } else {
                                    context.startActivity(intent)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionItem(title: String, description: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = WorkAloneTheme.typography.Heading04,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = description,
            style = WorkAloneTheme.typography.Body01,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
