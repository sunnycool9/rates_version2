
pipeline {
  agent any
  tools {
        maven 'mvnhome1'
        jdk 'openjdk11'
        oc 'oc'
  }
  environment {
        //IMG_TAG = "${DATE}.${BUILD_NUMBER}"
        DATE = new Date().format('yyyddMMHHmmss')
        IMG_TAG = "${DATE}"
  }
  stages {
  stage ('Initialize') {
          steps {
              sh '''
                  echo "PATH = ${PATH}"
                  echo "M2_HOME = ${M2_HOME}"
              '''
          }
      }
    stage('Build App') {
      steps {
        git branch: "${env.GIT_BRANCH}", credentialsId: 'githubscrt1', url: "${env.GIT_URL}"
        sh "mvn -DskipTests=true clean compile package"
        echo "completed first rollout 2"
      }
    }
    stage('Build and Deploy Image') {
      steps {
        // sh "cp target/rates2-ver1.jar ocp/rates2-ver1.jar"
        script {

          docker.build("sunnycool17/rates1:${IMG_TAG}")

          docker.withRegistry('https://registry.hub.docker.com', 'mydocker_credential') {
                        docker.image("sunnycool17/rates1:${IMG_TAG}").push()
                        docker.image("sunnycool17/rates1:${IMG_TAG}").push("latest")
          }

          openshift.withCluster("${env.CLUSTER_NAME}") {
            openshift.withProject("${env.DEV_ENV}") {
               openshift.tag( 'docker.io/sunnycool17/rates1:${IMG_TAG}', 'ratesinfo-project/ratesinfo1:main1')
               echo "completed first rollout"
            }
          }
        }
      }
    }
  }
}
