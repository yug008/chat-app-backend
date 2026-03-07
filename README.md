# 💬 Chat App — Backend

A production-ready **Spring Boot** REST API and WebSocket server powering a real-time chat application with JWT authentication and private/public messaging.

🔗 **Frontend Repository:** [chat-app-frontend](https://github.com/yug008/chat-app-frontend)

---

## 🚀 Features

- **JWT Authentication** — Secure register and login with BCrypt password hashing
- **Spring Security** — Protected endpoints with stateless session management
- **WebSocket Messaging** — Real-time communication using STOMP over SockJS
- **Public Group Chat** — Broadcast messages to all connected users
- **Private Messaging** — Direct messages between specific users
- **Typing Indicators** — Real-time typing events over WebSocket
- **Read Receipts** — Notify senders when messages are read
- **Chat History** — Persistent message storage with MySQL
- **CORS Configuration** — Configured for React frontend on port 3000

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Spring Boot 3 | Backend framework |
| Spring Security | Authentication and authorization |
| JWT (jjwt) | Token-based authentication |
| Spring WebSocket + STOMP | Real-time messaging |
| SockJS | WebSocket fallback |
| Spring Data JPA | Database ORM |
| MySQL | Persistent storage |
| BCrypt | Password hashing |
| Lombok | Reduce boilerplate code |
| Maven | Dependency management |

---

## 📁 Project Structure

```
src/main/java/com/chatapp/chat_app_backend/
├── auth/
│   ├── AuthenticationController.java   # Register and login endpoints
│   ├── AuthenticationRequest.java      # Login request body
│   ├── AuthenticationResponse.java     # JWT token response
│   ├── AuthenticationService.java      # Auth business logic
│   └── RegisterRequest.java            # Register request body
├── config/
│   ├── ApplicationConfig.java          # UserDetailsService, PasswordEncoder
│   ├── CustomHandshakeHandler.java     # Assign Principal from username
│   ├── HttpHandshakeInterceptor.java   # Extract username from WS URL
│   ├── SecurityConfig.java             # Spring Security filter chain
│   └── WebSocketConfig.java            # STOMP endpoints and broker config
├── controller/
│   └── ChatController.java             # WebSocket message handlers
├── model/
│   ├── ChatMessage.java                # WebSocket message DTO
│   ├── ChatMessageEntity.java          # Database entity for messages
│   ├── Role.java                       # User roles enum
│   └── User.java                       # User entity
├── repository/
│   ├── ChatMessageRepository.java      # Message queries
│   └── UserRepository.java             # User queries
├── security/
│   ├── JwtAuthenticationFilter.java    # JWT validation filter
│   └── JwtService.java                 # JWT generate and validate
└── service/
    └── ChatService.java                # Chat business logic
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL

### Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/chat-app-backend.git
cd chat-app-backend
```

### Database Setup

Create a MySQL database:
```sql
CREATE DATABASE chatapp_db;
```

### Configuration

Create `src/main/resources/application.properties` using the example file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chatapp_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### Run

```bash
mvn spring-boot:run
```

Server starts at `http://localhost:8080`

---

## 🔌 API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/authenticate` | Login and get JWT token |

### Chat (Protected — requires Bearer token)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/users` | Get all registered users |
| GET | `/api/history` | Get public chat history |
| GET | `/api/conversation?user1=x&user2=y` | Get private conversation |

### WebSocket Endpoints
| Destination | Direction | Description |
|---|---|---|
| `/ws?username=` | Connect | WebSocket handshake |
| `/app/chat.sendMessage` | Client → Server | Send public message |
| `/app/chat.sendPrivateMessage` | Client → Server | Send private message |
| `/app/chat.addUser` | Client → Server | Announce user joined |
| `/app/chat.typing` | Client → Server | Send typing indicator |
| `/app/chat.read` | Client → Server | Send read receipt |
| `/topic/public` | Server → Client | Receive public messages |
| `/user/queue/private` | Server → Client | Receive private messages |

---

## 🔐 Authentication Flow

```
Client                          Server
  │                               │
  │── POST /api/auth/register ───►│ BCrypt hash password, save user
  │◄── { token: "eyJ..." } ───────│ Generate JWT
  │                               │
  │── POST /api/auth/authenticate►│ Validate credentials
  │◄── { token: "eyJ..." } ───────│ Generate JWT
  │                               │
  │── GET /api/users              │
  │   Authorization: Bearer eyJ..►│ JwtAuthenticationFilter validates token
  │◄── ["user1", "user2", ...] ───│ Return users
```

---

## 📡 WebSocket Flow

```
Client                          Server
  │                               │
  │── Connect /ws?username=batman►│ HttpHandshakeInterceptor extracts username
  │                               │ CustomHandshakeHandler sets Principal
  │── SUBSCRIBE /topic/public ───►│
  │── SUBSCRIBE /user/queue/priv►│
  │                               │
  │── /app/chat.sendMessage ─────►│ ChatController.sendMessage()
  │◄── /topic/public ─────────────│ Broadcast to all
  │                               │
  │── /app/chat.sendPrivateMessage►│ ChatController.sendPrivateMessage()
  │◄── /user/batman/queue/private─│ Deliver to specific user
```

---

## 👨‍💻 Author

**Yug Mehta**  
[GitHub](https://github.com/yug008) 
