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
                content = { ComposeRoot(viewModel) }
            )
        }
    }
}

private const val BLUE_SCREEN = "blue"
private const val RED_SCREEN = "red"
private const val GREEN_SCREEN = "green"

@Composable
private fun ComposeRoot(viewModel: NameViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = BLUE_SCREEN) {
        val goToBlue = { navController.navigate(BLUE_SCREEN) }
        val goToRed = { navController.navigate(RED_SCREEN) }
        val goToGreen = { navController.navigate(GREEN_SCREEN) }
        composable(route = BLUE_SCREEN) {
            BlueScreen(navController, viewModel, goToRed, goToGreen)
        }
        composable(route = RED_SCREEN) {
            RedScreen(navController, viewModel, goToBlue, goToGreen)
        }
        composable(route = GREEN_SCREEN) {
            GreenScreen(navController, viewModel, goToBlue, goToRed)
        }
    }
}

@Composable
fun BlueScreen(navController: NavHostController, viewModel: NameViewModel, goToRed: () -> Unit, goToGreen: () -> Unit) {
    GenericScreen(Color.Blue, viewModel, {}, goToRed, goToGreen)
}

@Composable
fun RedScreen(navController: NavHostController, viewModel: NameViewModel, goToBlue: () -> Unit, goToGreen: () -> Unit) {
    GenericScreen(Color.Red, viewModel, goToBlue, {}, goToGreen)
}

@Composable
fun GreenScreen(navController: NavHostController, viewModel: NameViewModel, goToBlue: () -> Unit, goToRed: () -> Unit) {
    GenericScreen(Color.Green, viewModel, goToBlue, goToRed, {})
}

/*BottomAppBar*/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GenericScreen(color: Color, viewModel: NameViewModel, goToBlue: () -> Unit, goToRed: () -> Unit, goToGreen: () -> Unit) {
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = Color.LightGray) {
                ScreenNavigationItem("Blue", goToBlue)
                ScreenNavigationItem("Red", goToRed)
                ScreenNavigationItem("Green", goToGreen)
            }
        },
        content = {
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
