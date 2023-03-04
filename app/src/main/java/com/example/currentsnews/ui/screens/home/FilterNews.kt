package com.example.currentsnews.ui.screens.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currentsnews.model.Filters
import com.example.currentsnews.model.categoryFilters
import com.example.currentsnews.ui.theme.CurrentsNewsTheme


@Composable
fun FilterNews(
    modifier: Modifier = Modifier,
    filterList: List<Filters> = categoryFilters,
    onClickFilter: (Filters) -> Unit,
    selectedFilters: Filters,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 8.dp),
        modifier = modifier
    ) {

        items(filterList) {
            FilterChip(filters = it, onClickFilter = onClickFilter, selectedFilters = selectedFilters)
        }
    }
}

@Composable
fun FilterChip(
    modifier: Modifier = Modifier,
    filters: Filters,
    onClickFilter: (Filters) -> Unit,
    selectedFilters: Filters,
) {
    val isSelected = selectedFilters == filters
    val chipColor by animateColorAsState(
        targetValue = if(isSelected)Color(0xFF03A9F4) else Color.White ,
        animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
    )
    val selectedModifier : Modifier = if(isSelected){
        modifier
            .clip(RoundedCornerShape(50))
            .background(chipColor)
            .padding(vertical = 4.dp, horizontal = 8.dp)

    }else{
        modifier
    }

    Box(
        modifier = selectedModifier
            .toggleable(
                value = isSelected,
                onValueChange = {onClickFilter(filters)},
                interactionSource = remember{ MutableInteractionSource() },
                indication = null
            )
    ) {
        Text(
            text = stringResource(id = filters.category),
            color =if (isSelected) Color.White else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFilters() {
    CurrentsNewsTheme {
        FilterNews(onClickFilter = {}, selectedFilters = Filters.All)
    }
}