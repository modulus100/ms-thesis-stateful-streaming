helm uninstall theodolite
kubectl delete pods --all -n default --grace-period=0 --force