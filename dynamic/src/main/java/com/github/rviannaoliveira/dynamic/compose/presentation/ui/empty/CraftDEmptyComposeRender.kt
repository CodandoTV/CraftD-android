package com.github.rviannaoliveira.dynamic.compose.presentation.ui.empty

import androidx.compose.runtime.Composable
import com.github.codandotv.craftd.androidcore.data.model.base.SimpleProperties
import com.github.codandotv.craftd.androidcore.presentation.CraftDViewListener
import com.github.rviannaoliveira.dynamic.compose.presentation.builder.CraftDComposeBuilder

class CraftDEmptyComposeRender : CraftDComposeBuilder(
    key = ""
) {
    @Composable
    override fun craft(model: SimpleProperties, listener: CraftDViewListener) {
    }
}