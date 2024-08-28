
#!/bin/sh

namespace=
imagepullsecret=
while getopts n:s: option
do
case "${option}"
in
n) namespace=${OPTARG};;
s) imagepullsecret=${OPTARG};;
esac
done

echo "namespace=$namespace"
echo "imagepullsecret=$imagepullsecret"

if [ -z "$namespace" ]
  then
   echo "   missing namespace:"
   echo "sample usage: create-image-pull-secret.sh -n <namespace> -s <imagepullsecret>"
   exit 1;
fi

if [ -z "$imagepullsecret" ]
  then
   echo "   missing imagepullsecret:"
   echo "sample usage: create-secret.sh -n <namespace> -s <imagepullsecret>"
   exit 1;
fi

kubectl  get  secrets -n $namespace | grep $imagepullsecret
resultCode=$?

if [ ${resultCode} -eq 0 ] ; then
    echo "##### Removing secret"
    kubectl delete secret $imagepullsecret -n $namespace
    resultCode=$?
    if [ ${resultCode} -ne 0 ] ; then
        echo "Unexpected Error when Removing secret"
        exit 1
    fi
    echo ""
    echo "##### Gernerating secret."
    kubectl create secret generic $imagepullsecret --from-file=.dockerconfigjson=/home/lciadm100/.docker/config.json --type=kubernetes.io/dockerconfigjson --namespace $namespace
    resultCode=$?
    if [ ${resultCode} -ne 0 ] ; then
        echo "Unexpected Error when Creating the secret"
        exit 1
    fi
    echo "##### completed."
    echo ""
    echo "done"
else
    echo "##### Gernerating secret."
    kubectl create secret generic $imagepullsecret --from-file=.dockerconfigjson=/home/lciadm100/.docker/config.json --type=kubernetes.io/dockerconfigjson --namespace $namespace
    resultCode=$?
    if [ ${resultCode} -ne 0 ] ; then
        echo "Unexpected Error when Creating the secret"
        exit 1
    fi
    echo ""
    echo "done"
fi

  


