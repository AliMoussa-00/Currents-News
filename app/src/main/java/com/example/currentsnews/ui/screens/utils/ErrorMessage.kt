package com.example.currentsnews.ui.screens.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currentsnews.R

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    onClickRefresh: () -> Unit,
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_error_outline_24),
                contentDescription = stringResource(id = R.string.bad_connection)
            )
            Text(
                text = stringResource(id = R.string.bad_connection),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        IconButton(onClick = onClickRefresh) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = stringResource(id = R.string.refresh))
        }
    }
}