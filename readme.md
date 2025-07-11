# Real-time Feedback Platform

A simple and effective way to give and receive live feedback.

## Features

- **Real-time Feedback**: Get instant feedback from users via WebSockets.
- **Admin Dashboard**: A dedicated dashboard for administrators to view feedback in real-time.
- **Simple Feedback Submission**: Easy-to-use form for users to submit feedback with ratings and comments.
- **REST API**: A well-defined API for managing admins and feedback.

## Technologies Used

- Java 17  
- Spring Boot  
- Spring Modulith for modular application structure  
- Spring Web for building web applications and REST APIs  
- Spring Data Redis for data storage  
- Thymeleaf for server-side HTML template rendering  
- WebSocket for real-time communication  
- Maven for dependency management  

## Prerequisites

- Java 17 or later  
- Maven 3.2+  
- Docker (for containerization)  

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/fred-maina/feedbackSystem
   ```

2. **Navigate to the project directory:**
   ```bash
   cd simple
   ```

3. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will be available at `http://localhost:8080`.

## API Endpoints

The API documentation is available at `/docs/index.html` once the application is running.

### Main Endpoints:

- `POST /api/admins/register`: Register a new admin  
- `GET /api/admins/me`: Get the current admin's details  
- `POST /api/feedback`: Submit feedback for an admin  
- `GET /api/feedback/mine`: Retrieve feedback for the currently authenticated admin  

## Live Demo

🔗 [Click here to try the live demo](https://53c4-197-139-44-10.ngrok-free.app)

## Repository

📦 [GitHub Repository](https://github.com/fred-maina/feedbackSystem)
