def buildApp() {
    stage('Build with Maven') {
        echo '🔧 Building the Java application (from shared lib)...'
        def mvnHome = tool name: 'Maven3', type: 'maven'
        sh "'${mvnHome}/bin/mvn' clean package -DskipTests"
    }
}

def dockerBuildAndPush() {
    stage('Docker Build & Push') {
        echo '🐳 Building and pushing Docker image (from shared lib)...'
        withCredentials([usernamePassword(
            credentialsId: 'dockerhub',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )]) {
            sh """
                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                docker build -t java-app:${BUILD_NUMBER} .
                docker tag java-app:${BUILD_NUMBER} $DOCKER_USER/java-app:${BUILD_NUMBER}
                docker push $DOCKER_USER/java-app:${BUILD_NUMBER}
                docker logout
            """
        }
    }
}

def deployApp() {
    stage('Deploy') {
        echo '🚀 Deploying the container (from shared lib)...'
        sh """
            docker rm -f java-app || true
            docker run -d --name java-app -p 8090:8090 ahmedhany28/java-app:${BUILD_NUMBER}
        """
    }
}
