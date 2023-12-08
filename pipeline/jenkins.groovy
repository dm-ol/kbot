pipeline {
agent any
	environment {
		REPO = 'https://github.com/dm-ol/kbot'
		BRANCH = 'main'
	}
    parameters {

        choice(name: 'OS', choices: ['linux', 'darwin', 'windows', 'all'], description: 'Choice the type of operating system')
        
        choice(name: 'ARCH', choices: ['amd64', 'arm64', 'all'], description: 'Choice the type of system')

    }
    stages {
        stage('Example') {
            steps {
                echo "Build for platform: ${params.OS}"

                echo "Build for arch: ${params.ARCH}"

            }
        }
		
		stage("clone") {
		    steps {
		        echo 'CLONE REPOSITORY'
				  git branch: "${BRANCH}, url: "${REPO}"
			}
	    }
        
		stage("test") {
		     steps {
		        echo 'TEST EXECUTION STARTED'
				sh 'make test'
			}
	    }
		
		stage("build") {
		     steps {
		        echo 'BUILD EXECUTION STARTED'
				sh 'make build'
			}
	    }
		
		stage("image") {
		     steps {
		        echo 'IMAGE EXECUTION STARTED'
				sh 'make image'
			}
	    }
		
		stage("push") {
		     steps {
			    script {
		            docker.withRegistry('', 'dockerhub') {
				    sh 'make push'
				    }
				}
			}
	    }
	}
}