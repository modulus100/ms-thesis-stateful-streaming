aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 778851315920.dkr.ecr.us-east-1.amazonaws.com
docker buildx build -t 778851315920.dkr.ecr.us-east-1.amazonaws.com/load-generator --no-cache . --platform=linux/amd64 --push