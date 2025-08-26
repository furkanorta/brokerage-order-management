Write-Host "Hizli Test Baslatiliyor" -ForegroundColor Cyan

Write-Host "1. Login..." -ForegroundColor Green
& "$PSScriptRoot\login.ps1"

if (-not $global:authToken) {
    Write-Host "Test durduruldu - Login basarisiz!" -ForegroundColor Red
    exit 1
}

Write-Host "`n2. Varliklar..." -ForegroundColor Green
& "$PSScriptRoot\list-assets.ps1"

Write-Host "`n3. Alim emri..." -ForegroundColor Green
& "$PSScriptRoot\create-order.ps1" -asset "AAPL" -side "BUY" -size 2 -price 150.00

Write-Host "`n4. Guncel varliklar..." -ForegroundColor Green
& "$PSScriptRoot\list-assets.ps1"

Write-Host "`nHizli Test Tamamlandi" -ForegroundColor Cyan
