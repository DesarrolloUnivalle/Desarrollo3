apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{SERVICE_NAME}}-deployment
  labels:
    app: {{SERVICE_NAME}}
spec:
  replicas: 2
  selector:
    matchLabels:
      app: {{SERVICE_NAME}}
  template:
    metadata:
      labels:
        app: {{SERVICE_NAME}}
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/actuator/prometheus"
    spec:
      containers:
      - name: {{SERVICE_NAME}}-container
        image: {{DOCKER_IMAGE}}:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        resources:
          limits:
            memory: "512Mi"
            cpu: "500m"
          requests:
            memory: "256Mi"
            cpu: "200m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 45
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10