package com.github.rviannaoliveira.dynamic.compose.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.codandotv.craftd.androidcore.data.model.action.ActionProperties
import com.github.codandotv.craftd.androidcore.data.model.base.SimpleProperties
import com.github.rviannaoliveira.dynamic.compose.presentation.builder.CraftDComposeBuilders

@Composable
fun DynamicComposeController(
    properties: List<SimpleProperties>,
    modifier: Modifier = Modifier,
    dynamicBuilder : CraftDComposeBuilders,
    onAction: (ActionProperties) -> Unit
) {
    LazyColumn(
        modifier
    ) {
        items(
            count = properties.size,
          ) { index ->
            val model = properties[index]
            val builder = dynamicBuilder.getBuilder(model.key)
            builder.craft(model = model) {
                onAction(it)
            }
        }
    }
}