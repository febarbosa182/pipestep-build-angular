package br.com.dynamic.build

class Build{
    def call (jenkins) {
        jenkins.podTemplate(
            containers: [
                jenkins.containerTemplate(name: 'node', image: jenkins.env.CI_IMAGE, ttyEnabled: true, command: 'cat')
            ],
            yamlMergeStrategy: jenkins.merge(),
            workspaceVolume: jenkins.persistentVolumeClaimWorkspaceVolume(
                claimName: "pvc-${jenkins.env.JENKINS_AGENT_NAME}",
                readOnly: false
            )
        )

        {
            jenkins.node(jenkins.POD_LABEL){
                jenkins.container('node'){
                    jenkins.sh label: "Build application", script: "npm run build"
                }
            }
        }
    }
}