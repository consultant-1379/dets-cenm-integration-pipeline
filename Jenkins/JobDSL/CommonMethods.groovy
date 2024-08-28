def checking_error_pods(){
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        def PodStatus = sh(script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}\' kubectl -n ${NAMESPACE} get pods | egrep \"ImagePullBackOff|CrashLoopBackOff|Error|Pending|ErrImagePull\" | awk \" {print \\\$1} {print \\\$3} \" ||  true '", returnStdout: true).trim()
        echo " Pod status ${PodStatus}"
        if(PodStatus == ""){
            echo "All pods are up and running"
        }
        else{
            echo "Below pods are not up and running"
            def report = sh(script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}\' kubectl -n ${NAMESPACE} get pods | egrep \"ImagePullBackOff|CrashLoopBackOff|Error|Pending|ErrImagePull\" || true '", returnStdout: true).trim()
                echo "${report}"
                sh "exit 1"
        }
    }
    else{
        def PodStatus = sh(script : "${kubectl} -n ${NAMESPACE} get pods | egrep 'ImagePullBackOff|CrashLoopBackOff|Error|Pending|ErrImagePull' | awk '{print \$1} {print \$3}' || true", returnStdout: true).trim()
        echo "pod status ${PodStatus} "
        if(PodStatus == ""){
            echo "All pods are up and running"
        }
        else{
            echo "Below pods are not up and running"
            def report = sh(script : "${kubectl} -n ${NAMESPACE} get pods | egrep 'ImagePullBackOff|CrashLoopBackOff|Error|Pending|ErrImagePull' || true", returnStdout: true).trim()
            echo " ${report} "
            sh "exit 1"
        }
    }
}

def compare_productsetversions(new_ps,cmp_ps){
    def new_ps_list = (new_ps.split('-'))[0].split("\\.")
    def cmp_ps_list = (cmp_ps.split('-'))[0].split("\\.")
    try{
        cmp_ps_list+=cmp_ps.split('-')[1]
    }
    catch(err){
        cmp_ps_list+="0"
    }
    try{
        new_ps_list+=new_ps.split('-')[1]
    }
    catch(err){
        new_ps_list+="0"
    }
    def count = new_ps_list.size()

    if (new_ps_list[0].toInteger() > cmp_ps_list[0].toInteger()){
       return true
    }
    else if(new_ps_list[0].toInteger() == cmp_ps_list[0].toInteger()){
        if (new_ps_list[1].toInteger() > cmp_ps_list[1].toInteger()){
            return true
        }
        else if (new_ps_list[1].toInteger() == cmp_ps_list[1].toInteger()){
            if (new_ps_list[2].toInteger() > cmp_ps_list[2].toInteger()){
                return true
            }
        }
    }

   count1 = 0
   for(int ind=0; ind<count-1; ind++) {
    if (new_ps_list[ind].toInteger() == cmp_ps_list[ind].toInteger()) {
        count1+=1
     }
   }
    if(count1 == 3){
        if (new_ps_list[3].toInteger() == cmp_ps_list[3].toInteger()){
            return true
        }
    }
   return false
}

def remove_yaml_with_artifacts(){
     try{
        snapshot_string="${snapshot_artifacts}"
     }
     catch(Exception ex){
        snapshot_string=""
     }

     if(snapshot_string != ""){
          echo "${snapshot_string}"
          def snapshot_artifacts_list=snapshot_string.split("@@")
          for (item in snapshot_artifacts_list){
                 println(item)
                 def integration_value_file_name=item.split("::")[0]
                 println(integration_value_file_name)
                 def values_file_URL=item.split("::")[1]
                 println(values_file_URL)
                 if ("${integration_value_file_name}" == "eric-enm-integration-functional-kaas-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf  cENM/scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                 }
                 if ("${integration_value_file_name}" == "eric-enm-integration-functional-test-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf cENM/Scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                 }
                 if ("${integration_value_file_name}" == "eric-enm-integration-production-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf cENM/Scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                 }
                 if ("${integration_value_file_name}" == "eric-enm-integration-kaas-core-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf cENM/Scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                 }
                 if ("${integration_value_file_name}" == "eric-enm-integration-openstack-core-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf cENM/Scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                 }
                 if ("${integration_value_file_name}" == "eric-enm-single-instance-production-integration-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf cENM/Scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                 }
                 if ("${integration_value_file_name}" == "eric-enm-multi-instance-functional-integration-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf cENM/Scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                 }
                 if ("${integration_value_file_name}" == "eric-enm-integration-extra-large-production-values"){
                     def version=values_file_URL.split("-values-")[1].split(".yaml")[0]
                     println(version)
                     env.integration_value_version=version
                     sh "rm -rf cENM/Scripts/${integration_value_file_name}-*"
                     sh "curl -4 ${values_file_URL} -o cENM/Scripts/${integration_value_file_name}-${version}.yaml"
                  }
              }
     }
     else{
        echo "No snapshot artifacts are provided and cENM MT jobs will not support snapshot so this stage created for Snapshot purpose"
     }
   }
def overriding_key_value_pairs() {

 def new_keys=[:]

 try{
        key_value_string="${snapshot_integration_key_value_pairs}"
     }
     catch(Exception ex){
        key_value_string=""
     }
         if(key_value_string != ""){
          echo "${snapshot_string}"
          def snapshot_integration_key_value_pairs_list=key_value_string.split("@@")
          for (item in snapshot_integration_key_value_pairs_list){
                 println(item)
                 def integration_value_key=item.split(";;")[0]
                 println(integration_value_key)
                 def integration_value=item.split(";;")[1]
                 println(integration_value)
                 new_keys[integration_value_key]=integration_value
          }
        print(new_keys)
        filename ="${HOME_DIR}/cENM/Scripts/${integration_values_file_path}"
        values = readYaml file: filename
       for (parameter in new_keys){
         def value = parameter.value
         def key = parameter.key
         println(value)
         println(key)
         if (integration_values_file_keys_exists( values, key, value)){
               if ( value == "TARGET_DOCKER_REGISTRY_URL"){
                   value="TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT"
               }
               if (value=='null'){
                  value=null
                }
               if (value=='true'){
                  value=true
                }
               if (value=='false'){
                  value=false
                }
              update_IntegrationValues_with_values_snapshot(key,value)
         }
        else {
        echo "ERROR: The given ${key} is not present in the ${filename}"
        sh "exit 1"
     }
   }
   sh "rm -f ${filename}"
   writeYaml file: filename, data: values
   sh "sed -i \"/annotations*/s/'//g\" ${filename}"
   sh "sed -i \"/annotations*/s/null/{}/g\" ${filename}"
         }
   else{
          echo "No snapshot key_value are provided andcENM MT jobs will not support snapshot so this stage created for Snapshot purpose"
   }
 }
def remove_tgz_with_artifacts(){
     try{
        snapshot_string="${snapshot_artifacts}"
     }
     catch(Exception ex){
        snapshot_string=""
     }
     if(snapshot_string != ""){
          echo "${snapshot_string}"
          def snapshot_artifacts_list=snapshot_string.split("@@")
          for (item in snapshot_artifacts_list){
                 println(item)
                 def chart_name=item.split("::")[0]
                 println(chart_name)
                 def chart_URL=item.split("::")[1]
                 println(chart_URL)
                 version=chart_URL.split("-integration-")[1].split(".tgz")[0]
                 println(version)
                 if ("${chart_name}" == "eric-enm-bro-integration"){
                     env.bro_chart_version=version
                     sh "rm -rf cENM/Definitions/OtherTemplates/${chart_name}-*"
                     sh "curl ${chart_URL} -o cENM/Definitions/OtherTemplates/${chart_name}-${version}.tgz"
                 }
                 if ("${chart_name}" == "eric-enm-monitoring-integration"){
                     env.monitoring_chart_version=version
                     sh "rm -rf cENM/Definitions/OtherTemplates/${chart_name}-*"
                     sh "curl ${chart_URL} -o cENM/Definitions/OtherTemplates/${chart_name}-${version}.tgz"
                 }
                 if ("${chart_name}" == "eric-enm-pre-deploy-integration"){
                     env.pre_deploy_chart_version=version
                     sh "rm -rf cENM/Definitions/OtherTemplates/${chart_name}-*"
                     sh "curl ${chart_URL} -o cENM/Definitions/OtherTemplates/${chart_name}-${version}.tgz"
                 }
                 if ("${chart_name}" == "eric-enm-infra-integration"){
                     env.infra_chart_version=version
                     sh "rm -rf cENM/Definitions/OtherTemplates/${chart_name}-*"
                     sh "curl ${chart_URL} -o cENM/Definitions/OtherTemplates/${chart_name}-${version}.tgz"
                 }
                 if ("${chart_name}" == "eric-enm-stateless-integration"){
                     env.stateless_chart_version=version
                     sh "rm -rf cENM/Definitions/OtherTemplates/${chart_name}-*"
                     sh "curl ${chart_URL} -o cENM/Definitions/OtherTemplates/${chart_name}-${version}.tgz"
                 }
              }
     }
     else{
        echo "no snapshot string is provided"
     }
}

def get_cn_build_version_info(){
       echo "Product Set Version: ${product_set_version}"
       sh "curl -4 --location --request GET 'https://ci-portal.seli.wh.rnd.internal.ericsson.com/api/cloudnative/getCloudNativeProductSetContent/${drop}/${product_set_version}/'>cn_confidence_level_response.json"
       if (env.deployment_mechanism == 'charts') {
            env.infra_chart_version= sh (script : "./jq '.[1] .integration_charts_data|.[] | select(.chart_name==\"eric-enm-infra-integration\")|.chart_version' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
            if(env.infra_chart_version==''){
               error("Invalid Infra Integration chart provided. There is no Cloud native content for the given Product Set Version.")
            }
            env.bro_chart_version= sh (script : "./jq '.[1].integration_charts_data|.[]|select(.chart_name==\"eric-enm-bro-integration\")|.chart_version' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
            if(env.bro_chart_version==''){
               error("Invalid Bro Integration chart provided. There is no Cloud native content for the given Product Set Version.")
            }
            env.pre_deploy_chart_version= sh (script : "./jq '.[1].integration_charts_data|.[]|select(.chart_name==\"eric-enm-pre-deploy-integration\")|.chart_version' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
            if(env.pre_deploy_chart_version==''){
               error("Invalid Predeploy Integration chart provided. There is no Cloud native content for the given Product Set Version.")
            }
            env.stateless_chart_version= sh (script : "./jq '.[1].integration_charts_data|.[]|select(.chart_name==\"eric-enm-stateless-integration\")|.chart_version' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
            if(env.stateless_chart_version==''){
               error("Invalid Stateless Integration chart provided. There is no Cloud native content for the given Product Set Version.")
            }
            if (check_version_a_newer_than_version_b("${product_set_version}","21.03.57")){
                env.monitoring_chart_version= sh (script : "./jq '.[1].integration_charts_data|.[]|select(.chart_name==\"eric-enm-monitoring-integration\")|.chart_version' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
                if(env.monitoring_chart_version==''){
                    error("Invalid Monitoring Integration chart provided. There is no Cloud native content for the given Product Set Version.")
                    }
                  }
            else {
                env.monitoring_chart_version = ''
             }
            env.integration_value_version= sh (script : "./jq -r '.[2].integration_values_file_data[0].values_file_version' cn_confidence_level_response.json", returnStdout: true).trim()
            if(env.integration_value_version==''){
               error("Invalid Integration values provided. There is no Cloud native content for the given Product Set Version.")
            }
            println("bro integration chart version":env.bro_chart_version)
            println("pre deploy integration chart version":env.pre_deploy_chart_version)
            println("infra integration chart version":env.infra_chart_version)
            println("stateless integration chart version":env.stateless_chart_version)
            println("integration values version":env.integration_value_version)
      } else {
           def csar_index = 1
           def package_name = sh (script : "./jq '.[0] .csar_data[0].csar_name' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
           if ("${package_name}" == env.csar_package_name){
               csar_index = 0
           }
           echo "${csar_index}"
           env.csar_package_version=sh (script : "./jq -r '.[0] .csar_data[${csar_index}].csar_version' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
           if(env.csar_package_version==''){
              error("Invalid csar package provided. There is no Cloud native content for the given Product Set Version.")
           }
           env.csar_verified= sh (script : "./jq '.[0] .csar_data[${csar_index}].csar_verify' cn_confidence_level_response.json|sed 's/\"//g'", returnStdout: true).trim()
           println("csar package version":env.csar_package_version)
           println("csar verified status":env.csar_verified)
      }
}

/*
* This function is required to capture the status of running pods
 */

def checkrunningpods(){
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh'''
        ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} "${cenmbuildutilities_client}" kubectl get pods -n ${NAMESPACE} | grep Running | awk '{split($2,a,"/"); sum1+=a[1]; sum2+=a[2]} END {print sum1; print sum2; if(sum1==sum2){print "all pods are up and running"} else{print "all pods are not up and running, sleeping for 60 seconds..."; system("sleep 60"); exit 1}}'
        '''
    }
    else{
        sh'''
        ${kubectl} get pods -n ${NAMESPACE} | grep Running | awk '{split($2,a,"/"); sum1+=a[1]; sum2+=a[2]} END {print sum1; print sum2; if(sum1==sum2){print "all pods are up and running"} else{print "all pods are not up and running, sleeping for 60 seconds..."; system("sleep 60"); exit 1}}'
        '''
    }
}

/*
 * This function is required to capture the status of healthy pods
 */


def checkHealthyPods(){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        desiredStatefulset = sh ( script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get sts |  grep -v 'NAME' | awk -F\" \" \" {stssum += \\\$2} END {print stssum}\" '", returnStdout: true ).trim()
        echo "${desiredStatefulset}"
        desiredDeployment = sh ( script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get deploy |  grep -v 'NAME' | awk -F\" \" \" {depsum += \\\$2} END {print depsum}\" '", returnStdout: true ).trim()
        echo "${desiredDeployment}"
        currentHealthyPods = sh ( script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get pods   | grep -v 'NAME' | grep 'Running' | awk -F\" \" \"{print \\\$2}\" | awk -F\"/\" \"{podsum += \\\$1} END {print podsum}\" '", returnStdout: true ).trim()
        echo "${currentHealthyPods}"
    }
    else{
        desiredStatefulset = sh ( script: "${kubectl} -n ${NAMESPACE} get sts    | grep -v 'NAME' | awk -F' ' '{stssum += \$2} END {print stssum}'", returnStdout: true ).trim()
        echo "${desiredStatefulset}"
        desiredDeployment  = sh ( script: "${kubectl} -n ${NAMESPACE} get deploy | grep -v 'NAME' | awk -F' ' '{depsum += \$2} END {print depsum}'", returnStdout: true ).trim()
        echo "${desiredDeployment}"
        currentHealthyPods = sh ( script: "${kubectl} -n ${NAMESPACE} get pods   | grep -v 'NAME' | grep 'Running' | awk -F' ' '{print \$2}' | awk -F'/' '{podsum += \$1} END {print podsum}'", returnStdout: true ).trim()
        echo "${currentHealthyPods}"
    }
    desiredDeploy=desiredDeployment.toInteger()
    currentHealthy=currentHealthyPods.toInteger()
    desiredStateful=desiredStatefulset.toInteger()
    if ( desiredStateful + desiredDeploy <= currentHealthy ) {
        echo "Desired:Statefulset + Desired:Deployment is lesser than or equal to Current: Running Healthy Pods"
    } else {
        echo "Desired:Statefulset + Desired:Deployment is not matching Current: Running Healthy Pods"
        sh "sleep 60"
        sh "exit 1"
    }
}

/*
 * This function will captures the report of the deployment and sends the report through mail
 */

def emailReport(content){

    def subject = "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}!"

    try {
        emailext(body: content, mimeType: 'text/html', from: 'jenkins_monitoring@ericsson.com',
            subject: subject,
            to: "${EMAIL_LIST}" )
    } catch( err ){
        echo "$err"
    }
}

/* 
 * This function will generates report of the deployment.
 */

def reportHeading(status){
        def deployResult
        def color
        def investigation

        if(status == 'Success'){
                deployResult = "Success"
                color = "#9ACD32"
                investigation = "NA"
          } else if(status == 'Failed'){
                deployResult = "Failed"
                color = "#DC143C"
                investigation = "(If applicable), will be included in a follow up email"
          } else if(status == 'Aborted'){
                deployResult = "Aborted"
                color = "#95A5A6"
                investigation = "(If applicable), will be included in a follow up email"
          }
        def report = "Hi All,\n\n <h2>${environment_name} Deployment Result: <span style=\"background-color:${color};\">${status}</span></h2>"

        if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){

                env.BrochartVersion = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' helm list -n ${NAMESPACE} | grep eric-enm-bro-integration-${NAMESPACE} | awk \"{print \\\$(NF-1)}\"'", returnStdout: true)
                env.MonitoringchartVersion = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' helm list -n ${NAMESPACE} | grep eric-enm-monitoring-integration-${NAMESPACE} | awk \"{print \\\$(NF-1)}\"'", returnStdout: true)
                env.PredeploychartVersion = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' helm list -n ${NAMESPACE} | grep eric-enm-pre-deploy-integration-${NAMESPACE} | awk \"{print \\\$(NF-1)}\"'", returnStdout: true)
                env.InfrachartVersion = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' helm list -n ${NAMESPACE} | grep eric-enm-infra-integration-${NAMESPACE} | awk \"{print \\\$(NF-1)}\"'", returnStdout: true)
                env.StatelesschartVersion = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' helm list -n ${NAMESPACE} | grep eric-enm-stateless-integration-${NAMESPACE} | awk \"{print \\\$(NF-1)}\"'", returnStdout: true)
                report += " <ul><li><strong>Login URL :</strong> <a href=https://${ENM_LAUNCHER_HOSTNAME}/login> ${ENM_LAUNCHER_HOSTNAME}/login </a></li>"
        }
         else{
                env.BrochartVersion = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-bro-integration-${NAMESPACE} | awk \'{print \$(NF-1)}\'", returnStdout: true)
                if (check_version_a_newer_than_version_b("${env.product_set_version}","21.03.57")){
                    def MonitoringchartVersion = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-monitoring-integration-${NAMESPACE} | awk \'{print \$(NF-1)}\'", returnStdout: true)
                }
                env.PredeploychartVersion = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-pre-deploy-integration-${NAMESPACE} | awk \'{print \$(NF-1)}\'", returnStdout: true)
                env.InfrachartVersion = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-infra-integration-${NAMESPACE} | awk \'{print \$(NF-1)}\'", returnStdout: true)
                env.StatelesschartVersion = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-stateless-integration-${NAMESPACE} | awk \'{print \$(NF-1)}\'", returnStdout: true)
                report += " <ul><li><strong>Login URL :</strong> <a href=https://${ENM_LAUNCHER_HOSTNAME}/login> https://${ENM_LAUNCHER_HOSTNAME}/login </a></li>"
        }
        report += "<li><strong>Bro Chart version :</strong> ${env.BrochartVersion}</li>"
        report += "<li><strong>Pre Deploy Chart version :</strong> ${env.PredeploychartVersion}</li>"
        report += "<li><strong>Infra Chart version :</strong> ${env.InfrachartVersion}</li>"
        report += "<li><strong>Stateless Chart version :</strong> ${env.StatelesschartVersion}</li>"
        report += "<li><strong>Monitoring Chart version :</strong> ${env.MonitoringchartVersion}</li>"
        report += "<li><strong>Please find the Deployment Job Details:</strong>  <a href=${BUILD_URL}>${BUILD_URL}</a></li>"
        report += "<li><strong>Investigation Results : ${investigation} </strong></li></ul>"
        report = report.replace("\n","</br>")
        return report
}

/* 
 * This function will print the list of unstable pods after Upgrade
 */

def printPods(){
      if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        env.RestartPods = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get pods --sort-by='.status.containerStatuses[0].restartCount' --no-headers  | awk \"\\\$4>0 {print \\\$0}\"'", returnStdout: true)
        env.failedState = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get pods | egrep \"NAME|Running\" | egrep \"0/|NAME\" '" , returnStdout: true)
        env.initState = sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get pods | grep -v Running |grep -v Completed || true '", returnStdout: true)
        env.sidecarhttpd= sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get pods | egrep \"NAME|Running\" | egrep \"1/2|NAME\" '" , returnStdout: true)
        env.sidecarmonitoring= sh (script : "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} ''\${cenmbuildutilities_client}' kubectl -n ${NAMESPACE} get pods | egrep \"NAME|Running\" | egrep \"2/3|NAME\" '" , returnStdout: true)
       }
     else{
        env.RestartPods = sh (script : "\${kubectl} -n \${NAMESPACE} get pods --sort-by=\'.status.containerStatuses[0].restartCount\' --no-headers  | awk \'\$4>0 {print \$0}\'", returnStdout: true)
        env.failedState = sh (script : "${kubectl} -n ${NAMESPACE} get pods | egrep \'NAME|Running\' | egrep \'0/|NAME\'" , returnStdout: true)
        env.initState = sh (script : "${kubectl} -n ${NAMESPACE} get pods | grep -v Running |grep -v Completed || true", returnStdout: true)
        env.sidecarhttpd= sh (script : "${kubectl} -n ${NAMESPACE} get pods | egrep \'NAME|Running\' | egrep \'1/2|NAME\'" , returnStdout: true)
        env.sidecarmonitoring= sh (script : "${kubectl} -n ${NAMESPACE} get pods | egrep \'NAME|Running\' | egrep \'2/3|NAME\'" , returnStdout: true)
       }
        def report = "<ul><li><strong>Please find the Restart Pods in Deployment:</strong> \n<pre>${RestartPods}</pre> \n\n\n #kubectl -n ${NAMESPACE} get pods | egrep \'NAME|Running\' | egrep \'0/|NAME\' \n<pre>${failedState}</pre> \n\n\n #kubectl -n ${NAMESPACE} get pods | grep -v Running \n<pre>${initState}</pre> \n\n\n #kubectl -n ${NAMESPACE} get pods | egrep \'NAME|Running\' | egrep \'1/2|NAME\' \n<pre>${sidecarhttpd}</pre> \n\n\n #kubectl -n ${NAMESPACE} get pods | egrep \'NAME|Running\' | egrep \'2/3|NAME\' \n<pre>${sidecarmonitoring}</pre>"
        echo "${report}"
        report = report.replace("\n","</br>")
        report += "<style> pre {display: block;font-family: Lucida Console, Monaco, monospace; white-space: pre;} </style>";
        return report
}

/* 
 * This function is required to check if the integration parameters  exists in integration values.yaml file or not.
 */

def integration_values_file_keys_exists(element, keys,value){
     def str = keys.tokenize(".");
    _element = element
    _flag = false
     for (i = 0; i < str.size(); i++) {
        try{
           if ( _element!=null&&_element.containsKey(str[i])){
              _element = _element[str[i]]
              if(i==str.size()-1){
                _flag=true
              }
           }
           else{
               return false
           }
           } catch( err ) {
              return false
            }
        }
         return  _flag
}

/*
 * This function will update value of the key in integration values.yaml file.
 */

def update_IntegrationValues_with_values(keys,value){
     def str = keys.tokenize(".");
     if ('annotations' in str && ( value == null  || value == '{}' || value == 'null') ){
     }
     else{
         if("schedules" in str){
             dic_map = [[ 'every':env.BACKUP_INTERVAL, 'start': env.SCHEDULE_START_TIME , 'stop': env.SCHEDULE_STOP_TIME]]
             values."global"."bro"."backup"."scheduling".schedules = dic_map
         }
        else if("remoteHosts" in str){
             dic_map = [[ 'host':env.LOG_STREAMING_HOST, 'port': env.LOG_STREAMING_PORT ]]
             values."eric-enm-int-log-transformer"."eric-log-transformer"."egress"."syslog"."remoteHosts" = dic_map
         }
         else{
             def val= [:]
             val.putAll(values)
             for (parameter in str) {
                 if (val.containsKey(parameter)) {
                    val = val[parameter]
                   }
             }
             def bindings = [:]
             def map = bindings
             if (val == null || ( val != null && value != null ) ) {
                    for (i = 0; i < str.size(); i++) {
                         def part = str[i];
                         if (!map.containsKey(part)) {
                         map[part] = values.get(part)
                         }
                         if (i == str.size() - 1) {
                              map[part] = value
                         }
                         else {
                              map = map[part]
                         }
                   }
             }
         values.putAll(bindings)
         }
      }
}


/*
 * This function is required to Update the integration values.yaml file with values in snapshot_integration_key_value_pairs
 */
def update_IntegrationValues_with_values_snapshot(keys,value){
      def str = keys.tokenize(".");
      def val= [:]
      val.putAll(values)
      for (parameter in str) {
          if (val.containsKey(parameter)) {
              val = val[parameter]
          }
      }
      def bindings = [:]
      def map = bindings
      for (i = 0; i < str.size(); i++) {
           def part = str[i];
           if (!map.containsKey(part)) {
                map[part] = values.get(part)
           }
           if (i == str.size() - 1) {
                map[part] = value
           }
           else {
                map = map[part]
           }
           values.putAll(bindings)
       }
}

/*
 * This function is required to Update the integration values.yaml file with site-config properties
 */

def updateIntegrationValues() {
    try {
        def integration_value_map = [
                       'tags.deploy_fileaccess_nbi':'DEPLOY_FILEACCESS_NBI',
                       'tags.value_pack_ebs_m':'VALUE_PACK_EBS_M',
                       'tags.value_pack_ebs_ln':'VALUE_PACK_EBS_LN',
                       'tags.deploy_ext_app_launch_enabler':'DEPLOY_EXT_APP_LAUNCH_ENABLER',
                       'tags.deploy_cenmproxy':'DEPLOY_CENMPROXY',
                       'tags.deploy_nbalarmirp':'DEPLOY_NBALARMIRP',
                       'tags.deploy_fmx':'DEPLOY_FMX',
                       'tags.deploy_cellserv':'DEPLOY_CELLSERV',
                       'tags.deploy_cmevents':'DEPLOY_CMEVENTS',
                       'tags.deploy_dc-history_domainproxy':'DEPLOY_DC_HISTORY_DOMAINPROXY',
                       'tags.deploy_nbfmsnmp':'DEPLOY_NBFMSNMP',
                       'tags.deploy_saserv':'DEPLOY_SASERV',
                       'tags.deploy_msfm':'DEPLOY_MSFM',
                       'tags.deploy_mssnmpcm':'DEPLOY_MSSNMPCM',
                       'tags.deploy_apserv_msap':'DEPLOY_APSERV_MSAP',
                       'tags.deploy_nbi-bnsi-fm':'DEPLOY_NBI_BNSI_FM',
                       'tags.deploy_ipsmserv':'DEPLOY_IPSMSERV',
                       'tags.deploy_msnetlog':'DEPLOY_MSNETLOG',
                       'tags.deploy_mskpirt':'DEPLOY_MSKPIRT',
                       'tags.deploy_autoidservice':'DEPLOY_AUTOIDSERVICE',
                       'tags.deploy_fmhistory':'DEPLOY_FMHISTORY',
                       'tags.deploy_elasticsearch-admin':'DEPLOY_ELASTICSEARCH_ADMIN',
                       'tags.WinFIOL_OPS_AXE_Mediation':'WINFIOL_OPS_AXE_MEDIATION',
                       'global.pullSecret':'PULLSECRET',
                       'global.registry.url':'TARGET_DOCKER_REGISTRY_URL',
                       'global.registry.pullSecret':'PULLSECRET',
                       'global.persistentVolumeClaim.storageClass':'STORAGE_CLASS',
                       'global.ingress.enmHost':'ENM_LAUNCHER_HOSTNAME',
                       'global.loadBalancerIP.securityServiceLoadBalancerIP':'SECURITYSERVICELOADBALANCER_IP',
                       'global.loadBalancerIP.securityServiceLoadBalancerIP_IPv6':'SECURITYSERVICELOADBALANCERIP_IPV6',
                       'global.loadBalancerIP.ingressControllerLoadBalancerIP':'INGRESSCONTROLLERLOADBALANCERIP',
                       'global.loadBalancerIP.ingressControllerLoadBalancerIP_IPv6':'INGRESSCONTROLLERLOADBALANCERIP_IPV6',
                       'global.enmProperties.COM_INF_LDAP_ROOT_SUFFIX':'COM_INF_LDAP_ROOT_SUFFIX',
                       'global.enmProperties.COM_INF_LDAP_ADMIN_CN':'COM_INF_LDAP_ADMIN_CN',
                       'global.rwx.storageClass':'RWX_STORAGE_CLASS',
                       'global.rwx.storageClassCephCapped':'RWX_STORAGECLASS_CEPHCAPPED',
                       'global.rwx.homeSyncRequired':'RWX_HOMESYNC_REQUIRED',
                       'global.timezone':'TIME_ZONE',
                       'global.enmProperties.host_system_identifier':'HOST_SYSTEM_IDENTIFIER',
                       'global.enmProperties.PKI_EntityProfile_DN_ORGANIZATION_UNIT':'PKI_ENTITYPROFILE_DN_ORGANIZATION_UNIT',
                       'global.enmProperties.PKI_EntityProfile_DN_ORGANIZATION':'PKI_ENTITYPROFILE_DN_ORGANIZATION',
                       'global.enmProperties.PKI_EntityProfile_DN_COUNTRY_NAME':'PKI_ENTITYPROFILE_DN_COUNTRY_NAME',
                       'eric-net-ingress-l4.interfaces.internal':'LVS_INTERNAL_INTERFACE',
                       'eric-net-ingress-l4.interfaces.external':'LVS_EXTERNAL_INTERFACE',
                       'eric-net-ingress-l4.cniMode':'LVS_CNIMODE',
                       'eric-net-ingress-l4.podNetworkCIDR':'LVS_PODNETWORK_CIDR',
                       'eric-net-ingress-l4.ipv6podNetworkCIDR':'IPV6_LVS_PODNETWORK_CIDR',
                       'eric-data-graph-database-nj.persistentVolumeClaim.storageClass':'STORAGE_CLASS',
                       'eric-data-graph-database-nj.persistentVolumeClaim.backup.storageClass':'STORAGE_CLASS',
                       'eric-data-graph-database-nj.persistentVolumeClaim.logging.storageClass':'STORAGE_CLASS',
                       'global.vips.svc_FM_vip_ipv6address':'SVC_FM_VIP_IPV6ADDRESS',
                       'global.vips.svc_CM_vip_ipv6address':'SVC_CM_VIP_IPV6ADDRESS',
                       'global.vips.svc_PM_vip_ipv6address':'SVC_PM_VIP_IPV6ADDRESS',
                       'global.vips.amos_service_IPv6_IPs':'AMOS_SERVICE_IPV6_IPS',
                       'global.vips.scripting_service_IPv6_IPs':'SCRIPTING_SERVICE_IPV6_IPS',
                       'global.vips.visinamingsb_service_IPv6_IPs':'VISINAMINGSB_SERVICE_IPV6_IPS',
                       'global.vips.itservices_service_0_IPv6_IPs':'ITSERVICES_SERVICE_0_IPV6_IPS',
                       'global.vips.itservices_service_1_IPv6_IPs':'ITSERVICES_SERVICE_1_IPV6_IPS',
                       'global.vips.svc_FM_vip_fwd_ipv6address':'SVC_FM_VIP_FWD_IPV6ADDRESS',
                       'global.vips.fm_vip_address':'FM_VIP_ADDRESS',
                       'global.vips.svc_FM_vip_ipaddress':'FM_VIP_ADDRESS',
                       'global.vips.cm_vip_address':'CM_VIP_ADDRESS',
                       'global.vips.svc_CM_vip_ipaddress':'CM_VIP_ADDRESS',
                       'global.vips.pm_vip_address':'PM_VIP_ADDRESS',
                       'global.vips.svc_PM_vip_ipaddress':'PM_VIP_ADDRESS',
                       'global.vips.svc_FM_vip_fwd_ipaddress':'SVC_FM_VIP_FWD_IPADDRESS',
                       'global.vips.amos_vip_address':'AMOS_VIP_ADDRESS',
                       'global.vips.general_scripting_vip_address':'SCRIPT_VIP_ADDRESS',
                       'global.vips.element_manager_vip_address':'ELEMENT_MANAGER_VIP',
                       'global.vips.visinamingsb_vip_address':'CM_VIP_ADDRESS',
                       'global.vips.visinamingsb_service':'VISINAMINGSB_SERVICE',
                       'global.vips.itservices_0_vip_address':'ITSERVICES_0_VIP_ADDRESS',
                       'global.vips.itservices_1_vip_address':'ITSERVICES_1_VIP_ADDRESS',
                       'global.ip_version':'IP_VERSION',
                       'global.monitoring.kubelet.enabled':'ENABLE_KUBELET',
                       'global.monitoring.cadvisormetrics.enabled':'ENABLE_CADVISORMETRICS',
                       'global.monitoring.nodeexporter.enabled':'ENABLE_NODEEXPORTER',
                       'global.monitoring.clusterAccess.enabled':'ENABLE_CLUSTERACCESS',
                       'global.monitoring.clusterAccess.createClusterRole.enabled':'ENABLE_CREATECLUSTERROLE',
                       'eric-oss-ingress-controller-nx.service.loadBalancerIP':'LOADBALANCER_IP',
                       'eric-oss-ingress-controller-nx.service.loadBalancerIP_IPv6':'LOADBALANCERIP_IPV6',
                       'eric-oss-ingress-controller-nx.service.annotations':'ANNOTATIONS',
                       'eric-ctrl-bro.persistence.persistentVolumeClaim.storageClassName':'STORAGE_CLASS',
                       'eric-data-search-engine.persistence.data.persistentVolumeClaim.storageClassName':'STORAGE_CLASS',
                       'eric-data-search-engine.persistence.backup.persistentVolumeClaim.storageClassName':'RWX_STORAGE_CLASS',
                       'eric-data-search-engine.persistence.master.persistentVolumeClaim.storageClassName':'STORAGE_CLASS',
                       'eric-data-eshistory-search-engine.persistence.data.persistentVolumeClaim.storageClassName':'STORAGE_CLASS',
                       'eric-data-eshistory-search-engine.persistence.backup.persistentVolumeClaim.storageClassName':'RWX_STORAGE_CLASS',
                       'eric-data-eshistory-search-engine.persistence.master.persistentVolumeClaim.storageClassName':'STORAGE_CLASS',
                       'eric-enm-monitoring-master.monitoring.autoUpload.ddpsite':'DDPSITE',
                       'eric-enm-monitoring-master.monitoring.autoUpload.account':'ACCOUNT',
                       'eric-enm-monitoring-master.monitoring.autoUpload.password':'PASSWORD',
                       'eric-enm-ddc.eric-oss-ddc.autoUpload.ddpid':'DDC_DDPID',
                       'eric-enm-ddc.eric-oss-ddc.autoUpload.account':'DDC_ACCOUNT',
                       'eric-enm-ddc.eric-oss-ddc.autoUpload.password':'DDC_PASSWORD',
                       'eric-enm-ddc.eric-oss-ddc.persistentVolumeClaim.storageClassName':'RWX_STORAGE_CLASS',
                       'eric-pm-node-exporter.prometheus.nodeExporter.service.hostPort':'NODE_EXPORTER_HOST_PORT',
                       'eric-pm-node-exporter.prometheus.nodeExporter.service.servicePort':'NODE_EXPORTER_SERVICE_PORT',
                       'eric-pm-node-exporter.nodeExporter.service.hostPort':'NODE_EXPORTER_HOST_PORT',
                       'eric-pm-server.server.persistentVolume.storageClass':'STORAGE_CLASS',
                       'eric-enm-int-pm-server.eric-pm-server.server.persistentVolume.storageClass':'STORAGE_CLASS',
                       'eric-pm-alert-manager.persistence.persistentVolumeClaim.storageClassName':'STORAGE_CLASS',
                       'eric-enm-int-pm-alert-manager.eric-pm-alert-manager.persistence.persistentVolumeClaim.storageClassName':'STORAGE_CLASS',
                       'eric-cnom-document-database-mg.persistence.storageClass':'STORAGE_CLASS',
                       'eric-esm-server.ingress.hostname':'ENM_LAUNCHER_HOSTNAME',
                       'eric-enm-int-esm-server.eric-esm-server.ingress.hostname':'ENM_LAUNCHER_HOSTNAME',
                       'eric-cnom-server.ingress.hostname':'ENM_LAUNCHER_HOSTNAME',
                       'global.bro.backup.retention.limit':'RETENTION_LIMIT',
                       'global.bro.backup.retention.autoDelete':'AUTO_DELETE',
                       'global.bro.backup.scheduling.backupPrefix':'BACKUP_PREFIX',
                       'global.bro.backup.scheduling.schedules':'',
                       'global.bro.backup.preUpgradeAutomaticBackup':'PREUPGRADEAUTOMATICBACKUP',
                       'global.bro.backup.preUpgradeBackupName':'PREUPGRADEBACKUPNAME',
                       'global.bro.externalStorageCredentials':'SFTP_SECRET_NAME',
                       'eric-enm-int-log-transformer.eric-log-transformer.egress.syslog.enabled':'LOG_STREAMING',
                       'eric-enm-int-log-transformer.eric-log-transformer.egress.syslog.remoteHosts':''
                       ]
        def list_all_environment = env.getEnvironment()
        filename ="${HOME_DIR}/cENM/Scripts/${integration_values_file_path}"
        values = readYaml file: filename
       for (parameter in integration_value_map){
         def value = parameter.value
         def key = parameter.key
         if (integration_values_file_keys_exists( values, key, value)){
               if ( value == "TARGET_DOCKER_REGISTRY_URL"){
                   value="TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT"
               }
               value= list_all_environment.get(value)
                if(value=='null') {
                   value=null
              }
              if(value=='true') {
                     value=true
              }
              if(value=='false') {
                   value=false
              }
               update_IntegrationValues_with_values(key,value)
         }
       }
    if ("eric-enm-int-pm-server" in values){
           values."eric-enm-int-pm-server"."eric-pm-server"."custom_scrape_config" = ['enabled':false]
       }
       else {
           values."eric-pm-server"."custom_scrape_config" = ['enabled':false]
       }
       if (env.CUSTOM_SCRAPE_CONFIG_ENABLED == 'true'){
         if ("eric-enm-int-pm-server" in values)
         {
           values."eric-enm-int-pm-server"."eric-pm-server"."custom_scrape_config"."enabled" = true
           values."eric-enm-int-pm-server"."eric-pm-server"."custom_scrape_config"."jobs" = [['job_name':'ceph','static_configs':[['targets':['rook-ceph-mgr.rook-ceph.svc.cluster.local:9283']]],'metrics_path': '/metrics']]
         }
         else{
           values."eric-pm-server"."custom_scrape_config"."enabled" = true
           values."eric-pm-server"."custom_scrape_config"."jobs" = [['job_name':'ceph','static_configs':[['targets':['rook-ceph-mgr.rook-ceph.svc.cluster.local:9283']]],'metrics_path': '/metrics']]
         }
      }
        sh "rm -f ${filename}"
        writeYaml file: filename, data: values
        sh "sed -i \"/port/s/'//g\" ${filename}"
        if ("${ANNOTATIONS}" != null && "${ANNOTATIONS}" != '{}' && "${ANNOTATIONS}" != 'null'){
            sh "sed -i '{/{\$/d}' ${filename}"
            sh "sed -i 's/|-/{/g' ${filename}"
            sh "sed -i 's/\\\\/\"/g' ${filename}"
        }
    } catch( err ) {
        echo "$err"
        sh "exit 1"
    }
}

/*
 * This function is required to download the integration charts and integration values file from CI Internal area which can be used by * both Integration and MT teams.
 */

def download_charts_ci_internal(){
  try{
          if(env.bro_chart_version != ''){
               sh "curl -4 ${helm_repository_ci_internal}/eric-enm-bro-integration/eric-enm-bro-integration-${bro_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-bro-integration-${bro_chart_version}.tgz"
          }
          if (check_version_a_newer_than_version_b("${product_set_version}","21.03.57")){
              if(env.monitoring_chart_version != ''){
                   sh "curl -4 ${helm_repository_ci_internal}/eric-enm-monitoring-integration/eric-enm-monitoring-integration-${monitoring_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-monitoring-integration-${monitoring_chart_version}.tgz"
              }
          }
          if(env.pre_deploy_chart_version != ''){
               sh "curl -4 ${helm_repository_ci_internal}/eric-enm-pre-deploy-integration/eric-enm-pre-deploy-integration-${pre_deploy_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-pre-deploy-integration-${pre_deploy_chart_version}.tgz"
          }
          if(env.infra_chart_version != ''){
               sh "curl -4 ${helm_repository_ci_internal}/eric-enm-infra-integration/eric-enm-infra-integration-${infra_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-infra-integration-${infra_chart_version}.tgz"
          }
          if(env.stateless_chart_version != ''){
               sh "curl -4 ${helm_repository_ci_internal}/eric-enm-stateless-integration/eric-enm-stateless-integration-${stateless_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-stateless-integration-${stateless_chart_version}.tgz"
          }
          if(env.integration_value_version != 'null'){
              sh "curl -4 ${helm_repository_ci_internal}/eric-enm-integration-values/${integration_value_type}-${integration_value_version}.yaml -o cENM/Scripts/${integration_value_type}-${integration_value_version}.yaml"
         }
     }catch( err ) {
        echo "$err"
        sh "exit 1"
      }
}


/* 
 * This function is required to download the integration charts and integration values file from drop area which can be used by
 * both Integration and MT teams.
 */

def download_charts_release_area(){
  try{
          if(env.bro_chart_version != ''){
               sh "curl -4 ${helm_repository_release}/eric-enm-bro-integration/eric-enm-bro-integration-${bro_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-bro-integration-${bro_chart_version}.tgz"
          }
          if (check_version_a_newer_than_version_b("${product_set_version}","21.03.57")){
              if(env.monitoring_chart_version != ''){
                   sh "curl -4 ${helm_repository_release}/eric-enm-monitoring-integration/eric-enm-monitoring-integration-${monitoring_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-monitoring-integration-${monitoring_chart_version}.tgz"
              }
          }
          if(env.pre_deploy_chart_version != ''){
               sh "curl -4 ${helm_repository_release}/eric-enm-pre-deploy-integration/eric-enm-pre-deploy-integration-${pre_deploy_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-pre-deploy-integration-${pre_deploy_chart_version}.tgz"
          }
          if(env.infra_chart_version != ''){
               sh "curl -4 ${helm_repository_release}/eric-enm-infra-integration/eric-enm-infra-integration-${infra_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-infra-integration-${infra_chart_version}.tgz"
          }
          if(env.stateless_chart_version != ''){
               sh "curl -4 ${helm_repository_release}/eric-enm-stateless-integration/eric-enm-stateless-integration-${stateless_chart_version}.tgz -o cENM/Definitions/OtherTemplates/eric-enm-stateless-integration-${stateless_chart_version}.tgz"
          }
          if(env.integration_value_version != 'null'){
              sh "curl -4 ${helm_repository_release}/eric-enm-integration-values/${integration_value_type}-${integration_value_version}.yaml -o cENM/Scripts/${integration_value_type}-${integration_value_version}.yaml"
         }
     }catch( err ) {
        echo "$err"
        sh "exit 1"
      }
}

/*
 * This function is required to get the charts and integration values file from respective folder which will be used in the Upgrade
 * stage
 */

def get_integration_charts_path() {
   if(env.bro_chart_version != ''){
       env.bro_integration_chart_path = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep eric-enm-bro-integration*", returnStdout: true ).trim()
    }
    if (check_version_a_newer_than_version_b("${product_set_version}","21.03.57")){
       if(env.monitoring_chart_version != ''){
           env.monitoring_integration_chart_path = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep eric-enm-monitoring-integration*", returnStdout: true ).trim()
         }
    }
    if(env.pre_deploy_chart_version != ''){
       env.pre_deploy_integration_chart_path = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep eric-enm-pre-deploy-integration*", returnStdout: true ).trim()
    }
    if(env.infra_chart_version != ''){
       env.infra_integration_chart_path = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep eric-enm-infra-integration*", returnStdout: true ).trim()
    }
    if(env.stateless_chart_version != ''){
       env.stateless_integration_chart_path = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep eric-enm-stateless-integration*", returnStdout: true ).trim()
    }
    if(env.integration_value_version != ''){
       env.integration_values_file_path = sh (script: "ls cENM/Scripts |grep ${integration_value_type}*", returnStdout: true ).trim()
    }
}

/*
 *This function is required to get the chart versions.
 */

def get_integration_charts_version() {
 if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
       env.bro_chart_version = sh (script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'ls ${Client_HOME}/cENM/Definitions/OtherTemplates/ | grep \"eric-enm-bro-integration*\" |awk -F \"integration-\" \"{print \\\$(NF)}\"|awk -F \".tgz\" \"{print \\\$(NF-1)}\" '", returnStdout: true ).trim()
       if(env.bro_chart_version==''){
           error("Invalid Bro Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       if (check_version_a_newer_than_version_b("${env.product_set_version}","21.03.57")){
          env.monitoring_chart_version = sh (script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'ls ${Client_HOME}/cENM/Definitions/OtherTemplates/ | grep \"eric-enm-monitoring-integration*\" |awk -F \"integration-\" \"{print \\\$(NF)}\"|awk -F \".tgz\" \"{print \\\$(NF-1)}\" '", returnStdout: true ).trim()
          if(env.monitoring_chart_version==''){
              error("Invalid Monitoring Integration chart provided. There is no Cloud native content for the given Product Set Version.")
          }
       }
       env.pre_deploy_chart_version  = sh (script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'ls ${Client_HOME}/cENM/Definitions/OtherTemplates/ | grep \"eric-enm-pre-deploy-integration*\" |awk -F \"integration-\" \"{print \\\$(NF)}\"|awk -F \".tgz\" \"{print \\\$(NF-1)}\" '", returnStdout: true ).trim()
       if(env.pre_deploy_chart_version==''){
           error("Invalid Predeploy Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       env.infra_chart_version = sh (script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'ls ${Client_HOME}/cENM/Definitions/OtherTemplates/ | grep \"eric-enm-infra-integration*\" |awk -F \"integration-\" \"{print \\\$(NF)}\"|awk -F \".tgz\" \"{print \\\$(NF-1)}\" '", returnStdout: true ).trim()
       if(env.infra_chart_version==''){
           error("Invalid Infra Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       env.stateless_chart_version = sh (script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'ls ${Client_HOME}/cENM/Definitions/OtherTemplates/ | grep \"eric-enm-stateless-integration*\" |awk -F \"integration-\" \"{print \\\$(NF)}\"|awk -F \".tgz\" \"{print \\\$(NF-1)}\" '", returnStdout: true ).trim()
       if(env.stateless_chart_version==''){
           error("Invalid Stateless Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       env.integration_value_version = sh (script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'ls ${Client_HOME}/cENM/Scripts/ | grep \"${integration_value_type}*\" |awk -F \"values-\" \"{print \\\$(NF)}\"|awk -F \".yaml\" \"{print \\\$(NF-1)}\" '", returnStdout: true ).trim()
       if(env.integration_value_version==''){
           error("Invalid Integration values provided. There is no Cloud native content for the given Product Set Version.")
       }
    }
    else{
       env.bro_chart_version = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep \"eric-enm-bro-integration*\"|awk -F \'integration-\' \'{print \$(NF)}\'|awk -F \'.tgz\' \'{print \$(NF-1)}\'", returnStdout: true ).trim()
       if(env.bro_chart_version==''){
           error("Invalid Bro Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       if (check_version_a_newer_than_version_b("${product_set_version}","21.03.57")){
           env.monitoring_chart_version = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep \"eric-enm-monitoring-integration*\"|awk -F \'integration-\' \'{print \$(NF)}\'|awk -F \'.tgz\' \'{print \$(NF-1)}\'", returnStdout: true ).trim()
           if(env.monitoring_chart_version==''){
              error("Invalid Monitoring Integration chart provided. There is no Cloud native content for the given Product Set Version.")
          }
       }
       env.pre_deploy_chart_version = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep \"eric-enm-pre-deploy-integration*\"|awk -F \'integration-\' \'{print \$(NF)}\'|awk -F \'.tgz\' \'{print \$(NF-1)}\'", returnStdout: true ).trim()
       if(env.pre_deploy_chart_version==''){
           error("Invalid Predeploy Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       env.infra_chart_version = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep \"eric-enm-infra-integration*\"|awk -F \'integration-\' \'{print \$(NF)}\'|awk -F \'.tgz\' \'{print \$(NF-1)}\'", returnStdout: true ).trim()
       if(env.infra_chart_version==''){
           error("Invalid Infra Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       env.stateless_chart_version = sh (script: "ls cENM/Definitions/OtherTemplates/ |grep \"eric-enm-stateless-integration*\"|awk -F \'integration-\' \'{print \$(NF)}\'|awk -F \'.tgz\' \'{print \$(NF-1)}\'", returnStdout: true ).trim()
       if(env.stateless_chart_version==''){
           error("Invalid Stateless Integration chart provided. There is no Cloud native content for the given Product Set Version.")
       }
       env.integration_value_version = sh (script: "ls cENM/Scripts |grep \"${integration_value_type}*\"| awk -F \'values-\' \'{print \$(NF)}\'|awk -F \'.yaml\' \'{print \$(NF-1)}\'", returnStdout: true ).trim()
       if(env.integration_value_version==''){
           error("Invalid Integration values provided. There is no Cloud native content for the given Product Set Version.")
       }
    }
}

/*
This function is required for reading siteconfig properties for other resources
*/
def read_site_config_file_other_resource(){
    env.site_information_document_id= sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/deployments/?q=name=$environment_name\" |./jq '.[].documents[] | select(.schema_name==\"cENM_site_information\") | .document_id' | sed 's/\"//g'", returnStdout: true).trim()
    sh "curl -4 --location --request GET 'https://atvdit.athtem.eei.ericsson.se/api/documents/$site_information_document_id'>deployment_site_config_information.json"
    env.NAMESPACE =  sh (script : "./jq '.content.global.namespace' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.kubeConfig = "${workspace}/.kube/${KUBE_CRED}"
    env.HELM_BINARY = "helm"
    env.helm = "docker run --rm -v ${kubeConfig}:/root/.kube/config -v ${WORKSPACE}:${WORKSPACE} --workdir ${WORKSPACE} ${cenm_utilities_docker_image} helm"
    env.kubectl = "docker run --rm  -v ${kubeConfig}:/root/.kube/config -v ${WORKSPACE}:${WORKSPACE} --workdir ${WORKSPACE} ${cenm_utilities_docker_image} kubectl"
}

/*
 *This function is required to Check whether ENM launcher page is opening or not.
 */

def smokeTest() {
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
            response = sh (script: "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'curl --insecure -X POST -d \"IDToken1=Administrator&IDToken2=TestPassw0rd\" https://${ENM_LAUNCHER_HOSTNAME}/login -H \"Content-Type: application/x-www-form-urlencoded\" -H \"Accept-Encoding: gzip,deflate\" -H \"Accept: */*\" -L -H \"cache-control: no-cache\"'", returnStdout: true ).trim()
}
   else{
    response = sh (script: "curl --insecure -X POST -d \"IDToken1=Administrator&IDToken2=TestPassw0rd\" https://${ENM_LAUNCHER_HOSTNAME}/login -H \"Content-Type: application/x-www-form-urlencoded\" -H \"Accept-Encoding: gzip,deflate\" -H \"Accept: */*\" -L -H \'cache-control: no-cache\'", returnStdout: true ).trim()
}
    echo response
    if ( response.contains("Authentication Successful") ){
       echo "Success! Can login to ENM"
    } else {
       echo "Failed! Can\'t login to ENM"
       sh "exit 1"
    }
}

/*
 *This function is required to connect to the respective environment through kube-config file.
 */

def set_kube_config_file(){
    sh 'mkdir -p ${PWD}/.kube && chmod 775 ${PWD}/.kube && cp -v ${PWD}/Kube-Config-Files/${KUBE_CRED} ${PWD}/.kube/${KUBE_CRED} && chmod 620 ${PWD}/.kube/${KUBE_CRED}'
}

/*
 *This function is required to create docker secret.
 */

def create_docker_secret(){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
       sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo docker login  ${TARGET_DOCKER_REGISTRY_URL} --username=${CONTAINER_REGISTRY_USERNAME} --password=${CONTAINER_REGISTRY_PASSWORD}' "
    }
    sh "${kubectl} delete secret ${PULLSECRET}  --namespace ${NAMESPACE}|| true "
    sh "${kubectl} create secret generic ${PULLSECRET} --from-file=.dockerconfigjson=/root/.docker/config.json --type=kubernetes.io/dockerconfigjson --namespace ${NAMESPACE}"
}

/*
 *This function is required to download the CSAR package from nexus area.
 */

def download_csar_package_nexus(){
    if(env.csar_package_version != 'null'){
        echo "Download the CSAR package from nexus area"
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
          sh " ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo curl -4 -s --noproxy \"*\" -L ${nexus_repositoryUrl}/cENM/csar/${csar_package_name}/${csar_package_version}/${csar_package_name}-${csar_package_version}.csar -o ${Client_HOME}/cENM/${csar_package_name}-${csar_package_version}.csar' "
    }
    else{
          sh 'curl -4 -s --noproxy \\* -L "${nexus_repositoryUrl}/cENM/csar/${csar_package_name}/${csar_package_version}/${csar_package_name}-${csar_package_version}.csar" -o ${csar_package_name}-${csar_package_version}.csar'
     }
    }
}

/*
 *This function is required to download the CSAR package from release area.
 */

def download_csar_package_release(){
   if(env.csar_verified == 'true'){
        echo "Download the CSAR package from release area"
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh " ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo curl -4 -s --noproxy \"*\" -L ${helm_repository_release}/${csar_package_name}/${csar_package_name}-${csar_package_version}.csar -o ${Client_HOME}/cENM/${csar_package_name}-${csar_package_version}.csar' "
     }
   else{
        sh 'curl -4 -s --noproxy \\* -L "${helm_repository_release}/${csar_package_name}/${csar_package_name}-${csar_package_version}.csar" -o ${csar_package_name}-${csar_package_version}.csar'
   }
 }
}

/*
 *This function is required to extract the csar package.
 */

def extract_csar_package(){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
      sh "ssh -o ' LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo unzip -o ${Client_HOME}/cENM/${csar_package_name}-${csar_package_version}.csar -d ${Client_HOME}/cENM/' "
      }
   else{
       sh 'mkdir -p ${PWD}/cENM'
       sh 'unzip -o ${PWD}/${csar_package_name}-${csar_package_version}.csar -d ${PWD}/cENM'
       }
}

/*
 *This function is required to run the csar_utils.sh script after the extraction of csar with the respective docker registry url.
 */

def csar_utils(){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
      sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'bash ${Client_HOME}/cENM/Scripts/csar_utils.sh  --docker-registry-url=${TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT}'"
       }
   else{
    sh "bash ${WORKSPACE}/cENM/Scripts/csar_utils.sh  --docker-registry-url=${TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT}"
       }
}

/*
 *This function is required for pre configuring the workspace to CSAR set up.
 */

def csar_pre_config_setup(){
    sh 'mkdir -p ${PWD}/cENM && mkdir -p ${PWD}/cENM/Scripts && mkdir -p  ${PWD}/cENM/Definitions/OtherTemplates'
}

/*
 *This function is required for generating the artifact properties.
 */

def generate_artifact_file(){
    sh 'echo "environment_name=${environment_name}" >> artifact.properties'
    sh 'echo "product_set_version=${product_set_version}" >> artifact.properties'
    sh 'echo "drop=${drop}" >> artifact.properties'
    archiveArtifacts 'artifact.properties'
}


/*
 * Clean up modeling jobs pre-upgrade
 */
def pre_upgrade_cleanup() {
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} kubectl -n ${NAMESPACE} delete job eric-enm-models-all-services-job || true'"
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} kubectl -n ${NAMESPACE} delete job eric-enm-models-nrm-job || true'"
        }
    else{
        sh("${kubectl} delete jobs eric-enm-models-all-services-job -n ${NAMESPACE} || true")
        sh("${kubectl} delete jobs eric-enm-models-nrm-job -n ${NAMESPACE} || true")
        }
}

/*
 *This bro_integration_chart function performs the install or upgrade based on its deploy type.
 */

def bro_integration_chart(deploy_type,timeout){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} helm ${deploy_type} eric-enm-bro-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}* ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-bro-integration* -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}'")
        }
   else{
        sh("${helm} ${deploy_type} eric-enm-bro-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path}  cENM/Definitions/OtherTemplates/${bro_integration_chart_path} -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}")
       }
}

def upgrade_bro_integration_chart_with_hook(deploy_type,timeout){
  if(env.backup_name != ''){
      if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
          sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} helm ${deploy_type} eric-enm-bro-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}* ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-bro-integration* -n ${NAMESPACE} --set global.bro.backup.preUpgradeBackupName=$backup_name --wait --wait-for-jobs --timeout ${timeout}'")
      }
      else{
          sh("${helm} ${deploy_type} eric-enm-bro-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path}  cENM/Definitions/OtherTemplates/${bro_integration_chart_path} -n ${NAMESPACE} --set global.bro.backup.preUpgradeBackupName=$backup_name --wait --wait-for-jobs --timeout ${timeout}")
      }
  }
  else{
      echo "backup_name parameter is empty. This is a mandatory paramter for Upgrade. Please provide it and retry"
      sh "exit 1"
  }
}

/*
 *This monitoring_integration_chart function performs the install or upgrade based on its deploy type.
 */

def monitoring_integration_chart(deploy_type,timeout){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh ("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} helm ${deploy_type} eric-enm-monitoring-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}* ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-monitoring-integration* -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}'")
      }
   else{
     sh("${helm}  ${deploy_type} eric-enm-monitoring-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path} cENM/Definitions/OtherTemplates/${monitoring_integration_chart_path} -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}")
       }
}

/*
 *This pre_deploy_integration_chart function performs the install or upgrade based on its deploy type.
 */

def pre_deploy_integration_chart(deploy_type,timeout){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh ("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} helm ${deploy_type} eric-enm-pre-deploy-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}* ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-pre-deploy-integration* -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}'")
        }
   else{
        sh("${helm}  ${deploy_type} eric-enm-pre-deploy-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path} cENM/Definitions/OtherTemplates/${pre_deploy_integration_chart_path} -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}")
    }
}

/*
 *This infra_integration_chart function performs the install or upgrade based on its deploy type.
 */

def infra_integration_chart(deploy_type,timeout){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-infra-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
             if (env.revision == null){
                  env.revision = 0
             }
            echo "${revision}"
        sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'docker run --rm -d -v ${kubeConfig}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} --workdir ${Client_HOME}/conf armdocker.rnd.ericsson.se/proj-enm/cenm-build-utilities:latest  helm ${deploy_type} eric-enm-infra-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}*  ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-infra-integration* -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}'")
        sh "sleep 300s"
        sh '''
            while [[ true ]];
                   do
                if [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-infra-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-infra-integration-'\${NAMESPACE}'')" == *"deployed"* ]]; then
                         break
                elif [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-infra-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-infra-integration-'\${NAMESPACE}'')" == *"failed"* ]]; then
                         exit 1
                else
                         logger "Waiting for infra to get to deployed ...";
                         echo "${revision}"
                         sleep 300s ;
                fi
             done
           '''
}
    else{
    sh("${helm} ${deploy_type} eric-enm-infra-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path} cENM/Definitions/OtherTemplates/${infra_integration_chart_path} -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}")
}
}

/*
 *This stateless_integration_chart function performs the install or upgrade based on its deploy type.i
 */

def stateless_integration_chart(deploy_type,timeout){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-stateless-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
        if (env.revision == null){
             env.revision = 0
             }
            echo "${revision}"
        sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'docker run --rm -d -v ${kubeConfig}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} --workdir ${Client_HOME}/conf armdocker.rnd.ericsson.se/proj-enm/cenm-build-utilities:latest  helm ${deploy_type} eric-enm-stateless-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}* ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-stateless-integration* -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}'")
        sh "sleep 300s"
        sh '''
              while [[ true ]];
                  do
                if [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-stateless-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-stateless-integration-'\${NAMESPACE}'')" == *"deployed"* ]]; then
                         break
                elif [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-stateless-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-stateless-integration-'\${NAMESPACE}'')" == *"failed"* ]]; then

                         exit 1
                else
                         logger "Waiting for stateless to get to deployed ...";
                         echo "${revision}"
                         sleep 300s ;
                fi
             done
           '''
}
    else{
    sh("${helm} ${deploy_type} eric-enm-stateless-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path} cENM/Definitions/OtherTemplates/${stateless_integration_chart_path}  -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}")
       }
}

/*
 *This stateless_integration_chart function performs the install with hooks while restore and rollback
 */

def stateless_install_with_hook(deploy_type,timeout){
   if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-stateless-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
        if (env.revision == null){
             env.revision = 0
             }
            echo "${revision}"
        sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'docker run --rm -d -v ${kubeConfig}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} --workdir ${Client_HOME}/conf armdocker.rnd.ericsson.se/proj-enm/cenm-build-utilities:latest  helm ${deploy_type} eric-enm-stateless-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}* ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-stateless-integration* --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}'")
        sh "sleep 300s"
        sh '''
              while [[ true ]];
                  do
                if [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-stateless-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-stateless-integration-'\${NAMESPACE}'')" == *"deployed"* ]]; then
                         break
                elif [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-stateless-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-stateless-integration-'\${NAMESPACE}'')" == *"failed"* ]]; then

                         exit 1
                else
                         logger "Waiting for stateless to get to deployed ...";
                         echo "${revision}"
                         sleep 300s ;
                fi
             done
           '''
}
    else{
    sh("${helm} ${deploy_type} eric-enm-stateless-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path} cENM/Definitions/OtherTemplates/${stateless_integration_chart_path} --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout ${timeout}")
       }
}

/*
 * create an environment variable at the start of the cENM UG and II pipeline
 */

def start_time_deployment(){
     env.start_time = sh (script: "date +\'%F %T\'", returnStdout: true ).trim()
     echo "start_time :${start_time}"
}

/*
 * create an environment variable at the end of the cENM UG and II pipeline
 */

def end_time_deployment(){
    env.end_time = sh (script: "date +\'%F %T\'", returnStdout: true ).trim()
    echo "end_time :${end_time}"
}


/*
 * This function is required for performing cENM cleanup
 */

def cenm_uninstall(){
    sh "${helm} uninstall eric-enm-stateless-integration-${NAMESPACE} -n ${NAMESPACE} || true"
    sh '''
        while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
        do
            logger "Waiting for pods to terminate ...";
            sleep 30 ;
        done
       '''
    sh "${helm} uninstall eric-enm-infra-integration-${NAMESPACE} -n ${NAMESPACE} || true"
    sh '''
        while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
        do
            logger "Waiting for pods to terminate ...";
            sleep 30 ;
        done
       '''
    sh "${helm} uninstall eric-enm-pre-deploy-integration-${NAMESPACE} -n ${NAMESPACE} || true"
    sh '''
        while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
        do
            logger "Waiting for pods to terminate ...";
            sleep 30 ;
        done
       '''
    
    sh "${helm} uninstall eric-enm-monitoring-integration-${NAMESPACE} -n ${NAMESPACE} || true"
    sh '''
        while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
        do
            logger "Waiting for pods to terminate ...";
            sleep 30 ;
        done
       '''
    
    sh "${helm} uninstall eric-enm-bro-integration-${NAMESPACE} -n ${NAMESPACE} || true"
    sh '''
        while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
        do
            logger "Waiting for pods to terminate ...";
            sleep 30 ;
        done
       '''

    sh "${kubectl} delete jobs eric-enm-models-all-services-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs eric-enm-models-service-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs eric-enm-models-post-install-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs neo4j-dps-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs sfwkdb-schemamgt -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs flsdb-schemamgt -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs eric-enm-symlink-creation-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs eric-enm-update-software-version-history-job -n ${NAMESPACE} || true"
    sh '''
         for i in $(${kubectl} get pvc -n ${NAMESPACE} | egrep 'pg-data-postgres|pv-eric|datadir|neo4j-pvc|ctrl-bro|elasticsearch|eric-pm-server|eric-pm-alert-manager|eshistory|eric-enm|eric-cnom-document-database-mg|evnfm|fmx-rabbitmq' | awk '{print $1}');
         do
           ${kubectl} delete pvc $i -n ${NAMESPACE}|| true;
         done
       '''
}

def monitoring_uninstall()
{
         sh "${helm} uninstall eric-enm-monitoring-integration-${NAMESPACE} -n ${NAMESPACE} || true"
         sh "sleep 15"
         cenm_check_termination("pods","Terminating")
         sh "${kubectl} delete pvc prometheus-eric-enm-prometheus-db-prometheus-eric-enm-prometheus-0 -n ${NAMESPACE} || true"
         sh "sleep 5"
         cenm_check_termination("pvc","Terminating")
         sh "${kubectl} delete crd alertmanagers.monitoring.coreos.com podmonitors.monitoring.coreos.com prometheuses.monitoring.coreos.com prometheusrules.monitoring.coreos.com servicemonitors.monitoring.coreos.com || true"
         sh "sleep 5"
         cenm_check_termination("crd","monitoring.coreos.com")
         sh "${kubectl} delete svc eric-enm-kubelet -n ${NAMESPACE} || true"
         sh "sleep 5"
         cenm_check_termination("svc","Terminating")
}

def cenm_complete_uninstall(resource_name){
    command=sh(script:"${kubectl} get ${resource_name} -n ${NAMESPACE} 2>&1", returnStdout: true).trim()
        if (!command.contains("No resources found")){
           echo "Removing all ${resource_name}"
           sh "${kubectl} delete ${resource_name} --all -n ${NAMESPACE} || true"
           }
        else{
            sh "exit 0"
            }
}

def cenm_charts_remove(){
    sh '''
         for i in $(${helm} list -a --short -n ${NAMESPACE});
         do
           ${helm} uninstall $i -n ${NAMESPACE}|| true;
           elapsed_time=0
           POLLING_INTERVAL_SEC=30
           RESOURCE_VERIFICATION_TIMEOUT_SEC=600
           while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
           do
            logger "Waiting for pods to terminate ...";
            sleep 30;
            elapsed_time=$(($elapsed_time+$POLLING_INTERVAL_SEC))
            if [[ "${elapsed_time}" -ge "${RESOURCE_VERIFICATION_TIMEOUT_SEC}" ]]
            then
                echo "Unable to remove all resources in namespces"
                exit 1
            fi
           done
         done
       '''
}

def cenm_check_termination(resource_name,condition){
    def elapsed_time = 0
    def POLLING_INTERVAL_SEC = 10
    def RESOURCE_VERIFICATION_TIMEOUT_SEC = 300
    command=sh(script:"${kubectl} get ${resource_name} -n ${NAMESPACE} 2>&1 ", returnStdout: true).trim()
        while (command.contains(condition)){
            echo "${command}"
            echo "Waiting for termination of $resource_name....."
            sh "sleep ${POLLING_INTERVAL_SEC} "
            elapsed_time = elapsed_time + POLLING_INTERVAL_SEC
            if ( elapsed_time >= RESOURCE_VERIFICATION_TIMEOUT_SEC ){
                echo "Failed to terminate $resource_name "
                echo "Exiting the script "
                sh "exit 1"
               }
            command=sh(script:"${kubectl} get ${resource_name} -n ${NAMESPACE} 2>&1 ", returnStdout: true).trim()
            }

}

/*
*This function is required to download the kube config file of specified deployment from DIT page.
*/
def download_kube_config_file_from_dit(){
    env.kube_config_document_id= sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/deployments/?q=name=$environment_name\" |./jq '.[].documents[] | select(.schema_name==\"cloud_native_enm_kube_config\") | .document_id' | sed 's/\"//g'", returnStdout: true).trim()
    env.KUBE_CRED =  sh (script : "curl -4 -s \"https://atvdit.athtem.eei.ericsson.se/api/documents/$kube_config_document_id\" | ./jq '.name' | sed 's/\"//g'", returnStdout: true).trim()

    env.site_information_document_id= sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/deployments/?q=name=$environment_name\" |./jq '.[].documents[] | select(.schema_name==\"cENM_site_information\") | .document_id' | sed 's/\"//g'", returnStdout: true).trim()
    sh "curl -4 --location --request GET 'https://atvdit.athtem.eei.ericsson.se/api/documents/$site_information_document_id'>deployment_site_config_information.json"
    env.CLIENT_MACHINE_IP_ADDRESS = sh (script : "./jq '.content.global.client_machine.ipaddress' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CLIENT_MACHINE_USERNAME = sh (script : "./jq '.content.global.client_machine.username' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CLIENT_MACHINE_TYPE = sh (script : "./jq '.content.global.client_machine.type' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()

    sh "rm -rf ${workspace}/Kube-Config-Files/${KUBE_CRED}"
    sh "touch ${workspace}/Kube-Config-Files/${KUBE_CRED}"
    sh "curl -4 -s \"https://atvdit.athtem.eei.ericsson.se/api/documents/$kube_config_document_id\" | ./jq '.content' | sed 's/\"//g' >> ${workspace}/Kube-Config-Files/${KUBE_CRED}"
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' -tt ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'mkdir -p ${Client_HOME}/conf/'"
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' -tt ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'rm -rf ${Client_HOME}/conf/${KUBE_CRED}'"
        sh "scp -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${workspace}/Kube-Config-Files/${KUBE_CRED} ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS}:${Client_HOME}/conf/${KUBE_CRED}"
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' -tt ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'chmod 620 ${Client_HOME}/conf/${KUBE_CRED}'"
}
}

/*
 * Below function is to create a folder in sftp server
 */
def creation_folder_in_sftp_server(){
 if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
     sh'''
      ssh -o LogLevel=error -o 'StrictHostKeyChecking no' -tt ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} "${cenmbuildutilities_client} sshpass -p '${SFTP_MACHINE_PASSWORD}' ssh -o ' LogLevel=error' -o 'StrictHostKeyChecking no' -tt ${SFTP_MACHINE_USERNAME}@${SFTP_MACHINE_HOSTNAME} ' mkdir -p /shares/cENM-backups/${NAMESPACE}| true && chown ${SFTP_MACHINE_USERNAME}:${SFTP_MACHINE_USERNAME} /shares/cENM-backups/${NAMESPACE}'"
    '''
}
  else{
    sh '''
    docker run --rm -v ${WORKSPACE}:${WORKSPACE} --workdir ${WORKSPACE} ${cenm_utilities_docker_image} sshpass -p ${SFTP_MACHINE_PASSWORD} ssh  -o ' LogLevel=error' -o 'StrictHostKeyChecking no' -tt ${SFTP_MACHINE_USERNAME}@${SFTP_MACHINE_HOSTNAME} " mkdir -p /shares/cENM-backups/${NAMESPACE}|| true && chown ${SFTP_MACHINE_USERNAME}:${SFTP_MACHINE_USERNAME} /shares/cENM-backups/${NAMESPACE} "
    '''
    }
}

/*
 * Below function is to export backup to sftp server
 */
def export_backup(){
  echo "Exporting backup"
  if(env.BRO_DEFAULT_BACKUP_TYPE =='External'){
    def res= sh(script : """curl -4 -i -X POST -H "Content-Type:application/json" -d '{ \"action\": \"EXPORT\", \"payload\": { \"backupName\": \"${backup_name}\", \"uri\":\"sftp://$SFTP_MACHINE_USERNAME@$SFTP_MACHINE_HOSTNAME:22/shares/cENM-backups/${NAMESPACE}\", \"password\": \"$SFTP_MACHINE_PASSWORD\"}}' "https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/backup-manager/DEFAULT/action" -k --cookie cookie_cenmproxy""", returnStdout: true).trim()
    echo "export result : $res"
    //echo "$res"
    def actionid=sh( script :"""echo "$res" | grep id | grep -Eo '[0-9]{1,7}'""",returnStdout: true).trim()
    echo "ActionId: ${actionid} "

    while (true){
      def monitor=sh(script:"""curl -4 --header "Content-Type:application/json" -X GET https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/backup-manager/DEFAULT/action/$actionid -k --cookie cookie_cenmproxy -H 'Accept:application/json' -i | grep -i state""", returnStdout: true).trim()
      echo "Backup Information: ${monitor}"
      def state=sh( script :"echo '$monitor' | ./jq '.result'",returnStdout: true).trim()
          echo "Result Status : ${state}"
          if ( "${state}" == '"SUCCESS"'){
            echo "Successfully backup Exported"
            break
          }
          else if("${state}" == '"FAILURE"'){
            echo "Export failed"
            echo "${state}"
            exit 1
           }
          else {
           echo "Backup Exporting .............."
           sh 'sleep 30s'
        }
      }
   }
  else {
  echo "export the backup to sftp server"
  sh "${brocli} export $backup_name --uri 'sftp://$SFTP_MACHINE_USERNAME@$SFTP_MACHINE_HOSTNAME:22/shares/cENM-backups/${NAMESPACE}' --password '$SFTP_MACHINE_PASSWORD'"
  }
}

def import_backup(){
  echo "Importing backup from sftp server"
  sh "${brocli} import $backup_name --uri 'sftp://$SFTP_MACHINE_USERNAME@$SFTP_MACHINE_HOSTNAME:22/shares/cENM-backups/${NAMESPACE}' --password '$SFTP_MACHINE_PASSWORD'"
}

def check_version_a_newer_than_version_b(latest_productset_version,old_productset_version) {
    if(latest_productset_version > old_productset_version){
        return true
    } else{
        return false
    }

}

/*
*This function is required to untar or extract the JQ tar file from Software folder.
*/
def extract_jq(){
    echo "Extracting the jq software"
    sh "tar -xvf Software/jq-1.0.1.tar ; chmod +x ./jq"
}

/*
*This function is required to read the values from the integration_value_file and site_information file of specified deployment from DIT page.
*/
def read_site_config_info_from_dit(){

    env.site_information_document_id= sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/deployments/?q=name=$environment_name\" |./jq '.[].documents[] | select(.schema_name==\"cENM_site_information\") | .document_id' | sed 's/\"//g'", returnStdout: true).trim()
    env.integration_value_file_document_id= sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/deployments/?q=name=$environment_name\" |./jq '.[].documents[] | select(.schema_name==\"cENM_integration_values\") | .document_id' | sed 's/\"//g'", returnStdout: true).trim()

    sh "curl -4 --location --request GET 'https://atvdit.athtem.eei.ericsson.se/api/documents/$site_information_document_id'>deployment_site_config_information.json"
    sh "curl -4 --location --request GET 'https://atvdit.athtem.eei.ericsson.se/api/documents/$integration_value_file_document_id'>deployment_integration_values_file.json"

    env.NAMESPACE =  sh (script : "./jq '.content.global.namespace' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.EMAIL_LIST= sh (script : "./jq '.content.global.email_id' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()

    env.SFTP_MACHINE_HOSTNAME = sh (script : "./jq '.content.global.sftp_machine.hostname' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SFTP_MACHINE_USERNAME = sh (script : "./jq '.content.global.sftp_machine.users.username' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SFTP_MACHINE_PASSWORD = sh (script : "./jq '.content.global.sftp_machine.users.password' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()

    env.CLIENT_MACHINE_IP_ADDRESS = sh (script : "./jq '.content.global.client_machine.ipaddress' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CLIENT_MACHINE_USERNAME = sh (script : "./jq '.content.global.client_machine.username' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CLIENT_MACHINE_TYPE = sh (script : "./jq '.content.global.client_machine.type' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()

    env.RWX_STORAGE_CLASS = sh (script : "./jq '.content.global.rwx.storageClass' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.RWX_STORAGECLASS_CEPHCAPPED = sh (script : "./jq '.content.global.rwx.storageClassCephCapped' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.RWX_HOMESYNC_REQUIRED = sh (script : "./jq '.content.global.rwx.homeSyncRequired' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.STORAGE_CLASS = sh (script : "./jq '.content.global.persistentVolumeClaim.storageClass' deployment_integration_values_file.json | sed 's/\"//g'", returnStdout: true).trim()
    env.TIME_ZONE = sh (script : "./jq '.content.global.timezone' deployment_integration_values_file.json|sed 's/\"//g'", returnStdout: true).trim()
    env.HOST_SYSTEM_IDENTIFIER = sh (script : "./jq '.content.global.enmProperties.host_system_identifier' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.PKI_ENTITYPROFILE_DN_ORGANIZATION_UNIT = sh (script : "./jq '.content.global.enmProperties.PKI_EntityProfile_DN_ORGANIZATION_UNIT' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.PKI_ENTITYPROFILE_DN_ORGANIZATION = sh (script : "./jq '.content.global.enmProperties.PKI_EntityProfile_DN_ORGANIZATION' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.PKI_ENTITYPROFILE_DN_COUNTRY_NAME = sh (script : "./jq '.content.global.enmProperties.PKI_EntityProfile_DN_COUNTRY_NAME' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ENM_LAUNCHER_HOSTNAME= sh (script : "./jq '.content.global.ingress.enmHost' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.COM_INF_LDAP_ROOT_SUFFIX= sh (script : "./jq '.content.global.enmProperties.COM_INF_LDAP_ROOT_SUFFIX' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.COM_INF_LDAP_ADMIN_CN= sh (script : "./jq '.content.global.enmProperties.COM_INF_LDAP_ADMIN_CN' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.FM_VIP_ADDRESS = sh (script : "./jq '.content.global.vips.fm_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SVC_FM_VIP_FWD_IPADDRESS = sh (script : "./jq '.content.global.vips.svc_FM_vip_fwd_ipaddress' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CM_VIP_ADDRESS = sh (script : "./jq '.content.global.vips.cm_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CUSTOM_SCRAPE_CONFIG_ENABLED = sh (script : "./jq '.content.\"eric-pm-server\".custom_scrape_config.enabled' deployment_integration_values_file.json | sed 's/\"//g'", returnStdout: true).trim()
    env.PM_VIP_ADDRESS = sh (script : "./jq '.content.global.vips.pm_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.AMOS_VIP_ADDRESS = sh (script : "./jq '.content.global.vips.amos_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ELEMENT_MANAGER_VIP = sh (script : "./jq '.content.global.vips.element_manager_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SCRIPT_VIP_ADDRESS = sh (script : "./jq '.content.global.vips.general_scripting_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.VISINAMINGSB_SERVICE = sh (script : "./jq '.content.global.vips.visinamingsb_service' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ITSERVICES_0_VIP_ADDRESS = sh (script : "./jq '.content.global.vips.itservices_0_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ITSERVICES_1_VIP_ADDRESS = sh (script : "./jq '.content.global.vips.itservices_1_vip_address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SVC_FM_VIP_IPV6ADDRESS = sh (script : "./jq '.content.global.vips.svc_FM_vip_ipv6address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SVC_CM_VIP_IPV6ADDRESS = sh (script : "./jq '.content.global.vips.svc_CM_vip_ipv6address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SVC_PM_VIP_IPV6ADDRESS = sh (script : "./jq '.content.global.vips.svc_PM_vip_ipv6address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.AMOS_SERVICE_IPV6_IPS = sh (script : "./jq '.content.global.vips.amos_service_IPv6_IPs' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SCRIPTING_SERVICE_IPV6_IPS = sh (script : "./jq '.content.global.vips.scripting_service_IPv6_IPs' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.VISINAMINGSB_SERVICE_IPV6_IPS = sh (script : "./jq '.content.global.vips.visinamingsb_service_IPv6_IPs' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ITSERVICES_SERVICE_0_IPV6_IPS = sh (script : "./jq '.content.global.vips.itservices_service_0_IPv6_IPs' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ITSERVICES_SERVICE_1_IPV6_IPS = sh (script : "./jq '.content.global.vips.itservices_service_1_IPv6_IPs' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_FILEACCESS_NBI= sh (script : "./jq '.content.tags.deploy_fileaccess_nbi' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.VALUE_PACK_EBS_M= sh (script : "./jq '.content.tags.value_pack_ebs_m' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.VALUE_PACK_EBS_LN= sh (script : "./jq '.content.tags.value_pack_ebs_ln' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_EXT_APP_LAUNCH_ENABLER= sh (script : "./jq '.content.tags.deploy_ext_app_launch_enabler' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_CENMPROXY= sh (script : "./jq '.content.tags.deploy_cenmproxy' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.IP_VERSION = sh (script : "./jq '.content.global.ip_version' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SVC_FM_VIP_FWD_IPV6ADDRESS = sh (script : "./jq '.content.global.vips.svc_FM_vip_fwd_ipv6address' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SECURITYSERVICELOADBALANCER_IP = sh (script : "./jq '.content.global.loadBalancerIP.securityServiceLoadBalancerIP' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SECURITYSERVICELOADBALANCERIP_IPV6 = sh (script : "./jq '.content.global.loadBalancerIP.securityServiceLoadBalancerIP_IPv6' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.INGRESSCONTROLLERLOADBALANCERIP = sh (script : "./jq '.content.global.loadBalancerIP.ingressControllerLoadBalancerIP' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.INGRESSCONTROLLERLOADBALANCERIP_IPV6 = sh (script : "./jq '.content.global.loadBalancerIP.ingressControllerLoadBalancerIP_IPv6' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ENABLE_KUBELET = sh (script : "./jq '.content.global.monitoring.kubelet.enabled' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ENABLE_CADVISORMETRICS = sh (script : "./jq '.content.global.monitoring.cadvisormetrics.enabled' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ENABLE_NODEEXPORTER = sh (script : "./jq '.content.global.monitoring.nodeexporter.enabled' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_NBALARMIRP= sh (script : "./jq '.content.tags.deploy_nbalarmirp' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_FMX= sh (script : "./jq '.content.tags.deploy_fmx' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_CELLSERV= sh (script : "./jq '.content.tags.deploy_cellserv' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_CMEVENTS= sh (script : "./jq '.content.tags.deploy_cmevents' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_DC_HISTORY_DOMAINPROXY= sh (script : "./jq '.content.tags.deploy_dc-history_domainproxy' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_NBFMSNMP= sh (script : "./jq '.content.tags.deploy_nbfmsnmp' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_SASERV= sh (script : "./jq '.content.tags.deploy_saserv' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_MSFM= sh (script : "./jq '.content.tags.deploy_msfm' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_MSSNMPCM= sh (script : "./jq '.content.tags.deploy_mssnmpcm' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_APSERV_MSAP= sh (script : "./jq '.content.tags.deploy_apserv_msap' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_NBI_BNSI_FM= sh (script : "./jq '.content.tags.deploy_nbi-bnsi-fm' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_IPSMSERV= sh (script : "./jq '.content.tags.deploy_ipsmserv' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_MSNETLOG= sh (script : "./jq '.content.tags.deploy_msnetlog' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_MSKPIRT= sh (script : "./jq '.content.tags.deploy_mskpirt' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_AUTOIDSERVICE= sh (script : "./jq '.content.tags.deploy_autoidservice' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_FMHISTORY= sh (script : "./jq '.content.tags.deploy_fmhistory' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DEPLOY_ELASTICSEARCH_ADMIN= sh (script : "./jq '.content.tags.deploy_elasticsearch-admin' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.WINFIOL_OPS_AXE_MEDIATION= sh (script : "./jq '.content.tags.deploy_WinFIOL_OPS_AXE_Mediation' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ENABLE_CLUSTERACCESS = sh (script : "./jq '.content.global.monitoring.clusterAccess.enabled' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ENABLE_CREATECLUSTERROLE = sh (script : "./jq '.content.global.monitoring.clusterAccess.createClusterRole.enabled' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LOADBALANCER_IP = sh (script : "./jq '.content.\"eric-oss-ingress-controller-nx\".service.loadBalancerIP' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LOADBALANCERIP_IPV6 = sh (script : "./jq '.content.\"eric-oss-ingress-controller-nx\".service.loadBalancerIP_IPv6' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ANNOTATIONS = sh (script : "./jq '.content.\"eric-oss-ingress-controller-nx\".service.annotations' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LVS_INTERNAL_INTERFACE = sh (script : "./jq '.content.\"eric-net-ingress-l4\".interfaces.internal' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LVS_EXTERNAL_INTERFACE = sh (script : "./jq '.content.\"eric-net-ingress-l4\".interfaces.external' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LVS_CNIMODE = sh (script : "./jq '.content.\"eric-net-ingress-l4\".cniMode' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LVS_PODNETWORK_CIDR = sh (script : "./jq '.content.\"eric-net-ingress-l4\".podNetworkCIDR' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.IPV6_LVS_PODNETWORK_CIDR = sh (script : "./jq '.content.\"eric-net-ingress-l4\".ipv6podNetworkCIDR' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.NODE_EXPORTER_HOST_PORT= sh (script : "./jq '.content.\"eric-pm-node-exporter\".prometheus.nodeExporter.service.hostPort' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.NODE_EXPORTER_SERVICE_PORT=  sh (script : "./jq '.content.\"eric-pm-node-exporter\".prometheus.nodeExporter.service.servicePort' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DDPSITE= sh (script : "./jq '.content.\"eric-enm-monitoring-master\".monitoring.autoUpload.ddpsite' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.ACCOUNT= sh (script : "./jq '.content.\"eric-enm-monitoring-master\".monitoring.autoUpload.account' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.PASSWORD= sh (script : "./jq '.content.\"eric-enm-monitoring-master\".monitoring.autoUpload.password' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DDC_DDPID= sh (script : "./jq '.content.\"eric-enm-ddc\".\"eric-oss-ddc\".autoUpload.ddpid' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DDC_ACCOUNT= sh (script : "./jq '.content.\"eric-enm-ddc\".\"eric-oss-ddc\".autoUpload.account' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.DDC_PASSWORD= sh (script : "./jq '.content.\"eric-enm-ddc\".\"eric-oss-ddc\".autoUpload.password' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.HELM_BINARY = "helm"
    env.RETENTION_LIMIT = sh (script : "./jq '.content.global.bro.backup.retention.limit' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.AUTO_DELETE = sh (script : "./jq '.content.global.bro.backup.retention.autoDelete' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.BACKUP_PREFIX = sh (script : "./jq '.content.global.bro.backup.scheduling.backupPrefix' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SFTP_SECRET_NAME = sh (script : "./jq '.content.global.bro.externalStorageCredentials' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.BACKUP_INTERVAL = sh (script : "./jq '.content.global.bro.backup.scheduling.schedules.every' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SCHEDULE_START_TIME = sh (script : "./jq '.content.global.bro.backup.scheduling.schedules.start' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.SCHEDULE_STOP_TIME = sh (script : "./jq '.content.global.bro.backup.scheduling.schedules.stop' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LOG_STREAMING = sh (script : "./jq '.content.\"eric-enm-int-log-transformer\".\"eric-log-transformer\".egress.syslog.enabled' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LOG_STREAMING_HOST = sh (script : "./jq '.content.\"eric-enm-int-log-transformer\".\"eric-log-transformer\".egress.syslog.remoteHosts.host' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.LOG_STREAMING_PORT = sh (script : "./jq '.content.\"eric-enm-int-log-transformer\".\"eric-log-transformer\".egress.syslog.remoteHosts.port' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.PREUPGRADEAUTOMATICBACKUP = sh (script : "./jq '.content.global.bro.backup.preUpgradeAutomaticBackup' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    env.PREUPGRADEBACKUPNAME = sh (script : "./jq '.content.global.bro.backup.preUpgradeBackupName' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        env.kubeConfig = "${Client_HOME}/conf/${KUBE_CRED}"
        env.cenmbuildutilities_client= "docker run --rm  -v ${Client_HOME}/conf/${KUBE_CRED}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} -v /root/.docker/config.json:/root/.docker/config.json --workdir ${Client_HOME}/conf ${cenm_utilities_docker_image}"
        env.helm = "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} helm'"
        env.kubectl = "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} kubectl'"
    }
    else{
        env.kubeConfig = "${workspace}/.kube/${KUBE_CRED}"
        env.helm = "docker run --rm -v ${kubeConfig}:/root/.kube/config -v ${WORKSPACE}:${WORKSPACE} --workdir ${WORKSPACE} ${cenm_utilities_docker_image} helm"
        env.kubectl = "docker run --rm  -v ${kubeConfig}:/root/.kube/config -v ${params.DOCKER_CONFIG_FILE_PATH}:/root/.docker/config.json -v ${WORKSPACE}:${WORKSPACE} --workdir ${WORKSPACE} ${cenm_utilities_docker_image} kubectl"
    }
    if (env.deployment_mechanism){
      if (deployment_mechanism.equals("csar")){
        env.TARGET_DOCKER_REGISTRY_URL = sh (script : "./jq '.content.global.registry.hostname' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
        def TARGET_DOCKER_REGISTRY_URL_WITH_PORT = TARGET_DOCKER_REGISTRY_URL.tokenize(":");
        if (TARGET_DOCKER_REGISTRY_URL_WITH_PORT.size() == 2) {
           TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT = TARGET_DOCKER_REGISTRY_URL_WITH_PORT[TARGET_DOCKER_REGISTRY_URL_WITH_PORT.size()-2]
        }else{
           TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT = "${TARGET_DOCKER_REGISTRY_URL}"
           println TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT
           println TARGET_DOCKER_REGISTRY_URL
        }
        if(env.product_set_version >= "23.04.53-2"){
            TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT += '/proj-enm'
        }
        env.TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT = TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT
        env.CONTAINER_REGISTRY_USERNAME = sh (script : "./jq '.content.global.registry.users.username' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
        env.CONTAINER_REGISTRY_PASSWORD = sh (script : "./jq '.content.global.registry.users.password' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
        env.cenm_build_utilities_docker_image = "docker run --rm -v ${kubeConfig}:/root/.kube/config -v ${WORKSPACE}:${WORKSPACE} -v /home/lciadm100/:/home/lciadm100/ --workdir ${WORKSPACE} ${cenm_utilities_docker_image}"
    }else {
        env.TARGET_DOCKER_REGISTRY_URL_WITH_OUT_PORT = sh (script : "./jq '.content.global.registry.url' deployment_integration_values_file.json| sed 's/\"//g'", returnStdout: true).trim()
    }
    env.PULLSECRET = sh (script : "./jq '.content.global.pullsecret' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
   }
}

def bro_agent_registered(){
       def bro_agent_map = [
                       'eric-enm-rwxpvc':'eric-enm-rwxpvc',
                       'Search Engine':'elasticsearch',
                       'eric-enm-omnidaemon':'omnidaemon-bragent',
                       'neo4j':'neo4j-bragent',
                       'Document Database PG':'eric-data-document-database-pg',
                       'eric-enmsg-sentinel':'sentinel-bragent',
                       'eric-enm-kvstore-hc':'kvstore-bragent',
                       'Model-Deployment-Tool':'eric-enm-mdt-bro-agent',
                       'opendj':'opendj-bragent'
                      ]
       def List_of_bro_agent_name_exist_backup = []
       for (bro_agents in bro_agent_map){
         def agent_name = bro_agents.value
         def service_name = bro_agents.key
         if (bro_agent_services_exists(service_name)){
                List_of_bro_agent_name_exist_backup.add(agent_name)
         }
       }
       for (String bro_agent_name : List_of_bro_agent_name_exist_backup){
            env.broagent_name=bro_agent_name
            if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
                sh '''
                    while [[ true ]];
                    do
                        if [[ "$(ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} docker run --rm -v ${Client_HOME}/conf/${KUBE_CRED}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} -v /root/.docker/config.json:/root/.docker/config.json --workdir ${Client_HOME}/conf ${cenm_utilities_docker_image} kubectl exec deploy/brocli -n ${NAMESPACE} -i -- brocli status 2>&1)" == *${broagent_name}* ]]; then
                            echo "BR Agents are registered with BRO,  available : ${broagent_name}"
                            break
                        else
                            echo "Waiting for BR agent is register : ${broagent_name}"
                            sleep 60;
                        fi
                    done
                '''
            }
            else{
                sh '''
                    while [[ true ]];
                    do
                        if [[ "$(${brocli} status 2>&1)" == *${broagent_name}* ]]; then
                            echo "BR Agents are registered with BRO,  available : ${broagent_name}"
                            break
                        else
                            echo "Waiting for BR agent is register : ${broagent_name}"
                            sleep 60;
                        fi
                    done
                '''
            }
       }
}

def bro_agent_services_exists(service_name){
    if ( Backup_Info.contains(service_name) ){
            return true
     }
}

def cenm_rollback_uninstall(){
    sh "${kubectl} delete jobs eric-enm-models-all-services-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs eric-enm-models-service-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs eric-enm-models-nrm-job -n ${NAMESPACE} || true"
    sh "${kubectl} delete jobs eric-enm-models-post-install-job -n ${NAMESPACE} || true"
    sh "${helm} uninstall eric-enm-stateless-integration-${NAMESPACE} -n ${NAMESPACE} || true"
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh '''
            while [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" kubectl get pods -n "${NAMESPACE}")" == *"Terminating"* ]];
            do
                logger "Waiting for pods to terminate ...";
                sleep 30 ;
            done
        '''
    }
    else{
        sh '''
            while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
            do
                logger "Waiting for pods to terminate ...";
                sleep 30 ;
            done
        '''
    }
    sh "${helm} uninstall eric-enm-infra-integration-${NAMESPACE} -n ${NAMESPACE} || true"
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh '''
            while [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" kubectl get pods -n "${NAMESPACE}")" == *"Terminating"* ]];
            do
                logger "Waiting for pods to terminate ...";
                sleep 30 ;
            done
        '''
    }
    else {
        sh '''
            while [[ "$(${kubectl} get pods -n ${NAMESPACE})" == *"Terminating"* ]];
            do
               logger "Waiting for pods to terminate ...";
                sleep 30 ;
            done
        '''
    }
}

def cenm_rollback_pvc_delete(){
       delete_pvc_rollback("neo4j")
       delete_pvc_rollback("eshistory")
       delete_pvc_rollback("kvstore")
       if (check_version_a_newer_than_version_b("${product_set_version}","21.06.99")){
           delete_pvc_rollback("postgres")
           delete_pvc_rollback("cts")
      }
}

def delete_pvc_rollback(pvcname)
{
    env.pvc_name=pvcname
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh '''
            for i in $(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" kubectl get pvc -n "${NAMESPACE}" |  grep "$pvc_name" | awk '{print $1}');
            do
                ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" kubectl delete pvc $i -n ${NAMESPACE}|| true;
            done
        '''
    }
    else {
        sh '''
            for i in $(${kubectl} get pvc -n ${NAMESPACE} |  grep $pvc_name | awk '{print $1}');
            do
               ${kubectl} delete pvc $i -n ${NAMESPACE}|| true;
            done
        '''
    }
}

/*
 * Below function will create a generic secret containing sftp machine credentials
 */
def create_sftpmachine_secret(){
    env.sftp_url = "sftp://${SFTP_MACHINE_USERNAME}@${SFTP_MACHINE_HOSTNAME}:22/shares/cENM-backups/${NAMESPACE}"
    if(env.SFTP_SECRET_NAME != '' && env.SFTP_SECRET_NAME != null){
        if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
            sh'''
               ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} "${cenmbuildutilities_client}" kubectl delete secret ${SFTP_SECRET_NAME} -n ${NAMESPACE} || true
            '''
            sh'''
               ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} "${cenmbuildutilities_client}" kubectl create secret generic ${SFTP_SECRET_NAME} --from-literal=externalStorageURI="${sftp_url}" --from-literal=externalStorageCredentials="${SFTP_MACHINE_PASSWORD}" -n ${NAMESPACE}
            '''
        }
        else{
            sh '''
               ${kubectl} delete secret ${SFTP_SECRET_NAME} -n ${NAMESPACE} || true
            '''
            sh'''
               ${kubectl} create secret generic ${SFTP_SECRET_NAME} --from-literal=externalStorageURI="${sftp_url}" --from-literal=externalStorageCredentials=passwd -n ${NAMESPACE}
            '''
        }
    }
    else{
      echo "SFTP secret name is not provided. Skipping secret creation step"
    }
}

def create_sftpmachine_secret_for_restore(){
    env.sftp_url = "sftp://${SFTP_MACHINE_USERNAME}@${SFTP_MACHINE_HOSTNAME}:22/shares/cENM-backups/${NAMESPACE}"
    if(env.SFTP_SECRET_NAME != '' && env.SFTP_SECRET_NAME != 'null'){
        if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
            sh'''
               ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} "${cenmbuildutilities_client}" kubectl delete secret ${SFTP_SECRET_NAME} -n ${NAMESPACE} || true
            '''
            sh'''
               ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} "${cenmbuildutilities_client}" kubectl create secret generic ${SFTP_SECRET_NAME} --from-literal=externalStorageURI="${sftp_url}" --from-literal=externalStorageCredentials="${SFTP_MACHINE_PASSWORD}" -n ${NAMESPACE}
            '''
        }
        else{
            sh '''
               ${kubectl} delete secret ${SFTP_SECRET_NAME} -n ${NAMESPACE} || true
            '''
            sh'''
               ${kubectl} create secret generic ${SFTP_SECRET_NAME} --from-literal=externalStorageURI="${sftp_url}" --from-literal=externalStorageCredentials=passwd -n ${NAMESPACE}
            '''
        }
    }
    else{
      echo "Failed!!! SFTP secret name is not provided. Please add SFTP secret name in the DIT and retry."
      sh "exit 1"
    }
}

def delete_build_utilities_image()
{
    images = sh (script: '''docker images|awk '{ print $1":"$2 }' ''', returnStdout: true ).trim()
    if ( images.contains("${cenm_utilities_docker_image}") ){
       sh '''  docker rmi -f "${cenm_utilities_docker_image}" '''
       echo "${cenm_utilities_docker_image} image removed from the slave"
    }
}

def delete_and_pull_utilitiesimage_clientmachine()
{
    images = sh (script: '''ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} docker images|awk '{ print $1":"$2 }' ''', returnStdout: true ).trim()
    if ( images.contains("${cenm_utilities_docker_image}") ){
       sh '''ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} docker rmi -f "${cenm_utilities_docker_image}" '''
       echo "${cenm_utilities_docker_image} image removed from the clinet machine"
       }
       sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} docker pull ${cenm_utilities_docker_image}"
       echo "${cenm_utilities_docker_image} image pulled successfully"
}

def client_integrationvaluepath(){
        sh "mkdir -p ${HOME_DIR}/cENM/Scripts/"
        sh "scp -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS}:${Client_HOME}/cENM/Scripts/${integration_value_type}* ${HOME_DIR}/cENM/Scripts/"
        env.integration_values_file_path= sh (script: "ls ${HOME_DIR}/cENM/Scripts/|grep ${integration_value_type}*", returnStdout: true ).trim()
}

def client_pushfile(){
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo chmod 777 ${Client_HOME}/cENM/Scripts/${integration_value_type}*'"
        sh "scp  -o ' LogLevel=error' -o 'StrictHostKeyChecking no' ${HOME_DIR}/cENM/Scripts/${integration_values_file_path} ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS}:${Client_HOME}/cENM/Scripts/"
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo chmod 664 ${Client_HOME}/cENM/Scripts/${integration_values_file_path}*'"
}

def csar_uninstall(){
    if(env.CLIENT_MACHINE_TYPE  !='client_machine'){
        if (check_version_a_newer_than_version_b("${product_set_version}","22.01.19")){
            sh '''
                export KUBECONFIG=${PWD}/.kube/${KUBE_CRED}
                ${cenm_build_utilities_docker_image} bash -x ${WORKSPACE}/cENM/Scripts/cenm_uninstall.sh --cenm-name-space=${NAMESPACE} --helm=${HELM_BINARY} --ignore-verification
            '''
        }
        else{
            sh '''
                export KUBECONFIG=${PWD}/.kube/${KUBE_CRED}
                ${cenm_build_utilities_docker_image} bash -x ${WORKSPACE}/cENM/Scripts/cenm_uninstall.sh --cenm-name-space=${NAMESPACE} --helm=${HELM_BINARY}
            '''
        }
    }
    else{
        if (check_version_a_newer_than_version_b("${product_set_version}","22.01.19")){
            sh " ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} bash ${Client_HOME}/cENM/Scripts/cenm_uninstall.sh --cenm-name-space=${NAMESPACE} --helm=${HELM_BINARY} --ignore-verification'"
        }
        else{
            sh " ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no'  ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} '${cenmbuildutilities_client} bash ${Client_HOME}/cENM/Scripts/cenm_uninstall.sh --cenm-name-space=${NAMESPACE} --helm=${HELM_BINARY}'"
        }
    }
}

def client_cleancsar() {
                        sh '''
                            ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS}  'sudo rm -rf '\${Client_HOME}'/cENM/*||true'
                            ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS}  'sudo find /tmp/*.log -mtime +30 -exec rm -f {} \\;||true'
                            ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS}  'mkdir -p '\${Client_HOME}'/cENM'
                         '''
}

/*
*This function is required to download required parameters for client machine initial configurations from DIT page.
*/
def client_machine_initial_config_param_from_dit(){
    env.site_information_document_id= sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/deployments/?q=name=$environment_name\" |./jq '.[].documents[] | select(.schema_name==\"cENM_site_information\") | .document_id' | sed 's/\"//g'", returnStdout: true).trim()
    sh "curl -4 --location --request GET 'https://atvdit.athtem.eei.ericsson.se/api/documents/$site_information_document_id'>deployment_site_config_information.json"

    env.CLIENT_MACHINE_IP_ADDRESS = sh (script : "./jq '.content.global.client_machine.ipaddress' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CLIENT_MACHINE_USERNAME = sh (script : "./jq '.content.global.client_machine.username' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CLIENT_MACHINE_TYPE = sh (script : "./jq '.content.global.client_machine.type' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()

    env.TARGET_DOCKER_REGISTRY_URL = sh (script : "./jq '.content.global.registry.hostname' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CONTAINER_REGISTRY_USERNAME = sh (script : "./jq '.content.global.registry.users.username' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
    env.CONTAINER_REGISTRY_PASSWORD = sh (script : "./jq '.content.global.registry.users.password' deployment_site_config_information.json| sed 's/\"//g'", returnStdout: true).trim()
}

/*
 * Below function is used to push brocli image extracted from CSAR package to respective docker registry
 */
def csar_brocli_image(){
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo docker load --input ${Client_HOME}/cENM/Scripts/backup-restore-cli-docker.tar'"
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo docker image tag backup-restore-cli:latest ${TARGET_DOCKER_REGISTRY_URL}/backup-restore-cli:latest'"
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'sudo docker push ${TARGET_DOCKER_REGISTRY_URL}/backup-restore-cli:latest'"
    }
    else{
        sh '''
            sudo docker load --input ${HOME_DIR}/cENM/Scripts/backup-restore-cli-docker.tar
            sudo docker image tag backup-restore-cli:latest ${TARGET_DOCKER_REGISTRY_URL}/backup-restore-cli:latest
            sudo docker push ${TARGET_DOCKER_REGISTRY_URL}/backup-restore-cli:latest
        '''
    }
}

/*
 * Below function will provide brocli image based on deployment mechanism
 */
def brocli_configuration(){
    if(env.BRO_DEFAULT_BACKUP_TYPE =='External')
   {
     def res=sh (script: "${kubectl} get po -n ${NAMESPACE} | grep cenmproxy | awk \'FNR == 1 {print \$3}\' ",returnStdout: true).trim()
     echo "$res"
     if (res == "Running"){
        echo "Cenmproxy is up and running"
      }
      else {
            echo "cenm proxy is not up and running ${res}"
            exit 1
         }
     sh "curl -4 -k --request POST 'https://${ENM_LAUNCHER_HOSTNAME}/login' -d IDToken1='administrator' -d IDToken2='TestPassw0rd' --cookie-jar cookie_cenmproxy"
   }
   else
   {
   env.brocli="${kubectl} exec deploy/brocli -n ${NAMESPACE} -i -- brocli"
}

}
/*
* Below function is for getting the brocli status
*/
def brocli_status(){
  if(env.BRO_DEFAULT_BACKUP_TYPE =='External'){
  def res=sh(script:"""curl -4 --header "Content-Type:application/json" -X GET https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/health -k --cookie cookie_cenmproxy -H 'Accept:application/json' -i""", returnStdout: true).trim()
   echo " Brocli health : $res"
   }
  else{
     sh"${brocli} status"
   }
}



/*
 * Below function will perform rollback on pre-deploy chart
 */
def rollback_predeploy(){
    env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-pre-deploy-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
    if(env.revision > "1") {
        env.rollback_predeploy_revision = sh (script : "${helm} history eric-enm-pre-deploy-integration-${NAMESPACE} -n ${NAMESPACE} | awk '/${pre_deploy_chart_version}/' | awk '/superseded/ {print \$1}' | tail -n 1", returnStdout: true).trim()
        echo "Rollback can be done"
        try{
           sh "${helm} rollback eric-enm-pre-deploy-integration-${NAMESPACE} ${rollback_predeploy_revision} -n ${NAMESPACE}"
        }
        catch( err ){
        env.predeploy_secret = sh(script :"${kubectl} get secret --namespace ${NAMESPACE} | grep eric-enm-pre-deploy-integration-${NAMESPACE} | tail -n 1 | awk '{print \$1}'", returnStdout: true).trim()
        sh '''${kubectl} patch secret ${predeploy_secret} --type=merge -p '{"metadata":{"labels":{"status":"deployed"}}}' --namespace ${NAMESPACE}'''
        echo "Trying Rollback of pre-deploy chart again"
        sh "${helm} rollback eric-enm-pre-deploy-integration-${NAMESPACE} ${rollback_predeploy_revision} -n ${NAMESPACE}"
        }
    }
    else {
        echo "Rollback cannot be done"
        sh "exit 0"
    }
}

/*
 * Below function will perform rollback on monitoring chart
 */
def rollback_monitoring(){
    env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-monitoring-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
    if(env.revision > "1") {
        env.rollback_monitoring_revision = sh(script : "${helm} history eric-enm-monitoring-integration-${NAMESPACE} -n ${NAMESPACE} | awk '/${monitoring_chart_version}/' | awk '/superseded/ {print \$1}' | tail -n 1", returnStdout: true).trim()
        echo "Rollback can be done"
        sh "${helm} rollback eric-enm-monitoring-integration-${NAMESPACE} ${rollback_monitoring_revision} -n ${NAMESPACE}"
    }
    else {
        echo "Rollback cannot be done"
        sh "exit 0"
    }
}

/*
 * Below function will perform rollback and gets product number for BRO
 */
def rollback_bro_and_fetch_product_number(){
    env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-bro-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
    if(env.revision > "1") {
        echo "Rollback can be done"
        if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
            sh '''
                 for i in $(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" helm history eric-enm-bro-integration-"${NAMESPACE}" -n "${NAMESPACE}" | awk "/${bro_chart_version}/ {print \\$1}");
                 do
                   ps_version=$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no'  "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" helm get values eric-enm-bro-integration-"${NAMESPACE}" --revision "$i" -n ${NAMESPACE} | awk -F ': ' '/productSet:/ {print $2}')
                   if [[ ${product_set_version} == ${ps_version} ]]
                   then
                       echo "Rolling back to the product_set_version: ${ps_version}"
                       rollback_bro_revision=$i
                       echo "ROLLBACK_BRO_REVISION: ${rollback_bro_revision}"
                       ssh -o LogLevel=error -o 'StrictHostKeyChecking no'  "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" helm rollback eric-enm-bro-integration-"${NAMESPACE}" "${rollback_bro_revision}" -n "${NAMESPACE}"
                       break
                   fi
                 done
                 if [[ ${rollback_bro_revision} == "" ]]
                 then
                     echo "Could not find the product_set_version to which rollback has to be performed"
                     exit 1
                 fi
            '''
        }
        else {
            env.rollback_bro_revision = sh(script : "${helm} history eric-enm-bro-integration-${NAMESPACE} -n ${NAMESPACE} | awk '/${bro_chart_version}/ {print \$1}' | awk 'NR==1{print \$1}'", returnStdout: true).trim()
            sh "${helm} rollback eric-enm-bro-integration-${NAMESPACE} ${rollback_bro_revision} -n ${NAMESPACE}"
        }
    }
    else {
        echo "Rollback cannot be done"
        sh "exit 0"
    }
    sh "${kubectl} delete pod eric-ctrl-bro-0 -n ${NAMESPACE}"
    sh "sleep 60s"
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh '''
            while [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" kubectl get pod eric-ctrl-bro-0 -n "${NAMESPACE}")" != *"Running"* ]];
            do
                logger "Waiting for eric-ctrl-bro-0 pod to running ...";
                sleep 10 ;
            done
        '''
    }
    else{
        sh '''
            while [[ "$(${kubectl} get pod eric-ctrl-bro-0 -n ${NAMESPACE})" != *"Running"* ]];
            do
                logger "Waiting for eric-ctrl-bro-0 pod to running ...";
                sleep 10 ;
            done
        '''
    }
    if(env.product_set_version < "22.08.38"){
        env.Productnumber= sh (script : "${kubectl} get cm product-version-configmap -o yaml -n $NAMESPACE | grep ericsson.com/product-number|sort -V | head -n 1|awk -F ':' '{print \$2}'|awk '\$1=\$1'", returnStdout: true).trim()
        echo "${Productnumber}"
    }
}

/*
 * Below function will show backup details and verifies whether the product number of the imported backup for rollback matches the product number for BRO
 */
def verify_rollback_exist(){
    sh "sleep 120s"
    sh "${brocli} show $backup_name --scope=ROLLBACK ||true"
    env.Backup_Info=sh(script: "${brocli} show $backup_name --scope=ROLLBACK", returnStdout: true).trim()
    if ( Backup_Info.contains(Productnumber) ){
        echo "Product numbers are equal"
    }
    else {
        echo "Failed!! Product number of the backup for rollback doesnot match with the product number BRO"
        sh "exit 1"
    }
}

/*
 * Below function shows backup list and stops backup port forwarding
 */
def show_backup_list_and_stop_backup_port_forward(){
    sh "${brocli} list"
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh "ssh -o 'LogLevel=error' -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'docker stop backup_port_forward'"
    }
    else{
        sh "docker stop backup_port_forward"
    }
}

/*
 * Below function installs infra chart with flags enabling rollback mode i.e RESTORE_STATE to ongoing, RESTORE_SCOPE to rollback, RESTORE_BACKUP_NAME to backupname
 */
def rollback_infra(){
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
           env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-infra-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
        if (env.revision == null){
             env.revision = 0
             }
           echo "${revision}"

        sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'docker run --rm -d -v ${kubeConfig}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} --workdir ${Client_HOME}/conf armdocker.rnd.ericsson.se/proj-enm/cenm-build-utilities:latest helm install eric-enm-infra-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}*  ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-infra-integration* --set global.restore.state=ongoing --set global.restore.scope=ROLLBACK --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout 8h'")
        sh "sleep 300s"
        sh '''
        while [[ true ]];
              do
            if [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-infra-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-infra-integration-'\${NAMESPACE}'')" == *"deployed"* ]]; then
                         break
            elif [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-infra-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-infra-integration-'\${NAMESPACE}'')" == *"failed"* ]]; then
                         exit 1
            else
                     logger "Waiting for infra to get to deployed ...";
                     echo "${Revision}"
                     sleep 300s ;
                fi
             done
           '''
    }
    else{
        sh "${helm} install eric-enm-infra-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path}  cENM/Definitions/OtherTemplates/${infra_integration_chart_path} --set global.restore.state=ongoing --set global.restore.scope=ROLLBACK --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout 8h"
    }
}

/*
 * Below function checks restore state in backup-restore-configmap , if value is ongoing then performs restore with scope rollback
 */
def verify_restorestate_value_and_restore(){
    sh "sleep 300"
    env.restorestatevalue= sh(script: "${kubectl} get configmap/backup-restore-configmap -o yaml -n ${NAMESPACE}|grep RESTORE_STATE|sort -V | head -n 1|awk -F ':' '{print \$2}'", returnStdout: true).trim()
    if (restorestatevalue.contains("ongoing")){
        bro_agent_registered()
        sh "${brocli} restore $backup_name --scope ROLLBACK"
    }
    else{
        echo "Failure in restoring"
        sh "exit 1"
    }
}

/*
 * Below function checks infra status and if infra chart is deployed, sets the backup-restore-configmap to post-rollback state
 */
def set_configmap_to_post_rollback_state(){
    env.infra_status=sh(script: "${helm} list -n ${NAMESPACE} | grep eric-enm-infra-integration-${NAMESPACE} | awk \'{print \$(NF-2)}\'",returnStdout: true).trim()
    if (infra_status.contains("deployed")){
        if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
            sh "ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} \"${cenmbuildutilities_client} kubectl patch configmap/backup-restore-configmap --patch '{\\\"data\\\": {\\\"RESTORE_STATE\\\": \\\"finished\\\"}}' -n ${NAMESPACE} \""
        }
        else{
            sh '''${kubectl} patch configmap/backup-restore-configmap --patch '{"data": {"RESTORE_STATE": "finished"}}' -n ${NAMESPACE} '''
        }
    }
    else{
        echo "Infra chart is not deployed"
        sh "exit 1"
    }
}

/*
 * Below function resets the values for restore in the backup-restore-configmap
 */
def reset_restore_values_in_configmap(){
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh "ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} \"${cenmbuildutilities_client} kubectl patch configmap/backup-restore-configmap --patch '{\\\"data\\\": {\\\"RESTORE_STATE\\\": \\\"\\\"}}' -n ${NAMESPACE} \""
        sh "ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} \"${cenmbuildutilities_client} kubectl patch configmap/backup-restore-configmap --patch '{\\\"data\\\": {\\\"RESTORE_SCOPE\\\": \\\"\\\"}}' -n ${NAMESPACE} \""
        sh "ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} \"${cenmbuildutilities_client} kubectl patch configmap/backup-restore-configmap --patch '{\\\"data\\\": {\\\"RESTORE_BACKUP_NAME\\\": \\\"\\\"}}' -n ${NAMESPACE} \""
    }
    else{
        sh '''${kubectl} patch configmap/backup-restore-configmap --patch '{"data": {"RESTORE_STATE": "","RESTORE_SCOPE": "","RESTORE_BACKUP_NAME": ""}}' -n ${NAMESPACE} '''
    }
}

/*
 * Below function is to create a backup name and scope for rollback
 */
def backup_rollback() {
    if(env.BRO_DEFAULT_BACKUP_TYPE =='External'){
        def res= sh(script : """curl -4 -i -X POST -H "Content-Type:application/json" -d '{ \"action\": \"CREATE_BACKUP\", \"payload\": { \"backupName\": \"${backup_name}\"}}'  "https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/backup-manager/ROLLBACK/action" -k --cookie cookie_cenmproxy""", returnStdout: true).trim()
        echo "$res"
        def httpres= sh( script :"""echo "$res" | grep HTTP/1.1""",returnStdout: true).trim()
        echo "$httpres"
        def resnum= sh( script :"""echo "$httpres" | awk \'{print \$2}\' """,returnStdout: true).trim()
        echo "$resnum"
        if(resnum == "201"){
         echo "backup creating and http response :$resnum"
        }
        else{
         echo "backup failed and http response :$resnum"
         exit 1
        }
        def actionid=sh( script :"""echo "$res" | grep id | grep -Eo '[0-9]{1,7}'""",returnStdout: true).trim()
        echo "ActionId: ${actionid} "
        while (true){
          def monitor=sh(script:"""curl -4 --header "Content-Type:application/json" -X GET https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/backup-manager/ROLLBACK/action/$actionid -k --cookie cookie_cenmproxy -H 'Accept:application/json' -i | grep -i state""", returnStdout: true).trim()
          echo "Backup Information: ${monitor}"
          def state=sh( script :"echo '$monitor' | ./jq '.result'",returnStdout: true).trim()
          echo "Result Status : ${state}"
          if ( "${state}" == '"SUCCESS"'){
            echo "Successfully backup taken"
            break
          }
          else if("${state}" == '"FAILURE"'){
           echo "Backup failed"
           echo "${state}"
           exit 1
          }
          else {
           echo "Backup taking .............."
           sh 'sleep 30s'
          }

    }
   }
   else{
    sh "${brocli} create $backup_name --scope $backup_scope"
    sh "${brocli} show $backup_name --scope $backup_scope"
    }

}

/*
 * Below function is to create a backup name and scope for restore
 */
def backup_restore() {
    if(env.BRO_DEFAULT_BACKUP_TYPE =='External'){
      def res= sh(script : """curl -4 -i -X POST -H "Content-Type:application/json" -d '{ \"action\": \"CREATE_BACKUP\", \"payload\": { \"backupName\": \"${backup_name}\"}}'  "https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/backup-manager/DEFAULT/action" -k --cookie cookie_cenmproxy""", returnStdout: true).trim()
      echo "backup $res"
      //def http_res= sh(script :"""echo "$res" | grep HTTP/1.1""", returnStdout: true).trim()
      def httpres= sh( script :"""echo "$res" | grep HTTP/1.1""",returnStdout: true).trim()
      echo "$httpres"
      def resnum= sh( script :"""echo "$httpres" | awk \'{print \$2}\' """,returnStdout: true).trim()
      echo "$resnum"
      if(resnum == "201"){
         echo "backup creating and http response :$resnum"
        }
      else{
         echo "backup failed and http response :$resnum"
         exit 1
        }
      def actionid=sh( script :"""echo "$res" | grep id | grep -Eo '[0-9]{1,7}'""",returnStdout: true).trim()
      echo "ActionId: ${actionid} "
      while (true){
           def monitor=sh(script:"""curl -4 --header "Content-Type:application/json" -X GET https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/backup-manager/DEFAULT/action/$actionid -k --cookie cookie_cenmproxy -H 'Accept:application/json' -i | grep -i state""", returnStdout: true).trim()
           echo "Backup Information: ${monitor}"
           def state=sh( script :"echo '$monitor' | ./jq '.result'",returnStdout: true).trim()
           echo "Result Status : ${state}"
          if ( "${state}" == '"SUCCESS"'){
            echo "Successfully backup taken"
            break
           }
          else if("${state}" == '"FAILURE"'){
            echo "Backup failed"
            echo "${state}"
            exit 1
            }
          else {
            echo "Backup taking .............."
            sh 'sleep 30s'
            }
         }
    }

  else{
   sh "${brocli} create $backup_name"
   sh "${brocli} show $backup_name"
}
}

// Below function is for list the backups under backupmanagers

def brocli_list(){
  if(env.BRO_DEFAULT_BACKUP_TYPE =='External'){
  def res=sh(script:"""curl -4 --header "Content-Type:application/json" -X GET https://${ENM_LAUNCHER_HOSTNAME}/broproxy/backup/v1/backup-manager/${backup_scope}/backup -k --cookie cookie_cenmproxy -H 'Accept:application/json' -i""", returnStdout: true).trim()
   echo "Brocli list: $res"
   }
  else{
     sh"${brocli} list"
   }

}




/*
 * Below function is used by restore. It deletes eric-ctrl-bro pod, waits for it to re-create and fetches product number
 */
def restore_bro_and_fetch_product_number(){
    sh "${kubectl} delete pod eric-ctrl-bro-0 -n ${NAMESPACE}"
    sh "sleep 30"
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
        sh '''
            while [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" "${cenmbuildutilities_client}" kubectl get pod eric-ctrl-bro-0 -n "${NAMESPACE}")" != *"Running"* ]];
            do
                logger "Waiting for eric-ctrl-bro-0 pod to running ...";
                sleep 10 ;
            done
        '''
    }
    else{
        sh '''
            while [[ "$(${kubectl} get pod eric-ctrl-bro-0 -n ${NAMESPACE})" != *"Running"* ]];
            do
                logger "Waiting for eric-ctrl-bro-0 pod to running ...";
                sleep 10 ;
            done
        '''
   }
    env.Productnumber= sh (script : "${kubectl} get cm product-version-configmap -o yaml -n $NAMESPACE | grep ericsson.com/product-number|sort -V | head -n 1|awk -F ':' '{print \$2}'|awk '\$1=\$1'", returnStdout: true).trim()
    echo "${Productnumber}"
}

/*
 * Below function will show backup details and verifies whether the product number of the imported backup for restore matches with the product number BRO
 */
def verify_restore_exist(){
    sh "${brocli} show $backup_name||true"
    env.Backup_Info=sh(script: "${brocli} show $backup_name", returnStdout: true).trim()
    if ( Backup_Info.contains(Productnumber) ){
        echo "Product numbers are equal"
    }
    else {
        echo "Failed!! Product number of the backup for restore doesnot match with the product number BRO"
        sh "exit 1"
    }
}


/*
 * Below function installs infra chart with flags enabling restore mode i.e RESTORE_STATE to ongoing, RESTORE_SCOPE to DEFAULT, RESTORE_BACKUP_NAME to backupname
 */
def restore_infra(){
    if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
         env.revision = sh (script : "${helm} list -n ${NAMESPACE} | grep eric-enm-infra-integration-${NAMESPACE} | awk \'{print \$(NF-7)}\'", returnStdout: true).trim()
         if (env.revision == null){
             env.revision = 0
         }
         echo "${revision}"
         if(env.product_set_version < "22.08.38"){
             sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'docker run --rm -d -v ${kubeConfig}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} --workdir ${Client_HOME}/conf armdocker.rnd.ericsson.se/proj-enm/cenm-build-utilities:latest helm install eric-enm-infra-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}*  ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-infra-integration* --set global.restore.state=ongoing --set global.restore.scope=DEFAULT --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout 8h'")
         }
         else{
             sh("ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} 'docker run --rm -d -v ${kubeConfig}:/root/.kube/config -v ${Client_HOME}:${Client_HOME} --workdir ${Client_HOME}/conf armdocker.rnd.ericsson.se/proj-enm/cenm-build-utilities:latest helm install eric-enm-infra-integration-${NAMESPACE} --values ${Client_HOME}/cENM/Scripts/${integration_values_file_path}*  ${Client_HOME}/cENM/Definitions/OtherTemplates/eric-enm-infra-integration* --set global.restore.externalStorageCredentials=${sftp_secret_name} --set global.restore.state=ongoing --set global.restore.scope=DEFAULT --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout 8h'")
         }
         sh "sleep 300s"
         sh '''
        while [[ true ]];
              do
           if [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-infra-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-infra-integration-'\${NAMESPACE}'')" == *"deployed"* ]]; then
                         break
           elif [[ "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list -n '\${NAMESPACE}' | grep eric-enm-infra-integration-'\${NAMESPACE}' | awk \"{print \\\$(NF-7)}\" ')" == \$((revision+1)) && "$(ssh -o LogLevel=error -o 'StrictHostKeyChecking no' "${CLIENT_MACHINE_USERNAME}"@"${CLIENT_MACHINE_IP_ADDRESS}" ''\${cenmbuildutilities_client}' helm list --all -n '\${NAMESPACE}'|grep eric-enm-infra-integration-'\${NAMESPACE}'')" == *"failed"* ]]; then

                 exit 1
           else
                     logger "Waiting for infra to get to deployed ...";
                     echo "${revision}"
                     sleep 300s ;
                fi
             done
           '''
    }
    else{
        if(env.product_set_version < "22.08.38"){
            sh "${helm} install eric-enm-infra-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path}  cENM/Definitions/OtherTemplates/${infra_integration_chart_path} --set global.restore.state=ongoing --set global.restore.scope=DEFAULT --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout 8h"
        }
        else{
            sh "${helm} install eric-enm-infra-integration-${NAMESPACE} --values cENM/Scripts/${integration_values_file_path}  cENM/Definitions/OtherTemplates/${infra_integration_chart_path} --set global.bro.externalStorageCredentials=${sftp_secret_name} --set global.restore.state=ongoing --set global.restore.scope=DEFAULT --set global.restore.backupName=$backup_name -n ${NAMESPACE} --wait --wait-for-jobs --timeout 8h"
        }
    }
}

/*
 * Below function checks restore state in backup-restore-configmap, if value is ongoing then performs restore. By default scope will be DEFAULT
 */
def verify_restorestate_value_and_restore_default(){
    sh "sleep 300"
    env.restorestatevalue= sh(script: "${kubectl} get configmap/backup-restore-configmap -o yaml -n ${NAMESPACE}|grep RESTORE_STATE|sort -V | head -n 1|awk -F ':' '{print \$2}'", returnStdout: true).trim()
    if (restorestatevalue.contains("ongoing")){
        bro_agent_registered()
        sh "${brocli} restore $backup_name"
    }
    else{
        echo "Failure in restoring"
        sh "exit 1"
    }
}

/*
 * Below function checks infra status and if infra chart is deployed, sets the RESTORE_STATE to finished in backup-restore-configmap
 */
def set_configmap_to_post_restore_state(){
    env.infra_status=sh(script: "${helm} list -n ${NAMESPACE} | grep eric-enm-infra-integration-${NAMESPACE} | awk \'{print \$(NF-2)}\'",returnStdout: true).trim()
    if (infra_status.contains("deployed")){
        if(env.CLIENT_MACHINE_TYPE =='client_machine' && env.deployment_mechanism =='csar'){
            sh "ssh -o LogLevel=error -o 'StrictHostKeyChecking no' ${CLIENT_MACHINE_USERNAME}@${CLIENT_MACHINE_IP_ADDRESS} \"${cenmbuildutilities_client} kubectl patch configmap/backup-restore-configmap --patch '{\\\"data\\\": {\\\"RESTORE_STATE\\\": \\\"finished\\\"}}' -n ${NAMESPACE} \""
        }
        else{
            sh '''
                ${kubectl} patch configmap/backup-restore-configmap --patch '{"data": {"RESTORE_STATE": "finished"}}' -n ${NAMESPACE}
            '''
        }
    }
    else{
        echo "Infra chart is not deployed"
        sh "exit 1"
    }
}
/*
* create a new deployment in queue or update the deployment when deployment is exist
*/

def start_queue(jobType){
    get_cloud_name()
    search_and_create_or_update_deployment(jobType)
    if (deployment_id != ""){
        check_queue_status()
    }
}

/*
*when the deployment is exist it will update the deployment to finished state 
*/


def end_queue(jobType){
   if (deployment_id != ""){
       if ( update_queue_status("Finished",jobType) == '200'){
               echo "Deployment status has been updated to Finished"
        }
        else{
               echo "deployment status has not been updated"
           }
   }
   else{
       echo "Processed without Queue"
   }
}


/*
*search the deployment with name when exist it update the deployment status otherwise it create the deployment in queue
*/

def search_and_create_or_update_deployment(jobType){
    def get_deployment_response_code='500'
    def get_deployment_response=""
    for (i= 0; i<3; i++){
        get_deployment_response = sh (script : "curl -k --write-out \"%{http_code}\" \"${OQS_URL}/search?name=${environment_name}\" ", returnStdout: true).trim()
        get_deployment_response_code= sh ( script : """echo "$get_deployment_response" | grep -o '...\$' """,returnStdout: true).trim()
        echo "Response code is : ${get_deployment_response_code}"
        if ( get_deployment_response_code == '200' )
            break
        }
    if ( get_deployment_response_code == '200' ){
       env.productSet=sh (script : """echo $drop::$product_set_version""",returnStdout: true).trim()
       def get_deployment_response_body= sh ( script : """echo "$get_deployment_response" | awk '{print substr(\$0,1,length(\$0)-3)}' """,returnStdout: true).trim()
       if(get_deployment_response_body.contains("[]")){
           env.deployment_id=add_deployment_to_queue(jobType,productSet)
           if (deployment_id != ""){
                   echo "Deployment is Added in queue-handling"
           }else{
                deployment_id=""
                echo "A problem occurred while queue-handling. Proceeding without queue-handling"
                }
       }
       else{
          env.deployment_id= get_deployment_id()
          if (deployment_id != ""){
              env.productSet=sh (script : """echo $drop::$product_set_version""",returnStdout: true).trim()
              echo "updating deployment:${deployment_id}"
              if (update_queue_status("Queued",jobType) == '200'){
                 echo "Queue status has been updated in queue-handling"
              }
              else{
                deployment_id=""
                echo "A problem occurred while queue-handling. Proceeding without queue-handling"
              }

           }
          else{
          deployment_id=""
          echo "A problem occurred while queue-handling. Proceeding without queue-handling"
          }

       }
    }
    else{
        deployment_id=""
        echo "A problem occurred while queue-handling. Proceeding without queue-handling"
    }


}


/*
*It continously check queue status for install pre-deploye chart.
*/


def check_queue_status(){
    sh '''
    while [[  "$(curl -k  \"${OQS_URL}/search?name=${environment_name}\" |./jq '.[].queueStatus' | sed 's/\"//g')" == *"Queued"*  ]];
    do
        echo "watting..."
        sleep 10;
        done
    '''
}

/*
*It update the queue states form one status to another status.
*/

def update_queue_status(status,jobType){
    def update_queue_response_code='500'
    for ( i=0; i<3; i++){
        def update_queue_response=sh(script: """curl -k  --write-out \"%{http_code}\" -X PUT "${OQS_URL}/${deployment_id}" -H "accept: application/json" -H "Content-Type: application/json" -d '{ \"queueStatus\": \"${status}\",\"jobType\": \"${jobType}\",\"productSet\": \"${productSet}\",\"customTimeout\": 40}' """,returnStdout: true).trim()
        update_queue_response_code = sh ( script : """echo "$update_queue_response" | grep -o '...\$' """,returnStdout: true).trim()
        if ( update_queue_response_code == '200')
            break
        }
    return update_queue_response_code


}

/*
*It will create new deployment in oqs tool
*/

def add_deployment_to_queue(jobType,productSet){
    def post_response_code='500'
    for (i=0; i<3; i++){
        def post_response = sh (script:"""curl -k --write-out \"%{http_code}\" -X POST \"${OQS_URL}\" -H \"accept: application/json\" -H \"Content-Type: application/json\" -d '{ \"name\": \"${environment_name}\", \"associatedPod\": ${cloud_name}, \"jobType\": \"${jobType}\", \"productSet\": \"${productSet}\", \"product\": \"cENM\", \"customTimeout\": 40}' """, returnStdout: true ).trim()
        post_response_code=sh ( script : """echo "$post_response" | grep -o '...\$' """,returnStdout: true).trim()
        if ( post_response_code == '201' )
            break
    }
    if (post_response_code == '201'){
        return get_deployment_id()
        }
    return ""
}

/*
*It returns the deployment id
*/

def get_deployment_id(){
     return  sh (script : "curl -k  \"${OQS_URL}/search?name=${environment_name}\" |./jq '.[]._id' | sed 's/\"//g'", returnStdout: true).trim()
}

/*
*It get cloud/pod name from dit.
*/

def get_cloud_name(){
    env.project_information_document_id= sh (script : "curl -s \"http://atvdit.athtem.eei.ericsson.se/api/deployments/?q=name=$environment_name\" |./jq '.[].project_id' ", returnStdout: true).trim()
    echo "${project_information_document_id}"
    env.pod_information_document_id=sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/projects/$project_information_document_id\" |./jq '.pod_id'", returnStdout: true).trim()
    echo "${pod_information_document_id}"
    env.cloud_name=sh (script : "curl -4 -s \"http://atvdit.athtem.eei.ericsson.se/api/pods/${pod_information_document_id}\" |./jq '.name'", returnStdout: true).trim()
    echo "${cloud_name}"
}





return this
