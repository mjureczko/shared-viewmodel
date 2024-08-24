package pl.marianjureczko.sharedviewmodel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = lightColors(),
                content = {
                    var name = remember { mutableStateOf("") }
                    Column {
                        val name1 = name.value
                        Greeting(name1)
                        NameInput(name = name.value, onValueChange = {
                            name.value = it
                        })
                    }
                }
            )
        }
    }
}

@Composable
fun NameInput(name: String, onValueChange: (String) -> Unit) {

    TextField(
        value = name,
        onValueChange = onValueChange,
        label = { Text("Name") }
    )

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting("Android")
}