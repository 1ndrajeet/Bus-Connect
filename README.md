# Bus Pass Management System

## Overview
The **Bus Pass Management System** is an Android application designed to streamline the process of requesting, approving, and managing bus passes. The application uses a PHP-based backend with a MySQL database to handle user data and pass requests. It also includes features for user authentication and admin functionalities to manage requests efficiently.

## Features
- **User Authentication:**
  - Secure user registration and login.
  - Passwords hashed using `bcrypt` for enhanced security.

- **Pass Management:**
  - Users can request new bus passes with specific destinations and payment modes.
  - Passes have expiration dates based on user-selected durations.

- **Admin Panel:**
  - View all pass requests with their statuses.
  - Approve or reject pass requests.

- **Data Management:**
  - View user details and retrieve pass history for specific users.

## Technology Stack
- **Frontend:** Android (Java/Kotlin)
- **Backend:** PHP
- **Database:** MySQL (MariaDB 10.4.32)
- **API Testing:** Postman

## Installation and Setup
### Prerequisites
1. PHP 8.2.12 or later
2. MySQL 10.4.32 or later
3. Android Studio for building the app
4. Postman for API testing

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/1ndrajeet/Bus-Connect
   ```

2. Import the `bus.sql` file into your MySQL database.
   ```bash
   mysql -u root -p bus < bus.sql
   ```

3. Configure the backend:
   - Update `conn.php` with your database credentials:
     ```php
     $host = 'localhost';
     $db = 'bus';
     $user = '<your_username>';
     $pass = '<your_password>';
     ```
   - By default user is `root` and password is ``

4. Deploy the backend code to a local or live server.

5. Open the Android project in Android Studio, configure the base URL in the app's API service class, and build the application.

## API Endpoints
### User APIs
- **Sign Up:**
  ```bash
  POST /signup.php
  {
      "name": "John Doe",
      "email": "john@example.com",
      "number": "1234567890",
      "password": "securepassword"
  }
  ```

- **Login:**
  ```bash
  POST /login.php
  {
      "email": "john@example.com",
      "password": "securepassword"
  }
  ```

- **Request Pass:**
  ```bash
  POST /new_pass.php
  {
      "user_id": 6,
      "from": "City A",
      "to": "City B",
      "duration": 1,
      "paymentMode": "Credit Card"
  }
  ```

- **Retrieve User Passes:**
  ```bash
  GET /all_passes.php?user_id=6
  ```

### Admin APIs
- **Get All Requests:**
  ```bash
  GET /admin_pass.php
  ```

- **Approve or Reject a Request:**
  ```bash
  POST /admin_pass.php
  {
      "request_id": 2,
      "action": "approve"
  }
  ```

- **Get User Details:**
  ```bash
  GET /admin_users.php?user_id=6
  ```

## Database Schema
### `users` Table
| Column     | Type        | Description           |
|------------|-------------|-----------------------|
| id         | int(11)     | Primary Key           |
| name       | varchar(255)| User's full name      |
| email      | varchar(255)| Unique email address  |
| number     | varchar(15) | User's phone number   |
| password   | varchar(255)| Hashed password       |
| created_at | timestamp   | Record creation time  |

### `pass_requests` Table
| Column          | Type        | Description                     |
|-----------------|-------------|---------------------------------|
| id              | int(11)     | Primary Key                     |
| user_id         | int(11)     | Foreign Key referencing `users` |
| from_destination| varchar(255)| Starting location               |
| to_destination  | varchar(255)| Destination location            |
| payment_mode    | varchar(50) | Payment method                  |
| status          | enum        | Pass status (pending/approved/rejected) |
| created_at      | timestamp   | Request creation time           |
| expiration_date | date        | Pass expiration date            |

## Developer Information
- **Developer Name:** Omkar

## Future Enhancements
- Add push notifications for pass status updates.
- Implement a feature for users to extend pass duration.
- Integrate payment gateway for direct payments.

---
