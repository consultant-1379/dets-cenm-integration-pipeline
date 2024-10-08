import java.io.File

String projectsSGFile = readFileFromWorkspace('Jenkins/service_group_list')
projectsSGFile.eachLine {
    project_name -> createPipelineBuildJob(project_name)
}


String pipeline_name
def createPipelineBuildJob(project_name) {
    project_name = project_name.replaceAll("\\s","") // remove empty spaces
    chart_dir = project_name.split('/').last()
    pipeline_name = project_name.split('/').last()
    pipeline_name = pipeline_name + '-pull-jfrog'
    jenkins_file = 'Jenkins/JobDSL/pull_ljfrog_Jenkinsfile'
    pipelineJob(pipeline_name) {
        description ('ENM Cloud Native Build pipeline ' + pipeline_name + ' - autogenerated using JobDSL - all manual changes will be overwritten!')
        //concurrentBuild(allowConcurrentBuild = false)
        properties {
        disableConcurrentBuilds()
        }
        parameters {
            stringParam('REQUIREMENT_FILE')
        }
        logRotator {
            numToKeep(30)
        }
        environmentVariables {
            env('REPO', project_name)
            env('CHART_DIR', chart_dir)
         }
        definition {
            cps {
                script(readFileFromWorkspace(jenkins_file))
                sandbox(true)
            }
        }
    }
}

