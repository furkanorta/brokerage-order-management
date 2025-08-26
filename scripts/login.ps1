$baseUrl = "http://localhost:8080"

Write-Host "Login yapiliyor..." -ForegroundColor Green

$loginData = @{
    username = "customer1"
    password = "password123"
} | ConvertTo-Json

try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    $token = $result.token
    
    Write-Host "Login basarili!" -ForegroundColor Green
    Write-Host "Token: $token" -ForegroundColor Yellow
    
    $global:authToken = $token
    
} catch {
    Write-Host "Login hatasi: $($_.Exception.Message)" -ForegroundColor Red
}
