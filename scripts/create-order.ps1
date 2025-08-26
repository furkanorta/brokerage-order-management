param(
    [string]$asset = "AAPL",
    [string]$side = "BUY",
    [int]$size = 5,
    [decimal]$price = 150.00,
    [string]$customerId = "CUST001"
)

$baseUrl = "http://localhost:8080"

Write-Host "Login kontrolu..." -ForegroundColor Yellow
if (-not $global:authToken) {
    Write-Host "Token bulunamadi, login yapiliyor..." -ForegroundColor Yellow
    & "$PSScriptRoot\login.ps1"
}

if (-not $global:authToken) {
    Write-Host "Login basarisiz!" -ForegroundColor Red
    exit 1
}

Write-Host "Emir olusturuluyor..." -ForegroundColor Green
Write-Host "Asset: $asset" -ForegroundColor Gray
Write-Host "Side: $side" -ForegroundColor Gray
Write-Host "Size: $size" -ForegroundColor Gray
Write-Host "Price: $price" -ForegroundColor Gray

$orderData = @{
    customerId = $customerId
    asset = $asset
    side = $side
    size = $size
    price = $price
} | ConvertTo-Json

$headers = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $global:authToken"
}

try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/orders" -Method POST -Body $orderData -Headers $headers
    Write-Host "Emir olusturuldu!" -ForegroundColor Green
    Write-Host "Emir ID: $($result.id)" -ForegroundColor Yellow
    Write-Host "Durum: $($result.status)" -ForegroundColor Yellow
} catch {
    Write-Host "Emir olusturma hatasi: $($_.Exception.Message)" -ForegroundColor Red
}
