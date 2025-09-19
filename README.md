# 🎴 Deckbox

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blueviolet)
![License](https://img.shields.io/badge/License-MIT-yellow)

> An API for managing **Pokémon TCG decks** — create, manage, validate, and play by the rules.  
> Designed with **Clean Architecture** and **SOLID principles** for flexibility, scalability, and long-term maintainability.  
> Future plans include a **React frontend** for web access, and deployment with **Docker** and **AWS** for reliable cloud hosting.

---

## ✨ Features

- 🗂 **Deck Management**: Create, update, and delete decks.  
- 🃏 **Card Operations**: Add, remove, and update cards in a deck.  
- ✅ **Validation Rules**: Ensure decks comply with official formats.  
- ⚙️ **Format Management**: Define and manage deck formats with custom rules.  
- 💾 **Persistence**: Powered by PostgreSQL (runtime) and H2 (development).  

---

## 🛠️ Tech Stack

| Category      | Technology |
|---------------|------------|
| Language      | Java 17 |
| Framework     | Spring Boot 3.5.5 |
| Database      | PostgreSQL (prod) / H2 (dev) |
| Libraries     | [Vavr](https://www.vavr.io/) (functional programming), Spring Data JPA |
| Build Tool    | Maven |

---

## 🛣️ Roadmap

Here’s what’s coming next for Deckbox:

- [ ] 🌐 **React Frontend** — a web interface for building and managing decks.
- [ ] 🐳 **Docker Support** — containerized deployments for consistent environments.
- [ ] ☁️ **AWS Hosting** — scalable and reliable cloud deployment.
- [ ] 🔒 **Authentication & Authorization** — secure user accounts and deck ownership.
- [ ] 📊 **Analytics & Insights** — usage metrics and deck statistics.
- [ ] 🧪 **CI/CD Pipeline** — automated testing and deployments.
- [ ] 🔌 **GraphQL API** (exploration) — alternative to REST for flexible queries.

---

## 🚀 Getting Started

### Prerequisites
- ☕ Java 17+
- 🛠 Maven 3.8+
- 🐘 PostgreSQL (for production)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/AngeloAG/deckbox-java.git
cd deckbox-java

# 2. Build the project
./mvnw clean install

# 3. Run the application
./mvnw spring-boot:run
```

👉 API available at: http://localhost:8080

---

### ⚙️ Configuration
The application uses YAML-based configuration files located in src/main/resources:
 
- Development: application-dev.yaml
- Production: application.yaml

Update the database connection settings in the respective file:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://<your-db-host>:<your-db-port>/<your-db-name>
    username: <your-username>
    password: <your-password>
```

---

### 📂 Project Structure
```
src/
├── main/
│   ├── java/com/molardev/deckbox/
│   │   ├── application/    # Application services and commands
│   │   ├── domain/         # Core domain entities and value objects
│   │   ├── infrastructure/ # Persistence and repository implementations
│   │   └── [DeckboxApplication.java](http://_vscodecontentref_/0) # Main entry point
│   └── resources/          # Configuration files
└── test/                   # Unit and integration tests
```

---

## 🤝 Contributing
Contributions, ideas, and feedback are welcome!
Feel free to open an issue
 or submit a pull request

--- 

### 📜 License
This project is licensed under the MIT License.

