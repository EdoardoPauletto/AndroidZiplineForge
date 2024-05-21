import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MiaApp(modifier: Modifier = Modifier){
    //rememberSaveable lo salva anche in caso di rotazione schermo o altre cose strane
    var mostraBenvenuto by rememberSaveable { mutableStateOf(true) } //by è più comodo perchè non necessita del .value
    Surface(modifier) {
        if (mostraBenvenuto) {
            SchermataBenvenuto(onContinuaClicked = {mostraBenvenuto = false})//lancio e passo la funzione da eseguire al click del pulsante, così non devo passare variabili che rimangono private
        } else {
            App()
        }
    }
}

@Composable
fun SchermataBenvenuto(
    modifier: Modifier = Modifier,
    onContinuaClicked:() -> Unit
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Benvenuto nel prototipo di Zipline")
        Button(
            modifier = modifier.padding(vertical = 24.dp),
            onClick = onContinuaClicked
        ) {
            Text("Continua")
        }
    }
}