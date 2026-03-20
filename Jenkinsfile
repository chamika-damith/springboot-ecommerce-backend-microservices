pipeline {
    agent any

    environment {
        REGISTRY = "${env.DOCKER_REGISTRY ?: 'docker.io'}"
        IMAGE_NAMESPACE = "${env.IMAGE_NAMESPACE ?: 'chamika'}"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        DOCKER_BUILDKIT = '1'
        COMPOSE_PROJECT_NAME = 'ecommerce-microservices'
    }

    options {
        timestamps()
        ansiColor('xterm')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'chmod +x Eureka-server/mvnw api-gateway/mvnw product-service/mvnw user-service/mvnw order-service/mvnw'
            }
        }

        stage('Build Artifacts') {
            steps {
                sh '''
                    set -e
                    cd product-service && ./mvnw -B clean install -DskipTests
                    cd ../user-service && ./mvnw -B clean package -DskipTests
                    cd ../order-service && ./mvnw -B clean package -DskipTests
                    cd ../api-gateway && ./mvnw -B clean package -DskipTests
                    cd ../Eureka-server && ./mvnw -B clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Images') {
            steps {
                sh '''
                    set -e
                    docker build -f Eureka-server/Dockerfile -t $REGISTRY/$IMAGE_NAMESPACE/eureka-server:$IMAGE_TAG .
                    docker build -f api-gateway/Dockerfile -t $REGISTRY/$IMAGE_NAMESPACE/api-gateway:$IMAGE_TAG .
                    docker build -f product-service/Dockerfile -t $REGISTRY/$IMAGE_NAMESPACE/product-service:$IMAGE_TAG .
                    docker build -f user-service/Dockerfile -t $REGISTRY/$IMAGE_NAMESPACE/user-service:$IMAGE_TAG .
                    docker build -f order-service/Dockerfile -t $REGISTRY/$IMAGE_NAMESPACE/order-service:$IMAGE_TAG .
                '''
            }
        }

        stage('Push Docker Images') {
            when {
                expression { return env.DOCKER_CREDENTIALS_ID?.trim() }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: env.DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        set -e
                        echo "$DOCKER_PASSWORD" | docker login $REGISTRY -u "$DOCKER_USERNAME" --password-stdin
                        docker push $REGISTRY/$IMAGE_NAMESPACE/eureka-server:$IMAGE_TAG
                        docker push $REGISTRY/$IMAGE_NAMESPACE/api-gateway:$IMAGE_TAG
                        docker push $REGISTRY/$IMAGE_NAMESPACE/product-service:$IMAGE_TAG
                        docker push $REGISTRY/$IMAGE_NAMESPACE/user-service:$IMAGE_TAG
                        docker push $REGISTRY/$IMAGE_NAMESPACE/order-service:$IMAGE_TAG
                    '''
                }
            }
        }

        stage('Deploy With Compose') {
            when {
                expression { return env.DEPLOY_WITH_COMPOSE?.toBoolean() }
            }
            steps {
                sh '''
                    set -e
                    docker compose down || true
                    docker compose up -d --build
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout $REGISTRY || true'
        }
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
