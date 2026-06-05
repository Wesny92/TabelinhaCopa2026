# Tabelinha da Copa 2026

Aplicativo Android para acompanhar a Copa do Mundo FIFA 2026. Desenvolvido 100% em Kotlin com Jetpack Compose, Room Database e arquitetura MVVM.

## Funcionalidades

- **12 Grupos (A-L)** — tabela de classificação com pontos, saldo de gols, gols pró
- **Calendário completo** — 104 partidas com datas, horários, estádios, bandeiras e transmissão
- **Chaveamento** — Round of 32 até Final com propagação automática de vencedores
- **Bracket interativo** — visualização WebView com React/Babel (bundle local, sem CDN)
- **Regulamento FIFA 2026** — classificação de melhores terceiros conforme artigo 12.6
- **Transmissão** — logos das emissoras (CazeTV e Globo) em cada partida
- **Notificações** — WorkManager 5 minutos antes do início com bandeiras emoji
- **Pênaltis** — diálogo de desempate para mata-mata empatado
- **Navegação por swipe** — HorizontalPager entre fases eliminatórias

## Tecnologias

| Camada | Tecnologia |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Estado | ViewModel + StateFlow |
| Banco de dados | Room (SQLite) v9 |
| Concorrência | Corrotinas + Flow |
| Bracket | WebView + React/Babel (assets locais) |
| Notificações | WorkManager |
| Navegação | NavHost + HorizontalPager |
| Injeção | Manual (AppContainer) |

## Estrutura

```
com.exemplo.copa2026/
├── data/
│   ├── local/
│   │   ├── dao/           # PartidaDao, TimeDao, GrupoDao, PartidaMataMataDao
│   │   ├── AppDatabase.kt
│   │   └── PrePopulateData.kt
│   ├── model/             # Partida, Time, Grupo, PartidaMataMata, Fase
│   └── repository/        # MataMataRepository
├── di/
│   └── AppContainer.kt
├── domain/
│   ├── model/             # TerceiroColocado, ClassificacaoTime
│   └── usecase/           # MontarChaveamento, CalcularClassificacao, SelecionarTerceiros
└── ui/
    ├── calendario/        # CalendarioScreen + ViewModel
    ├── grupo/             # GrupoScreen + ViewModel
    ├── matamata/          # MataMataScreen + ViewModel + BracketView
    ├── navigation/        # Rotas
    └── theme/             # Cores, fontes, tema FIFA
```

## Emulador

```
emulator -avd Pixel_6_API_35 -gpu swiftshader_indirect
```

## Licença

Projeto educacional — uso livre para estudo e referência.
