  # Brokerage Emir Yönetim Sistemi

  Bu proje, aracı firmaların müşterileri adına hisse senedi emirlerini yönetebilmesi için geliştirilmiş bir Spring Boot API'sidir. Sistem, emir oluşturma, listeleme, iptal etme ve varlık yönetimi işlevlerini sağlar.

  ##  Özellikler

  - **Emir Yönetimi**: Alım/satım emirleri oluşturma, listeleme ve iptal etme
  - **Varlık Takibi**: Müşteri varlıklarının otomatik güncellenmesi
  - **Güvenlik**: JWT tabanlı kimlik doğrulama ve yetkilendirme
  - **Müşteri Erişimi**: Her müşteri sadece kendi verilerine erişebilir
  - **Admin Paneli**: Admin kullanıcılar tüm emirleri görüntüleyebilir ve eşleştirebilir
  - **Swagger Dökümantasyonu**: API endpoint'lerinin otomatik dökümantasyonu

  ##  Teknolojiler

  - **Java 21** - Ana programlama dili
  - **Spring Boot 3.2.0** - Web framework
  - **Spring Security** - Güvenlik katmanı
  - **Spring Data JPA** - Veritabanı erişimi
  - **H2 Database** - Geliştirme veritabanı
  - **Maven** - Bağımlılık yönetimi
  - **Swagger/OpenAPI** - API dökümantasyonu

  ##  Gereksinimler

  - Java 21 JDK
  - Maven 3.6+
  - Git

  ##  Kurulum

  ### 1. Projeyi İndirin
  ```bash
  git clone https://github.com/furkanorta/brokerage-order-management.git
  cd brokerage-order-management
  ```

  ### 2. Bağımlılıkları Yükleyin
  ```bash
  $env:JAVA_HOME = "C:\Users\furkan\Downloads\OpenJDK21U-jdk_x64_windows_hotspot_21.0.6_7\jdk-21.0.6+7"
  .\mvnw.cmd clean install
  ```

  ### 3. Uygulamayı Çalıştırın
  ```bash
  .\mvnw.cmd spring-boot:run
  ```

  Uygulama varsayılan olarak `http://localhost:8080` adresinde çalışacaktır.

  ##  Erişim Noktaları

  - **API Base URL**: http://localhost:8080
  - **Swagger UI**: http://localhost:8080/swagger-ui/index.html
  - **H2 Console**: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
    - Username: `sa`
    - Password: `password`

  ##  Veritabanı Şeması

  ### Asset Tablosu
  - `customerId` - Müşteri kimliği
  - `assetName` - Varlık adı (TRY, AAPL, GOOGL vb.)
  - `size` - Toplam varlık miktarı
  - `usableSize` - Kullanılabilir varlık miktarı

  ### Order Tablosu
  - `customerId` - Müşteri kimliği
  - `assetName` - İşlem yapılacak varlık
  - `orderSide` - BUY veya SELL
  - `size` - İşlem miktarı
  - `price` - Birim fiyat
  - `status` - PENDING, MATCHED, CANCELED
  - `createDate` - Oluşturulma tarihi

  ##  Test Verileri

  Sistem başlatıldığında otomatik olarak aşağıdaki test verileri oluşturulur:

  ### Müşteriler
  - **customer1** / password123 (CUST001)
  - **customer2** / password123 (CUST002)

  ### Başlangıç Varlıkları
  - **CUST001**: 10,000 TRY, 100 AAPL
  - **CUST002**: 5,000 TRY, 50 GOOGL

  ##  API Kullanımı

  ### 1. Kimlik Doğrulama

  Önce giriş yaparak JWT token alın:

  #### Bash/Linux/Mac:
  ```bash
  curl -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{
      "username": "customer1",
      "password": "password123"
    }'
  ```

  #### PowerShell (Windows):
  ```powershell
  $loginBody = @{
      username = "customer1"
      password = "password123"
  } | ConvertTo-Json

  $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
  $token = $response.token
  Write-Host "JWT Token: $token"
  ```

  ### 2. Emir Oluşturma

  #### Bash/Linux/Mac:
  ```bash
  curl -X POST http://localhost:8080/api/orders \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer <JWT_TOKEN>" \
    -d '{
      "customerId": "CUST001",
      "asset": "AAPL",
      "side": "BUY",
      "size": 10,
      "price": 150.00
    }'
  ```

  #### PowerShell (Windows):
  ```powershell
  $orderBody = @{
      customerId = "CUST001"
      asset = "AAPL"
      side = "BUY"
      size = 10
      price = 150.00
  } | ConvertTo-Json

  $headers = @{
      "Content-Type" = "application/json"
      "Authorization" = "Bearer $token"
  }

  $response = Invoke-RestMethod -Uri "http://localhost:8080/api/orders" -Method POST -Body $orderBody -Headers $headers
  $response | ConvertTo-Json -Depth 10
  ```

  ### 3. Emirleri Listeleme

  #### Bash/Linux/Mac:
  ```bash
  curl -X GET "http://localhost:8080/api/orders?customerId=CUST001" \
    -H "Authorization: Bearer <JWT_TOKEN>"
  ```

  #### PowerShell (Windows):
  ```powershell
  $headers = @{
      "Authorization" = "Bearer $token"
  }

  $response = Invoke-RestMethod -Uri "http://localhost:8080/api/orders?customerId=CUST001" -Method GET -Headers $headers
  $response | ConvertTo-Json -Depth 10
  ```

  ### 4. Emir İptal Etme

  #### Bash/Linux/Mac:
  ```bash
  curl -X DELETE http://localhost:8080/api/orders/1 \
    -H "Authorization: Bearer <JWT_TOKEN>"
  ```

  #### PowerShell (Windows):
  ```powershell
  $headers = @{
      "Authorization" = "Bearer $token"
  }

  $response = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/1" -Method DELETE -Headers $headers
  Write-Host "Response: $response"
  ```

  ### 5. Varlıkları Listeleme

  #### Bash/Linux/Mac:
  ```bash
  curl -X GET "http://localhost:8080/api/assets?customerId=CUST001" \
    -H "Authorization: Bearer <JWT_TOKEN>"
  ```

  #### PowerShell (Windows):
  ```powershell
  $headers = @{
      "Authorization" = "Bearer $token"
  }

  $response = Invoke-RestMethod -Uri "http://localhost:8080/api/assets?customerId=CUST001" -Method GET -Headers $headers
  $response | ConvertTo-Json -Depth 10
  ```

  ##  İş Kuralları

  1. **Emir Oluşturma**:
    - Alım emirleri için yeterli TRY bakiyesi gerekli
    - Satım emirleri için yeterli varlık miktarı gerekli
    - Emirler PENDING durumunda oluşturulur

  2. **Emir İptal**:
    - Sadece PENDING durumundaki emirler iptal edilebilir
    - İptal edilen emirler için kullanılan bakiye geri yüklenir

  3. **Emir Eşleştirme** (Admin):
    - Admin kullanıcılar emirleri eşleştirebilir
    - Eşleştirilen emirler MATCHED durumuna geçer
    - Varlık miktarları otomatik güncellenir

  ##  Test

  Unit testleri çalıştırmak için:

  ```bash
  .\mvnw.cmd test
  ```


  ##  Notlar

  - Sistem H2 in-memory veritabanı kullanır (geliştirme için)
  - Production ortamında PostgreSQL gibi kalıcı bir veritabanı kullanılmalı
  - JWT token'lar 24 saat geçerlidir
  - Tüm para birimi işlemleri BigDecimal ile yapılır

##  Hazır PowerShell Script'leri

Projede `scripts/` klasöründe kullanıma hazır PowerShell script'leri bulunmaktadır:

### Hızlı Başlangıç
```powershell
# Script'leri çalıştırma izni ver
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Scripts klasörüne git
cd scripts

# Hızlı test yap
.\quick-test.ps1
```

### Mevcut Script'ler
- `login.ps1` - Sadece login
- `test-api.ps1` - Kapsamlı test
- `create-order.ps1` - Emir oluşturma
- `list-assets.ps1` - Varlık listeleme
- `cancel-order.ps1` - Emir iptal etme
- `quick-test.ps1` - Hızlı test

Detaylı kullanım için `scripts/README.md` dosyasını inceleyin.

