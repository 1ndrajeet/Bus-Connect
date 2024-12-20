<?php
include 'conn.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = file_get_contents("php://input");
    $data = json_decode($input, true);

    if (!isset($data['user_id']) || !isset($data['from']) || !isset($data['to']) || !isset($data['duration'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit();
    }

    $userId = $data['user_id'];
    $from = $data['from'];
    $to = $data['to'];
    $paymentMode = $data['paymentMode'];
    $durationMonths = $data['duration'];
    
    $status = 'pending';  
    $createdAt = date('Y-m-d H:i:s'); 
    $expirationDate = date('Y-m-d', strtotime("+$durationMonths months"));

    $stmt = $conn->prepare("INSERT INTO pass_requests (user_id, from_destination, to_destination, payment_mode, status, created_at, expiration_date) VALUES (?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("issssss", $userId, $from, $to, $paymentMode, $status, $createdAt, $expirationDate);

    try {
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Pass request submitted successfully"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to submit pass request"]);
        }
    } catch (mysqli_sql_exception $e) {
        echo json_encode(["status" => "error", "message" => "Database error", "error" => $e->getMessage()]);
    }

    $stmt->close();
}

closeConnection($conn);
?>
