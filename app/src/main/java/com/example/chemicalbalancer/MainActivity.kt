package com.example.chemicalbalancer

import Equation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chemicalbalancer.ui.theme.ChemicalBalancerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChemicalBalancerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Cyan),
                    color = Color.Cyan
                ) {
                    val isExceptionDialogOpened = remember { mutableStateOf(false) }
                    val exceptionMessage = remember { mutableStateOf("") }
                    Column(
                        Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val equationInput = remember { mutableStateOf("") }
                        val balancedEquation = remember { mutableStateOf("Тут будет ответ") }

                        Spacer(Modifier.weight(0.7f))
                        Text(
                            text = "Как пользоваться?\nНужно ввести уравнение без коэффициентов, например, так:" +
                                    "\nCu(OH)2 + HCl = CuCl2+H2O\nВещества разделятся знаком +, а части уравнения – знаком =",
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Black)
                                .padding(10.dp),
                            textAlign = TextAlign.Center

                        )
                        Text(
                            text = balancedEquation.value,
                            modifier = Modifier.weight(3f),
                            fontSize = 20.sp
                        )
                        Box(
                            Modifier
                                .width(IntrinsicSize.Max)
                                .weight(2f)
                                .background(Color.Cyan)
                                .fillMaxSize()
                        )
                        TextField(
                            value = equationInput.value,
                            onValueChange = { newText -> equationInput.value = newText },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            placeholder = { Text("Cu(OH)2 + HCl = CuCl2+H2O") }
                        )
                        Box(
                            Modifier
                                .weight(2f)
                                // .background(Color.Cyan)
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            Button(onClick = {
                                try {
                                    val eq = Equation(equationInput.value)
                                    eq.balance()
                                    if (!eq.coefVector.all { it >= 0 }) {
                                        for (i in 1..10) {
                                            eq.shuffle()
                                            eq.balance()
                                            if (eq.coefVector.all { it >= 0 })
                                                break
                                        }
                                    }
                                    balancedEquation.value = eq.toString()
                                    //println(balancedEquation.value)
                                } catch (e: Exception) {
                                    isExceptionDialogOpened.value = true
                                    exceptionMessage.value =
                                        e.message ?: "Произошла непредвиденная ошибка"
                                }
                            }, modifier = Modifier.fillMaxSize()) {
                                Text("Уравнять", fontSize = 20.sp)
                            }
                        }
                    }
                    if (isExceptionDialogOpened.value) {
                        AlertDialog(
                            onDismissRequest = { isExceptionDialogOpened.value = false },
                            title = { Text("Ошибка") },
                            text = { Text(exceptionMessage.value) },
                            buttons = {
                                Button(onClick = { isExceptionDialogOpened.value = false }) {
                                    Text("OK")
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChemicalBalancerTheme {
        Greeting("Android")
    }
}