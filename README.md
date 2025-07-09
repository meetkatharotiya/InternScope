# 🎓 InternScope

InternScope is a Spring Boot-based web platform that empowers interns and mentors with personalized task management, AI-generated feedback, and secure user management. The platform leverages AI to provide actionable, concise feedback on mentor comments, helping interns grow and improve efficiently.

---

## ✨ Features

### 👨‍🎓 For Interns
- Register/Login with JWT authentication
- Log new tasks
- View your own tasks
- Download your internship report as a PDF
- Receive AI-generated feedback on mentor comments

### 👨‍🏫 For Mentors
- Assign tasks to interns
- View all assigned tasks 
- Add comments to intern tasks and generate AI-powered feedback
- Schedule meetings with interns (sends email notification)

### 🛠 For Admins
- View all registered users
- Remove any user by username

### 🤖 AI-Powered Features
- Generate structured feedback on mentor comments for intern tasks
- Feedback includes strengths, areas for improvement, and actionable next steps
- All AI features are powered by OpenRouter (DeepSeek model) via Spring AI

---

## ⚙ Tech Stack

- Spring Boot
- Spring Security + JWT
- MongoDB Atlas
- Spring AI (OpenRouter API, DeepSeek Model)
- Java Mail (for notifications)

---

## 📁 Project Structure

```
src/
  main/
    java/com/InternScope/
      controller/   # REST APIs (Auth, Task, MentorTask, Report)
      service/      # Business logic and services (AI, Email, UserDetails)
      model/        # Entity models (User, Role, Task)
      security/     # JWT utilities and filters
      repository/   # Spring Data JPA repositories
      config/       # Security configuration
      InternScopeApplication.java # Main entry point
    resources/
      application.properties      # App configuration
      templates/                  # Email templates
  test/
    java/com/InternScope/
      InternScopeApplicationTests.java # Basic tests
```

---

## 🧩 AI Integration

All AI features use [Spring AI](https://docs.spring.io/spring-ai/) with OpenRouter and the DeepSeek model.

**Example Usage:**  
When a mentor leaves a comment on an intern's work, the system uses AI to generate feedback with three sections:
- **Strengths:** One positive point or effort noted.
- **Improvements:** One area to refine based on the comment.
- **Next Steps:** One actionable recommendation.

**Configuration (in `src/main/resources/application.properties`):**
```properties
spring.ai.openai.api-key=your_openrouter_api_key
spring.ai.openai.base-url=https://openrouter.ai/api
spring.ai.openai.chat.options.model=deepseek/deepseek-r1-distill-llama-70b:free
```

---

## 🛠 Setup & Run Locally

### 1️⃣ Prerequisites

- Java 17+
- Maven
- MongoDB Atlas (or local MongoDB)
- OpenRouter API Key

### 2️⃣ Clone the Repo

```bash
git clone https://github.com/your-username/internscope.git
cd internscoope
```

### 3️⃣ Configure application.properties

Update the file at: `src/main/resources/application.properties`

```properties
# === Server Configuration ===
server.port=8080
spring.application.name=InternScope

# === MongoDB Atlas ===
spring.data.mongodb.uri=your_mongodb_uri
spring.data.mongodb.database=InternScope

# === AI Integration ===
spring.ai.openai.api-key=your_openrouter_api_key
spring.ai.openai.base-url=https://openrouter.ai/api
spring.ai.openai.chat.options.model=deepseek/deepseek-r1-distill-llama-70b:free

# === JWT Secret ===
jwt.secret=your_jwt_secret

# === Mail Service ===
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4️⃣ Run the Application

```bash
./mvnw spring-boot:run
```

---

## 🛁 API Endpoints

### 🔐 Authentication

| Method | Endpoint             | Description           | 
|--------|----------------------|-----------------------|
| POST   | /api/auth/register   | Register a user       | 
| POST   | /api/auth/login      | Login & get token     |

---

### 👨‍🏫 Mentor APIs

| Method | Endpoint                              | Description                        | 
|--------|---------------------------------------|------------------------------------|
| POST   | /api/mentor/tasks/assign              | Assign task to intern              | 
| POST   | /api/mentor/tasks/{taskId}/comment    | Add comment & get AI feedback      | 
| GET    | /api/mentor/tasks/all                 | View all assigned tasks            | 
| POST   | /api/mentor/schedule-meeting          | Schedule meeting (email to intern) | 

---

### 👨‍🎓 Intern APIs

| Method | Endpoint                    | Description                |
|--------|-----------------------------|----------------------------|
| POST   | /api/intern/tasks/log       | Log a new task             | 
| GET    | /api/intern/tasks           | View your tasks            |
| GET    | /api/reports/{username}/pdf | Download your report PDF   | 

---

### 🛡 Admin APIs

| Method | Endpoint                | Description                | 
|--------|-------------------------|----------------------------|
| GET    | /api/admin/users        | Get list of all users      | 
| DELETE | /api/admin/remove       | Delete a user by username  | 

---

## 📁 Headers Required (For Protected Endpoints)

```
Authorization: Bearer <your_jwt_token>
Content-Type: application/json
```

---

---


