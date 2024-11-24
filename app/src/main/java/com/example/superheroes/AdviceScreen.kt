package com.example.superheroes

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.superheroes.model.Advice
import com.example.superheroes.model.AdviceRepository
import com.example.superheroes.ui.theme.SuperheroesTheme


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdvicesList(
    advices: List<Advice>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) { val visibleState = remember {
    MutableTransitionState(false).apply {
        targetState = true
    }
}

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
        modifier = modifier
    ) {
        LazyColumn(contentPadding = contentPadding) {
            itemsIndexed(advices) { index, advice ->
                AdviceListItem(
                    advice = advice,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // Animate each list item to slide in vertically
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = StiffnessVeryLow,
                                    dampingRatio = DampingRatioLowBouncy
                                ),
                                initialOffsetY = { it * (index + 1) } // staggered entrance
                            )
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdviceListItem(
    advice: Advice,
    modifier: Modifier = Modifier
) {
    // Состояние для управления видимостью описания
    val isDescriptionVisible = remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = stringResource(advice.numberRes),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(advice.shortDescriptionRes),
                style = MaterialTheme.typography.displaySmall
            )
            Image(
                modifier = Modifier
                    .height(300.dp)
                    .padding(top = 10.dp)
                    .clickable {
                        // Переключаем видимость описания
                        isDescriptionVisible.value = !isDescriptionVisible.value
                    },
                painter = painterResource(advice.imageRes),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth
            )
            // Анимация для видимости текста
            AnimatedVisibility(
                visible = isDescriptionVisible.value,
                enter = slideInVertically(
                    animationSpec = spring(
                        stiffness = StiffnessVeryLow,
                        dampingRatio = DampingRatioLowBouncy
                    ),
                    initialOffsetY = { it }
                ),
                exit = fadeOut(animationSpec = spring())
            ) {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = stringResource(advice.descriptionRes),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@SuppressLint("ResourceType")
@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AdvicePreview() {
    val advice = Advice(
        R.string.day1_number,
        R.string.day1_short_description,
        R.string.day1_description,
        R.drawable.advice_day1
    )
    SuperheroesTheme {
        AdviceListItem(advice = advice)
    }
}

@Preview("Advices List")
@Composable
fun AdvicesPreview() {
    SuperheroesTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            AdvicesList(advices = AdviceRepository.advices)
        }
    }
}