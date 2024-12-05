# Project Name: **App Course** 📚

Welcome to the **App Course** repository! This project contains various services built with **Spring Boot** and **Docker** to manage and run different aspects of a course management system.

## Services in this Repository 🚀

This repository contains the following services:

1. **Event Service** 🎉
2. **Registration Service** 📝
3. **Course Information Server** 🖥️
4. **Course Information CLI** 💻
5. **Course Information Repository** 🏛️

---

## Folder Structure 📂

```plaintext
app-course (root)
├── **docker-compose.yml**          # Docker Compose file (located here)
├── course-calling-web
│   ├── **event-service**           # Event service package
│   │   └── **Dockerfile**          # Dockerfile for event service
│   └── **registration-service**    # Registration service package
│       └── **Dockerfile**          # Dockerfile for registration service
├── course-info-cli                 # Command-line interface for course info
├── course-info-repository          # Repository layer for course info
├── course-info-server              # Server for course info
└── (Other modules...)
```
# Project Setup and Docker Workflow 🚀

This guide explains how to set up and run the services using Docker and Docker Compose. It will help you build,
configure, and deploy the services in the project.

---

## 1. **Folder Structure** 📂

```plaintext
app-course (root)
├── docker-compose.yml          # Docker Compose file (located here)
├── course-calling-web
│   ├── event-service           # Event service package
│   │   └── Dockerfile          # Dockerfile for event service
│   └── registration-service   # Registration service package
│       └── Dockerfile          # Dockerfile for registration service
└── (Other modules...)
```

## 2. Build Docker Images for Services 🛠️

Each service has its own Dockerfile. Follow these steps to build the Docker images for the services:

- **Build** ``event-service`` Docker Image 🧩:

    1. **Navigate to the ``event-service`` directory**:
       ```bash
           cd app-course/course-calling-web/event-service
       ```
    2. **Build the Docker image**:
       ```bash
          docker build -t event-service:0.0.1-SNAPSHOT .
       ```
- **Build** ``registration-service`` Docker Image 🧩:
    1. **Navigate to the ``registration-service`` directory**:
       ```bash
           cd app-course/course-calling-web/event-service
       ```
    2. **Build the Docker image**:
       ```bash
          docker build -t registration-service:0.0.1-SNAPSHOT .
       ```

## 3. Run Services Using Docker Compose 🏃‍♀️

After building the Docker images for each service, you can bring up the services using Docker Compose.

1. **Navigate back to the root directory ``(app-course)``, where the ``docker-compose.yml`` file is located**:
   ```bash
          cd app-course
   ```
2. Run the services in detached mode:
   ```bash
       docker-compose up -d
   ```

This will start up all the services ``(e.g., event-service, registration-service, and others)`` defined in the
``docker-compose.yml``.

## 4. Stopping the Services ⏹️

To stop the services, run the following command from the root directory ``(app-course)``:

```bash
      docker-compose down
```

## 5. Important Notes ⚠️

- **Dockerfile Locations**: Each service ``(like event-service and registration-service)`` has its own Dockerfile
  located in their respective directories.
- **Docker Compose File**: The docker-compose.yml file is located at the root of the project ``(app-course)``, and it
  manages all services.
- **No Need for Root-Level Dockerfile**: The root directory ``(app-course)`` doesn't need a ``Dockerfile`` because we
  manage everything using ``docker-compose.yml``.

## 6. Quick Overview of Commands 🏃‍♂️

- **Build event-service image**:
  ```bash
   cd app-course/course-calling-web/event-service
   docker build -t event-service:0.0.1-SNAPSHOT .
  ```
- **Build event-service image**:
  ```bash
   cd app-course/course-calling-web/registration-service
   docker build -t registration-service:0.0.1-SNAPSHOT .
  ```
- **Run services using Docker Compose**:
  ```bash
   cd app-course
   docker-compose up -d
  ```
- **Run services using Docker Compose**:

 ```bash
  docker-compose down
 ```
  
## 7. Troubleshooting ⚡

- **If you encounter issues, check the logs of a specific service with**:
 ```bash
 docker logs <container-name>
 ```
- **To view the running containers:**:
 ```bash
 docker ps
 ```
- **To view the running containers:**:
```bash
    docker-compose build
```

## 8. Additional Notes for Developers 🧑‍💻

- Ensure Docker and Docker Compose are installed on your machine.
- You can modify the docker-compose.yml file to add more services, adjust configurations, or set up additional 
  networks and volumes.
- Keep an eye on the healthcheck configurations in ``docker-compose.ym``l to ensure each service is up and running.
