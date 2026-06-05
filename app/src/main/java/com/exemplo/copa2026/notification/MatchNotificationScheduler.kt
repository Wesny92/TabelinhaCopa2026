package com.exemplo.copa2026.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.exemplo.copa2026.data.model.Partida
import com.exemplo.copa2026.data.model.PartidaMataMata
import com.exemplo.copa2026.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object MatchNotificationScheduler {

    private val meses = mapOf(
        "Jan" to 1, "Fev" to 2, "Mar" to 3, "Abr" to 4,
        "Mai" to 5, "Jun" to 6, "Jul" to 7, "Ago" to 8,
        "Set" to 9, "Out" to 10, "Nov" to 11, "Dez" to 12
    )

    private val formatter = DateTimeFormatter.ofPattern("d MMM")
        .withLocale(java.util.Locale("pt", "BR"))

    fun schedule(context: Context, container: AppContainer) {
        CoroutineScope(Dispatchers.IO).launch {
            val partidaDao = container.database.partidaDao()
            val mataMataDao = container.database.partidaMataMataDao()
            val timeDao = container.database.timeDao()

            val times = timeDao.getAllTimes().first()
            val timeMap = times.associateBy { it.id }

            val partidas = partidaDao.getAllPartidas().first()
                .filter { !it.realizado }
                .map { p ->
                    val casa = timeMap[p.timeCasaId]
                    val fora = timeMap[p.timeForaId]
                    Quad(
                        casa?.nome ?: "?",
                        casa?.bandeiraEmoji ?: "",
                        fora?.nome ?: "?",
                        fora?.bandeiraEmoji ?: ""
                    ) to p
                }

            val mataMata = mataMataDao.getAllPartidasMataMata().first()
                .filter { !it.realizado }
                .map { mm ->
                    val casa = mm.timeCasaId?.let { timeMap[it] }
                    val fora = mm.timeForaId?.let { timeMap[it] }
                    Quad(
                        casa?.nome ?: "A definir",
                        casa?.bandeiraEmoji ?: "",
                        fora?.nome ?: "A definir",
                        fora?.bandeiraEmoji ?: ""
                    ) to mm
                }

            schedulePartidas(context, partidas.map { (quad, p) ->
                MatchInfo(
                    id = "g${p.id}",
                    timeCasa = quad.a, emojiCasa = quad.b,
                    timeFora = quad.c, emojiFora = quad.d,
                    horario = p.horario, data = p.data, estadio = p.estadio
                )
            })

            schedulePartidas(context, mataMata.map { (quad, mm) ->
                MatchInfo(
                    id = "m${mm.id}",
                    timeCasa = quad.a, emojiCasa = quad.b,
                    timeFora = quad.c, emojiFora = quad.d,
                    horario = mm.horario, data = mm.data, estadio = mm.estadio
                )
            })
        }
    }

    private fun schedulePartidas(context: Context, matches: List<MatchInfo>) {
        val now = System.currentTimeMillis()
        val workManager = WorkManager.getInstance(context)

        matches.forEach { match ->
            val kickoff = parseDateTime(match.data, match.horario) ?: return@forEach
            val notifyTime = kickoff - (5 * 60 * 1000) // 5 minutos antes
            val delay = notifyTime - now

            if (delay <= 0) return@forEach // ja passou

            val data = Data.Builder()
                .putString("timeCasa", match.timeCasa)
                .putString("emojiCasa", match.emojiCasa)
                .putString("timeFora", match.timeFora)
                .putString("emojiFora", match.emojiFora)
                .putString("horario", match.horario)
                .putString("estadio", match.estadio)
                .build()

            val request = OneTimeWorkRequestBuilder<MatchNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("match_notification")
                .build()

            workManager.enqueueUniqueWork(
                match.id,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }

    private fun parseDateTime(data: String, horario: String): Long? {
        return try {
            val partesHora = horario.split(":")
            val hora = partesHora[0].toInt()
            val minuto = partesHora[1].toInt()

            val partesData = data.split(" ")
            val dia = partesData[0].toInt()
            val mesNome = partesData[1]
            val mes = meses[mesNome] ?: return null

            val localDateTime = LocalDateTime.of(2026, mes, dia, hora, minuto)
            val zoneId = ZoneId.of("America/Sao_Paulo")
            localDateTime.atZone(zoneId).toInstant().toEpochMilli()
        } catch (e: Exception) {
            null
        }
    }

    private data class Quad(val a: String, val b: String, val c: String, val d: String)

    private data class MatchInfo(
        val id: String,
        val timeCasa: String,
        val emojiCasa: String,
        val timeFora: String,
        val emojiFora: String,
        val horario: String,
        val data: String,
        val estadio: String
    )
}
