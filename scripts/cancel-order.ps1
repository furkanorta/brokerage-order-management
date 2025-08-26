param(
    [Parameter(Mandatory=$true)]
    [int]$orderId
)

$baseUrl = "http://localhost:8080"

# Login kontrolü
if (-not $global:authToken) {
    Write-Host "Token bulunamadi, login yapiliyor..." -ForegroundColor Yellow
    & "$PSScriptRoot\login.ps1"
}

if (-not $global:authToken) {
    Write-Host "Login basarisiz!" -ForegroundColor Red
    exit 1
}

Write-Host "Emir iptal ediliyor..." -ForegroundColor Green
Write-Host "Emir ID: $orderId" -ForegroundColor Gray

$headers = @{
    "Authorization" = "Bearer $global:authToken"
}

try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/orders/$orderId" -Method DELETE -Headers $headers
    Write-Host "Emir iptal edildi!" -ForegroundColor Green
    Write-Host "Sonuc: $result" -ForegroundColor Yellow
} catch {
    Write-Host "Emir iptal hatasi: $($_.Exception.Message)" -ForegroundColor Red
}
