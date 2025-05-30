# PersonalTasks

Aplicativo Android de gerenciamento de tarefas desenvolvido para a disciplina de Programação para Dispositivos Móveis.

## 📱 Funcionalidades
- Criar tarefas com título, descrição e data limite.
- Editar e excluir tarefas.
- Visualizar detalhes em modo somente leitura.
- Persistência local usando Room.
- Interface com clique longo para exibir menu contextual (editar, excluir, detalhes).

## 🔧 Tecnologias utilizadas
- Android SDK com Kotlin
- Room (persistência local)
- ViewBinding
- ConstraintLayout
- RecyclerView

## ✅ Estrutura
- `model/Task.kt`: modelo da entidade Room.
- `database/AppDatabase.kt`: configuração do banco de dados.
- `controller/TaskAdapter.kt`: adapter da RecyclerView.
- `view/MainActivity.kt`: tela principal com listagem.
- `view/TaskFormActivity.kt`: formulário de criação e edição de tarefas.

## 🚀 Como executar
1. Abra o projeto no Android Studio.
2. Execute o projeto em um dispositivo físico ou emulador Android.

## 👨‍💻 Desenvolvedor teste
- Mateus Ferrari de Assis Alves

## 🎥 Demonstração do App
Assista ao vídeo de demonstração aqui - https://youtu.be/MfrEQG6T3jk
