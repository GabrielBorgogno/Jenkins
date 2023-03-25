node {
    def py_file=env.BUILD_ID
    def webex_integration= ""


    stage('RESULT') {
      def webex_dev = build job: 'NOTIFICATIONS_TEST'
      if (webex_dev.result.contains('FAILURE')) {
        webex_integration +="NOTIFICATIONS_TEST Failed"
      } else {
        webex_integration +="NOTIFICATIONS_TEST Succeeded"
      }
    }

    stage('Webex notification') {
        withCredentials([string(credentialsId: 'WEBEX', variable: 'webex')]) {
        echo "Webex Stage"
        sh"""
        python3 --version
        echo 'from webexteamssdk import WebexTeamsAPI\n'  >> ${py_file}.py
        echo "api = WebexTeamsAPI(access_token='${webex}')\n" >> ${py_file}.py
        echo "api.messages.create(roomId=${id}, parentId=None, toPersonId=None, toPersonEmail=None, text='${webex_integration}', markdown=None, files=None, attachments=None,)\n" >> ${py_file}}.py
        python3 ${py_file}.py
        rm ${py_file}.py

                """
        }
}

 stage('email_notification'){
        try {
        echo "Mail Stage";
        emailext body: """${webex_integration}""",
        mimeType: 'text/html',
        recipientProviders: [buildUser()],
        subject: """FAILED JOB ON NODE:[${env.NODE_NAME}]"""
        }
       catch(ArrayIndexOutOfBoundsException MAIL_ERROR) {
       println("Catching the exception");

}
        }
}

