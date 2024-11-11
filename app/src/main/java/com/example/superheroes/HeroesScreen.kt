package com.example.superheroes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.superheroes.model.Hero

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HeroesList(
    heroes: List<Hero>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) { val visibleState = remember {
    MutableTransitionState(false).apply {
        targetState = true
    }
}

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = Spring.dampingRatioLowBouncy()),
        exit = fadeOut(),
        modifier = modifier
    ) {
        LazyColumn(contentPadding = contentPadding) {
            itemsIndexed(heroes) { index, hero ->

            }
        }
    }
}

}