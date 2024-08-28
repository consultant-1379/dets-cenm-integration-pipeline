import java.io.File

String projectsSGFile = readFileFromWorkspace('Jenkins/infra_service_groups')
projectsSGFile.eachLine {
    project_name -> createPipelineBuildJob(project_name)
}


String pipeline_name
def createPipelineBuildJob(project_name) {
    project_name = project_name.replaceAll("\\s","")
    pipeline_name=project_name
    pipeline_name = pipeline_name + '-pull-jfrog'
    jenkins_file = 'Jenkins/JobDSL/pull_ljfrog_infra_Jenkinsfile'
    pipelineJob(pipeline_name) {
        description ('ENM Cloud Native Build pipeline ' + pipeline_name + ' - autogenerated using JobDSL - all manual changes will be overwritten!')
        concurrentBuild(allowConcurrentBuild = false)
        parameters {
            stringParam('INFRA_VESRION')
        }
        logRotator {
            numToKeep(30)
        }
        environmentVariables {
            env('CHART_DIR', project_name)
        }
        definition {
            cps {
                script(readFileFromWorkspace(jenkins_file))
                sandbox(true)
            }
        }
    }
}

