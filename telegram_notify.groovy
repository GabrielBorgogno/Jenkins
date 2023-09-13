"""
🔧 Este Jenkinsfile define um job de pipeline no Jenkins que realiza as seguintes etapas:

1. Checkout (Checkout): Faz o checkout do código-fonte a partir da URL especificada do repositório Git.
2. Build: Executa o comando 'mvn clean install' para compilar o projeto.
3. Notify (Notificar): Envía uma mensagem de notificação por meio do Telegram para o chat ou grupo especificado.

‼️ Para usar este Jenkinsfile, você precisa substituir os valores fictícios nas seguintes variáveis:

- repoUrl: A URL do seu repositório Git.
- telegramBotToken: O token do seu bot do Telegram.
- chatId: O ID do chat ou grupo onde você deseja enviar as notificações.

🚀 Antes de executar a etapa 'Notify', certifique-se de que o pacote 'telegram-send' esteja instalado. Isso é feito utilizando o comando 'pip install telegram-send'.

📣 A etapa 'Notify' utiliza a ferramenta de linha de comando 'telegram-send' para enviar uma mensagem de notificação para o chat ou grupo especificado. Ela utiliza o token do bot do Telegram, o ID do chat e uma mensagem pré-definida.

🔒 Certifique-se de ter as permissões e configurações necessárias para que o job do Jenkins acesse a API do Telegram e envie mensagens por meio do seu bot.

📝 Observação: Este Jenkinsfile pressupõe que você já tenha um ambiente configurado com Jenkins, Git e Maven instalados e configurados.
"""

def repoUrl = '<your-repo-url>'
def telegramBotToken = '<your-telegram-bot-token>'
def chatId = '<your-chat-id>'

pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        git repoUrl
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }

    stage('Notify') {
      steps {
        sh 'pip install telegram-send'
        

        script {
          def message = "✅ Build completed successfully! 🎉"
          sh "telegram-send --config telegram.conf --message '${message}' --token '${telegramBotToken}' --chat_id '${chatId}'"
        }
      }
    }
  }
}
