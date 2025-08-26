$baseUrl = "http://localhost:8080"
$customerId = "CUST001"

Write-Host "Brokerage API Test Baslatiliyor" -ForegroundColor Cyan
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host "Customer ID: $customerId" -ForegroundColor Yellow
Write-Host ""

Write-Host "1. Login testi..." -ForegroundColor Green
$loginData = @{
    username = "customer1"
    password = "password123"
} | ConvertTo-Json

try {
    $loginResult = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    $token = $loginResult.token
    Write-Host "   Login basarili" -ForegroundColor Green
    Write-Host "   Token: $($token.Substring(0,25))..." -ForegroundColor Gray
} catch {
    Write-Host "   Login hatasi: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n2. Varlık listesi testi..." -ForegroundColor Green
$authHeaders = @{
    "Authorization" = "Bearer $token"
}

try {
    $assets = Invoke-RestMethod -Uri "$baseUrl/api/assets?customerId=$customerId" -Method GET -Headers $authHeaders
    Write-Host "   Varliklar listelendi" -ForegroundColor Green
    Write-Host "   Varlik sayisi: $($assets.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   Varlik listesi hatasi: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n3. Alım emri testi..." -ForegroundColor Green
$buyOrderData = @{
    customerId = $customerId
    asset = "AAPL"
    side = "BUY"
    size = 3
    price = 150.50
} | ConvertTo-Json

$fullHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}

try {
    $buyOrder = Invoke-RestMethod -Uri "$baseUrl/api/orders" -Method POST -Body $buyOrderData -Headers $fullHeaders
    Write-Host "   Alim emri olusturuldu" -ForegroundColor Green
    Write-Host "   Emir ID: $($buyOrder.id)" -ForegroundColor Gray
    Write-Host "   Durum: $($buyOrder.status)" -ForegroundColor Gray
} catch {
    Write-Host "   Alim emri hatasi: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n4. Emir listesi testi..." -ForegroundColor Green
try {
    $orders = Invoke-RestMethod -Uri "$baseUrl/api/orders?customerId=$customerId" -Method GET -Headers $authHeaders
    Write-Host "   Emirler listelendi" -ForegroundColor Green
    Write-Host "   Emir sayisi: $($orders.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   Emir listesi hatasi: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n5. Satım emri testi..." -ForegroundColor Green
$sellOrderData = @{
    customerId = $customerId
    asset = "AAPL"
    side = "SELL"
    size = 1
    price = 155.00
} | ConvertTo-Json

try {
    $sellOrder = Invoke-RestMethod -Uri "$baseUrl/api/orders" -Method POST -Body $sellOrderData -Headers $fullHeaders
    Write-Host "   Satim emri olusturuldu" -ForegroundColor Green
    Write-Host "   Emir ID: $($sellOrder.id)" -ForegroundColor Gray
} catch {
    Write-Host "   Satim emri hatasi: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n6. Güncel varlık kontrolü..." -ForegroundColor Green
try {
    $updatedAssets = Invoke-RestMethod -Uri "$baseUrl/api/assets?customerId=$customerId" -Method GET -Headers $authHeaders
    Write-Host "   Guncel varliklar alindi" -ForegroundColor Green
    
    foreach ($asset in $updatedAssets) {
        Write-Host "   $($asset.assetName): $($asset.usableSize) kullanilabilir" -ForegroundColor Gray
    }
} catch {
    Write-Host "   Varlik kontrolu hatasi: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest Tamamlandi" -ForegroundColor Cyan
Write-Host "Tum testler calistirildi." -ForegroundColor Yellow
