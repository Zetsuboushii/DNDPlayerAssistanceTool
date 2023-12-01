package de.zetsu.dndplayerassistancetool

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

fun fetchData(onResult: (String) -> Unit) {
    val fileUrl =
        "https://raw.githubusercontent.com/Zetsuboushii/DNDPlayerAssistanceTool/master/NextSession"

    runBlocking(Dispatchers.IO) {
        try {
            val url = URL(fileUrl)
            val connection = url.openConnection()
            connection.connect()

            val bufferedReader = BufferedReader(InputStreamReader(connection.getInputStream()))
            val urlContent = bufferedReader.use { it.readText() }

            onResult(urlContent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}