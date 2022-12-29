package com.mind.market.translator_kmm.android.translate.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.mind.market.translator_kmm.android.core.theme.LightBlue
import com.mind.market.translator_kmm.core.presentation.UiLanguage

@Composable
fun TranslateTextField(
    fromText: String,
    toText: String?,
    isTranslating: Boolean,
    fromLanguage: UiLanguage,
    toLanguage: UiLanguage,
    onTranslateClick: () -> Unit,
    onTextChange: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    onTextFieldClick: () -> Unit,
    modifier: Modifier = Modifier
) {

}

@Composable
private fun IdleTranslateTextField(
    fromText: String,
    isTranslating: Boolean,
    onTextChange: (String) -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = fromText,
            onValueChange = onTextChange,
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface
            )
        )
        if (fromText.isEmpty() && isFocused.not()) {
            Text(
                text = stringResource(
                    id = com.mind.market.translator_kmm.android.R.string.enter_a_text_to_translate,
                ),
                color = LightBlue
            )
        }
        ProgressButton(
            text = stringResource(
                id = com.mind.market.translator_kmm.android.R.string.translate
            ),
            isLoading = isTranslating,
            onClick = onTranslateClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}