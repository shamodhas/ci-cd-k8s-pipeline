# 🚀 CI/CD Pipeline Deployment Guide for Spring Boot using Docker, GitHub Actions & Kubernetes (K3s)

> **Automate your Spring Boot app deployment from code push to Kubernetes cluster with GitHub Actions CI/CD pipeline**

---

## 🛠️ CI/CD Pipeline Deployment Workflow

1. **Code Commit & Push**  
   Developer pushes code changes to the GitHub repository (usually `main` branch).

2. **Build & Test**  
   GitHub Actions triggers the pipeline to build the Spring Boot app and run tests (optional).

3. **Docker Image Build & Push**  
   The pipeline builds a Docker image of the app and pushes it to Docker Hub with a unique tag.

4. **Kubernetes Deployment**  
   Using `kubectl`, the pipeline updates the Kubernetes deployment with the new Docker image.

5. **Verification & Rollout**  
   Kubernetes rolls out the new pods with the updated image; the pipeline monitors rollout status to ensure success.

---

## ☸️ Setup Kubernetes On the Server

- **Where:** Server or local VM  
- Follow the detailed install guide here:  
  [https://github.com/shamodhas/k8s-simple-deployment](https://github.com/shamodhas/k8s-simple-deployment)

- After install, verify cluster is running:
  ```bash
  sudo systemctl status k3s
  kubectl get nodes
  ```

---

## ⚠️ Before Starting

If you have previous Kubernetes deployments or services running, stop and clean them to avoid conflicts:

```bash
kubectl delete deployment <deployment-name>
kubectl delete service <service-name>
kubectl get all
docker rmi <your-docker-image
```

---

### 📁 1. Prepare Your Project (On your local computer)

```bash
project/
├── Dockerfile
├── pom.xml
├── src/
├── k8s/
│   ├── deployment.yaml
│   └── service.yaml
└── .github/
    └── workflows/
        └── ci-cd-pipeline.yml
```

- **k8s/deployment.yaml** [deployment.yaml](https://github.com/shamodhas/ci-cd-k8s-pipeline/blob/main/k8s/deployment.yaml)
- **k8s/service.yaml** [service.yaml](https://github.com/shamodhas/ci-cd-k8s-pipeline/blob/main/k8s/service.yaml)
- **.github/workflows/ci-cd-pipeline.yml** [ci-cd-pipeline.yml](https://github.com/shamodhas/ci-cd-k8s-pipeline/blob/main/.github/workflows/ci-cd-pipeline.yml)

---

### 🔑 2. Add GitHub Secrets (GitHub)

Go to your GitHub repo > **Settings** > **Secrets** > **Actions** and add:

| Secret Name      | Value                                                |
|------------------|------------------------------------------------------|
| DOCKER_USERNAME  | Your Docker Hub username                             |
| DOCKER_PASSWORD  | Docker Hub password or access token                  |
| KUBECONFIG       | Content of your Kubernetes config (`~/.kube/config`) |

---

### 📄 How to get your KUBECONFIG

On your server terminal, run:

```bash
cat ~/.kube/config
```

The output will be YAML-formatted, e.g.:

```yaml
apiVersion: v1
clusters:
  - cluster:
      certificate-authority-data: ...
      server: https://...
    name: ...
contexts:
  - context:
      cluster: ...
      user: ...
    name: ...
current-context: ...
kind: Config
preferences: { }
users:
  - name: ...
    user:
      client-certificate-data: ...
      client-key-data: ...
```

Copy the entire content and paste it as the value for the KUBECONFIG secret in GitHub.

---

### 🚀 3. Push Code to GitHub

```bash
git add .
git commit -m "Setup CI/CD pipeline"
git push origin main
```

---

### 🤖 4. Pipeline Runs Automatically

GitHub Actions will:  
- Build Spring Boot app  
- Build & push Docker image  
- Deploy new image to Kubernetes  
- Monitor rollout status  

---

### ✅ 5. Verify Deployment

```bash
kubectl get pods
kubectl get deployments
kubectl get svc
```

---

### 🌐 6. Access your app

```
http://<server-ip>:<node-port>
```

---

### 🔄 7. Need to Re-deploy?

Make changes, commit, and push — GitHub Actions will take care of the rest

---

## 🛑 Troubleshooting: Port 6443 Connection Error

If GitHub Actions or your local machine cannot connect to Kubernetes API server on port 6443 (connection refused or timeout), follow these small steps:

1. **Edit your Kubernetes config to replace the server IP**  
   Ensure K3s is active on your server:

   ```bash
   nano ~/.kube/config
   ```

2. **Find the clusters: section and locate the server: field**  
   It may look like:

   ```yaml
   clusters:
   - cluster:
     certificate-authority-data: <cert-data>
     server: https://127.0.0.1:6443
     name: k3s-default
   ```

3. **Replace the IP address (127.0.0.1) with your public IP address**  
   For example:

   ```yaml
    clusters:
    - cluster:
         certificate-authority-data: <cert-data>
         server: https://<your-public-ip>:6443
         name: k3s-default
    ```
   (Replace with your actual public IP)

4. **Save and exit the editor**  

5. **Ensure port 6443 is open and reachable**  
   Check firewall rules or cloud security groups to allow inbound TCP connections on port 6443.  
   Test connectivity with:
    ```bash
      nc -zv <your-public-ip> 6443
    ```
   
6. **Update your GitHub Secret KUBECONFIG**  
   Copy the updated ~/.kube/config content (now with your public IP in server: field) and paste it in your GitHub repository secrets as the value for KUBECONFIG.

7. **Try running your pipeline again**

---