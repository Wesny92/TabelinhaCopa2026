package com.exemplo.copa2026.data.local

import com.exemplo.copa2026.data.model.Grupo
import com.exemplo.copa2026.data.model.Partida
import com.exemplo.copa2026.data.model.PartidaMataMata
import com.exemplo.copa2026.data.model.Time

object PrePopulateData {

    suspend fun populate(db: AppDatabase) {
        val grupoDao = db.grupoDao()
        val timeDao = db.timeDao()
        val partidaDao = db.partidaDao()
        val mataMataDao = db.partidaMataMataDao()

        val grupos = ('A'..'L').mapIndexed { index, letra ->
            Grupo(
                id = index + 1,
                letra = letra.toString(),
                nome = "Grupo $letra"
            )
        }
        grupoDao.insertGrupos(grupos)

        val times = listOf(
            // Grupo A - México, South Africa, South Korea, Czech Republic
            Time(1, "México", "MEX", "\uD83C\uDDF2\uD83C\uDDFD", grupoId = 1),
            Time(2, "África do Sul", "RSA", "\uD83C\uDDFF\uD83C\uDDE6", grupoId = 1),
            Time(3, "Coreia do Sul", "KOR", "\uD83C\uDDF0\uD83C\uDDF7", grupoId = 1),
            Time(4, "República Tcheca", "CZE", "\uD83C\uDDE8\uD83C\uDDFF", grupoId = 1),
            // Grupo B - Canadá, Bosnia, Qatar, Switzerland
            Time(5, "Canadá", "CAN", "\uD83C\uDDE8\uD83C\uDDE6", grupoId = 2),
            Time(6, "Bósnia e Herzegovina", "BIH", "\uD83C\uDDE7\uD83C\uDDE6", grupoId = 2),
            Time(7, "Catar", "QAT", "\uD83C\uDDF6\uD83C\uDDE6", grupoId = 2),
            Time(8, "Suíça", "SUI", "\uD83C\uDDE8\uD83C\uDDED", grupoId = 2),
            // Grupo C - Brazil, Morocco, Haiti, Scotland
            Time(9, "Brasil", "BRA", "\uD83C\uDDE7\uD83C\uDDF7", grupoId = 3),
            Time(10, "Marrocos", "MAR", "\uD83C\uDDF2\uD83C\uDDE6", grupoId = 3),
            Time(11, "Haiti", "HAI", "\uD83C\uDDED\uD83C\uDDF9", grupoId = 3),
            Time(12, "Escócia", "SCO", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F", grupoId = 3),
            // Grupo D - USA, Paraguay, Austrália, Turkey
            Time(13, "Estados Unidos", "USA", "\uD83C\uDDFA\uD83C\uDDF8", grupoId = 4),
            Time(14, "Paraguai", "PAR", "\uD83C\uDDF5\uD83C\uDDFE", grupoId = 4),
            Time(15, "Austrália", "AUS", "\uD83C\uDDE6\uD83C\uDDFA", grupoId = 4),
            Time(16, "Turquia", "TUR", "\uD83C\uDDF9\uD83C\uDDF7", grupoId = 4),
            // Grupo E - Germany, Curaçao, Ivory Coast, Ecuador
            Time(17, "Alemanha", "GER", "\uD83C\uDDE9\uD83C\uDDEA", grupoId = 5),
            Time(18, "Curaçao", "CUW", "\uD83C\uDDE8\uD83C\uDDFC", grupoId = 5),
            Time(19, "Costa do Marfim", "CIV", "\uD83C\uDDE8\uD83C\uDDEE", grupoId = 5),
            Time(20, "Equador", "ECU", "\uD83C\uDDEA\uD83C\uDDE8", grupoId = 5),
            // Grupo F - Netherlands, Japan, Sweden, Tunísia
            Time(21, "Países Baixos", "NED", "\uD83C\uDDF3\uD83C\uDDF1", grupoId = 6),
            Time(22, "Japão", "JPN", "\uD83C\uDDEF\uD83C\uDDF5", grupoId = 6),
            Time(23, "Suécia", "SWE", "\uD83C\uDDF8\uD83C\uDDEA", grupoId = 6),
            Time(24, "Tunísia", "TUN", "\uD83C\uDDF9\uD83C\uDDF3", grupoId = 6),
            // Grupo G - Belgium, Egypt, Iran, New Zealand
            Time(25, "Bélgica", "BEL", "\uD83C\uDDE7\uD83C\uDDEA", grupoId = 7),
            Time(26, "Egito", "EGY", "\uD83C\uDDEA\uD83C\uDDEC", grupoId = 7),
            Time(27, "Irã", "IRN", "\uD83C\uDDEE\uD83C\uDDF7", grupoId = 7),
            Time(28, "Nova Zelândia", "NZL", "\uD83C\uDDF3\uD83C\uDDFF", grupoId = 7),
            // Grupo H - Spain, Cape Verde, Saudi Arabia, Uruguay
            Time(29, "Espanha", "ESP", "\uD83C\uDDEA\uD83C\uDDF8", grupoId = 8),
            Time(30, "Cabo Verde", "CPV", "\uD83C\uDDE8\uD83C\uDDFB", grupoId = 8),
            Time(31, "Arábia Saudita", "KSA", "\uD83C\uDDF8\uD83C\uDDE6", grupoId = 8),
            Time(32, "Uruguai", "URU", "\uD83C\uDDFA\uD83C\uDDFE", grupoId = 8),
            // Grupo I - France, Senegal, Iraq, Norway
            Time(33, "França", "FRA", "\uD83C\uDDEB\uD83C\uDDF7", grupoId = 9),
            Time(34, "Senegal", "SEN", "\uD83C\uDDF8\uD83C\uDDF3", grupoId = 9),
            Time(35, "Iraque", "IRQ", "\uD83C\uDDEE\uD83C\uDDF6", grupoId = 9),
            Time(36, "Noruega", "NOR", "\uD83C\uDDF3\uD83C\uDDF4", grupoId = 9),
            // Grupo J - Argentina, Algeria, Áustria, Jordan
            Time(37, "Argentina", "ARG", "\uD83C\uDDE6\uD83C\uDDF7", grupoId = 10),
            Time(38, "Argélia", "ALG", "\uD83C\uDDE9\uD83C\uDDFF", grupoId = 10),
            Time(39, "Áustria", "AUT", "\uD83C\uDDE6\uD83C\uDDF9", grupoId = 10),
            Time(40, "Jordânia", "JOR", "\uD83C\uDDEF\uD83C\uDDF4", grupoId = 10),
            // Grupo K - Portugal, DR Congo, Uzbekistan, Colômbia
            Time(41, "Portugal", "POR", "\uD83C\uDDF5\uD83C\uDDF9", grupoId = 11),
            Time(42, "RD Congo", "COD", "\uD83C\uDDE8\uD83C\uDDE9", grupoId = 11),
            Time(43, "Uzbequistão", "UZB", "\uD83C\uDDFA\uD83C\uDDFF", grupoId = 11),
            Time(44, "Colômbia", "COL", "\uD83C\uDDE8\uD83C\uDDF4", grupoId = 11),
            // Grupo L - England, Croatia, Ghana, Panamá
            Time(45, "Inglaterra", "ENG", "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F", grupoId = 12),
            Time(46, "Croácia", "CRO", "\uD83C\uDDED\uD83C\uDDF7", grupoId = 12),
            Time(47, "Gana", "GHA", "\uD83C\uDDEC\uD83C\uDDED", grupoId = 12),
            Time(48, "Panamá", "PAN", "\uD83C\uDDF5\uD83C\uDDE6", grupoId = 12)
        )
        timeDao.insertTimes(times)

        // Mapa de times por grupo: grupoId -> lista de timeIds (ordem de insercao: pos 0-3)
        val timesPorGrupo = times.groupBy { it.grupoId }.mapValues { it.value.map { t -> t.id } }
        fun t(grupoId: Int, pos: Int) = timesPorGrupo[grupoId]!![pos]

        data class DadosJogo(val id: Int, val g: Int, val p1: Int, val p2: Int, val r: Int,
                             val d: String, val h: String, val e: String, val c: String, val pa: String, val tr: String = "CazeTV")
        val jogos = listOf(
            // Grupo A (MEX 0, RSA 1, KOR 2, CZE 3) - BRASILIA times
            DadosJogo(1,  1, 0, 1, 1, "11 Jun (Quinta-feira)", "16:00", "Estadio Azteca", "Cidade do México", "México", "CazeTV,Globo"),
            DadosJogo(2,  1, 2, 3, 1, "11 Jun (Quinta-feira)", "23:00", "Estadio Akron", "Guadalajara (Zapopan)", "México"),
            DadosJogo(25, 1, 3, 1, 2, "18 Jun (Quinta-feira)", "13:00", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            DadosJogo(28, 1, 0, 2, 2, "18 Jun (Quinta-feira)", "22:00", "Estadio Akron", "Guadalajara (Zapopan)", "México", "CazeTV,Globo"),
            DadosJogo(53, 1, 3, 0, 3, "24 Jun (Quarta-feira)", "22:00", "Estadio Azteca", "Cidade do México", "México"),
            DadosJogo(54, 1, 1, 2, 3, "24 Jun (Quarta-feira)", "22:00", "Estadio BBVA", "Monterrey (Guadalupe)", "México"),
            // Grupo B (CAN 0, BIH 1, QAT 2, SUI 3) - BRASILIA times
            DadosJogo(3,  2, 0, 1, 1, "12 Jun (Sexta-feira)", "16:00", "BMO Field", "Toronto", "Canadá"),
            DadosJogo(8,  2, 2, 3, 1, "13 Jun (Sábado)", "16:00", "Levi's Stadium", "São Francisco (Santa Clara)", "EUA"),
            DadosJogo(26, 2, 3, 1, 2, "18 Jun (Quinta-feira)", "16:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA", "CazeTV,Globo"),
            DadosJogo(27, 2, 0, 2, 2, "18 Jun (Quinta-feira)", "19:00", "BC Place", "Vancouver", "Canadá"),
            DadosJogo(51, 2, 3, 0, 3, "24 Jun (Quarta-feira)", "16:00", "BC Place", "Vancouver", "Canadá"),
            DadosJogo(52, 2, 1, 2, 3, "24 Jun (Quarta-feira)", "16:00", "Lumen Field", "Seattle", "EUA"),
            // Grupo C (BRA 0, MAR 1, HAI 2, SCO 3) - BRASILIA times
            DadosJogo(7,  3, 0, 1, 1, "13 Jun (Sábado)", "19:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA", "CazeTV,Globo"),
            DadosJogo(5,  3, 2, 3, 1, "13 Jun (Sábado)", "22:00", "Gillette Stadium", "Boston (Foxborough)", "EUA"),
            DadosJogo(30, 3, 3, 1, 2, "19 Jun (Sexta-feira)", "19:00", "Gillette Stadium", "Boston (Foxborough)", "EUA"),
            DadosJogo(29, 3, 0, 2, 2, "19 Jun (Sexta-feira)", "21:30", "Lincoln Financial Field", "Filadélfia", "EUA", "CazeTV,Globo"),
            DadosJogo(49, 3, 3, 0, 3, "24 Jun (Quarta-feira)", "19:00", "Hard Rock Stadium", "Miami (Miami Gardens)", "EUA"),
            DadosJogo(50, 3, 1, 2, 3, "24 Jun (Quarta-feira)", "19:00", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            // Grupo D (USA 0, PAR 1, AUS 2, TUR 3) - BRASILIA times
            DadosJogo(4,  4, 0, 1, 1, "12 Jun (Sexta-feira)", "22:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA", "CazeTV,Globo"),
            DadosJogo(6,  4, 2, 3, 1, "14 Jun (Domingo)", "01:00", "BC Place", "Vancouver", "Canadá", "CazeTV,Globo"),
            DadosJogo(32, 4, 0, 2, 2, "19 Jun (Sexta-feira)", "16:00", "Lumen Field", "Seattle", "EUA"),
            DadosJogo(31, 4, 3, 1, 2, "20 Jun (Sábado)", "00:00", "Levi's Stadium", "São Francisco (Santa Clara)", "EUA", "CazeTV,Globo"),
            DadosJogo(59, 4, 3, 0, 3, "25 Jun (Quinta-feira)", "23:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA"),
            DadosJogo(60, 4, 1, 2, 3, "25 Jun (Quinta-feira)", "23:00", "Levi's Stadium", "São Francisco (Santa Clara)", "EUA"),
            // Grupo E (GER 0, CUW 1, CIV 2, ECU 3) - BRASILIA times
            DadosJogo(10, 5, 0, 1, 1, "14 Jun (Domingo)", "14:00", "NRG Stadium", "Houston", "EUA"),
            DadosJogo(9,  5, 2, 3, 1, "14 Jun (Domingo)", "20:00", "Lincoln Financial Field", "Filadélfia", "EUA", "CazeTV,Globo"),
            DadosJogo(33, 5, 0, 2, 2, "20 Jun (Sábado)", "17:00", "BMO Field", "Toronto", "Canadá", "CazeTV,Globo"),
            DadosJogo(34, 5, 3, 1, 2, "20 Jun (Sábado)", "21:00", "Arrowhead Stadium", "Kansas City", "EUA"),
            DadosJogo(55, 5, 3, 0, 3, "25 Jun (Quinta-feira)", "17:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA"),
            DadosJogo(56, 5, 1, 2, 3, "25 Jun (Quinta-feira)", "17:00", "Lincoln Financial Field", "Filadélfia", "EUA"),
            // Grupo F (NED 0, JPN 1, SWE 2, TUN 3) - BRASILIA times
            DadosJogo(11, 6, 0, 1, 1, "14 Jun (Domingo)", "17:00", "AT&T Stadium", "Dallas (Arlington)", "EUA", "CazeTV,Globo"),
            DadosJogo(12, 6, 2, 3, 1, "14 Jun (Domingo)", "23:00", "Estadio BBVA", "Monterrey (Guadalupe)", "México", "CazeTV,Globo"),
            DadosJogo(35, 6, 0, 2, 2, "20 Jun (Sábado)", "14:00", "NRG Stadium", "Houston", "EUA"),
            DadosJogo(36, 6, 3, 1, 2, "20 Jun (Sábado)", "23:00", "Estadio BBVA", "Monterrey (Guadalupe)", "México", "CazeTV,Globo"),
            DadosJogo(57, 6, 1, 2, 3, "25 Jun (Quinta-feira)", "20:00", "AT&T Stadium", "Dallas (Arlington)", "EUA"),
            DadosJogo(58, 6, 3, 0, 3, "25 Jun (Quinta-feira)", "20:00", "Arrowhead Stadium", "Kansas City", "EUA"),
            // Grupo G (BEL 0, EGY 1, IRN 2, NZL 3) - BRASILIA times
            DadosJogo(16, 7, 0, 1, 1, "15 Jun (Segunda-feira)", "16:00", "Lumen Field", "Seattle", "EUA", "CazeTV,Globo"),
            DadosJogo(15, 7, 2, 3, 1, "15 Jun (Segunda-feira)", "22:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA"),
            DadosJogo(39, 7, 0, 2, 2, "21 Jun (Domingo)", "16:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA"),
            DadosJogo(40, 7, 3, 1, 2, "21 Jun (Domingo)", "22:00", "BC Place", "Vancouver", "Canadá", "CazeTV,Globo"),
            DadosJogo(63, 7, 1, 2, 3, "27 Jun (Sábado)", "00:00", "Lumen Field", "Seattle", "EUA"),
            DadosJogo(64, 7, 3, 0, 3, "27 Jun (Sábado)", "00:00", "BC Place", "Vancouver", "Canadá"),
            // Grupo H (ESP 0, CPV 1, KSA 2, URU 3) - BRASILIA times
            DadosJogo(14, 8, 0, 1, 1, "15 Jun (Segunda-feira)", "13:00", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            DadosJogo(13, 8, 2, 3, 1, "15 Jun (Segunda-feira)", "19:00", "Hard Rock Stadium", "Miami (Miami Gardens)", "EUA", "CazeTV,Globo"),
            DadosJogo(38, 8, 0, 2, 2, "21 Jun (Domingo)", "13:00", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            DadosJogo(37, 8, 3, 1, 2, "21 Jun (Domingo)", "19:00", "Hard Rock Stadium", "Miami (Miami Gardens)", "EUA", "CazeTV,Globo"),
            DadosJogo(65, 8, 1, 2, 3, "26 Jun (Sexta-feira)", "21:00", "NRG Stadium", "Houston", "EUA"),
            DadosJogo(66, 8, 3, 0, 3, "26 Jun (Sexta-feira)", "21:00", "Estadio Akron", "Guadalajara (Zapopan)", "México"),
            // Grupo I (FRA 0, SEN 1, IRQ 2, NOR 3) - BRASILIA times
            DadosJogo(17, 9, 0, 1, 1, "16 Jun (Terça-feira)", "16:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA", "CazeTV,Globo"),
            DadosJogo(18, 9, 2, 3, 1, "16 Jun (Terça-feira)", "19:00", "Gillette Stadium", "Boston (Foxborough)", "EUA"),
            DadosJogo(42, 9, 0, 2, 2, "22 Jun (Segunda-feira)", "18:00", "Lincoln Financial Field", "Filadélfia", "EUA"),
            DadosJogo(41, 9, 3, 1, 2, "22 Jun (Segunda-feira)", "21:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA", "CazeTV,Globo"),
            DadosJogo(61, 9, 3, 0, 3, "26 Jun (Sexta-feira)", "16:00", "Gillette Stadium", "Boston (Foxborough)", "EUA"),
            DadosJogo(62, 9, 1, 2, 3, "26 Jun (Sexta-feira)", "16:00", "BMO Field", "Toronto", "Canadá"),
            // Grupo J (ARG 0, ALG 1, AUT 2, JOR 3) - BRASILIA times
            DadosJogo(19, 10, 0, 1, 1, "16 Jun (Terça-feira)", "22:00", "Arrowhead Stadium", "Kansas City", "EUA"),
            DadosJogo(20, 10, 2, 3, 1, "17 Jun (Quarta-feira)", "01:00", "Levi's Stadium", "São Francisco (Santa Clara)", "EUA", "CazeTV,Globo"),
            DadosJogo(43, 10, 0, 2, 2, "22 Jun (Segunda-feira)", "14:00", "AT&T Stadium", "Dallas (Arlington)", "EUA", "CazeTV,Globo"),
            DadosJogo(44, 10, 3, 1, 2, "22 Jun (Segunda-feira)", "23:00", "Levi's Stadium", "São Francisco (Santa Clara)", "EUA", "CazeTV,Globo"),
            DadosJogo(69, 10, 1, 2, 3, "27 Jun (Sábado)", "23:00", "Arrowhead Stadium", "Kansas City", "EUA"),
            DadosJogo(70, 10, 3, 0, 3, "27 Jun (Sábado)", "23:00", "AT&T Stadium", "Dallas (Arlington)", "EUA"),
            // Grupo K (POR 0, COD 1, UZB 2, COL 3) - BRASILIA times
            DadosJogo(23, 11, 0, 1, 1, "17 Jun (Quarta-feira)", "14:00", "NRG Stadium", "Houston", "EUA"),
            DadosJogo(24, 11, 2, 3, 1, "17 Jun (Quarta-feira)", "21:00", "Estadio Azteca", "Cidade do México", "México", "CazeTV,Globo"),
            DadosJogo(47, 11, 0, 2, 2, "23 Jun (Terça-feira)", "14:00", "NRG Stadium", "Houston", "EUA"),
            DadosJogo(48, 11, 3, 1, 2, "23 Jun (Terça-feira)", "23:00", "Estadio Akron", "Guadalajara (Zapopan)", "México", "CazeTV,Globo"),
            DadosJogo(71, 11, 3, 0, 3, "27 Jun (Sábado)", "20:30", "Hard Rock Stadium", "Miami (Miami Gardens)", "EUA"),
            DadosJogo(72, 11, 1, 2, 3, "27 Jun (Sábado)", "20:30", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            // Grupo L (ENG 0, CRO 1, GHA 2, PAN 3) - BRASILIA times
            DadosJogo(22, 12, 0, 1, 1, "17 Jun (Quarta-feira)", "17:00", "AT&T Stadium", "Dallas (Arlington)", "EUA", "CazeTV,Globo"),
            DadosJogo(21, 12, 2, 3, 1, "17 Jun (Quarta-feira)", "20:00", "BMO Field", "Toronto", "Canadá"),
            DadosJogo(45, 12, 0, 2, 2, "23 Jun (Terça-feira)", "17:00", "Gillette Stadium", "Boston (Foxborough)", "EUA", "CazeTV,Globo"),
            DadosJogo(46, 12, 3, 1, 2, "23 Jun (Terça-feira)", "20:00", "BMO Field", "Toronto", "Canadá"),
            DadosJogo(67, 12, 3, 0, 3, "27 Jun (Sábado)", "18:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA"),
            DadosJogo(68, 12, 1, 2, 3, "27 Jun (Sábado)", "18:00", "Lincoln Financial Field", "Filadélfia", "EUA")
        )

        val partidas = jogos.map { j ->
            Partida(
                id = j.id,
                timeCasaId = t(j.g, j.p1),
                timeForaId = t(j.g, j.p2),
                rodada = j.r,
                grupoId = j.g,
                data = j.d,
                horario = j.h,
                estadio = j.e,
                cidade = j.c,
                pais = j.pa,
                transmissao = j.tr
            )
        }
        partidaDao.insertPartidas(partidas)

        val mataMata = criarEstruturaMataMata()
        mataMataDao.insertPartidasMataMata(mataMata)
    }

    private fun criarEstruturaMataMata(): List<PartidaMataMata> {
        data class DadosMM(val id: Int, val fase: String, val n: Int, val pos: Int,
                           val pCasa: Int? = null, val pFora: Int? = null,
                           val d: String, val h: String, val e: String, val c: String, val pa: String, val tr: String = "CazeTV")
        return listOf(
            // Round of 32 (73-88) - Horarios oficiais FIFA (Brasilia)
            DadosMM(100+1, "R32",  1, 1,   null, null, "28 Jun (Domingo)", "16:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA"),
            DadosMM(100+2, "R32",  2, 2,   null, null, "29 Jun (Segunda-feira)", "17:30", "Gillette Stadium", "Boston (Foxborough)", "EUA"),
            DadosMM(100+3, "R32",  3, 3,   null, null, "29 Jun (Segunda-feira)", "22:00", "Estadio BBVA", "Monterrey (Guadalupe)", "México"),
            DadosMM(100+4, "R32",  4, 4,   null, null, "29 Jun (Segunda-feira)", "14:00", "NRG Stadium", "Houston", "EUA"),
            DadosMM(100+5, "R32",  5, 5,   null, null, "30 Jun (Terça-feira)", "18:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA"),
            DadosMM(100+6, "R32",  6, 6,   null, null, "30 Jun (Terça-feira)", "14:00", "AT&T Stadium", "Dallas (Arlington)", "EUA"),
            DadosMM(100+7, "R32",  7, 7,   null, null, "30 Jun (Terça-feira)", "22:00", "Estadio Azteca", "Cidade do México", "México"),
            DadosMM(100+8, "R32",  8, 8,   null, null, "1 Jul (Quarta-feira)",  "13:00", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            DadosMM(100+9, "R32",  9, 9,   null, null, "1 Jul (Quarta-feira)",  "21:00", "Levi's Stadium", "São Francisco (Santa Clara)", "EUA"),
            DadosMM(100+10,"R32", 10, 10,  null, null, "1 Jul (Quarta-feira)",  "17:00", "Lumen Field", "Seattle", "EUA"),
            DadosMM(100+11,"R32", 11, 11,  null, null, "2 Jul (Quinta-feira)",  "20:00", "BMO Field", "Toronto", "Canadá"),
            DadosMM(100+12,"R32", 12, 12,  null, null, "2 Jul (Quinta-feira)",  "16:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA"),
            DadosMM(100+13,"R32", 13, 13,  null, null, "2 Jul (Quinta-feira)",  "00:00", "BC Place", "Vancouver", "Canadá"),
            DadosMM(100+14,"R32", 14, 14,  null, null, "3 Jul (Sexta-feira)",  "19:00", "Hard Rock Stadium", "Miami (Miami Gardens)", "EUA"),
            DadosMM(100+15,"R32", 15, 15,  null, null, "3 Jul (Sexta-feira)",  "22:30", "Arrowhead Stadium", "Kansas City", "EUA"),
            DadosMM(100+16,"R32", 16, 16,  null, null, "3 Jul (Sexta-feira)",  "15:00", "AT&T Stadium", "Dallas (Arlington)", "EUA"),
            // Oitavas (89-96) - Horarios oficiais FIFA (Brasilia)
            DadosMM(116+1, "OITAVAS", 1, 1,  102, 105, "4 Jul (Sábado)",  "18:00", "Lincoln Financial Field", "Filadélfia", "EUA"),
            DadosMM(116+2, "OITAVAS", 2, 2,  101, 103, "4 Jul (Sábado)",  "14:00", "NRG Stadium", "Houston", "EUA"),
            DadosMM(116+3, "OITAVAS", 3, 3,  104, 106, "5 Jul (Domingo)",  "17:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA"),
            DadosMM(116+4, "OITAVAS", 4, 4,  107, 108, "5 Jul (Domingo)",  "21:00", "Estadio Azteca", "Cidade do México", "México"),
            DadosMM(116+5, "OITAVAS", 5, 5,  111, 112, "6 Jul (Segunda-feira)",  "16:00", "AT&T Stadium", "Dallas (Arlington)", "EUA"),
            DadosMM(116+6, "OITAVAS", 6, 6,  109, 110, "6 Jul (Segunda-feira)",  "21:00", "Lumen Field", "Seattle", "EUA"),
            DadosMM(116+7, "OITAVAS", 7, 7,  114, 116, "7 Jul (Terça-feira)",  "12:00", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            DadosMM(116+8, "OITAVAS", 8, 8,  113, 115, "7 Jul (Terça-feira)",  "17:00", "BC Place", "Vancouver", "Canadá"),
            // Quartas (97-100) - Horarios oficiais FIFA (Brasilia)
            DadosMM(124+1, "QUARTAS", 1, 1, 117, 118, "9 Jul (Quinta-feira)",  "17:00", "Gillette Stadium", "Boston (Foxborough)", "EUA"),
            DadosMM(124+2, "QUARTAS", 2, 2, 121, 122, "10 Jul (Sexta-feira)", "16:00", "SoFi Stadium", "Los Angeles (Inglewood)", "EUA"),
            DadosMM(124+3, "QUARTAS", 3, 3, 119, 120, "11 Jul (Sábado)", "18:00", "Hard Rock Stadium", "Miami (Miami Gardens)", "EUA"),
            DadosMM(124+4, "QUARTAS", 4, 4, 123, 124, "11 Jul (Sábado)", "22:00", "Arrowhead Stadium", "Kansas City", "EUA"),
            // Semis (101-102) - Horarios oficiais FIFA (Brasilia)
            DadosMM(129, "SEMI", 1, 1, 125, 126, "14 Jul (Terça-feira)", "16:00", "AT&T Stadium", "Dallas (Arlington)", "EUA"),
            DadosMM(130, "SEMI", 2, 2, 127, 128, "15 Jul (Quarta-feira)", "16:00", "Mercedes-Benz Stadium", "Atlanta", "EUA"),
            // 3o Lugar (103)
            DadosMM(131, "TERCEIRO", 1, 1, 129, 130, "18 Jul (Sábado)", "18:00", "Hard Rock Stadium", "Miami (Miami Gardens)", "EUA"),
            // Final (104)
            DadosMM(132, "FINAL", 1, 1, 129, 130, "19 Jul (Domingo)", "16:00", "MetLife Stadium", "Nova York (E. Rutherford)", "EUA")
        ).map { d ->
            PartidaMataMata(
                id = d.id, fase = d.fase, numeroJogo = d.n, posicaoChave = d.pos,
                partidaOrigemCasa = d.pCasa, partidaOrigemFora = d.pFora,
                data = d.d, horario = d.h, estadio = d.e, cidade = d.c, pais = d.pa, transmissao = d.tr
            )
        }
    }
}
