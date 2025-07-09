# 🎓 InternScope

InternScope is a Spring Boot-based web platform that empowers interns and mentors with personalized task management, AI-generated feedback, and secure user management. The platform leverages AI to provide actionable, concise feedback on mentor comments, helping interns grow and improve efficiently.

---

## ✨ Features

### 👨‍🎓 For Interns
- Register/Login with JWT authentication
- View and manage assigned tasks
- Receive AI-generated feedback on mentor comments
- Track your feedback/report history
- Delete your account anytime

### 👨‍🏫 For Mentors
- Assign tasks to interns
- Provide comments on intern work
- Generate and review AI-powered feedback for interns
- Delete your mentor profile

### 🛠 For Admins
- View all registered users (with their roles)
- Remove any intern or mentor
- Automatically remove associated data (tasks, reports, user registrations)

### 🤖 AI-Powered Features
- Analyze mentor comments and generate structured feedback for interns
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

| Method | Endpoint         | Description         | Access  |
|--------|------------------|---------------------|---------|
| POST   | /api/auth/login  | Login & get token   | Public  |
| POST   | /api/auth/register | Register a user   | Public  |

### 👨‍🏫 Mentor APIs

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| POST   | /api/mentor/tasks       | Assign task to intern      |
| GET    | /api/mentor/tasks       | View assigned tasks        |
| POST   | /api/mentor/feedback    | Generate AI feedback for intern |
| DELETE | /api/mentor/delete      | Delete mentor profile      |

### 👨‍🎓 Intern APIs

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | /api/intern/tasks       | View assigned tasks        |
| GET    | /api/intern/reports     | View AI-generated feedback |
| DELETE | /api/intern/delete      | Delete intern profile      |

### 🛡 Admin APIs

| Method | Endpoint                | Description                | Access |
|--------|-------------------------|----------------------------|--------|
| GET    | /api/admin/users        | Get list of all users      | ADMIN  |
| DELETE | /api/admin/remove       | Delete a user by username  | ADMIN  |

---

## 📁 Headers Required (For Protected Endpoints)

```
Authorization: Bearer <your_jwt_token>
Content-Type: application/json
```

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

[MIT](LICENSE) (or specify your license) 