helm install theodolite theodolite/theodolite -f https://raw.githubusercontent.com/cau-se/theodolite/main/helm/preconfigs/extended-metrics.yaml

kubectl delete configmap load-generator-configmap
kubectl delete configmap kafka-streams-configmap

kubectl create configmap load-generator-configmap --from-file=load-generator-deployment.yaml
kubectl create configmap kafka-streams-configmap --from-file=kafka-streams-deployment.yaml

kubectl apply -f kafka-streams-benchmark.yaml
kubectl apply -f kafka-streams-benchmark-executer.yaml