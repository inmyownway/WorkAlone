package com.ssafy.workalone.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.workalone.presentation.ui.theme.WalkOneGray50
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ssafy.workalone.presentation.ui.theme.WalkOneBlue400

// 기록 상세 컴포넌트
@Composable
fun ExerciseRecordDetail(
    title: String,
    setCount: Int,
    exerciseCount: Int = 0,
    exerciseDuration: Int,
    calorie: Int
){
    val hours: Int = exerciseDuration/3600
    val minutes: Int = (exerciseDuration%3600)/60
    val seconds: Int = exerciseDuration%60

    Column(
        modifier = Modifier
            .background(WalkOneGray50)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = WalkOneBlue400,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )

            Text(
                text =
                    if(title == "플랭크"){
                        "${setCount}세트 X ${exerciseCount}초"
                    } else {
                        "${setCount}세트 X ${exerciseCount}회"
                    },
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "운동 시간",
                fontSize = 14.sp,
            )

            Text(
                text =
                    if (hours > 0){
                        "${String.format("%02d", hours)}:${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
                    } else {
                        "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
                    },
                fontSize = 14.sp,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "소모 칼로리",
                fontSize = 14.sp,
            )

            Text(
                text = "${calorie}Kcal",
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseRecodePreview() {
    ExerciseRecordDetail("플랭크",3, 2, 3675, 300)
}