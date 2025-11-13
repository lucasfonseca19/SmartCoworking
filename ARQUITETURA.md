# 🏗️ Arquitetura do Projeto Smart Coworking

## 📐 Visão Geral da Arquitetura

Este projeto segue o padrão **MVVM (Model-View-ViewModel)** combinado com princípios de **Clean Architecture**, que é o padrão recomendado pela Google para aplicativos Android modernos com Jetpack Compose em 2025.

### Por que MVVM + Clean Architecture?

- ✅ **Separação de responsabilidades** - Cada camada tem uma função clara
- ✅ **Testabilidade** - Lógica de negócio isolada e fácil de testar
- ✅ **Manutenibilidade** - Código organizado e fácil de entender
- ✅ **Escalabilidade** - Fácil adicionar novas features
- ✅ **Recomendado pela Google** - Padrão oficial para Jetpack Compose

---

## 📁 Estrutura de Pastas do Projeto

```
app/src/main/java/com/example/smartcoworking/
│
├── data/                          # CAMADA DE DADOS
│   ├── models/                    # Modelos de dados (data classes)
│   │   ├── Usuario.kt
│   │   ├── EstacaoDeTrabalho.kt
│   │   ├── Reserva.kt
│   │   ├── LeituraSensor.kt
│   │   └── Enums.kt              # TipoEstacao, StatusEstacao, etc.
│   │
│   ├── repository/                # Repositórios (acesso aos dados)
│   │   ├── UsuarioRepository.kt
│   │   ├── EstacaoRepository.kt
│   │   └── ReservaRepository.kt
│   │
│   └── mock/                      # Dados mockados
│       ├── UsuariosMockData.kt
│       ├── EstacoesMockData.kt
│       └── ReservasMockData.kt
│
├── domain/                        # CAMADA DE DOMÍNIO (Lógica de Negócio)
│   ├── usecases/                  # Casos de uso (operações específicas)
│   │   ├── autenticacao/
│   │   │   ├── ValidarLoginUseCase.kt
│   │   │   └── HashSenhaUseCase.kt
│   │   │
│   │   ├── estacoes/
│   │   │   ├── ObterEstacoesUseCase.kt
│   │   │   └── SimularTempoRealUseCase.kt
│   │   │
│   │   └── reservas/
│   │       ├── CriarReservaUseCase.kt
│   │       ├── ValidarReservaUseCase.kt
│   │       └── VerificarConflitosUseCase.kt
│   │
│   └── validators/                # Validadores reutilizáveis
│       ├── EmailValidator.kt
│       ├── SenhaValidator.kt
│       └── DataHoraValidator.kt
│
├── presentation/                  # CAMADA DE APRESENTAÇÃO (UI)
│   ├── navigation/                # Navegação entre telas
│   │   ├── NavGraph.kt
│   │   └── Screen.kt             # Sealed class com rotas
│   │
│   ├── screens/                   # Telas do app
│   │   ├── login/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   └── LoginState.kt
│   │   │
│   │   ├── mapa/
│   │   │   ├── MapaCoworkingScreen.kt
│   │   │   ├── MapaViewModel.kt
│   │   │   ├── MapaState.kt
│   │   │   └── components/
│   │   │       ├── MapaCoworkingCanvas.kt
│   │   │       ├── FormasBasicas.kt
│   │   │       ├── EstacaoCanvas.kt
│   │   │       ├── PadroesVisuais.kt
│   │   │       ├── EstacaoLabel.kt
│   │   │       └── LegendaDialog.kt
│   │   │
│   │   ├── detalhes/
│   │   │   ├── DetalhesEstacaoScreen.kt
│   │   │   ├── DetalhesViewModel.kt
│   │   │   └── DetalhesState.kt
│   │   │
│   │   └── reserva/
│   │       ├── ReservaScreen.kt
│   │       ├── ReservaViewModel.kt
│   │       └── ReservaState.kt
│   │
│   └── components/                # Componentes reutilizáveis
│       ├── CustomButton.kt
│       ├── CustomTextField.kt
│       ├── LoadingIndicator.kt
│       └── ErrorMessage.kt
│
├── ui/                            # TEMA E ESTILOS
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       ├── Type.kt
│       └── MapColors.kt          # Cores específicas do mapa
│
├── utils/                         # UTILITÁRIOS
│   ├── Constants.kt              # Constantes do app
│   ├── Extensions.kt             # Extension functions
│   └── SecurityUtils.kt          # Utilitários de segurança
│
└── MainActivity.kt                # Ponto de entrada do app
```

---

## 🔄 Fluxo de Dados (MVVM)

```
┌─────────────────────────────────────────────────────────────┐
│                         USER                                │
│                    (Usuário do App)                         │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ Interage
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                         VIEW                                │
│                  (Composable Screens)                       │
│  - LoginScreen.kt                                           │
│  - MapaCoworkingScreen.kt                                   │
│  - DetalhesEstacaoScreen.kt                                 │
│                                                             │
│  Responsabilidades:                                         │
│  ✓ Renderizar UI                                           │
│  ✓ Capturar eventos do usuário                             │
│  ✓ Observar estado do ViewModel                            │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ Emite Eventos / Observa Estado
                           │ (Unidirectional Data Flow)
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                      VIEWMODEL                              │
│                  (Gerencia Estado da UI)                    │
│  - LoginViewModel.kt                                        │
│  - MapaViewModel.kt                                         │
│  - ReservaViewModel.kt                                      │
│                                                             │
│  Responsabilidades:                                         │
│  ✓ Manter estado da UI (StateFlow/State)                   │
│  ✓ Processar eventos do usuário                            │
│  ✓ Chamar Use Cases                                        │
│  ✓ Transformar dados para exibição                         │
│  ✓ Gerenciar corrotinas (viewModelScope)                   │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ Chama Use Cases
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                      USE CASES                              │
│                  (Lógica de Negócio)                        │
│  - ValidarLoginUseCase.kt                                   │
│  - CriarReservaUseCase.kt                                   │
│  - SimularTempoRealUseCase.kt                               │
│                                                             │
│  Responsabilidades:                                         │
│  ✓ Implementar regras de negócio                           │
│  ✓ Validações complexas                                    │
│  ✓ Orquestrar múltiplos repositórios                       │
│  ✓ Transformar dados entre camadas                         │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ Acessa Dados
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                     REPOSITORY                              │
│                  (Acesso aos Dados)                         │
│  - UsuarioRepository.kt                                     │
│  - EstacaoRepository.kt                                     │
│  - ReservaRepository.kt                                     │
│                                                             │
│  Responsabilidades:                                         │
│  ✓ Abstrair fonte de dados (mock/API/database)             │
│  ✓ Fornecer interface única de acesso                      │
│  ✓ Cache de dados (futuro)                                 │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ Retorna Dados
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                    DATA SOURCE                              │
│                  (Origem dos Dados)                         │
│  - UsuariosMockData.kt                                      │
│  - EstacoesMockData.kt                                      │
│  - ReservasMockData.kt                                      │
│                                                             │
│  Responsabilidades:                                         │
│  ✓ Fornecer dados mockados                                 │
│  ✓ Simular API (neste projeto)                             │
│  ✓ Pode ser substituído por Room/Retrofit futuramente      │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Descrição Detalhada das Camadas

### 1️⃣ Data Layer (Camada de Dados)

**Localização:** `app/src/main/java/com/example/smartcoworking/data/`

**Responsabilidades:**
- Definir estruturas de dados (data classes)
- Fornecer acesso aos dados através de repositórios
- Abstrair a origem dos dados (mock, API, banco local)

**Componentes:**

#### `models/` - Modelos de Dados
```kotlin
// Exemplo: EstacaoDeTrabalho.kt
package com.example.smartcoworking.data.models

data class EstacaoDeTrabalho(
    val id: String,
    val numero: Int,
    val nome: String,
    val tipo: TipoEstacao,
    val status: StatusEstacao,
    val leituraSensor: LeituraSensor,
    val posicaoX: Float,
    val posicaoY: Float,
    val largura: Float,
    val altura: Float,
    val forma: FormaEstacao
)
```

#### `repository/` - Repositórios
```kotlin
// Exemplo: EstacaoRepository.kt
package com.example.smartcoworking.data.repository

class EstacaoRepository {
    fun obterTodasEstacoes(): List<EstacaoDeTrabalho> {
        return EstacoesMockData.obterEstacoes()
    }

    fun obterEstacaoPorId(id: String): EstacaoDeTrabalho? {
        return EstacoesMockData.obterEstacoes().find { it.id == id }
    }

    suspend fun atualizarStatus(id: String, novoStatus: StatusEstacao) {
        // Lógica para atualizar status
    }
}
```

#### `mock/` - Dados Mockados
```kotlin
// Exemplo: EstacoesMockData.kt
package com.example.smartcoworking.data.mock

object EstacoesMockData {
    fun obterEstacoes(): List<EstacaoDeTrabalho> = listOf(
        // ... 15 estações mockadas
    )
}
```

---

### 2️⃣ Domain Layer (Camada de Domínio)

**Localização:** `app/src/main/java/com/example/smartcoworking/domain/`

**Responsabilidades:**
- Conter a lógica de negócio pura
- Implementar validações complexas
- Orquestrar operações entre múltiplos repositórios
- **Independente do Android** (pode ser testado com JUnit puro)

**Componentes:**

#### `usecases/` - Casos de Uso
```kotlin
// Exemplo: CriarReservaUseCase.kt
package com.example.smartcoworking.domain.usecases.reservas

class CriarReservaUseCase(
    private val reservaRepository: ReservaRepository,
    private val estacaoRepository: EstacaoRepository
) {
    suspend operator fun invoke(
        usuarioId: String,
        estacaoId: String,
        inicio: String,
        fim: String
    ): Result<Reserva> {
        // 1. Validar horários
        if (!validarHorarios(inicio, fim)) {
            return Result.failure(Exception("Horários inválidos"))
        }

        // 2. Verificar disponibilidade
        val temConflito = reservaRepository.verificarConflito(
            estacaoId, inicio, fim
        )
        if (temConflito) {
            return Result.failure(Exception("Estação já reservada"))
        }

        // 3. Criar reserva
        val reserva = Reserva(
            id = UUID.randomUUID().toString(),
            usuarioId = usuarioId,
            estacaoId = estacaoId,
            dataHoraInicio = inicio,
            dataHoraFim = fim,
            status = StatusReserva.ATIVA
        )

        return reservaRepository.salvarReserva(reserva)
    }

    private fun validarHorarios(inicio: String, fim: String): Boolean {
        // Lógica de validação
        return true
    }
}
```

#### `validators/` - Validadores
```kotlin
// Exemplo: EmailValidator.kt
package com.example.smartcoworking.domain.validators

object EmailValidator {
    fun validar(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }
}
```

---

### 3️⃣ Presentation Layer (Camada de Apresentação)

**Localização:** `app/src/main/java/com/example/smartcoworking/presentation/`

**Responsabilidades:**
- Definir toda a interface do usuário (UI)
- Gerenciar estado da UI com ViewModels
- Implementar navegação entre telas
- Reagir a eventos do usuário

**Componentes:**

#### `screens/` - Telas

Cada tela segue o padrão **Screen + ViewModel + State**:

```
mapa/
├── MapaCoworkingScreen.kt    # UI (Composable)
├── MapaViewModel.kt           # Lógica da UI + Estado
├── MapaState.kt               # Data class do estado
└── components/                # Componentes específicos do mapa
    ├── MapaCoworkingCanvas.kt
    └── ...
```

**Exemplo de Screen:**
```kotlin
// MapaCoworkingScreen.kt
package com.example.smartcoworking.presentation.screens.mapa

@Composable
fun MapaCoworkingScreen(
    viewModel: MapaViewModel = viewModel(),
    onEstacaoClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { /* TopBar */ },
        floatingActionButton = { /* FAB Legenda */ }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorMessage(state.error!!)
            else -> MapaCoworkingCanvas(
                estacoes = state.estacoes,
                onEstacaoClick = { estacao ->
                    onEstacaoClick(estacao.id)
                }
            )
        }
    }
}
```

**Exemplo de ViewModel:**
```kotlin
// MapaViewModel.kt
package com.example.smartcoworking.presentation.screens.mapa

class MapaViewModel(
    private val obterEstacoesUseCase: ObterEstacoesUseCase,
    private val simularTempoRealUseCase: SimularTempoRealUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapaState())
    val state: StateFlow<MapaState> = _state.asStateFlow()

    init {
        carregarEstacoes()
        iniciarSimulacao()
    }

    private fun carregarEstacoes() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val estacoes = obterEstacoesUseCase()

            _state.value = _state.value.copy(
                estacoes = estacoes,
                isLoading = false
            )
        }
    }

    private fun iniciarSimulacao() {
        viewModelScope.launch {
            simularTempoRealUseCase().collect { estacoesAtualizadas ->
                _state.value = _state.value.copy(
                    estacoes = estacoesAtualizadas
                )
            }
        }
    }
}
```

**Exemplo de State:**
```kotlin
// MapaState.kt
package com.example.smartcoworking.presentation.screens.mapa

data class MapaState(
    val estacoes: List<EstacaoDeTrabalho> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val mostrarLegenda: Boolean = false
)
```

#### `navigation/` - Navegação

```kotlin
// Screen.kt (Sealed class com rotas)
package com.example.smartcoworking.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Mapa : Screen("mapa")
    object DetalhesEstacao : Screen("detalhes/{estacaoId}") {
        fun createRoute(estacaoId: String) = "detalhes/$estacaoId"
    }
    object Reserva : Screen("reserva/{estacaoId}") {
        fun createRoute(estacaoId: String) = "reserva/$estacaoId"
    }
}
```

```kotlin
// NavGraph.kt
package com.example.smartcoworking.presentation.navigation

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Mapa.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Mapa.route) {
            MapaCoworkingScreen(
                onEstacaoClick = { estacaoId ->
                    navController.navigate(
                        Screen.DetalhesEstacao.createRoute(estacaoId)
                    )
                }
            )
        }

        composable(
            route = Screen.DetalhesEstacao.route,
            arguments = listOf(
                navArgument("estacaoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val estacaoId = backStackEntry.arguments?.getString("estacaoId")
            DetalhesEstacaoScreen(
                estacaoId = estacaoId ?: "",
                onReservarClick = {
                    navController.navigate(
                        Screen.Reserva.createRoute(estacaoId ?: "")
                    )
                },
                onVoltarClick = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.Reserva.route,
            arguments = listOf(
                navArgument("estacaoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val estacaoId = backStackEntry.arguments?.getString("estacaoId")
            ReservaScreen(
                estacaoId = estacaoId ?: "",
                onReservaSuccess = { navController.navigateUp() },
                onVoltarClick = { navController.navigateUp() }
            )
        }
    }
}
```

#### `components/` - Componentes Reutilizáveis

```kotlin
// CustomButton.kt
package com.example.smartcoworking.presentation.components

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(text)
        }
    }
}
```

---

### 4️⃣ UI Theme (Tema e Estilos)

**Localização:** `app/src/main/java/com/example/smartcoworking/ui/theme/`

**Responsabilidades:**
- Definir paleta de cores
- Configurar tipografia
- Aplicar tema Material 3
- Cores específicas do domínio (MapColors)

```kotlin
// MapColors.kt
package com.example.smartcoworking.ui.theme

object MapColors {
    val StatusLivre = Color(0xFF4CAF50)
    val StatusOcupado = Color(0xFFF44336)
    val StatusReservado = Color(0xFFFFC107)
    val AreaComum = Color(0xFFE0E0E0)
    val Borda = Color(0xFF757575)
}
```

---

### 5️⃣ Utils (Utilitários)

**Localização:** `app/src/main/java/com/example/smartcoworking/utils/`

**Responsabilidades:**
- Constantes do app
- Extension functions
- Utilitários de segurança

```kotlin
// Constants.kt
package com.example.smartcoworking.utils

object Constants {
    const val CANVAS_SIZE = 1000f
    const val SIMULACAO_INTERVALO_MS = 10000L
    const val MIN_SENHA_LENGTH = 6
}
```

```kotlin
// Extensions.kt
package com.example.smartcoworking.utils

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Float.toPercentage(): String = "${(this * 100).toInt()}%"
```

---

## 🔗 Integração das Camadas

### MainActivity.kt (Ponto de Entrada)

```kotlin
// MainActivity.kt
package com.example.smartcoworking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCoworkingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}
```

---

## 🎯 Padrões de Nomenclatura

### Arquivos e Classes

| Tipo | Padrão | Exemplo |
|------|--------|---------|
| Screen (Composable) | `[Nome]Screen.kt` | `LoginScreen.kt` |
| ViewModel | `[Nome]ViewModel.kt` | `MapaViewModel.kt` |
| State | `[Nome]State.kt` | `LoginState.kt` |
| Repository | `[Nome]Repository.kt` | `EstacaoRepository.kt` |
| Use Case | `[Verbo][Nome]UseCase.kt` | `CriarReservaUseCase.kt` |
| Validator | `[Nome]Validator.kt` | `EmailValidator.kt` |
| Mock Data | `[Nome]MockData.kt` | `EstacoesMockData.kt` |
| Component | `[Nome].kt` (descritivo) | `MapaCoworkingCanvas.kt` |

### Pacotes (Pastas)

- Sempre em **minúsculas**
- Sem underscores
- Plurais para coleções: `models`, `validators`, `usecases`
- Singular para features: `login`, `mapa`, `reserva`

---

## 📝 Exemplo Completo: Feature de Login

Para ilustrar como tudo se conecta, vejamos a feature de Login completa:

```
presentation/screens/login/
├── LoginScreen.kt          # UI
├── LoginViewModel.kt       # Lógica + Estado
└── LoginState.kt           # Estado da tela

domain/usecases/autenticacao/
├── ValidarLoginUseCase.kt  # Lógica de negócio
└── HashSenhaUseCase.kt     # Criptografia

domain/validators/
├── EmailValidator.kt       # Validação de email
└── SenhaValidator.kt       # Validação de senha

data/repository/
└── UsuarioRepository.kt    # Acesso aos dados

data/mock/
└── UsuariosMockData.kt     # Dados mockados
```

**Fluxo:**
1. Usuário preenche email/senha no `LoginScreen.kt`
2. `LoginScreen` chama `LoginViewModel.onLoginClick()`
3. `LoginViewModel` chama `ValidarLoginUseCase`
4. `ValidarLoginUseCase` usa `EmailValidator` e `SenhaValidator`
5. Se válido, usa `HashSenhaUseCase` para comparar hash
6. `HashSenhaUseCase` acessa `UsuarioRepository`
7. `UsuarioRepository` busca em `UsuariosMockData`
8. Resultado retorna até `LoginViewModel`
9. `LoginViewModel` atualiza `LoginState`
10. `LoginScreen` reage ao estado e navega ou exibe erro

---

## 📚 Dependências Necessárias

Adicionar ao `app/build.gradle.kts`:

```kotlin
dependencies {
    // Jetpack Compose (já tem)
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Navegação
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Segurança (para hash de senha)
    implementation("org.mindrot:jbcrypt:0.4")

    // Testes
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
```

---

## 🚀 Ordem de Implementação Recomendada

### Fase 1: Fundação
1. ✅ Criar estrutura de pastas
2. ✅ Definir todos os modelos de dados (`data/models/`)
3. ✅ Criar dados mockados (`data/mock/`)
4. ✅ Implementar repositórios básicos (`data/repository/`)

### Fase 2: Navegação
5. ✅ Configurar navegação (`presentation/navigation/`)
6. ✅ Criar telas vazias (scaffolding)
7. ✅ Testar navegação entre telas

### Fase 3: Features (uma por vez)
8. ✅ **Login**
   - Validators → UseCase → Repository → ViewModel → Screen
9. ✅ **Mapa Interativo**
   - Components → Canvas → ViewModel → Screen
10. ✅ **Detalhes da Estação**
    - UseCase → ViewModel → Screen
11. ✅ **Reserva**
    - Validators → UseCase → ViewModel → Screen

### Fase 4: Polimento
12. ✅ Componentes reutilizáveis
13. ✅ Tratamento de erros
14. ✅ Loading states
15. ✅ Testes

---

## ✅ Checklist de Arquitetura

Use este checklist para garantir que está seguindo a arquitetura corretamente:

### Data Layer
- [ ] Todos os modelos de dados estão em `data/models/`
- [ ] Dados mockados estão em `data/mock/`
- [ ] Repositórios implementados em `data/repository/`
- [ ] Repositórios retornam `Result` ou tipos primitivos

### Domain Layer
- [ ] Use Cases estão em `domain/usecases/[feature]/`
- [ ] Validators estão em `domain/validators/`
- [ ] Lógica de negócio está isolada (não depende do Android)
- [ ] Use Cases são testáveis com JUnit puro

### Presentation Layer
- [ ] Cada screen tem seu ViewModel
- [ ] Estados são data classes imutáveis
- [ ] ViewModels usam StateFlow para expor estado
- [ ] Screens são apenas UI (Composables puros)
- [ ] Navegação está centralizada em `NavGraph.kt`

### Geral
- [ ] Nomenclatura consistente
- [ ] Pacotes organizados por feature
- [ ] Sem lógica de negócio em Composables
- [ ] Sem acesso direto a dados em ViewModels (sempre via UseCase)

---

## 📖 Recursos Adicionais

- [Guia Oficial - Arquitetura Android](https://developer.android.com/topic/architecture)
- [Jetpack Compose - Arquitetura](https://developer.android.com/develop/ui/compose/architecture)
- [MVVM Pattern](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [StateFlow e SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

---

**Esta arquitetura foi projetada para ser:**
- ✅ Fácil de entender para iniciantes em Kotlin
- ✅ Escalável para adicionar novas features
- ✅ Testável em todas as camadas
- ✅ Compatível com as melhores práticas de 2025
- ✅ Preparada para evolução futura (substituir mock por API, adicionar Room, etc.)
