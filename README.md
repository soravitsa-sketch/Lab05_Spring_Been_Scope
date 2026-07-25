# Coffee API — Spring Boot Demo

REST API ง่าย ๆ สำหรับจัดการรายการกาแฟ (CRUD) เขียนด้วย Spring Boot
ข้อมูลเก็บอยู่ใน memory (`ArrayList`) เท่านั้น — ข้อมูลจะหายทุกครั้งที่ restart แอป

## Tech Stack

| รายการ | เวอร์ชัน |
| --- | --- |
| Java | 26 |
| Spring Boot | 4.1.0 |
| Build tool | Maven (มี Maven Wrapper ให้แล้ว) |
| Starter | `spring-boot-starter-webmvc` |

## โครงสร้างโปรเจกต์

```
Spring/
├── README.md
└── demo/                       <- Maven project root (pom.xml อยู่ที่นี่)
    ├── mvnw / mvnw.cmd
    ├── pom.xml
    └── src/
        ├── main/java/com/example/demo/
        │   ├── DemoApplication.java
        │   ├── controller/CoffeeAPIController.java
        │   ├── model/Coffee.java
        │   └── service/CoffeeService.java
        └── test/java/com/example/demo/
            └── DemoApplicationTests.java
```

## วิธีรันโปรเจกต์ (Maven)

### สิ่งที่ต้องมีก่อน

- **JDK 26** (โปรเจกต์ตั้ง `java.version` เป็น 26 ใน `pom.xml`)
- ไม่ต้องติดตั้ง Maven เอง — ใช้ Maven Wrapper (`mvnw` / `mvnw.cmd`) ที่มากับโปรเจกต์ได้เลย

เช็กเวอร์ชัน Java — PowerShell:

```powershell
java -version
$env:JAVA_HOME
```

bash:

```bash
java -version
echo $JAVA_HOME
```

> คำสั่งทั้งหมดต้องรันจากโฟลเดอร์ `demo/` เพราะ `pom.xml` อยู่ที่นั่น

PowerShell:

```powershell
Set-Location demo
```

bash:

```bash
cd demo
```

### 1. รันแอป (โหมด development)

PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

bash (macOS / Linux / Git Bash):

```bash
./mvnw spring-boot:run
```

ถ้าติดตั้ง Maven ไว้ในเครื่องอยู่แล้วก็ใช้ `mvn spring-boot:run` ได้เช่นกัน

แอปจะรันที่ **http://localhost:8080** — กด `Ctrl+C` เพื่อหยุด

### 2. Build เป็น JAR แล้วรัน

PowerShell:

```powershell
.\mvnw.cmd clean package
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

bash:

```bash
./mvnw clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

ข้าม test ตอน build:

```powershell
.\mvnw.cmd clean package -DskipTests
```

### 3. รันเทส

PowerShell:

```powershell
.\mvnw.cmd test
```

bash:

```bash
./mvnw test
```

### 4. เปลี่ยน port (ถ้า 8080 ชนกับโปรแกรมอื่น)

แก้ `src/main/resources/application.properties`:

```properties
server.port=9090
```

หรือส่งตอนรัน — PowerShell:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=9090"
```

bash:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

> ใน PowerShell ต้องครอบ argument ที่มี `--` ด้วย double quote ไม่งั้นจะโดน PowerShell ตีความเป็น operator

เช็กว่ามีอะไรจอง port 8080 อยู่ (PowerShell):

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen | Select-Object OwningProcess
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080 -State Listen).OwningProcess
```

### 5. คำสั่ง Maven อื่น ๆ ที่ใช้บ่อย

| คำสั่ง (PowerShell) | คำอธิบาย |
| --- | --- |
| `.\mvnw.cmd clean` | ลบโฟลเดอร์ `target/` |
| `.\mvnw.cmd compile` | คอมไพล์อย่างเดียว |
| `.\mvnw.cmd dependency:tree` | ดู dependency ทั้งหมด |
| `.\mvnw.cmd -v` | เช็กเวอร์ชัน Maven / Java ที่ใช้อยู่ |

## Data Model

```json
{
  "id": 1,
  "name": "Espresso",
  "price": 45.0
}
```

| Field | Type | หมายเหตุ |
| --- | --- | --- |
| `id` | `Long` | ระบบสร้างให้อัตโนมัติตอน POST (ไม่ต้องส่งมา) |
| `name` | `String` | ชื่อกาแฟ |
| `price` | `double` | ราคา |

ข้อมูลเริ่มต้นตอนแอปสตาร์ต:

| id | name | price |
| --- | --- | --- |
| 1 | Espresso | 45.0 |
| 2 | Latte | 55.0 |

## API Endpoints

Base URL: `http://localhost:8080/coffees`

| Method | Path | คำอธิบาย | Success | Error |
| --- | --- | --- | --- | --- |
| GET | `/coffees` | ดึงรายการกาแฟทั้งหมด | `200 OK` | — |
| GET | `/coffees/{id}` | ดึงกาแฟตาม id | `200 OK` | `404 Not Found` |
| POST | `/coffees` | เพิ่มกาแฟใหม่ | `201 Created` | — |
| PUT | `/coffees/{id}` | แก้ไขกาแฟตาม id | `200 OK` | `404 Not Found` |
| DELETE | `/coffees/{id}` | ลบกาแฟตาม id | `204 No Content` | `404 Not Found` |

> **หมายเหตุสำหรับ PowerShell:** ใน Windows PowerShell `curl` เป็น alias ของ `Invoke-WebRequest`
> ซึ่งใช้ flag แบบ `-X` `-d` ไม่ได้ ต้องเรียก **`curl.exe`** ให้ชัดเจนเสมอ
> (หรือใช้ `Invoke-RestMethod` แบบ native ตามตัวอย่างที่ให้ไว้ในแต่ละข้อ)

---

### 1. GET /coffees — ดึงทั้งหมด

bash:

```bash
curl -i http://localhost:8080/coffees
```

PowerShell:

```powershell
curl.exe -i http://localhost:8080/coffees
```

PowerShell แบบ native (ได้ object กลับมาเลย):

```powershell
Invoke-RestMethod -Uri http://localhost:8080/coffees | Format-Table
```

Response `200 OK`:

```json
[
  { "id": 1, "name": "Espresso", "price": 45.0 },
  { "id": 2, "name": "Latte", "price": 55.0 }
]
```

---

### 2. GET /coffees/{id} — ดึงตาม id

bash:

```bash
curl -i http://localhost:8080/coffees/1
```

PowerShell:

```powershell
curl.exe -i http://localhost:8080/coffees/1
```

PowerShell แบบ native:

```powershell
Invoke-RestMethod -Uri http://localhost:8080/coffees/1
```

Response `200 OK`:

```json
{ "id": 1, "name": "Espresso", "price": 45.0 }
```

กรณีไม่พบ (`404 Not Found`, body ว่าง):

```bash
curl -i http://localhost:8080/coffees/999
```

```powershell
curl.exe -i http://localhost:8080/coffees/999
```

> `Invoke-RestMethod` จะ throw exception เมื่อเจอ 404 ถ้าอยากเห็น status code เฉย ๆ ให้ใช้ `curl.exe -i`
> หรือครอบด้วย `try { ... } catch { $_.Exception.Response.StatusCode }`

---

### 3. POST /coffees — เพิ่มกาแฟใหม่

bash:

```bash
curl -i -X POST http://localhost:8080/coffees \
  -H "Content-Type: application/json" \
  -d '{"name":"Cappuccino","price":60.0}'
```

PowerShell (ต้อง escape double quote ใน JSON):

```powershell
curl.exe -i -X POST http://localhost:8080/coffees `
  -H "Content-Type: application/json" `
  -d '{\"name\":\"Cappuccino\",\"price\":60.0}'
```

PowerShell แบบ native (อ่านง่ายกว่า ไม่ต้อง escape):

```powershell
$body = @{ name = "Cappuccino"; price = 60.0 } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/coffees `
  -ContentType "application/json" -Body $body
```

Response `201 Created`:

```json
{ "id": 3, "name": "Cappuccino", "price": 60.0 }
```

> `id` ที่ส่งมาใน body จะถูกเขียนทับด้วยค่าที่ระบบสร้างให้เสมอ (เริ่มที่ 3)

---

### 4. PUT /coffees/{id} — แก้ไข

bash:

```bash
curl -i -X PUT http://localhost:8080/coffees/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Espresso Doppio","price":65.0}'
```

PowerShell:

```powershell
curl.exe -i -X PUT http://localhost:8080/coffees/1 `
  -H "Content-Type: application/json" `
  -d '{\"name\":\"Espresso Doppio\",\"price\":65.0}'
```

PowerShell แบบ native:

```powershell
$body = @{ name = "Espresso Doppio"; price = 65.0 } | ConvertTo-Json
Invoke-RestMethod -Method Put -Uri http://localhost:8080/coffees/1 `
  -ContentType "application/json" -Body $body
```

Response `200 OK`:

```json
{ "id": 1, "name": "Espresso Doppio", "price": 65.0 }
```

กรณีไม่พบ (`404 Not Found`):

```bash
curl -i -X PUT http://localhost:8080/coffees/999 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ghost","price":0.0}'
```

```powershell
curl.exe -i -X PUT http://localhost:8080/coffees/999 `
  -H "Content-Type: application/json" `
  -d '{\"name\":\"Ghost\",\"price\":0.0}'
```

---

### 5. DELETE /coffees/{id} — ลบ

bash:

```bash
curl -i -X DELETE http://localhost:8080/coffees/2
```

PowerShell:

```powershell
curl.exe -i -X DELETE http://localhost:8080/coffees/2
```

PowerShell แบบ native:

```powershell
Invoke-RestMethod -Method Delete -Uri http://localhost:8080/coffees/2
```

Response `204 No Content` (ไม่มี body)

กรณีไม่พบ (`404 Not Found`):

```bash
curl -i -X DELETE http://localhost:8080/coffees/999
```

```powershell
curl.exe -i -X DELETE http://localhost:8080/coffees/999
```

---

## ทดลองครบทุก endpoint รวดเดียว

bash:

```bash
BASE=http://localhost:8080/coffees

curl -s $BASE                                   # อ่านทั้งหมด
curl -s $BASE/1                                 # อ่านตาม id
curl -s -X POST $BASE -H "Content-Type: application/json" \
     -d '{"name":"Mocha","price":70.0}'         # สร้าง -> ได้ id 3
curl -s -X PUT $BASE/3 -H "Content-Type: application/json" \
     -d '{"name":"Mocha Hot","price":75.0}'     # แก้ไข id 3
curl -i -X DELETE $BASE/3                       # ลบ id 3
curl -s $BASE                                   # ตรวจผลลัพธ์
```

PowerShell:

```powershell
$base = "http://localhost:8080/coffees"

Invoke-RestMethod -Uri $base | Format-Table                    # อ่านทั้งหมด
Invoke-RestMethod -Uri "$base/1"                               # อ่านตาม id

$new = Invoke-RestMethod -Method Post -Uri $base `
    -ContentType "application/json" `
    -Body (@{ name = "Mocha"; price = 70.0 } | ConvertTo-Json) # สร้าง -> ได้ id 3

Invoke-RestMethod -Method Put -Uri "$base/$($new.id)" `
    -ContentType "application/json" `
    -Body (@{ name = "Mocha Hot"; price = 75.0 } | ConvertTo-Json)

Invoke-RestMethod -Method Delete -Uri "$base/$($new.id)"       # ลบ
Invoke-RestMethod -Uri $base | Format-Table                    # ตรวจผลลัพธ์
```

## ข้อจำกัดที่ควรรู้

- ข้อมูลเก็บใน memory ไม่มี database — restart แล้วกลับไปเป็นค่าเริ่มต้น
- ยังไม่มี validation ของ request body (ส่ง `name` ว่างหรือ `price` ติดลบก็ผ่าน)
- `nextId` เริ่มที่ 3 แบบ hardcode และไม่ thread-safe
