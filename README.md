# 🎬 Movie Ticket Booking System

A full-stack **Movie Ticket Booking System** built with **Spring Boot, Spring Data JPA, Hibernate, and MySQL**.  
It allows users to browse movies, select shows, choose seats, and book tickets with integrated payment details.

---

## 🚀 Features

- User Management (Register, Login, Bookings history)
- Movie and Show Management
- Theater and Screen Management
- Seat Selection & Booking
- Payment Integration (mock)
- REST API design with DTOs

---

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Data JPA, Hibernate  
- **Database:** MySQL  
- **Build Tool:** Maven  
- **Testing Tool:** Postman / cURL  
- **Version Control:** Git + GitHub  

---

## 📂 Project Structure

---
```
src/main/java/com/example/moviebooking/
├── controller # REST Controllers
├── dto # Data Transfer Objects
├── entity # JPA Entities (User, Booking, Movie, Show, etc.)
├── repository # Spring Data JPA Repositories
├── service # Business Logic
└── MovieBookingApplication.java
```


---

## ⚙️ Requirements

- **Java 17+**
- **Maven 3.8+**
- **MySQL 8+**
- **Spring Boot 3.x**

---

## 🔧 Setup & Installation

1. **Clone the repository**
   ```bash
   git clone  https://github.com/khandaitBhushan/MovieShowBooking.git
   cd movie-booking-system


---
## 📌 Example API Usage
➤ Create Booking Request

``` POST /api/bookings ```

``` {
  "userId": 1,
  "showId": 2,
  "seatIDs": [5, 6, 7],
  "paymentMethod": "CREDIT_CARD"
}
```
➤ Get All Movies

``` GET /api/movies ```

---

## 🎭 Sample Data

``` User: Rahul Sharma (rahul@example.com)

Movie: 3 Idiots (Comedy-Drama)

Theater: PVR Cinemas (Mumbai)

Show: 2025-10-05 10:00 to 13:00

Seats: A1, A2, A3
```
---

## 📜 License

This project is licensed under the MIT License😂.
---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you’d like to change.
---

## 👨‍💻 Author

Bhushan Khandait
🚀 Passionate about Backend Development & DSA
🔗 LinkedIn
 | GitHub



