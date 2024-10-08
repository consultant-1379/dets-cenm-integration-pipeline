import java.io.File

String pipeline_name
String projectsCommonFile = readFileFromWorkspace('Jenkins/MTE_Uninstall_Jobs')
projectsCommonFile.eachLine {
    project_name -> createPipelineBuildJob(project_name)
}

def createPipelineBuildJob(project_name) {
    pipelineJob(project_name) {
        description ('ENM Cloud Native Uninstall  Build pipeline ' + project_name + ' - autogenerated using JobDSL - all manual changes will be overwritten!')
        concurrentBuild(allowConcurrentBuild = true)
        logRotator {
            numToKeep(30)
        }
        parameters {
            stringParam('environment_name', '','The deployment name to run the job against')
            labelParam('slave') {
               defaultValue("${SLAVE}")
               description('')
            }
        }

        definition {
            cpsScm {
                scm {
                    git {
                        branch('master')
                        remote {
                            credentials('lciadm100_private_key')
                            url("${GERRIT_MIRROR}/OSS/com.ericsson.oss.containerisation/eric-enm-integration-pipeline-code")
                        }
                        extensions {
                             cleanBeforeCheckout()
                        }
                    }
                }
                scriptPath("Jenkins/Jenkinsfile/JenkinsfileUninstall")
                lightweight(lightweight = true)
            }
        }
    }
}
