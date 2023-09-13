def repoUrl = '<sua-url-do-repositório>'
def mongoToolsDownloadUrl = '<url-de-download-das-ferramentas-do-mongodb>'
def mongoToolsArchive = '<nome-do-arquivo-de-arquivamento-das-ferramentas>.tar.gz'
def dbConnectionString = '<string-de-conexão-do-banco-de-dados>'
def dumpDirectory = '<diretório-de-dump>'

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

    stage('MongoDB Dump') {
      steps {
        sh "wget ${mongoToolsDownloadUrl} && tar -zxvf ${mongoToolsArchive}"

        def dumpFileName = "dump_${currentDate}.bson"
        sh "mkdir -p ${dumpDirectory}"
        sh "${mongoToolsDirectory}/mongodump --uri ${dbConnectionString} --out ${dumpDirectory} --archive=${dumpDirectory}/${dumpFileName}"
      }
    }
  }

  post {
    failure {
      emailext body: '''O pipeline falhou! Verifique os logs do Jenkins para mais detalhes.''',
        subject: 'Erro no pipeline Jenkins',
        to: 'emailficticio@gmail.com'
    }
  }
}
