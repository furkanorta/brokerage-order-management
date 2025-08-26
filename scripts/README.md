# PowerShell Script'leri

Bu klasörde API'yi test etmek için kullanabileceğiniz PowerShell script'leri bulunmaktadır.

## Script Listesi

### 1. `login.ps1`
- Sadece login yapar
- Token'ı global değişkene atar
- Kullanım: `.\login.ps1`

### 2. `test-api.ps1`
- Tüm API işlemlerini test eder
- Kapsamlı test senaryosu
- Kullanım: `.\test-api.ps1`

### 3. `create-order.ps1`
- Emir oluşturur
- Parametreler: asset, side, size, price, customerId
- Kullanım: `.\create-order.ps1 -asset "AAPL" -side "BUY" -size 5 -price 150.00`

### 4. `list-assets.ps1`
- Varlıkları listeler
- Parametre: customerId
- Kullanım: `.\list-assets.ps1 -customerId "CUST001"`

### 5. `cancel-order.ps1`
- Emir iptal eder
- Parametre: orderId (zorunlu)
- Kullanım: `.\cancel-order.ps1 -orderId 1`

### 6. `quick-test.ps1`
- Hızlı test yapar
- Temel işlemleri sırayla çalıştırır
- Kullanım: `.\quick-test.ps1`

## Kullanım Örnekleri

### Basit Test
```powershell
# Hızlı test
.\quick-test.ps1
```

### Manuel Test
```powershell
# Login
.\login.ps1

# Varlıkları listele
.\list-assets.ps1

# Alım emri oluştur
.\create-order.ps1 -asset "AAPL" -side "BUY" -size 3 -price 150.00

# Emir iptal et
.\cancel-order.ps1 -orderId 1
```

### Kapsamlı Test
```powershell
# Tüm testleri çalıştır
.\test-api.ps1
```

## Notlar

- Script'ler otomatik olarak login kontrolü yapar
- Token global değişkende saklanır
- Hata durumunda renkli mesajlar gösterilir
- Tüm script'ler aynı dizinde olmalıdır

## Gereksinimler

- Uygulama çalışır durumda olmalı (http://localhost:8080)
- Execution Policy ayarlanmış olmalı

## Execution Policy Ayarı

```powershell
# Script çalıştırma izni ver
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```
