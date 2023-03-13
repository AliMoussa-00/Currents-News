package com.example.currentsnews.ui.screens.home

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.currentsnews.R

@Composable
fun NewsTopBar(
    onClickSetting: ()->Unit
){
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        title = { Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h1)},
        actions = {
            IconButton(onClick = { onClickSetting() }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = stringResource(id = R.string.setting))
            }
        },
        elevation = 0.dp
    )
}