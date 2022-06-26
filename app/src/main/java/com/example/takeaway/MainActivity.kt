package com.example.takeaway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.takeaway.about.AboutScreen
import com.example.takeaway.chinese.ChineseScreen
import com.example.takeaway.common.ui.MainUiState
import com.example.takeaway.common.ui.Screen
import com.example.takeaway.common.ui.rememberMainUiState
import com.example.takeaway.design.AudioPlayer
import com.example.takeaway.design.LocalAudioPlayer
import com.example.takeaway.design.theme.TakeAwaySampleTheme
import com.example.takeaway.search.SearchScreen
import com.example.takeaway.starred.StarredScreen
import com.example.takeaway.widget.StarredWordsWidget
import com.example.takeaway.word.WordScreen
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeAwaySampleTheme {
                val uiState = rememberMainUiState()
                Scaffold(
                    scaffoldState = uiState.scaffoldState,
                    bottomBar = { BottomNavigationBar(uiState) }
                ) { innerPadding ->
                    CompositionLocalProvider(
                        LocalAudioPlayer provides AudioPlayer(ExoPlayer.Builder(this).build())
                    ) {
                        NavHost(
                            navController = uiState.navController,
                            startDestination = Screen.Search.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Search.route) { SearchScreen(uiState) }
                            composable(Screen.Chinese.route) { ChineseScreen(uiState) }
                            composable(Screen.Starred.route) { StarredScreen(uiState) }
                            composable(
                                Screen.Word.route + "/{word}",
                                arguments = listOf(
                                    navArgument("word") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                WordScreen(
                                    uiState,
                                    backStackEntry.arguments?.getString("word").orEmpty()
                                )
                            }
                            composable(Screen.About.route) { AboutScreen() }
                        }
                    }
                }
                LaunchedEffect(intent) {
                    StarredWordsWidget.parseCurrentWord(intent).takeIf { it.isNotBlank() }?.let {
                        uiState.openScreen(Screen.Word.route + "/$it")
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomNavigationBar(uiState: MainUiState) {
        if (uiState.shouldShowBottomBar) {
            BottomNavigation {
                val navBackStackEntry by uiState.navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                uiState.bottomBarTabs.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(painter = painterResource(screen.icon), contentDescription = null)
                        },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            uiState.openTab(screen.route)
                        }
                    )
                }
            }
        }
    }
}
