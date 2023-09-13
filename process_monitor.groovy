pipeline {
  agent any
  
  stages {
    stage('Detectar e Relatório') {
      steps {
        script {
          def processosMortos = [:]
          def repeticaoMaxima = 5 // Configurar o limite de repetição permitido
          def relatorio = ""
          
          // Executar o comando para obter a lista de processos mortos
          def listaProcessos = sh(script: "grep -r 'Encerrar processos com alto uso de CPU' ${env.WORKSPACE}/logs/ | wc -l", returnStdout: true).trim()
          
          // Verificar se há repetição de processos mortos
          if (listaProcessos.toInteger() > repeticaoMaxima) {
            relatorio += "Repetição alta de processos mortos (${listaProcessos})\n"
          }
          
          // Gerar relatório
          if (relatorio) {
            println relatorio
            // Envie o relatório por e-mail ou qualquer outro meio necessário
          }
        }
      }
    }
  }
}
