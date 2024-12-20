<?php
include 'conn.php';

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if (!isset($_GET['user_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing user_id parameter"]);
        exit();
    }

    $userId = $_GET['user_id'];

    $stmt = $conn->prepare("
        SELECT 
            pr.id, 
            pr.from_destination, 
            pr.to_destination, 
            pr.payment_mode, 
            pr.status, 
            pr.created_at, 
            pr.expiration_date, 
            u.name AS user_name 
        FROM 
            pass_requests pr 
        JOIN 
            users u ON pr.user_id = u.id 
        WHERE 
            pr.user_id = ?
    ");
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $passes = [];
        while ($row = $result->fetch_assoc()) {
            $passes[] = $row;
        }
        echo json_encode(["status" => "success", "passes" => $passes]);
    } else {
        echo json_encode(["status" => "error", "message" => "No passes found for this user"]);
    }

    $stmt->close();
}

closeConnection($conn);
?>
