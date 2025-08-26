param(
    [string]$customerId = "CUST001"
)

$baseUrl = "http://localhost:8080"

if (-not $global:authToken) {
    Write-Host "Token bulunamadi, login yapiliyor..." -ForegroundColor Yellow
    & "$PSScriptRoot\login.ps1"
}

if (-not $global:authToken) {
    Write-Host "Login basarisiz!" -ForegroundColor Red
    exit 1
}

Write-Host "Varliklar listeleniyor..." -ForegroundColor Green
Write-Host "Customer ID: $customerId" -ForegroundColor Gray

$headers = @{
    "Authorization" = "Bearer $global:authToken"
}

try {
    $assets = Invoke-RestMethod -Uri "$baseUrl/api/assets?customerId=$customerId" -Method GET -Headers $headers
    
    Write-Host "`nVarlik Listesi:" -ForegroundColor Cyan
    Write-Host "==============" -ForegroundColor Cyan
    
    if ($assets.Count -eq 0) {
        Write-Host "Varlik bulunamadi." -ForegroundColor Yellow
    } else {
        foreach ($asset in $assets) {
            Write-Host "Asset: $($asset.assetName)" -ForegroundColor White
            Write-Host "  Toplam: $($asset.size)" -ForegroundColor Gray
            Write-Host "  Kullanilabilir: $($asset.usableSize)" -ForegroundColor Gray
            Write-Host ""
        }
    }
    
} catch {
    Write-Host "Varlik listesi hatasi: $($_.Exception.Message)" -ForegroundColor Red
}
