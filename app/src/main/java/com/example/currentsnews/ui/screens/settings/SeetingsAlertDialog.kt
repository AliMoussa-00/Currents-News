package com.example.currentsnews.ui.screens.settings

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.os.LocaleListCompat
import com.example.currentsnews.R
import com.example.currentsnews.ui.NewsViewModel


@Composable
fun SettingsDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    resetLanguage: () -> Unit,
    newsViewModel: NewsViewModel,
) {

    val isLightTheme = MaterialTheme.colors.isLight
    val context = LocalContext.current

    if (openDialog) {
        Dialog(
            onDismissRequest = { closeDialog() },
            content = {
                DialogContent(
                    selectedLanguage = getLanguage(),
                    selectedTheme = if (isLightTheme) NewsTheme.Light else NewsTheme.Dark,
                    selectLanguage = {
                        setLanguage(it)
                        resetLanguage()
                    },
                    onSelectTheme = { newsTheme ->
                        setTheme(
                            newsTheme = newsTheme,
                            context = context,
                            storeTheme = { newsViewModel.storeTheme(it) }
                        )
                    },
                    closeDialog = closeDialog
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
    selectLanguage: (NewsLanguage) -> Unit,
    onSelectTheme: (NewsTheme) -> Unit,
    closeDialog: () -> Unit,
) {
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
                style = MaterialTheme.typography.h5
            )

            Divider()

            Text(text = stringResource(id = R.string.theme))
            ChooseTheme(selectedTheme = selectedTheme, onSelectTheme = onSelectTheme)

            Divider()
            Text(text = stringResource(id = R.string.language))
            ChooseLanguage(selectedLanguage = selectedLanguage, selectLanguage = selectLanguage)

            Divider()
            OutlinedButton(
                onClick = { closeDialog() },
                modifier = Modifier.align(Alignment.End),
                border = null
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    }
}

@Composable
fun ChooseTheme(
    modifier: Modifier = Modifier,
    radioOptions: List<NewsTheme> = listOf(NewsTheme.Light, NewsTheme.Dark),
    selectedTheme: NewsTheme,
    onSelectTheme: (NewsTheme) -> Unit,

    ) {
    Column {

        radioOptions.forEach {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedTheme == it,
                        onClick = { onSelectTheme(it) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedTheme == it,
                    onClick = { onSelectTheme(it) }
                )

                Text(text = stringResource(id = it.textResource))
            }
        }
    }
}

@Composable
fun ChooseLanguage(
    modifier: Modifier = Modifier,
    radioOptions: List<NewsLanguage> = listOf(NewsLanguage.En, NewsLanguage.De, NewsLanguage.Arab),
    selectedLanguage: NewsLanguage,
    selectLanguage: (NewsLanguage) -> Unit,
) {
    Column {

        radioOptions.forEach {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedLanguage == it,
                        onClick = { selectLanguage(it) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedLanguage == it,
                    onClick = { selectLanguage(it) }
                )

                Text(text = stringResource(id = it.textResource))
            }
        }
    }
}

enum class NewsTheme(val textResource: Int) {
    Dark(textResource = R.string.darkTheme),
    Light(textResource = R.string.defaulTheme)
}

enum class NewsLanguage(val textResource: Int) {
    Arab(textResource = R.string.ar),
    De(textResource = R.string.de),
    En(textResource = R.string.en)
}

private fun setLanguage(language: NewsLanguage) {

    val localOptions = mapOf(
        NewsLanguage.En to "en",
        NewsLanguage.Arab to "ar",
        NewsLanguage.De to "de"
    )
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(
            localOptions[language]
        )
    )
}

private fun getLanguage(): NewsLanguage {
    val selectedLanguage = AppCompatDelegate.getApplicationLocales()
    return when (selectedLanguage.toLanguageTags()) {
        "ar" -> NewsLanguage.Arab
        "de" -> NewsLanguage.De
        else -> NewsLanguage.En
    }
}

private fun setTheme(
    newsTheme: NewsTheme,
    context: Context,
    storeTheme: (Int) -> Unit,
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

        if (newsTheme == NewsTheme.Light)
            uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
        else
            uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)

    } else {
        if (newsTheme == NewsTheme.Light) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            storeTheme(MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            storeTheme(MODE_NIGHT_YES)
        }
    }
}

