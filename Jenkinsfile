pipeline {
    agent any 
    tools {
        maven "maven-3.8.5"
    }
    environment {
        GIT_REPOSITORY = "<https://github.com/taminhhieu31gl/spring-petclinic.git>"
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "10.0.1.67:8081"
        NEXUS_CREDENTIAL_ID = "admin_nexus"
        NEXUS_REPOSITORY = "DemoNotes"
    }
    stages {
        stage("Checkout"){
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']],extensions: [[$class: 'CleanCheckout']], userRemoteConfigs: [[url: GIT_REPOSITORY]]])
            }
        }
        stage("Build"){
            steps {
                dir("target"){
                    bat "echo 'spring.profiles.active=${environment}' > application.properties"
                }
                //sh "mvn -f . -Dprofile=$ENV package"
                bat "mvn package"
            }
        }
        stage("Publish to Nexus"){
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob}"
                    artifactPath = filesByGlob[0].path;
                    echo "${filesByGlob[0].name} ${artifactPath} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        newVersion = "${environment}-${BUILD_NUMBER}";
                        echo "File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version: ${newVersion}";
                        result = nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: newVersion,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging]
                            ]
                        );
                        if(!result){
                            error "Upload artifact ${filesByGlob[0].name} fail!";
                        }
                    } else {
                        error "File: ${artifactPath}, could not be found";
                    }
                }
            }
        }
    }
}
