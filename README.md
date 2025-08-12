# 📱 Desafio Técnico - NewsAPI (Versão Mobile)

Aplicativo Android desenvolvido em **Kotlin** com **Jetpack Compose**, cujo objetivo é consumir a **NewsAPI**, exibir notícias de forma paginada, permitir busca por título e armazenar o histórico de pesquisas no banco de dados local.

## 🎯 Objetivo
Criar uma aplicação mobile que consome dados de notícias a partir da **NewsAPI**, exibindo-as em uma interface responsiva e intuitiva.  
O app possibilita a busca por título e mantém um histórico dessas buscas usando banco de dados local.

---

## 🚀 Funcionalidades Implementadas

- **Listagem de Notícias**  
  Exibição paginada de notícias em formato de lista, carregando mais itens à medida que o usuário rola a tela.  
  Ao iniciar, o app exibe notícias aleatórias.

- **Busca por Título**  
  Campo de texto para pesquisar notícias por título.

- **Histórico de Buscas**  
  Todas as buscas realizadas são salvas no **Room Database**.  
  O histórico é exibido em um menu dropdown no campo de pesquisa, permitindo reutilizar consultas anteriores.

- **Tela de Detalhes**  
  Ao clicar em uma notícia, é aberta uma nova tela com o conteúdo completo.

- **Design Responsivo**  
  Interface adaptável a diferentes tamanhos e orientações de tela (retrato e paisagem), com paleta de cores em tons de azul.

---

## 🛠 Tecnologias Utilizadas

- **Linguagem**: [Kotlin](https://kotlinlang.org/)  
- **Interface Gráfica**: [Jetpack Compose](https://developer.android.com/jetpack/compose)  
- **Arquitetura**: MVVM (Model-View-ViewModel)  
- **Banco de Dados Local**: Room Database  
- **Consumo de API**: Retrofit    
- **Assincronia**: Coroutines  
- **Carregamento de Imagens**: Coil  
- **Navegação**: Jetpack Navigation Compose  
- **Design System**: Material 3  

---
## 📂 Estrutura do Projeto

app/

└── src/

    └── main/
    
        ├── java/com/example/desafiotecniconewsapi/
        
        │   ├── model/         # Classes de dados (Article, NewsResponse) e entidade SearchQuery
        
        │   ├── network/       # Interface NewsApiService e RetrofitInstance
        
        │   ├── paging/        # Classe NewsPagingSource (paginação)
        
        │   ├── viewmodel/     # NewsViewModel (lógica de negócio e estado da UI)
        
        │   ├── ui/            # Telas e temas do Compose
        
        │   │   ├── screens/   # Telas (DetailScreen, WebViewScreen)
        
        │   │   └── theme/     # Temas (Theme.kt, Color.kt, Type.kt)

        |   |   └── state/      # Verificalão de dados (NewsUiState.kt)
        
        │   ├── dao/           # Interface SearchQueryDao (Room)
        
        │   └── data/          # AppDatabase (Room)
        
        └── res/               # Recursos visuais (layouts, ícones, cores)


---

## 🔑 Como configurar o NewsAPI e rodar o projeto
Crie conta em https://newsapi.org/ e gere uma chave de API .

 **Baixe ou clone este repositório**
git clone https://github.com/jussie-lopes23/Desafio_tecnico_NewsAPI.git

Abra o projeto no Android Studio

Menu → Arquivo → Abrir → selecione a pasta do projeto.

Copie sua chave de API .

**Substitua a chave no projeto**

Abra o arquivo: app/src/main/java/com/example/desafiotecniconewsapi/viewmodel/NewsViewModel.kt

Localize a variável "apiKey" e cole sua chave da NewsAPI.

**Sincronizar e executar**

Menu → Arquivo → Sincronizar projeto com arquivos Gradle ("Sync Project with Gradle Files")

Clique ▶ para rodar no emulador ou dispositivo físico.

---

