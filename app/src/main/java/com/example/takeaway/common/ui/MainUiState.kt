package com.example.takeaway.common.ui

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.takeaway.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberMainUiState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, resources, coroutineScope) {
        MainUiState(scaffoldState, navController, resources, coroutineScope)
    }

@Stable
class MainUiState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val resources: Resources,
    private val coroutineScope: CoroutineScope
) {
    val bottomBarTabs = Screen.items
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    private val currentRoute: String?
        get() = navController.currentDestination?.route

    fun navigateUp() {
        navController.navigateUp()
    }

    fun openTab(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun openScreen(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
            }
        }
    }

    fun showSnackbar(@StringRes messageId: Int) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(resources.getString(messageId))
        }
    }
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object Search : Screen("Search", Icons.Filled.Search, R.string.search_label)
    object Starred: Screen("Starred", Icons.Filled.Star, R.string.star_label)
    object Word: Screen("Word", Icons.Filled.PlayArrow, R.string.word_label)
    object About : Screen("About", Icons.Filled.Info, R.string.about_label)

    companion object {
        val items = listOf(Search, About)
    }
}
