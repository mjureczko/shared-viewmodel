package pl.marianjureczko.sharedviewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NameViewModel : ViewModel() {
    val name: MutableState<String> = mutableStateOf("")

}