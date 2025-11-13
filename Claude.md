Que# Smart Coworking - Frontend Mobile

## 📱 Sobre o Projeto

Aplicativo mobile para **reserva de mesas e salas em escritórios de coworking** com visualização de **dados em tempo real do ambiente** através de sensores IoT simulados.

**Exemplo de uso:** Usuário visualiza "Mesa 10A - Livre | 22°C | Silencioso | Qualidade do Ar: Boa" e pode reservá-la diretamente pelo app.

## 🛠️ Stack Tecnológico

- **Linguagem:** Kotlin
- **UI Framework:** Jetpack Compose
- **Dados:** Mockados localmente (não consome API real)

## 📋 Requisitos do Frontend Mobile

### Telas Obrigatórias (mínimo 3)

1. **Tela de Login**
   - Autenticação de usuário
   - Campos: email/usuário e senha
   - Validação de entrada

2. **Tela de Mapa de Estações (Visualização Espacial)**
   - Mapa interativo do espaço de coworking usando Canvas
   - Visualização em escala real do layout do ambiente
   - Estações representadas por formas geométricas:
     - 🟦 Quadrados: Mesas individuais
     - ⚫ Círculos: Mesas colaborativas
     - 🟨 Retângulos: Salas de reunião e cabines
   - Indicadores visuais de status:
     - 🟢 Verde sólido: Livre
     - 🔴 Vermelho hachurado: Ocupado
     - 🟡 Amarelo com pontos: Reservado
   - Numeração simples nas estações (1, 2, 3...)
   - Elementos decorativos (janelas, área comum)
   - Botão de legenda com ícone "?"
   - Atualização simulada em tempo real (status muda periodicamente)
   - Ao clicar em uma estação: navega para tela de detalhes

2b. **Tela de Detalhes da Estação**
   - Nome completo e tipo da estação
   - Dados dos sensores em tempo real:
     - 🌡️ Temperatura
     - 🔊 Nível de ruído (Silencioso/Moderado/Alto)
     - 💨 Qualidade do ar (Boa/Regular/Ruim)
   - Status atual (Livre/Ocupado/Reservado)
   - Botão "Reservar" (navega para tela de reserva)

3. **Tela de Reserva**
   - Formulário para criar nova reserva
   - Campos: data, horário início, horário fim
   - Seleção da estação de trabalho

### Características Técnicas

- Layout responsivo
- Boa usabilidade e experiência do usuário
- Dados mockados (não precisa integrar com backend)

## 🔒 Requisitos de Segurança

### Obrigatório Implementar:

1. **Sistema de Login Funcional**
   - Senha criptografada com hash
   - Armazenamento seguro das credenciais

2. **Pelo Menos 2 Práticas de Segurança:**
   - ✅ Validação de entrada de dados
   - ✅ Proteção contra XSS (Cross-Site Scripting)
   - Outras opções: sanitização de inputs, validação de formato de email, etc.

## 📊 Modelo de Dados

### Usuario
```kotlin
data class Usuario(
    val id: String,
    val nome: String,
    val email: String,
    val senhaHash: String
)
```

### EstacaoDeTrabalho
```kotlin
data class EstacaoDeTrabalho(
    val id: String,
    val numero: Int,               // Numeração simples exibida no mapa (1, 2, 3...)
    val nome: String,              // Ex: "Mesa 10A", "Sala de Reunião B"
    val tipo: TipoEstacao,         // MESA, SALA_REUNIAO, CABINE_PRIVADA
    val capacidade: Int,
    val status: StatusEstacao,     // LIVRE, OCUPADO, RESERVADO
    val leituraSensor: LeituraSensor,

    // Propriedades para renderização no mapa
    val posicaoX: Float,           // Coordenada X no canvas (0-1000)
    val posicaoY: Float,           // Coordenada Y no canvas (0-1000)
    val largura: Float,            // Largura da forma no canvas
    val altura: Float,             // Altura da forma no canvas
    val forma: FormaEstacao        // QUADRADO, CIRCULO, RETANGULO
)

enum class TipoEstacao {
    MESA, SALA_REUNIAO, CABINE_PRIVADA
}

enum class StatusEstacao {
    LIVRE, OCUPADO, RESERVADO
}

enum class FormaEstacao {
    QUADRADO,    // Mesas individuais pequenas
    CIRCULO,     // Mesas colaborativas redondas
    RETANGULO    // Salas de reunião e cabines
}
```

### LeituraSensor
```kotlin
data class LeituraSensor(
    val temperatura: Float,         // Em °C
    val nivelRuido: NivelRuido,
    val qualidadeAr: QualidadeAr,
    val timestamp: String           // ISO 8601 format
)

enum class NivelRuido {
    SILENCIOSO, MODERADO, ALTO
}

enum class QualidadeAr {
    BOA, REGULAR, RUIM
}
```

### Reserva
```kotlin
data class Reserva(
    val id: String,
    val usuarioId: String,
    val estacaoId: String,
    val dataHoraInicio: String,     // ISO 8601 format
    val dataHoraFim: String,        // ISO 8601 format
    val status: StatusReserva
)

enum class StatusReserva {
    ATIVA, CANCELADA, CONCLUIDA
}
```

## 🗺️ Especificações do Mapa de Coworking

### Visão Geral
O mapa é o diferencial do app, oferecendo visualização espacial intuitiva similar à escolha de assentos em cinemas. Implementado com **Jetpack Compose Canvas** para controle total sobre renderização e interatividade.

### Arquitetura do Mapa

#### Sistema de Coordenadas
- Canvas de 1000x1000 unidades (escala interna)
- Coordenadas normalizadas para responsividade
- Posicionamento hardcoded no modelo de dados

#### Elementos Visuais

**1. Estações de Trabalho (Interativas)**
- **Quadrados (50x50):** Mesas individuais
- **Círculos (raio 35):** Mesas colaborativas
- **Retângulos (100x60 / 80x50):** Salas e cabines

**2. Elementos Decorativos (Não Interativos)**
- Labels "JANELA" nas laterais (contexto espacial)
- Área central hachurada (lounge/área comum)
- Bordas e divisórias visuais

#### Sistema de Cores e Padrões

**Status das Estações:**
```kotlin
// Livre
Color(0xFF4CAF50) - Verde sólido

// Ocupado
Color(0xFFF44336) + padrão hachurado diagonal - Vermelho com linhas

// Reservado
Color(0xFFFFC107) + padrão pontilhado - Amarelo com pontos
```

**Acessibilidade:**
Combinação de cores + padrões visuais para usuários com daltonismo

#### Interatividade

**Detecção de Cliques:**
```kotlin
Canvas(
    modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                // Verificar qual estação foi clicada
                val clicked = estacoes.find { estacao ->
                    offset está dentro dos bounds da estação
                }
                // Navegar para DetalhesEstacaoScreen
            }
        }
)
```

**Fluxo de Navegação:**
1. Usuário toca em estação numerada (ex: "3")
2. App navega para tela de detalhes completos
3. Tela mostra: "Mesa 3A - Livre | 22°C | Silencioso"
4. Botão "Reservar" leva ao formulário

#### Simulação em Tempo Real

**Atualização Periódica:**
```kotlin
LaunchedEffect(Unit) {
    while(true) {
        delay(10000) // 10 segundos
        // Atualizar status aleatório de 1-2 estações
        // Simular mudança de sensores
    }
}
```

**Dados Simulados:**
- Status muda entre Livre ↔ Ocupado ↔ Reservado
- Temperatura varia ±2°C
- Nível de ruído muda baseado em ocupação
- Qualidade do ar varia levemente

#### Legenda Interativa

**FloatingActionButton com ícone "?"**

Ao clicar, exibe Dialog explicando:
- 🟢 Verde sólido = Livre
- 🔴 Vermelho hachurado = Ocupado
- 🟡 Amarelo pontilhado = Reservado
- 🟦 Quadrado = Mesa individual
- ⚫ Círculo = Mesa colaborativa
- 🟨 Retângulo = Sala/Cabine

### Estrutura de Arquivos

```
ui/
├── mapa/
│   ├── MapaCoworkingScreen.kt          // Tela principal do mapa
│   ├── DetalhesEstacaoScreen.kt        // Detalhes da estação
│   ├── MapaViewModel.kt                // Estado e lógica
│   └── components/
│       ├── EstacaoCanvas.kt            // Renderização das estações
│       ├── LegendaDialog.kt            // Dialog da legenda
│       └── PadraoVisual.kt             // Desenho de padrões
data/
└── EstacoesMockData.kt                 // Dados com coordenadas
```

### Implementação Incremental

**Fase 1 (MVP):**
- Canvas básico com estações estáticas
- Cores sólidas apenas (sem padrões)
- Click navigation funcionando

**Fase 2 (Aprimoramento):**
- Adicionar padrões visuais (hachurado, pontilhado)
- Implementar simulação de tempo real
- Adicionar legenda

**Fase 3 (Opcional - se sobrar tempo):**
- Zoom e pan com gestos
- Animações de transição
- Filtros por tipo de estação

## 🎯 Funcionalidades Principais

### Autenticação
- Login com email e senha
- Validação de credenciais (mockada)
- Persistência de sessão do usuário

### Visualização de Estações (Mapa Interativo)
- Mapa espacial do coworking com Canvas do Jetpack Compose
- Renderização de 10-15 estações em posições reais
- Sistema de coordenadas X,Y para posicionamento preciso
- Detecção de cliques com `detectTapGestures`
- Navegação contextual (Mapa → Detalhes → Reserva)
- Simulação de tempo real com LaunchedEffect
- Legenda explicativa acessível via botão flutuante

### Gestão de Reservas
- Criar nova reserva
- Validar disponibilidade da estação
- Validar conflito de horários
- Visualizar minhas reservas

## 📝 Notas de Desenvolvimento

### Dados Mockados
Como o frontend não consome API real, você precisará criar:

- Lista de usuários mockados para teste de login
- **Lista de 10-15 estações de trabalho com:**
  - Coordenadas X,Y hardcoded
  - Formas variadas (quadrados, círculos, retângulos)
  - Status iniciais diversos
  - Dados de sensores iniciais
- Gerador de leituras de sensores simuladas
- Lista de reservas de exemplo

**Exemplo de Estação Mockada:**
```kotlin
EstacaoDeTrabalho(
    id = "1",
    numero = 1,
    nome = "Mesa 1A",
    tipo = TipoEstacao.MESA,
    capacidade = 1,
    status = StatusEstacao.LIVRE,
    leituraSensor = LeituraSensor(
        temperatura = 22.5f,
        nivelRuido = NivelRuido.SILENCIOSO,
        qualidadeAr = QualidadeAr.BOA,
        timestamp = "2025-11-13T10:00:00Z"
    ),
    posicaoX = 100f,
    posicaoY = 150f,
    largura = 50f,
    altura = 50f,
    forma = FormaEstacao.QUADRADO
)
```

### Validações Importantes
- Data/hora de reserva não pode ser no passado
- Horário de fim deve ser posterior ao horário de início
- Estação não pode ter reservas conflitantes
- Validar formato de email
- Senha deve ter requisitos mínimos (tamanho, caracteres)

### Criptografia
Para o hash de senha, considere usar bibliotecas como:
- BCrypt ou similar para Android
- Armazenar apenas o hash, nunca a senha em texto plano

## 🎨 Considerações de UX/UI

- Interface intuitiva e moderna
- Feedback visual claro para ações do usuário
- Loading states para operações
- Tratamento de erros com mensagens amigáveis
- Design consistente entre telas
- Ícones representativos para sensores e status

## 🧪 Requisitos de Testes

### Objetivo
Validar o funcionamento do app mobile através de testes, garantindo confiabilidade e qualidade do software.

### Entregáveis Obrigatórios
- Plano com pelo menos 5 casos de teste
- Execução de ao menos 3 testes
- Evidências (prints, logs ou relatórios)

### Tipos de Testes Recomendados

#### 1. Testes Unitários (JUnit)
Testar lógica de negócio, validações e funções isoladas:

**Exemplos de casos de teste:**
- Validação de formato de email
- Validação de senha (requisitos mínimos)
- Verificação de hash de senha
- Validação de data/hora de reserva
- Verificação de conflitos de horário

#### 2. Testes de UI (Jetpack Compose Testing)
Testar componentes visuais e interações do usuário:

**Exemplos de casos de teste:**
- Login com credenciais válidas exibe lista de estações
- Login com credenciais inválidas exibe mensagem de erro
- Filtro de estações por tipo funciona corretamente
- Criação de reserva com dados válidos é bem-sucedida
- Formulário de reserva valida campos obrigatórios

### Estrutura de Caso de Teste

Cada caso de teste deve conter:
1. **ID:** Identificador único (ex: TC001)
2. **Cenário:** Descrição do que está sendo testado
3. **Entrada:** Dados ou ações realizadas
4. **Saída Esperada:** Resultado esperado
5. **Status:** Passou/Falhou

**Exemplo:**
```
ID: TC001
Cenário: Login com credenciais válidas
Entrada: email="usuario@test.com", senha="Senha123!"
Saída Esperada: Usuário autenticado, navega para lista de estações
Status: Passou
```

### Casos de Teste Sugeridos (Mínimo 5)

| ID | Cenário | Entrada | Saída Esperada |
|---|---|---|---|
| TC001 | Login válido | Email e senha corretos | Autenticação bem-sucedida |
| TC002 | Login inválido | Senha incorreta | Mensagem de erro exibida |
| TC003 | Validação de email | Email sem @ | Erro de formato inválido |
| TC004 | Criar reserva válida | Data futura, horários válidos | Reserva criada com sucesso |
| TC005 | Reserva com horário inválido | Hora fim antes da hora início | Erro de validação exibido |
