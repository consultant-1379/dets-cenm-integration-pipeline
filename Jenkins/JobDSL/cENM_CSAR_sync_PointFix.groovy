import java.io.File

pipelineJob('eric-enm-csar-sync-pointfix') {
    description ('Creation of CSAR sync - autogenerated using JobDSL - all manual changes will be overwritten!')
    concurrentBuild(allowConcurrentBuild = false)
    logRotator {
        numToKeep(30)
    }
    parameters {
        stringParam('CSAR_PACKAGE_VERSION')
        stringParam('PRODUCT_SET')
        stringParam('enm_iso_version')
        stringParam('SPRINT_TAG')
        labelParam('SLAVE') {
            defaultValue("${SLAVE}")
            description('The minimum required softwares and its versions needed for Slave are Kubernetes Client Version v1.17.3, Docker client version 17.04.0-ce, Helm version 3.2.0 or higher ')
        }
    }

    definition {
            cpsScm {
                scm {
                    git {
                        branch('master')
                        remote {
                            credentials('lciadm100_private_key')
                            url("${GERRIT_CENTRAL}/OSS/com.ericsson.oss.containerisation/eric-enm-integration-pipeline-code")
                        }
                        extensions {
                            cleanBeforeCheckout()
                        }
                    }
                }
                scriptPath("Jenkins/Jenkinsfile/Jenkinsfilecsarsync_PointFix")
                lightweight(lightweight = true)
            }
    }
}
