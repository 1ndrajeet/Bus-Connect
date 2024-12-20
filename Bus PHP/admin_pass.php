<?php
include 'conn.php';
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $stmt = $conn->prepare("
        SELECT 
            pr.id, 
            pr.user_id, 
            u.name AS user_name, 
            pr.from_destination, 
            pr.to_destination, 
            pr.payment_mode, 
            pr.status, 
            pr.created_at, 
            pr.expiration_date 
        FROM 
            pass_requests pr
        JOIN 
            users u ON pr.user_id = u.id
    ");
    $stmt->execute();
    $result = $stmt->get_result();

    $requests = [];
    while ($row = $result->fetch_assoc()) {
        $requests[] = $row;
    }

    echo json_encode(["status" => "success", "requests" => $requests]);
    $stmt->close();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = file_get_contents("php://input");
    $data = json_decode($input, true);

    if (!isset($data['request_id']) || !isset($data['action'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit();
    }

    $requestId = $data['request_id'];
    $action = $data['action']; // 'approve' or 'reject'
    $newStatus = $action === 'approve' ? 'approved' : 'rejected';

    $stmt = $conn->prepare("UPDATE pass_requests SET status = ? WHERE id = ?");
    $stmt->bind_param("si", $newStatus, $requestId);

    if ($stmt->execute()) {
        if ($stmt->affected_rows > 0) {
            echo json_encode(["status" => "success", "message" => "Request $newStatus successfully"]);
        } else {
            echo json_encode(["status" => "error", "message" => "No request found with the provided ID"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Failed to update request"]);
    }

    $stmt->close();
}

closeConnection($conn);
?>