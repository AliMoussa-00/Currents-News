package com.example.currentsnews.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.currentsnews.R

val DM_Serif_Display = FontFamily(
    Font(R.font.dm_serif_display_regular),
    Font(R.font.dm_serif_display_italic)
)

val DM_Serif_Text= FontFamily(
    Font(R.font.dm_serif_text_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(

    h1 = TextStyle(
        fontFamily = DM_Serif_Display,
        fontSize = 30.sp
    ),

    h2 = TextStyle(
        fontFamily = DM_Serif_Display,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),

    h3 = TextStyle(
        fontFamily = DM_Serif_Display,
        fontSize = 16.sp
    ),

    body1 = TextStyle(
        fontFamily = DM_Serif_Text,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = DM_Serif_Display,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
)