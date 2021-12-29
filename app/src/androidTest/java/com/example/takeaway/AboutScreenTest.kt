package com.example.takeaway

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.takeaway.about.AboutScreen
import com.example.takeaway.design.theme.TakeAwaySampleTheme
import com.example.takeaway.utils.assertScreenshotMatchesGolden
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

class AboutScreenTest {
    private val flag = "About"

    @get:Rule
    val testName = TestName()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenAboutScreen_whenDisplay_thenScreenshotMatches() {
        composeTestRule.setContent {
            TakeAwaySampleTheme(darkTheme = true) {
                Box(modifier = Modifier.semantics { testTag = flag }) {
                    AboutScreen()
                }
            }
        }

        assertScreenshotMatchesGolden(testName.methodName, composeTestRule.onNodeWithTag(flag))
    }
}
