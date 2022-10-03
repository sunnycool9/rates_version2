
pipeline {
  agent any
  tools {
        maven 'mvnhome1'
        jdk 'openjdk11'
        oc 'oc'
  }
  environment {
        DATE = new Date().format('yyyddMMHHmmss')
        IMG_TAG = "${DATE}"
		    CLUSTER_NAME="open_cluster1"
		    DEV_ENV="ratesinfo-project"
		    GIT_BRANCH="main"
		    GIT_URL="https://github.com/sunnycool9/rates_version2.git"
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
        echo "Checking out from Git"
		    git branch: "${GIT_BRANCH}", credentialsId: 'githubscrt1', url: "${GIT_URL}"
        sh "mvn -DskipTests=true clean compile package"        
      }
    }
    stage('Build and Deploy Image') {
      steps {
        script {

          echo "Creating docker image and deploying new docker image"		  
		      docker.build("sunnycool17/rates1:${IMG_TAG}")
          docker.withRegistry('https://registry.hub.docker.com', 'mydocker_credential') {
                        docker.image("sunnycool17/rates1:${IMG_TAG}").push()
                        docker.image("sunnycool17/rates1:${IMG_TAG}").push("latest")
          }

          openshift.withCluster("${CLUSTER_NAME}") {
            openshift.withProject("${DEV_ENV}") {
               openshift.tag( 'docker.io/sunnycool17/rates1:${IMG_TAG}', 'ratesinfo-project/ratesinfo1:main1')
               echo "completed first rollout"
            }
          }
        }
      }
    }
  }
}
