package pl.marianjureczko.sharedviewmodel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: NameViewModel by viewModels()
        setContent {
            MaterialTheme(
                colors = lightColors(),
                content = { ComposeRoot() }
            )
        }
    }
}

private const val BLUE_SCREEN = "blue"
private const val RED_SCREEN = "red"
private const val GREEN_SCREEN = "green"

@Composable
private fun ComposeRoot() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = BLUE_SCREEN) {
        val goToBlue = { navController.navigate(BLUE_SCREEN) }
        val goToRed = { navController.navigate(RED_SCREEN) }
        val goToGreen = { navController.navigate(GREEN_SCREEN) }
        composable(route = BLUE_SCREEN) {
            BlueScreen(navController, goToRed, goToGreen)
        }
        composable(route = RED_SCREEN) {
            RedScreen(navController, goToBlue, goToGreen)
        }
        composable(route = GREEN_SCREEN) {
            GreenScreen(navController, goToBlue, goToRed)
        }
    }
}

@Composable
fun BlueScreen(navController: NavHostController, goToRed: () -> Unit, goToGreen: () -> Unit) {
    GenericScreen(Color.Blue, {}, goToRed, goToGreen)
}

@Composable
fun RedScreen(navController: NavHostController, goToBlue: () -> Unit, goToGreen: () -> Unit) {
    GenericScreen(Color.Red, goToBlue, {}, goToGreen)
}

@Composable
fun GreenScreen(navController: NavHostController, goToBlue: () -> Unit, goToRed: () -> Unit) {
    GenericScreen(Color.Green, goToBlue, goToRed, {})
}

/*BottomAppBar*/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GenericScreen(color: Color, goToBlue: () -> Unit, goToRed: () -> Unit, goToGreen: () -> Unit) {
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = Color.LightGray) {
                ScreenNavigationItem("Blue", goToBlue)
                ScreenNavigationItem("Red", goToRed)
                ScreenNavigationItem("Green", goToGreen)
            }
        },
        content = {
            val viewModel: NameViewModel = viewModel()
            Column {
                Greeting(viewModel.name.value, color = color)
                NameInput(name = viewModel.name.value, onValueChange = {
                    viewModel.name.value = it
                })
            }
        }
    )

}

@Composable
fun RowScope.ScreenNavigationItem(text: String, goToRed: () -> Unit) {
    BottomNavigationItem(
        icon = {
            Icon(
                painter = rememberVectorPainter(Icons.Outlined.Star),
                contentDescription = text
            )
        },
        label = { Text(text) },
        selected = false,
        onClick = goToRed
    )
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
fun Greeting(name: String, modifier: Modifier = Modifier, color: Color) {
    Text(
        text = "Hello $name!",
        modifier = modifier.background(color)
    )
}
