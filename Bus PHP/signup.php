<?php
include 'conn.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = file_get_contents("php://input");
    $data = json_decode($input, true);

    $name = $data['name'];
    $email = $data['email'];
    $number = $data['number'];
    $password = password_hash($data['password'], PASSWORD_BCRYPT);

    $sql = "INSERT INTO users (name, email, number, password) VALUES ('$name', '$email', '$number', '$password')";

    try {
        if ($conn->query($sql) === TRUE) {
            echo json_encode(["status" => "success", "message" => "User created successfully"]);
        }
    } catch (mysqli_sql_exception $e) {
        echo json_encode(["status" => "error", "message" => "User registration failed", "error" => $e->getMessage()]);
    }
}

closeConnection($conn);
?>