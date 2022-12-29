package com.mind.market.translator_kmm.android.translate.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mind.market.translator_kmm.android.translate.presentation.components.LanguageDropDown
import com.mind.market.translator_kmm.android.translate.presentation.components.SwapLanguagesButton
import com.mind.market.translator_kmm.translate.presentation.TranslateEvent
import com.mind.market.translator_kmm.translate.presentation.TranslateState

@Composable
fun TranslateScreen(
    modifier: Modifier = Modifier,
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {}
    ) { padding ->
        LazyColumn(
            modifier = Modifier
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
                        onClick = { onEvent(TranslateEvent.SwapLanguages) }
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
        }
    }

}