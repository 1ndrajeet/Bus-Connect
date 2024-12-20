<?php
include 'conn.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = file_get_contents("php://input");
    $data = json_decode($input, true);

    $email = $data['email'];
    $password = $data['password'];

    // Prepare statement to get user information
    $stmt = $conn->prepare("SELECT id, name, email, number, password FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($id, $name, $email, $number, $hashedPassword);
        $stmt->fetch();

        if (password_verify($password, $hashedPassword)) {
            echo json_encode([
                "status" => "success",
                "message" => "Login successful",
                "userinfo" => [
                    "id" => $id,
                    "name" => $name,
                    "email" => $email,
                    "number" => $number
                ]
            ]);
        } else {
            echo json_encode(["status" => "error", "message" => "Invalid password"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "User not found"]);
    }

    $stmt->close();
}

closeConnection($conn);
?>
