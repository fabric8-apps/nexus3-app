#!/usr/bin/groovy
def stage(){
  return stageProject{
    project = 'fabric8-apps/nexus3-app'
    useGitTagForNextVersion = true
  }
}

def approveRelease(project){
  def releaseVersion = project[1]
  approve{
    room = null
    version = releaseVersion
    console = null
    environment = 'fabric8'
  }
}

def release(project){
  releaseProject{
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8.apps'
    githubOrganisation = 'fabric8-apps'
    artifactIdToWatchInCentral = 'nexus3-app'
    artifactExtensionToWatchInCentral = 'jar'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    imagesToPromoteToDockerHub = []
    extraImagesToTag = null
  }
}

def updateDownstreamDependencies(stagedProject) {
  pushPomPropertyChangePR {
    propertyName = 'nexus3-app.version'
    projects = [
            'fabric8io/fabric8-tenant-jenkins'
    ]
    version = stagedProject[1]
  }
}
return this;
