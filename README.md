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

## Database Setup

Make sure MySQL is running, then create the database:

```sql
CREATE DATABASE `room-booking-system_db`;
```

The tables are created automatically by Hibernate on first run (`spring.jpa.hibernate.ddl-auto=update`).

Default credentials in `application.properties`:
- **URL:** `jdbc:mysql://localhost:3306/room-booking-system_db`
- **Username:** `root`
- **Password:** *(empty)*

Update `src/main/resources/application.properties` if your MySQL credentials differ.

## Running the Backend

```bash
cd exam
./mvnw spring-boot:run
```

On Windows:
```bash
mvnw.cmd spring-boot:run
```

The backend starts on **http://localhost:8080**

## Running the Frontend

```bash
cd FRONTEND
npm install
npm run dev
```

The frontend starts on **http://localhost:5173**

> Make sure the backend is running first so API calls succeed.

## API Endpoints

### Rooms — `/api/rooms`

| Method | Endpoint        | Description       |
|--------|-----------------|-------------------|
| GET    | `/api/rooms`    | Get all rooms     |
| GET    | `/api/rooms/{id}` | Get room by ID  |
| POST   | `/api/rooms`    | Create a new room |
| PUT    | `/api/rooms/{id}` | Update a room   |

### Bookings — `/api/bookings`

| Method | Endpoint             | Description          |
|--------|----------------------|----------------------|
| GET    | `/api/bookings`      | Get all bookings     |
| POST   | `/api/bookings`      | Create a new booking |
| PUT    | `/api/bookings/{id}` | Update a booking     |
| DELETE | `/api/bookings/{id}` | Delete a booking     |

Full interactive API docs available at **http://localhost:8080/swagger-ui.html**

## Data Models

### Room
| Field        | Type    | Description                  |
|--------------|---------|------------------------------|
| id           | Long    | Auto-generated primary key   |
| name         | String  | Room name (required)         |
| capacity     | int     | Max number of people         |
| location     | String  | Physical location            |
| availability | boolean | True if room is available    |

### Booking
| Field       | Type      | Description                        |
|-------------|-----------|------------------------------------|
| id          | Long      | Auto-generated primary key         |
| studentName | String    | Full name of the student (required)|
| studentId   | String    | Student ID e.g. STU-001 (required) |
| roomId      | Long      | Reference to the booked room       |
| bookingDate | LocalDate | Date of the booking                |
| timeSlot    | String    | One of 3 available time slots      |
| release     | boolean   | Whether the booking is active      |
