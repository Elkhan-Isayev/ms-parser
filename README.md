# 📰 ms-parser

**ms-parser** is a Spring Boot application designed to scrape news articles from [https://www.informatik.az](https://www.informatik.az), extract relevant data (including images), and persist it to a PostgreSQL database.

---

## 🚀 Purpose

This project demonstrates:
- HTML scraping using **Jsoup**
- Parsing structured and unstructured article content
- Downloading and saving image files to disk
- Persisting `News` and `Files` entities in a relational database

---

## ⚙️ Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- PostgreSQL
- Jsoup
- Lombok
- SLF4J

---

## 🧱 Project Structure

```
az.informatik.msparser
├── controller           # REST controller
├── service              # Scraping and business logic
├── model                # JPA entities: NewsEntity and FilesEntity
├── repository           # Spring JPA repositories
└── util                 # HTML parsing and helper utilities
```

---

## 🛠️ Setup


### Configuration

Edit your `application.yml` or `application.properties`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db
    username: postgres
    password: your_password
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
```

---

## 🔄 How It Works

1. The app visits `https://www.informatik.az/news/?page=N` pages and collects article links.
2. Each article is parsed individually.
3. Extracted fields:
    - Title
    - Content (plain text)
    - Author
    - View count
    - Created date & time
    - List of `<img>` files
4. Images are downloaded and renamed using UUID.
5. Entities are persisted in PostgreSQL.

---

## 📁 File Saving

Downloaded images are stored in the `/files/` directory (relative to project root). Each file is saved with a unique UUID-based name (e.g., `b3423f0c-d7fa-48a1-a8d4-fc9e1ed8d937.jpg`).

---

## ✅ Handled Cases

The parser supports 3 types of articles:
1. Articles with **text only**
2. Articles with **text + images**
3. Articles with **images only**

---

## 🧪 Running

Make a `POST` request to your parsing endpoint:

```bash
curl -X POST http://localhost:8080/api/parse
```

---

## 📝 Sample Output

```log
✅ File saved: files/7b5f1c42-...jpg
✅ Saved news: ALPLogo-2024 winners announced
```

---

## 🪪 License

MIT License
