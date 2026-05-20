# Room Booking System

A full-stack web application for managing study room bookings, built as a Web Technologies final exam project. It allows students to view available rooms, make bookings, and manage reservations through a clean, responsive interface.

## Tech Stack

**Backend**
- Java 23 + Spring Boot 4.0.6
- Spring Data JPA + Hibernate
- MySQL database
- Lombok
- SpringDoc OpenAPI (Swagger UI)

**Frontend**
- Vue 3 (Composition API)
- Vite 5
- Pinia (state management)
- Vue Router 4
- Axios
- Tailwind CSS v4

## Features

- View all available study rooms with name, location, capacity, and availability status
- Add new rooms via the UI
- Book a room by selecting student name, student ID, date, room, and time slot
- Time slot conflict prevention — already-booked slots are hidden automatically
- View all existing bookings in a list
- Update or delete bookings
- REST API fully documented via Swagger UI

## Project Structure

```
exam/
├── src/main/java/julien/roombookingsystem/
│   ├── config/         # CORS configuration (WebConfig)
│   ├── controller/     # REST controllers (RoomController, BookingController)
│   ├── model/          # JPA entities (Room, Booking)
│   ├── repository/     # Spring Data JPA repositories
│   ├── service/        # Service interfaces and implementations
│   └── RoomBookingSystemApplication.java
├── src/main/resources/
│   └── application.properties
├── FRONTEND/
│   ├── src/
│   │   ├── components/
│   │   ├── router/         # Vue Router config
│   │   ├── services/       # Axios API service
│   │   ├── stores/         # Pinia stores (roomStore, bookingStore)
│   │   └── views/          # RoomsView, BookingView, BookingListView
│   ├── index.html
│   └── vite.config.js
├── Screenshots/
└── pom.xml
```

## Prerequisites

- Java 23+
- Maven (or use the included `mvnw` wrapper)
- Node.js 18+ and npm
- MySQL 8+

## Prerequisites

- Java 23+
- Maven (or use the included `mvnw` wrapper)
- Node.js 18+ and npm
- MySQL 8+
