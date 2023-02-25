package com.example.currentsnews.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.currentsnews.R


@Composable
fun SettingsDialog(
    openDialog: Boolean,
    closeDialog: ()->Unit,
    selectedLanguage: NewsLanguage,
    selectedTheme: NewsTheme,
    onSelectLanguage: () -> Unit,
    onSelectTheme: () -> Unit
){
    if(openDialog){
        Dialog(
            onDismissRequest = { closeDialog()},
            content = {
                DialogContent(
                    selectedLanguage = selectedLanguage,
                    selectedTheme = selectedTheme,
                    onSelectLanguage = { onSelectLanguage() },
                    onSelectTheme = {onSelectTheme()},
                    closeDialog= closeDialog
                )
            }
        )
    }
}

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    selectedLanguage: NewsLanguage,
    selectedTheme: NewsTheme,
    onSelectLanguage: () -> Unit,
    onSelectTheme: () -> Unit,
    closeDialog: () -> Unit
){
    Surface(
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {

            Text(
                text = stringResource(id = R.string.setting),
                style= MaterialTheme.typography.h5
            )

            Divider()

            Text(text = stringResource(id = R.string.theme))
            ChooseTheme(selectedTheme = selectedTheme, onSelectTheme = onSelectTheme)

            Divider()
            Text(text = stringResource(id = R.string.language))
            ChooseLanguage(selectedLanguage = selectedLanguage, onSelectLanguage = onSelectLanguage)

            Divider()
            OutlinedButton(
                onClick = {closeDialog()},
                modifier= Modifier.align(Alignment.End),
                border = null
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    }
}

@Composable
fun ChooseTheme(
    modifier: Modifier= Modifier,
    radioOptions: List<NewsTheme> = listOf(NewsTheme.Dark,NewsTheme.Light),
    selectedTheme: NewsTheme,
    onSelectTheme: ()->Unit

){
    Column {

        radioOptions.forEach {
            Row(
                modifier= modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedTheme == it,
                        onClick = { onSelectTheme() }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedTheme == it  ,
                    onClick = { onSelectTheme() }
                )

                Text(text = stringResource(id = it.textResource))
            }
        }
    }
}

@Composable
fun ChooseLanguage(
    modifier: Modifier = Modifier,
    radioOptions: List<NewsLanguage> = listOf(NewsLanguage.En,NewsLanguage.Fr,NewsLanguage.Arab),
    selectedLanguage: NewsLanguage,
    onSelectLanguage: ()->Unit
){
    Column {

        radioOptions.forEach {
            Row(
                modifier= modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedLanguage == it,
                        onClick = { onSelectLanguage() }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedLanguage == it  ,
                    onClick = { onSelectLanguage() }
                )

                Text(text = stringResource(id = it.textResource))
            }
        }
    }
}

enum class NewsTheme(val textResource: Int){
    Dark(textResource = R.string.defaulTheme),
    Light(textResource = R.string.darkTheme)
}

enum class NewsLanguage(val textResource: Int){
    Arab(textResource = R.string.ar),
    Fr(textResource = R.string.fr),
    En(textResource = R.string.en)
}