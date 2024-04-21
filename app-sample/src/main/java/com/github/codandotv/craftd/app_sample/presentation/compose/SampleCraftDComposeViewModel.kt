package com.github.codandotv.craftd.app_sample.presentation.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.codandotv.craftd.androidcore.data.model.base.SimpleProperties
import com.github.codandotv.craftd.app_sample.data.SampleCraftDRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SampleCraftDComposeViewModel(private val repository: SampleCraftDRepository) : ViewModel() {
    private val _properties = MutableStateFlow(emptyList<SimpleProperties>())
    val properties = _properties.stateIn(
        viewModelScope, SharingStarted.Eagerly,
        initialValue = _properties.value
    )
    fun loadProperties() {
        viewModelScope.launch {
            repository.getDynamic().collectLatest { newlist ->
                    _properties.update {
                        newlist
                    }
                }
        }
    }
}