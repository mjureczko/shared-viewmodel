package pl.marianjureczko.sharedviewmodel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
private const val NAME_ARGUMENT = "name"

@Composable
private fun ComposeRoot() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "$BLUE_SCREEN/{$NAME_ARGUMENT}") {
        val goToBlue: (String) -> Unit = { navController.navigate("$BLUE_SCREEN/$it") }
        val goToRed: (String) -> Unit = { navController.navigate("$RED_SCREEN/$it") }
        val goToGreen: (String) -> Unit = { navController.navigate("$GREEN_SCREEN/$it") }
        composable(
            route = "$BLUE_SCREEN/{$NAME_ARGUMENT}",
            arguments = listOf(navArgument(NAME_ARGUMENT) { type = NavType.StringType })
        ) {backStackEntry ->
            val argumentValue = backStackEntry.arguments?.getString(NAME_ARGUMENT) ?: ""
            BlueScreen(argumentValue, goToRed, goToGreen)
        }
        composable(
            route = "$RED_SCREEN/{$NAME_ARGUMENT}",
            arguments = listOf(navArgument(NAME_ARGUMENT) { type = NavType.StringType })
        ) {backStackEntry ->
            val argumentValue = backStackEntry.arguments?.getString(NAME_ARGUMENT) ?: ""
            RedScreen(argumentValue, goToBlue, goToGreen)
        }
        composable(
            route = "$GREEN_SCREEN/{$NAME_ARGUMENT}",
            arguments = listOf(navArgument(NAME_ARGUMENT) { type = NavType.StringType })
        ) {backStackEntry ->
            val argumentValue = backStackEntry.arguments?.getString(NAME_ARGUMENT) ?: ""
            GreenScreen(argumentValue, goToBlue, goToRed)
        }
    }
}

@Composable
fun BlueScreen(currentName:String, goToRed: (String) -> Unit, goToGreen: (String) -> Unit) {
    GenericScreen(Color.Blue, currentName, {}, goToRed, goToGreen)
}

@Composable
fun RedScreen(currentName:String, goToBlue: (String) -> Unit, goToGreen: (String) -> Unit) {
    GenericScreen(Color.Red, currentName, goToBlue, {}, goToGreen)
}

@Composable
fun GreenScreen(currentName:String, goToBlue: (String) -> Unit, goToRed: (String) -> Unit) {
    GenericScreen(Color.Green, currentName, goToBlue, goToRed, {})
}

/*BottomAppBar*/
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GenericScreen(color: Color, currentName:String, goToBlue: (String) -> Unit, goToRed: (String) -> Unit, goToGreen: (String) -> Unit) {
    val viewModel: NameViewModel = viewModel()
    viewModel.name.value = currentName
    Scaffold(
        bottomBar = {
            BottomNavigation(backgroundColor = Color.LightGray) {
                ScreenNavigationItem("Blue", viewModel.name, goToBlue)
                ScreenNavigationItem("Red", viewModel.name, goToRed)
                ScreenNavigationItem("Green", viewModel.name, goToGreen)
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
fun RowScope.ScreenNavigationItem(text: String, currentName: State<String>, goToRed: (String) -> Unit) {
    BottomNavigationItem(
        icon = {
            Icon(
                painter = rememberVectorPainter(Icons.Outlined.Star),
                contentDescription = text
            )
        },
        label = { Text(text) },
        selected = false,
        onClick = {goToRed(currentName.value)}
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
