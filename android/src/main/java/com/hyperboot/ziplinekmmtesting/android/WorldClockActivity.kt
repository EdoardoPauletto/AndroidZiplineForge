package com.hyperboot.ziplinekmmtesting.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperboot.ziplinekmmtesting.WorldClockAndroid
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

@NoLiveLiterals
class WorldClockActivity : ComponentActivity() {
    private val scope = MainScope()
    private lateinit var worldClockAndroid: WorldClockAndroid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SchermataZipline()
        }

        worldClockAndroid = WorldClockAndroid(scope)
        worldClockAndroid.start()
    }

    @androidx.compose.runtime.Composable
    fun SchermataZipline(modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier) {
        val model = worldClockAndroid.models.collectAsState()//prende tutti gli stati del flow e usa l'ultimo
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()//occupa tutto lo spazio in larghezza
                .fillMaxHeight(),//e in altezza
            verticalArrangement = Arrangement.Center,//al centro dello schermo (verticalmente)
            horizontalAlignment = Alignment.CenterHorizontally//al centro verticalmente
        ) {
            Text(
                text = model.value.label,//prende il testo dal js
                fontSize = 38.sp,
                textAlign = TextAlign.Left,
            )
            Text(text = "Prova di lable aggiuntiva")
        }
    }

    override fun onDestroy() {
        worldClockAndroid.close()
        scope.cancel()
        super.onDestroy()
    }
}
