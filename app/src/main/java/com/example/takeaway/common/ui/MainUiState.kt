package com.example.takeaway.common.ui

import android.content.res.Resources
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.takeaway.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
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

    private var waitEndAnimationJob: Job? = null
    fun navigateUp() {
        navController.navigateUp()
    }

    fun openTab(route: String) {
        val navigate = {
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
        // if we're already waiting for an other screen to start appearing
        // we need to cancel that job
        waitEndAnimationJob?.cancel()
        if (navController.visibleEntries.value.count() > 1) {
            // if navController.visibleEntries has more than one item
            // we need to wait animation to finish before starting next navigation
            waitEndAnimationJob = coroutineScope.launch {
                navController.visibleEntries
                    .collect { visibleEntries ->
                        if (visibleEntries.count() == 1) {
                            navigate()
                            waitEndAnimationJob = null
                            cancel()
                        }
                    }
            }
        } else {
            // otherwise we can navigate instantly
            navigate()
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

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message)
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
sealed class Screen(val route: String, @DrawableRes val icon: Int, @StringRes val resourceId: Int) {
    object Search : Screen("Search", R.drawable.ic_letter_english, R.string.search_label)
    object Chinese : Screen("Chinese", R.drawable.ic_letter_chinese, R.string.chinese_label)
    object Starred : Screen("Starred", R.drawable.ic_star, R.string.star_label)
    object Word : Screen("Word", R.drawable.ic_word_info, R.string.word_label)
    object About : Screen("About", R.drawable.ic_about, R.string.about_label)
    companion object {

        val items = listOf(Search, Chinese, About)
    }
}
