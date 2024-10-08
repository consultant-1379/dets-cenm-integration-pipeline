import java.io.File
pipelineJob('cENM_e2e_RFA250') {
        description ('ENM Cloud Native Build pipeline RFA Jobs autogenerated using JobDSL - all manual changes will be overwritten!')
        concurrentBuild(allowConcurrentBuild = false)
        parameters {
            stringParam('ISO_VERSION')
            stringParam('PRODUCT_SET')
            choiceParam('CLUSTER_ID',['ieatenmc12a009'],'Select Cluster id based on cluster')
            choiceParam('DMT_ID',['701'],'Select Clusters DMT ID based on cluster')
        }
        logRotator {
            numToKeep(30)
        }
        definition {
            cpsScm {
                scm {
                    git {
                        branch('master')
                        remote {
                            credentials('lciadm100_private_key')
                            url("${GERRIT_MIRROR}/" + "OSS/com.ericsson.oss.containerisation/eric-enm-integration-pipeline-code")
                        }
                        extensions {
                             cleanBeforeCheckout()
                        }
                    }
                }
                scriptPath("Jenkins/Jenkinsfile/JenkinsfileRFA")
            }
        }
    }

