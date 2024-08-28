#!/bin/bash

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
RED="\033[0;31m"
GREEN="\033[0;32m"
NOCOLOR="\033[0m"
PACKAGE_NAME="cenm_pre_health_check"
DATE=`date '+%Y-%m-%d-%H:%M:%S'`
PACKAGE_LOG="${PACKAGE_NAME}_$DATE.log"
SCRIPT_NAME=$(basename ${0})

# ********************************************************************
# Function Name: info
# Description: prints a info message to rsyslog.
# Arguments: $@ - Info message.
# Return: 0.
# ********************************************************************
info() {
  [ $# -eq 0 ] && { error "Function ${FUNCNAME[0]} requires atleast 1 argument"; exit 1; }
  echo -e "`date '+%Y-%m-%d %H:%M:%S:'` INFO ${SCRIPT_NAME}: $@" | tee -a ${PACKAGE_LOG}
}
export -f info
# ********************************************************************
# Function Name: error
# Description: prints a error message to rsyslog.
# Arguments: $@ - Error message.
# Return: 0.
# ********************************************************************
error() {
  [ $# -eq 0 ] && { error "Function ${FUNCNAME[0]} requires atleast 1 argument"; exit 1; }
  echo -e "`date '+%Y-%m-%d %H:%M:%S:'` ERROR ${SCRIPT_NAME}: $@" | tee -a ${PACKAGE_LOG}
}
export -f error

printUsage() {
  echo "usage:  --cenm-name-space=<CENM-NAME-SPACE>"
  echo ""
  echo "Example 1, uninstall using "
  echo "bash cenm_pre_health_check.sh --cenm-name-space=<namespace>"
  echo ""
  exit 1
}

check_integration_chart_status()
{
  if [[ "$(helm list --date --namespace $NAME_SPACE|grep $1)" == *"deployed"* ]]
  then
     info "$1 is deployed"
  else
     error "$1 is not deployed, Fix the chart issue before proceeding with upgrade procedure."
     exit 1
  fi
}

check_restarts()
{
  init_pods_restart_count=$(kubectl get pods -n $NAME_SPACE|grep -v NAME|grep -v Completed|awk '{print $1" " $4}' )
  info "sleeping for 5 minutes"
  sleep 300
  pods_restart_count=$(kubectl get pods -n $NAME_SPACE|grep -v NAME|grep -v Completed|awk '{print $1" " $4}' )
  pods_restart=$(kubectl get pods -n $NAME_SPACE --sort-by='.status.containerStatuses[0].restartCount' --no-headers  | awk '$4>0 {print $0}' )
  if [[ -z "$pods_restart" ]]
  then
    info "No Restart in pods"
  else
    info "Restarted Pods are:"
    info "$pods_restart"
  fi
  while IFS= read -r line
  do
     if [[ !($init_pods_restart_count == *"$line"*) ]]
     then
       error "Restarts observed at $(line|awk '{print $1 ", Number of restarts observed are " $2}'), Ensure there are no continues restarts on pods before starting upgrade."
       exit 1
     fi
  done < <(printf '%s\n' "$pods_restart_count")
  info "There are no continuous restarts on pods"
}

store_history()
{
  echo "<h3>$1</h3>">>integration_charts_history.html
  echo -e '<table border="1" width="100%" table-layout="auto" >\n  <tr>'>>integration_charts_history.html
  echo '    <th colspan="1" style="color:black;text-align:center;" nowrap>Revision</th>'>>integration_charts_history.html
  echo '    <th colspan="1" style="color:black;text-align:center;" nowrap>Updated</th>'>>integration_charts_history.html
  echo '    <th colspan="1" style="color:black;text-align:center;" nowrap>Status</th>'>>integration_charts_history.html
  echo '    <th colspan="1" style="color:black;text-align:center;" nowrap>Chart</th>'>>integration_charts_history.html
  echo '    <th colspan="1" style="color:black;text-align:center;" nowrap>App Version</th>'>>integration_charts_history.html
  echo -e '    <th colspan="1" style="color:black;text-align:center;" nowrap>Description</th>\n  </tr>'>>integration_charts_history.html
  line_count=$(helm history $1 --namespace $NAME_SPACE|wc -l)
  i=2
  while [ "$i" -le "$line_count" ]
  do
    echo "  <tr>">>integration_charts_history.html
    Revision=$(helm history $1 --namespace $NAME_SPACE|awk -v "i=$i" 'NR==i {print $1}')
    Updated=$(helm history $1 --namespace $NAME_SPACE|awk -v "i=$i" 'NR==i {print $2" "$3" "$4" "$5" "$6}')
    Status=$(helm history $1 --namespace $NAME_SPACE|awk -v "i=$i" 'NR==i {print $7}')
    Chart=$(helm history $1 --namespace $NAME_SPACE|awk -v "i=$i" 'NR==i {print $8}')
    Appversion=$(helm history $1 --namespace $NAME_SPACE|awk -v "i=$i" 'NR==i {print $9}')
    Description=$(helm history $1 --namespace $NAME_SPACE|awk -v "i=$i" 'NR==i {print $10" "$11}')
    echo "    <td colspan="1" nowrap>$Revision</td>">>integration_charts_history.html
    echo "    <td colspan="1" nowrap>$Updated</td>">>integration_charts_history.html
    echo "    <td colspan="1" nowrap>$Status</td>">>integration_charts_history.html
    echo "    <td colspan="1" nowrap>$Chart</td>">>integration_charts_history.html
    echo "    <td colspan="1" nowrap>$Appversion</td>">>integration_charts_history.html
    echo "    <td colspan="1" nowrap>$Description</td>">>integration_charts_history.html
    echo "  </tr>">>integration_charts_history.html
    i=`expr $i + 1`
  done
  echo "</table>">>integration_charts_history.html
}
store_history_call()
{
  store_history "eric-enm-bro-integration-$NAME_SPACE"
  store_history "eric-enm-pre-deploy-integration-$NAME_SPACE"
  store_history "eric-enm-infra-integration-$NAME_SPACE"
  store_history "eric-enm-stateless-integration-$NAME_SPACE"
}
integration_release_charts_history()
{
  touch integration_charts_history.html
  echo -e "<!DOCTYPE html>\n<html>\n<head>\n<style>">integration_charts_history.html
  echo -e "h1 {font-family: courier, monospace; color: blue;}">>integration_charts_history.html
  echo -e "table {font-family: arial, sans-serif; border-collapse: collapse; font-size: 14px;}">>integration_charts_history.html
  echo -e "td {border: 1px solid #dddddd; text-align: left; padding: 8px;}">>integration_charts_history.html
  echo -e "th {border: 1px solid #dddddd; text-align: left; padding: 8px; color: white; font-size: 16px;}">>integration_charts_history.html
  echo -e "tr:nth-child(even) {background-color: #dddddd;}">>integration_charts_history.html
  echo -e "</style>\n</head>\n<body>">>integration_charts_history.html
  echo '<center><h1 style="text-align:center">Helm chart history information</h1></center>'>>integration_charts_history.html
  store_history_call
  echo -e "</body>\n</html>">>integration_charts_history.html
}
system_pre_health_check()
{
  # Retrieving the current helm chart status information
  check_integration_chart_status "eric-enm-bro-integration-$NAME_SPACE"
  check_integration_chart_status "eric-enm-pre-deploy-integration-$NAME_SPACE"
  check_integration_chart_status "eric-enm-infra-integration-$NAME_SPACE"
  check_integration_chart_status "eric-enm-stateless-integration-$NAME_SPACE"

  # Retrieving and recording current helm chart History information
  integration_release_charts_history
  # Verifying READY status of statefulset objects
  kubectl get statefulsets --namespace $NAME_SPACE|awk 'NR>1&&!(($2=="1/1"||$2=="2/2"||$2=="3/3")) { print "Statefulset "$1" is not ready";exit 1 }'
  state=`echo $?`
  if [[ $state == 1 ]]
  then
      error " All statefulsets are not in ready state, Ensure 'READY' status of all statefulset objects before starting upgrade."
      exit 1
  else
      info "All Statefulsets are in ready state"
  fi

  # Verifying all pods are in Running state
  kubectl get pods --namespace $NAME_SPACE|awk 'NR>1 && !((( $2 == "1/1" || $2 == "2/2" || $2 == "3/3" ) && $3 == "Running") || $3 == "Completed" || $3 == "Error") { print "Pod "$1" is not running";exit 1 }'
  state=`echo $?`
  if [[ $state == 1 ]]
  then
     error "Some pods are not running, Ensure all pods are in 'Running' state before starting upgrade."
     exit 1
  else
     info "All pods are running successfully"
     check_restarts
  fi

  # Verifying all Jobs are in completed state
  kubectl get jobs --namespace $NAME_SPACE|awk 'NR>1&&!($2=="1/1") { print "Job "$1" is still running";exit 1 }'
  state=`echo $?`
  if [[ $state == 1 ]]
  then
      error " Some jobs are still running, Ensure any running Job is completed before starting upgrade."
      exit 1
  else
      info "All Jobs are in completed state"
  fi

  # Verifying there is no running CronJob
  kubectl get cronjobs --namespace $NAME_SPACE|awk 'NR>1&&!($8=="0") { print "Cronjob "$1" is still in progress";exit 1 }'
  state=`echo $?`
  if [[ $state == 1 ]]
  then
      error "A Cronjob is in progress, Ensure any running CronJob to be completed before starting upgrade. "
      exit 1
  else
      info "Presently No cronjob is running"
  fi

  # Verifying all PVC are in bound state
  kubectl get pvc --namespace $NAME_SPACE|awk 'NR>1&&!($2=="Bound") { print $1" PVC not Bounded";exit 1 }'
  state=`echo $?`
  if [[ $state == 1 ]]
  then
      error " All PVCs are not Bounded, Ensure all PVC are in bound state before starting upgrade."
      exit 1
  else
      info "All PVCs are Bounded"
  fi
}

#####################################################
#        CENM PRE HELAT CHECK SCRIPT                #
#####################################################
cenm_pre_health_check() {
  info "Performing cENM pre Health Check...."
  system_pre_health_check;
}

#####################################################
#                                                   #
#                    MAIN                           #
#                                                   #
#####################################################

info "Starting cenm_pre_health_check.sh script for cENM. Writing output to log file ${PACKAGE_LOG}"

while [ "$1" != "" ]; do
  PARAM=`echo $1 | awk -F= '{print $1}'`
  VALUE=`echo $1 | awk -F= '{print $2}'`
  case ${PARAM} in
      -h | --help)
          printUsage;
          ;;
      --cenm-name-space)
          NAME_SPACE=${VALUE}
          ;;
      *)
          printUsage;
          ;;
  esac
  shift
done

if [ "$NAME_SPACE" = "" ]; then
  printUsage;
fi

if [[ "$NAME_SPACE" =~ [A-Z] ]] ; then
  error "${RED}Value used for name-space is invalid. The name space can not contain upper case letters or underscores or be empty.${NOCOLOR}"
  exit 1
fi


cenm_pre_health_check;

info "Script Execution Complete. See log file ${PACKAGE_LOG}"
