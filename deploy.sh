#!/usr/bin/env bash
# ============================================================
# Gacha Admin Platform — Deployment Script
# ============================================================
# Usage:
#   ./deploy.sh build          # Build all Docker images
#   ./deploy.sh push           # Push images to registry
#   ./deploy.sh deploy         # Apply K8s manifests
#   ./deploy.sh all            # Build + push + deploy
#   ./deploy.sh status         # Check rollout status
#   ./deploy.sh logs <svc>     # Tail logs from a service
# ============================================================

set -euo pipefail

REGISTRY="${REGISTRY:-registry.example.com/gacha}"
K8S_NAMESPACE="gacha-system"
SERVICES=(auth-service gateway-service)

cd "$(dirname "$0")"

build() {
  echo "=== Building admin-frontend ==="
  (cd admin-frontend && docker build -t "$REGISTRY/admin-frontend:latest" .)

  for svc in "${SERVICES[@]}"; do
    echo "=== Building $svc ==="
    docker build --build-arg SERVICE_NAME="$svc" \
      -t "$REGISTRY/$svc:latest" \
      -f docker/Dockerfile .
  done
}

push() {
  for svc in "${SERVICES[@]}" admin-frontend; do
    echo "=== Pushing $svc ==="
    docker push "$REGISTRY/$svc:latest"
  done
}

deploy() {
  echo "=== Applying K8s base manifests ==="
  kubectl apply -f k8s/base/namespace.yaml
  kubectl apply -f k8s/base/secret.yaml
  kubectl apply -f k8s/base/configmap.yaml
  kubectl apply -f k8s/base/infrastructure.yaml

  for svc in "${SERVICES[@]}"; do
    echo "=== Deploying $svc ==="
    kubectl apply -f "k8s/$svc/"
  done

  echo "=== Deploying admin-frontend ==="
  kubectl apply -f k8s/frontend/

  echo "=== Applying ingress ==="
  kubectl apply -f k8s/base/ingress.yaml

  echo "=== Waiting for rollouts ==="
  for svc in "${SERVICES[@]}" admin-frontend; do
    kubectl rollout status "deployment/${svc}" -n "$K8S_NAMESPACE" --timeout=120s
  done
}

status() {
  kubectl get all -n "$K8S_NAMESPACE"
  echo ""
  kubectl get ingress -n "$K8S_NAMESPACE"
}

logs() {
  local svc="${1:-}"
  if [[ -z "$svc" ]]; then
    echo "Usage: ./deploy.sh logs <service-name>"
    exit 1
  fi
  kubectl logs -f "deployment/${svc}" -n "$K8S_NAMESPACE"
}

case "${1:-}" in
  build)  build ;;
  push)   push ;;
  deploy) deploy ;;
  all)    build && push && deploy ;;
  status) status ;;
  logs)   logs "${2:-}" ;;
  *)
    echo "Usage: $0 {build|push|deploy|all|status|logs <svc>}"
    exit 1
    ;;
esac
