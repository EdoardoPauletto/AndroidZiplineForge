package it.uninsubria.prototype.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.uninsubria.prototype.WorldClockAndroid
import it.uninsubria.prototype.android.ui.theme.PrototypeTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

@NoLiveLiterals
class WorldClockActivity : ComponentActivity() {
    private val scope = MainScope()
    private lateinit var worldClockAndroid: WorldClockAndroid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PrototypeTheme {
                //SchermataZipline()
                SchermataQuiz()
            }
        }

        worldClockAndroid = WorldClockAndroid(scope)
        //worldClockAndroid.start()
        worldClockAndroid.startTrivia()
    }

    @Composable
    fun SchermataZipline(modifier: Modifier = Modifier) {
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
        }
    }

    @Composable
    fun SchermataQuiz(modifier: Modifier = Modifier) {
        val objJS = worldClockAndroid.trivia.collectAsState()
        if (objJS.value != null) {
            val mazziere = objJS.value
            if (mazziere != null){
                val gioca = mazziere.games()[0]
                var domandaNum by rememberSaveable { mutableIntStateOf(0) }
                var punteggio = 0
                var rispEsatte by rememberSaveable { mutableStateOf(BooleanArray(gioca.question.size)) }
                //aggiungere domande tenendo in memoria quelle già giuste
                val tmp = BooleanArray(gioca.question.size)
                val totDomande: Int = if (rispEsatte.size > tmp.size) tmp.size else rispEsatte.size
                var i = 0
                while (i < totDomande){
                    tmp[i] = rispEsatte[i]
                    i++
                }
                rispEsatte = tmp

                for (risposta in rispEsatte){
                    if (risposta) punteggio++
                }

                var answer by remember { mutableStateOf("") }
                var control by remember { mutableStateOf(false) }
                Surface(modifier.fillMaxSize()) { //serve per circondare tutto con il tema
                    Column(
                        modifier = modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = modifier.padding(15.dp),
                            text = gioca.question[domandaNum].text
                        )
                        OutlinedTextField(
                            value = answer,
                            onValueChange = { answer = it },
                            label = { Text("Risposta") },
                            //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = modifier.fillMaxWidth()
                        )
                        Button(
                            modifier = modifier.padding(top = 15.dp),
                            onClick = { control = true },
                            enabled = !rispEsatte[domandaNum]
                        ) {
                            Text(text = "Invia")
                        }
                        if (control) {
                            val result =
                                mazziere.answer(gioca.id, gioca.question[domandaNum].id, answer)
                            AlertDialog(
                                onDismissRequest = { control = false },
                                confirmButton = {
                                    Button(onClick = { control = false })
                                    {
                                        Text("Ok")
                                    }
                                },
                                text = { Text(result.message) }
                            )
                            if (result.correct) {
                                rispEsatte[domandaNum] = true
                                punteggio = 0
                                for (risposta in rispEsatte) {
                                    if (risposta) punteggio++
                                }
                                //enable = false
                            } /*else {
                             Toast.makeText(LocalContext.current, "Errato", Toast.LENGTH_SHORT).show()
                         }*/
                        }
                        Button(
                            modifier = modifier.padding(top = 35.dp),
                            onClick = {
                                if (domandaNum < gioca.question.size - 1) domandaNum++
                                else domandaNum = 0
                                answer = ""
                            }
                        ) {
                            Text(text = "Prossima domanda ->")
                        }
                        Text(
                            modifier = modifier.padding(top = 15.dp),
                            text = "Punteggio: $punteggio/${gioca.question.size}"
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    fontSize = 38.sp,
                    text = "Caricamento..."
                )
            }
        }
    }

    override fun onDestroy() {
        worldClockAndroid.close()
        scope.cancel()
        super.onDestroy()
    }
}
