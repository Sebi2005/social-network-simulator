## Social Network Simulator

A JavaFX desktop application that simulates a basic social network.  
The application allows users to create posts, subscribe to topics, and view a personalized feed based on interests and mentions.

## 🚀 Features

- 👤 Multiple user windows (one per user)
- 📝 Create and update posts
- 🔍 Subscribe to topics using substring search
- 📰 Personalized feed:
  - Posts containing subscribed hashtags (#topic)
  - Posts mentioning the user (@username)
- 🔄 Real-time updates using the **Observer design pattern**
- 🗄️ Data persisted in a **relational database (SQLite)**

---

## 🏗️ Architecture

The application follows a layered architecture:
domain → repository → service → ui
- **Domain**: Entities (`User`, `Post`, `Topic`)
- **Repository**: Database access (JDBC)
- **Service**: Business logic + Observer pattern
- **UI**: JavaFX interface

---

## 🧠 Design Patterns

- **Observer Pattern**
  - Used to update all user windows automatically when:
    - a post is added
    - a post is updated

---

## 🗄️ Database

SQLite is used for persistence.

### Tables:
- `User`
- `Topic`
- `UserTopic`
- `Post`

---

## ⚙️ Technologies

- Java 21
- JavaFX
- JDBC
- SQLite

  ## ▶️ How to Run

1. Clone the repository:
```bash
git clone https://github.com/your-username/social-network-simulator.git
2. Open the project in IntelliJ IDEA
3. Make sure JavaFX is configured:

Add VM options:

--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
4. Run MainApp.java
