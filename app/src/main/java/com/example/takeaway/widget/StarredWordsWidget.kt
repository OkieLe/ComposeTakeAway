package com.example.takeaway.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.takeaway.MainActivity
import com.example.takeaway.R
import com.example.takeaway.data.local.WordsDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

class StarredWordsWidget : GlanceAppWidget(errorUiLayout = R.layout.widget_error_layout) {

    override val sizeMode: SizeMode
        get() = SizeMode.Responsive(
            setOf(
                DpSize(144.dp, 48.dp),
                DpSize(192.dp, 48.dp),
                DpSize(240.dp, 48.dp)
            )
        )

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
    }

    @Preview
    @Composable
    override fun Content() {
        val widgetId = getWidgetId(LocalGlanceId.current)
        val context = LocalContext.current
        val currentWord = currentState<Preferences>()[stringPreferencesKey(widgetId)]
            ?: context.getString(R.string.search_box_hint)
        WidgetContainer {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentWord,
                    modifier = GlanceModifier.wrapContentSize(),
                    style = TextStyle(
                        color = ColorProvider(R.color.purple_500),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    modifier = GlanceModifier.clickable(actionRunCallback<RefreshWordActionCallback>()).padding(4.dp),
                    provider = ImageProvider(resId = R.drawable.ic_refresh),
                    contentDescription = context.getString(R.string.refresh_label)
                )
            }
        }
    }

    @Composable
    private fun WidgetContainer(modifier: GlanceModifier = GlanceModifier, content: @Composable () -> Unit) {
        val prefsState = currentState<Preferences>()
        val prefsKey = stringPreferencesKey(getWidgetId(LocalGlanceId.current))
        val currentWord = prefsState[prefsKey] ?: ""
        Box(
            modifier = modifier.fillMaxSize()
                .background(ImageProvider(R.drawable.widget_background))
                .appWidgetBackground()
                .clickable(
                    actionStartActivity(
                        MainActivity::class.java,
                        actionParametersOf(starredWordActionKey to currentWord)
                    )
                )
                .padding(10.dp),
            content = content
        )
    }

    companion object {

        const val DEFAULT_WORD = "hello"
        private const val KEY_CURRENT_WORD = "currentWord"
        private val starredWordActionKey = ActionParameters.Key<String>(KEY_CURRENT_WORD)

        fun getWidgetId(glanceId: GlanceId): String {
            return glanceId.toString().takeIf { it.isNotBlank() }.orEmpty()
        }

        fun parseCurrentWord(intent: Intent): String {
            return intent.getStringExtra(KEY_CURRENT_WORD).orEmpty()
        }
    }
}

class RefreshWordActionCallback : ActionCallback {

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val word = getNewWord(context)
        val widgetId = StarredWordsWidget.getWidgetId(glanceId)
        updateAppWidgetState(
            context = context,
            definition = PreferencesGlanceStateDefinition,
            glanceId = glanceId
        ) { preferences ->
            preferences.toMutablePreferences()
                .apply {
                    this[stringPreferencesKey(widgetId)] = word
                }
        }

        StarredWordsWidget().update(context, glanceId)
    }

    private suspend fun getNewWord(context: Context): String {
        val wordDao = WordsDatabase.getInstance(context).wordDao()
        val starred = wordDao.getAllWords().firstOrNull()
        val count = if (starred?.isNotEmpty() == true) starred.size else return StarredWordsWidget.DEFAULT_WORD
        return starred[Random.nextInt(0, count)]
    }
}
