<?php
$host = 'localhost';
$db = 'bus';
$user = 'k1n9';
$pass = 'k1n9';
header('Content-Type: application/json');

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die(json_encode([
        "message" => "Database connection failed",
        "error" => $conn->connect_error
    ]));
}


function closeConnection($conn)
{
    $conn->close();
}
?>