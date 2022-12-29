@file:OptIn(ExperimentalComposeUiApi::class)

package com.mind.market.translator_kmm.android.translate.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.mind.market.translator_kmm.android.translate.presentation.components.LanguageDropDown
import com.mind.market.translator_kmm.android.translate.presentation.components.SwapLanguagesButton
import com.mind.market.translator_kmm.android.translate.presentation.components.TranslateTextField
import com.mind.market.translator_kmm.translate.presentation.TranslateEvent
import com.mind.market.translator_kmm.translate.presentation.TranslateState

@Composable
fun TranslateScreen(
    modifier: Modifier = Modifier,
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {}
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LanguageDropDown(
                        language = state.fromLanguage,
                        isOpen = state.isChoosingFromLanguage,
                        onClick = {
                            onEvent(TranslateEvent.OpenFromLanguageDropdown)
                        },
                        onDismiss = {
                            onEvent(TranslateEvent.StopChoosingLanguage)
                        },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseFromLanguage(language))
                        }
                    )
                    Spacer(Modifier.weight(1f))

                    SwapLanguagesButton(
                        onClick = {
                            onEvent(TranslateEvent.SwapLanguages)
                        }
                    )

                    Spacer(Modifier.weight(1f))

                    LanguageDropDown(
                        language = state.toLanguage,
                        isOpen = state.isChoosingToLanguage,
                        onClick = {
                            onEvent(TranslateEvent.OpenToLanguageDropdown)
                        },
                        onDismiss = {
                            onEvent(TranslateEvent.StopChoosingLanguage)
                        },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseToLanguage(language))
                        }
                    )
                }
            }
            item {
                val clipBoardManager = LocalClipboardManager.current
                val keyboardController = LocalSoftwareKeyboardController.current
                TranslateTextField(
                    fromText = state.fromText,
                    toText = state.toText,
                    isTranslating = state.isTranslating,
                    fromLanguage = state.fromLanguage,
                    toLanguage = state.toLanguage,
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.Translate)
                    },
                    onTextChange = { newText ->
                        onEvent(TranslateEvent.ChangeTranslationText(newText))
                    },
                    onCopyClick = { textToCopy ->
                        clipBoardManager.setText(
                            buildAnnotatedString {
                                append(textToCopy)
                            }
                        )
                        Toast.makeText(
                            context,
                            context.getString(
                                com.mind.market.translator_kmm.android.R.string.copied_to_clipboard
                            ),
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onCloseClick = {
                        onEvent(TranslateEvent.CloseTranslation)
                    },
                    onSpeakerClick = {

                    },
                    onTextFieldClick = {
                        onEvent(TranslateEvent.EditTranslation)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}