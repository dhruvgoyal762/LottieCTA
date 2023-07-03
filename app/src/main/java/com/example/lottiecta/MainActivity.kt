package com.example.lottiecta

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieRetrySignal
import com.example.lottiecta.ui.theme.LottieCTATheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottieCTATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(), color = MaterialTheme.colors
                        .background
                ) {
                    CTA()
                }
            }
        }
    }
}

fun getJson(url: URL): String {
    val sb = StringBuffer()
    var line: String?
    val httpURLConnection: HttpURLConnection? = url.openConnection() as? HttpURLConnection
    val inputStream: InputStream? = httpURLConnection?.inputStream

    val bufferedReader = BufferedReader(InputStreamReader(inputStream))

    while (bufferedReader.readLine().also { line = it } != null) {
        sb.append(line)
    }
    return sb.toString()
}

@Composable
fun CTA() {

    val coroutineScope = rememberCoroutineScope()
    var json by remember {
        mutableStateOf("")
    }
    var oldValue by remember {
        mutableStateOf("Cta Text")
    }
    LaunchedEffect(key1 = null, block = {
        coroutineScope.launch(Dispatchers.IO) {
            json = ""
        }
    })
    val retrySignalLottieBtn = rememberLottieRetrySignal()
    val lottieBtnComposition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(json),
        onRetry = { _, exception ->
            Log.i("lottie", exception.message.toString())
            retrySignalLottieBtn.awaitRetry()
            true
        })


    var text by remember {
        mutableStateOf("")
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = lottieBtnComposition,
            isPlaying = true,
            iterations = 10000
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(value = text, onValueChange = {
            text = it
        })

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            json = json.replace(oldValue, text)
            oldValue = text
        }) {
            Text(text = "Update Text")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LottieCTATheme {
        CTA()
    }
}