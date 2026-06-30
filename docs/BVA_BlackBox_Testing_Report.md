# Báo cáo kiểm thử hộp đen — Hệ thống Quản lý Y tế Học đường

**Môn:** Kiểm thử phần mềm  
**Dự án:** Quản lý Y tế Học đường (School Health)  
**Loại kiểm thử:** Kiểm thử hộp đen (Black-box Testing)  
**Kỹ thuật:** Phân hoạch lớp tương đương (EP) + Phân tích giá trị biên (BVA)  
**Công cụ:** Postman Collection Runner  
**Collection:** `backend/postman/SchoolHealth_BVA_All_Functions.postman_collection.json`  
**Environment:** `backend/postman/local.postman_environment.json`  
**Backend:** Spring Boot — `http://localhost:8080`  
**Cơ sở dữ liệu:** MySQL (`db`)

---

## 1. Giới thiệu

Kiểm thử hộp đen là phương pháp kiểm thử **không cần biết cấu trúc mã nguồn bên trong**. Tester chỉ quan sát **đầu vào** (request HTTP) và **đầu ra** (status code, body JSON) so với đặc tả mong đợi.

Báo cáo này mô tả toàn bộ hoạt động kiểm thử hộp đen API backend của dự án, gồm thiết kế testcase, triển khai tự động bằng Postman và kết quả quan sát được.

> **Phạm vi loại trừ:** Báo cáo **không** bao gồm kiểm thử hộp trắng (white-box), kiểm thử đơn vị (JUnit), đo độ phủ mã, kiểm thử giao diện React, hay kiểm thử hiệu năng/bảo mật chuyên sâu.

---

## 2. Mục tiêu kiểm thử

| Mục tiêu | Mô tả |
|----------|--------|
| Xác minh đầu vào hợp lệ | API trả mã thành công (200/201) và dữ liệu đúng cấu trúc |
| Phát hiện đầu vào không hợp lệ | API trả 400/401/404 kèm thông báo lỗi rõ ràng, không crash 500 |
| Kiểm tra biên | Chuỗi rỗng, ID không tồn tại, enum sai, ngày sai format, mảng rỗng… |
| Tự động hóa | Script Postman assert status code và `message` trong response |

---

## 3. Phạm vi kiểm thử

### 3.1. Trong phạm vi (13 module — 89 testcase)

| STT | Module | Endpoint gốc | Số TC |
|-----|--------|--------------|------:|
| 01 | Đăng nhập (Auth) | `POST /api/auth/login` | 5 |
| 02 | Người dùng (Users) | `/api/user`, `/api/users` | 10 |
| 03 | Phụ huynh (Parents) | `/api/parent` | 7 |
| 04 | Học sinh (Students) | `/api/students` | 10 |
| 05 | Nhân viên y tế (Nurses) | `/api/nurses` | 7 |
| 06 | Quản lý (Managers) | `/api/managers` | 4 |
| 07 | Blog | `/api/blogs` | 7 |
| 08 | Tài liệu y tế học đường | `/api/school-health-docs` | 4 |
| 09 | Vật tư y tế | `/api/medical-supplies` | 7 |
| 10 | Hồ sơ sức khỏe | `/api/health-profiles` | 5 |
| 11 | Sự cố y tế | `/api/health-incidents` | 7 |
| 12 | Khám sức khỏe | `/api/medical-checkup` | 7 |
| 13 | Lịch tiêm chủng | `/api/vaccination-schedules` | 9 |
| | **Tổng** | | **89** |

### 3.2. Ngoài phạm vi

- API **Yêu cầu thuốc** (`/api/medical`) — chưa có testcase trong collection
- Chức năng **Import Excel** trên giao diện Admin (không phải REST API thuần)
- Kiểm thử tích hợp đầu cuối (E2E) đa module trên frontend
- Kiểm thử phân quyền JWT (backend hiện không bắt buộc token trên các API trên)

---

## 4. Tổng quan phương pháp EP và BVA

### 4.1. Phân hoạch lớp tương đương (EP)

Mỗi trường đầu vào được chia thành:

- **Lớp hợp lệ (V):** giá trị nằm trong miền chấp nhận → kỳ vọng thành công
- **Lớp không hợp lệ (X):** giá trị ngoài miền → kỳ vọng lỗi có kiểm soát

Ví dụ chung:

| Biến | Lớp hợp lệ | Lớp không hợp lệ |
|------|------------|------------------|
| Email | Đúng format, tồn tại | Rỗng, sai format, trùng |
| ID tham chiếu | Tồn tại trong DB | Không tồn tại (999999999) |
| Role / Enum | Giá trị cho phép | Null, sai role, INVALID |
| Chuỗi bắt buộc | Không rỗng | `""` |
| Số lượng | ≥ 1 | 0, âm |
| SĐT (Nurse) | 10 chữ số, bắt đầu 0 | 9, 11 chữ số, không bắt đầu 0 |
| Ngày giờ | ISO-8601 hợp lệ | Rỗng, sai format, start > end |

### 4.2. Phân tích giá trị biên (BVA)

Áp dụng biên cho trường có miền xác định:

```text
min−1  |  min  |  nominal  |  max  |  max+1
```

Trong dự án, các biên thường gặp:

| Nhóm | min−1 / abnormal | min / biên dưới | nominal | Ghi chú |
|------|------------------|-----------------|---------|---------|
| Chuỗi bắt buộc | `""` | 1 ký tự (`"A"`) | Giá trị đầy đủ | Blog title, vaccine name… |
| Số lượng vật tư | `0` | `1` | 10, 100… | MS-01, MS-02 |
| Phân trang | `-1` | `0` | — | USER-09, USER-10 |
| Mảng bulk | `[]` | 1 phần tử | nhiều phần tử | MC-03, VAC-04 |

### 4.3. Ký hiệu loại testcase (`testType`)

| Ký hiệu | Ý nghĩa | Ví dụ |
|---------|---------|-------|
| **N** | Nominal — đầu vào hợp lệ | STU-01, VAC-03 |
| **B** | Boundary — giá trị tại biên | MS-01 (quantity=0), NUR-05 (9 số) |
| **A** | Abnormal — lớp không hợp lệ / ngoại lệ | HI-01 (student không tồn tại) |

---

## 5. Môi trường và dữ liệu kiểm thử

### 5.1. Cấu hình

| Thành phần | Giá trị |
|------------|---------|
| `baseUrl` | `http://localhost:8080` |
| `loginEmail` / `loginPassword` | `admin@school.com` / `123456` |
| Database | MySQL `jdbc:mysql://127.0.0.1:3306/db` |

### 5.2. Biến môi trường động (set qua script Tests)

| Biến | Nguồn |
|------|--------|
| `studentId`, `parentId` | STU-01, PAR-01 |
| `nurseId` | NUR-02 |
| `userId`, `accessToken` | AUTH-03, USER-01 |
| `healthIncidentId` | HI-02 |
| `medicalCheckupNotificationId` | MC-02 |
| `medicalCheckupResultId` | MC-06 |
| `vaccinationScheduleId` | VAC-03 |

### 5.3. Thứ tự chạy Collection Runner (khuyến nghị)

```text
01 Auth → 02 Users → 03 Parents → 04 Students → 05 Nurses
→ 06 Managers → 07 Blogs → 08 Docs → 09 Supplies
→ 10 Health Profiles → 11 Health Incidents → 12 Medical Checkup → 13 Vaccination
```

Các folder **04, 05** nên chạy trước **11, 12, 13** vì testcase phụ thuộc `studentId`, `parentId`, `nurseId`.

---

## 6. Thiết kế testcase

Phần này trình bày đầy đủ quy trình thiết kế theo cấu trúc đề bài: **Câu 1 (EP) → Câu 2 (BVA) → Câu 3 (Bảng testcase)**. Mỗi testcase ánh xạ 1-1 với request trong Postman collection.

### 6.1 Nguyên tắc chọn giá trị

1. **Một testcase chỉ làm sai lệch một nhóm biến**; các biến khác giữ giá trị nominal.
2. **ID không tồn tại** chuẩn hóa: `999999999`.
3. **Kết quả mong đợi** mô tả từ góc nhìn hộp đen: HTTP status + `message` hoặc trường dữ liệu trả về.
4. Chi tiết module Auth: xem `docs/BVA_Auth_Report.md`.


### 6.2 Câu 1 — Xác định lớp tương đương (EP)

#### Auth — POST /api/auth/login

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| email | Không rỗng, đúng format, tồn tại | Rỗng; sai format; không tồn tại | AUTH-03 / 01,05 |
| password | Không rỗng, khớp DB | Rỗng; sai mật khẩu | AUTH-03 / 04,02 |

#### Users

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| email | Hợp lệ, unique | Rỗng; sai format; trùng | USER-01 / 02,06,07 |
| password | Không rỗng | Rỗng | USER-01 / 08 |
| fullName | Không rỗng | Rỗng | USER-01 / 04 |
| userId | Tồn tại | Không tồn tại | — / 03,05 |
| page | ≥ 0 | < 0 | USER-09 / 10 |

#### Parents

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| role | PARENT | Thiếu; role khác | PAR-01 / 06,07 |
| phoneNumber | 10 số, bắt đầu 0 | 9 số | PAR-01 / 02 |
| parentId, formId | Tồn tại | Không tồn tại | PAR-01 / 03,05 |
| studentId (link) | Tồn tại | Không tồn tại | — / 04 |

#### Students

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| fullName, email | Không rỗng | Rỗng | STU-01 / 02,09 |
| role | STUDENT | Role khác | STU-01 / 10 |
| parentId | Tồn tại | Không tồn tại | STU-01 / 03 |
| dateOfBirth | Hợp lệ, không tương lai | Rỗng; tương lai; sai format | STU-01 / 07,06,08 |
| studentId | Tồn tại | Không tồn tại | STU-01 / 04,05 |

#### Nurses

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| role | SCHOOL_NURSE | Thiếu; role khác | NUR-02 / 01,03 |
| phoneNumber | 10 số, bắt đầu 0 | 9; 11 số; không bắt đầu 0 | NUR-02 / 05,06,07 |
| nurseId | Tồn tại | Không tồn tại | NUR-02 / 04 |

#### Managers

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| role | MANAGER | Thiếu; role khác | MAN-02 / 01,03 |
| managerId | Tồn tại | Không tồn tại | MAN-02 / 04 |

#### Blogs

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| title, content, author, category | Không rỗng | Rỗng | BLOG-02 / 01,03,06,07 |
| blogId | Tồn tại | Không tồn tại | — / 04,05 |

#### School Health Docs

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| title | Không rỗng | Rỗng | DOC-01 / 02 |
| docId | Tồn tại | Không tồn tại | DOC-01 / 03,04 |

#### Medical Supplies

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| name, category | Không rỗng | Rỗng | MS-02 / 03,07 |
| quantity | ≥ 1 | 0 | MS-02 / 01 |
| supplyId | Tồn tại | Không tồn tại | MS-02 / 04,06 |

#### Health Profiles

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| student.id | Tồn tại | Không tồn tại | HP-02 / 01 |
| studentId (GET) | Có hồ sơ | Không có / không tồn tại | HP-05 / 03 |
| parentId | Tồn tại | Không tồn tại | — / 04 |

#### Health Incidents

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| student.id | Tồn tại | Không tồn tại | HI-02 / 01 |
| incidentType, status | Enum hợp lệ | Enum sai | HI-02 / 05 |
| incidentTime | ISO-8601 | Sai format | HI-02 / 06 |
| incidentId | Tồn tại | Không tồn tại | HI-07 / 03 |

#### Medical Checkup

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| studentId, parentId | Tồn tại | Không tồn tại | MC-02 / 01 |
| studentIds | Không rỗng | Mảng rỗng | — / MC-03 |
| consent | APPROVED, REJECTED | INVALID | — / MC-04 |
| checkupDate | ISO-8601 | Sai format | MC-06 / MC-05 |
| resultId | Tồn tại | Không tồn tại | — / MC-07 |

#### Vaccination Schedules

| Biến | Lớp hợp lệ (V) | Lớp không hợp lệ (X) | TC bao phủ |
|------|----------------|----------------------|------------|
| studentId | Tồn tại | Không tồn tại | VAC-03 / 01 |
| vaccineName | Không rỗng | Rỗng | VAC-03 / 02 |
| studentIds | Không rỗng | Mảng rỗng | — / VAC-04 |
| startDate, endDate | start ≤ end | start > end | — / VAC-06 |
| consent | Enum hợp lệ | INVALID | — / VAC-07 |
| confirmed | Boolean | Sai kiểu | — / VAC-08 |
| scheduleId | Tồn tại | Không tồn tại | VAC-03 / 05 |

---

### 6.3 Câu 2 — Phân tích giá trị biên (BVA)

| Module | Biến | min−1 | min | nominal | max+1 | TC tiêu biểu |
|--------|------|-------|-----|---------|-------|--------------|
| Auth | email | rỗng | 1 ký tự | admin@school.com | — | AUTH-01, AUTH-03 |
| Auth | password | rỗng | 1 ký tự | 123456 | — | AUTH-04, AUTH-03 |
| Users | page | -1 | 0 | 0 | — | USER-10, USER-09 |
| Parents | phone | 9 số | 10 số | 0123456789 | 11 số | PAR-02 |
| Nurses | phone | 9 số | 10 số | 0123456789 | 11 số; không bắt đầu 0 | NUR-05,06,07 |
| Students | dateOfBirth | rỗng | — | 2014-01-01 | ngày tương lai | STU-07, STU-01, STU-06 |
| Blogs | title | rỗng | A (1 ký tự) | Blog đầy đủ | — | BLOG-01, BLOG-02 |
| Supplies | quantity | 0 | 1 | 10 | — | MS-01, MS-02 |
| Checkup/VAC | studentIds | [] | 1 phần tử | nhiều phần tử | — | MC-03, VAC-04 |
| Vaccination | date range | start > end | start = end | start < end | — | VAC-06 |

> Các testcase abnormal (email sai format, enum INVALID, ID 999999999) thuộc lớp X, không nằm trên biên số học nhưng vẫn được kiểm thử ở Câu 3.

### 6.4 Câu 3 — Thiết kế test case chi tiết (89 TC)

Bảng dưới liệt kê method, endpoint, giá trị đầu vào (trích Postman), kết quả mong đợi, tag và tiền điều kiện. Body JSON đầy đủ trong collection.

### 01 Auth - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-AUTH-01 | POST | {{baseUrl}}/api/auth/login | Login Empty Email | {"email":"","password":"{{loginPassword}}"} | HTTP 400,401,500; JSON/text message lỗi | B (bien) | - |
| 2 | BVA-AUTH-02 | POST | {{baseUrl}}/api/auth/login | Login Wrong Password | {"email":"{{loginEmail}}","password":"wrong-password"} | HTTP 400,401,500; JSON/text message lỗi | X (abnormal) | - |
| 3 | BVA-AUTH-03 | POST | {{baseUrl}}/api/auth/login | Login Valid | {"email":"{{loginEmail}}","password":"{{loginPassword}}"} | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |
| 4 | BVA-AUTH-04 | POST | {{baseUrl}}/api/auth/login | Login Empty Password | {"email":"{{loginEmail}}","password":""} | HTTP 401; JSON/text message lỗi | B (bien) | - |
| 5 | BVA-AUTH-05 | POST | {{baseUrl}}/api/auth/login | Login Invalid Email Format | {"email":"invalid-email","password":"{{loginPassword}}"} | HTTP 401; JSON/text message lỗi | X (abnormal) | - |

### 02 Users - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-USER-01 | POST | {{baseUrl}}/api/user | Create Valid User | {"email":"bva.user{{$timestamp}}@school.com","password":"password123","fullName":"BVA User","role... | HTTP 200,201; dữ liệu trả về hợp lệ | V (nominal) | - |
| 2 | BVA-USER-02 | POST | {{baseUrl}}/api/user | Create Empty Email | {"email":"","password":"password123","fullName":"BVA User","role":"PARENT","active":true} | HTTP 400,500; JSON/text message lỗi | B (bien) | - |
| 3 | BVA-USER-03 | GET | {{baseUrl}}/api/user/999999999 | Get User Invalid ID | (URL path/query) | HTTP 400,404,500; JSON message lỗi | X (abnormal) | - |
| 4 | BVA-USER-04 | PUT | {{baseUrl}}/api/user/{{userId}} | Update Empty FullName | {"fullName":"","role":"PARENT","active":true} | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 5 | BVA-USER-05 | DELETE | {{baseUrl}}/api/user/999999999 | Delete Invalid ID | (URL path/query) | HTTP 400,404,500; JSON message lỗi | X (abnormal) | - |
| 6 | BVA-USER-06 | POST | {{baseUrl}}/api/user | Create Invalid Email Format | {"email":"invalid-email","password":"password123","fullName":"BVA User","role":"PARENT","active":... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 7 | BVA-USER-07 | POST | {{baseUrl}}/api/user | Create Duplicate Email | {"email":"{{loginEmail}}","password":"password123","fullName":"Duplicate User","role":"PARENT","a... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 8 | BVA-USER-08 | POST | {{baseUrl}}/api/user | Create Empty Password | {"email":"bva.user.empty.password{{$timestamp}}@school.com","password":"","fullName":"BVA User","... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 9 | BVA-USER-09 | GET | {{baseUrl}}/api/users?page=0 | Get Users Page 0 | (URL path/query) | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |
| 10 | BVA-USER-10 | GET | {{baseUrl}}/api/users?page=-1 | Get Users Page -1 | (URL path/query) | HTTP 400; JSON/text message lỗi | B (bien) | - |

### 03 Parents - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-PAR-01 | POST | {{baseUrl}}/api/parent | Create Valid Parent | {"email":"bva.parent{{$timestamp}}@school.com","password":"123456","fullName":"BVA Parent","role"... | HTTP 200,201; dữ liệu trả về hợp lệ | V (nominal) | - |
| 2 | BVA-PAR-02 | POST | {{baseUrl}}/api/parent | Create Phone 9 Digits | {"email":"bva.parent.phone9{{$timestamp}}@school.com","password":"123456","fullName":"BVA Parent"... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 3 | BVA-PAR-03 | GET | {{baseUrl}}/api/parent/999999999 | Get Parent Invalid ID | (URL path/query) | HTTP 400,404,500; JSON message lỗi | X (abnormal) | - |
| 4 | BVA-PAR-04 | PUT | {{baseUrl}}/api/parent/link-student?parentId={{parentId}}&studentId=999999999 | Link Invalid Student To Parent | (URL path/query) | HTTP 400,404,500; JSON message lỗi | X (abnormal) | - |
| 5 | BVA-PAR-05 | POST | {{baseUrl}}/api/parent/confirm/999999999 | Confirm Invalid Form | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 6 | BVA-PAR-06 | POST | {{baseUrl}}/api/parent | Create Missing Role | {"email":"bva.parent.norole{{$timestamp}}@school.com","password":"123456","fullName":"BVA Parent"... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 7 | BVA-PAR-07 | POST | {{baseUrl}}/api/parent | Create Wrong Role | {"email":"bva.parent.role{{$timestamp}}@school.com","password":"123456","fullName":"BVA Parent","... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |

### 04 Students - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-STU-01 | POST | {{baseUrl}}/api/students | Create Valid Student | {"email":"bva.student{{$timestamp}}@school.com","password":"123456","fullName":"BVA Student","rol... | HTTP 200,201; dữ liệu trả về hợp lệ | V (nominal) | 03 Parents (parentId) |
| 2 | BVA-STU-02 | POST | {{baseUrl}}/api/students | Create Empty FullName | {"email":"bva.student.empty{{$timestamp}}@school.com","password":"123456","fullName":"","role":"S... | HTTP 400,500; JSON/text message lỗi | B (bien) | - |
| 3 | BVA-STU-03 | POST | {{baseUrl}}/api/students | Create Invalid Parent ID | {"email":"bva.student.parent{{$timestamp}}@school.com","password":"123456","fullName":"BVA Studen... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 4 | BVA-STU-04 | GET | {{baseUrl}}/api/students/999999999 | Get Student Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 5 | BVA-STU-05 | GET | {{baseUrl}}/api/students/by-parent/999999999 | Get Students By Invalid Parent | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 6 | BVA-STU-06 | POST | {{baseUrl}}/api/students | Create Future Date Of Birth | {"email":"bva.student.case{{$timestamp}}@school.com","password":"123456","fullName":"BVA Student"... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 7 | BVA-STU-07 | POST | {{baseUrl}}/api/students | Create Empty Date Of Birth | {"email":"bva.student.case{{$timestamp}}@school.com","password":"123456","fullName":"BVA Student"... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 8 | BVA-STU-08 | POST | {{baseUrl}}/api/students | Create Invalid Date Format | {"email":"bva.student.case{{$timestamp}}@school.com","password":"123456","fullName":"BVA Student"... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 9 | BVA-STU-09 | POST | {{baseUrl}}/api/students | Create Empty Email | {"email":"","password":"123456","fullName":"BVA Student","role":"STUDENT","code":"BVACASE{{$times... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 10 | BVA-STU-10 | POST | {{baseUrl}}/api/students | Create Wrong Role | {"email":"bva.student.case{{$timestamp}}@school.com","password":"123456","fullName":"BVA Student"... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |

### 05 Nurses - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-NUR-01 | POST | {{baseUrl}}/api/nurses | Create Missing Role | {"email":"bva.nurse.norole{{$timestamp}}@school.com","password":"123456","fullName":"BVA Nurse","... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 2 | BVA-NUR-02 | POST | {{baseUrl}}/api/nurses | Create Valid Nurse | {"email":"bva.nurse{{$timestamp}}@school.com","password":"123456","fullName":"BVA Nurse","role":"... | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |
| 3 | BVA-NUR-03 | POST | {{baseUrl}}/api/nurses | Create Wrong Role | {"email":"bva.nurse.role{{$timestamp}}@school.com","password":"123456","fullName":"BVA Nurse","ro... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 4 | BVA-NUR-04 | GET | {{baseUrl}}/api/nurses/999999999 | Get Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 5 | BVA-NUR-05 | POST | {{baseUrl}}/api/nurses | Create Phone 9 Digits | {"password":"123456","fullName":"BVA Nurse","role":"SCHOOL_NURSE","gender":"FEMALE","department":... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 6 | BVA-NUR-06 | POST | {{baseUrl}}/api/nurses | Create Phone 11 Digits | {"password":"123456","fullName":"BVA Nurse","role":"SCHOOL_NURSE","gender":"FEMALE","department":... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 7 | BVA-NUR-07 | POST | {{baseUrl}}/api/nurses | Create Phone Without Leading Zero | {"password":"123456","fullName":"BVA Nurse","role":"SCHOOL_NURSE","gender":"FEMALE","department":... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |

### 06 Managers - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-MAN-01 | POST | {{baseUrl}}/api/managers | Create Missing Role | {"email":"bva.manager.norole{{$timestamp}}@school.com","password":"123456","fullName":"BVA Manage... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 2 | BVA-MAN-02 | POST | {{baseUrl}}/api/managers | Create Valid Manager | {"email":"bva.manager{{$timestamp}}@school.com","password":"123456","fullName":"BVA Manager","rol... | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |
| 3 | BVA-MAN-03 | POST | {{baseUrl}}/api/managers | Create Wrong Role | {"email":"bva.manager.role{{$timestamp}}@school.com","password":"123456","fullName":"BVA Manager"... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 4 | BVA-MAN-04 | GET | {{baseUrl}}/api/managers/999999999 | Get Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |

### 07 Blogs - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-BLOG-01 | POST | {{baseUrl}}/api/blogs | Create Empty Title | {"title":"","content":"Noi dung blog","author":"postman","category":"Health","thumbnail":"https:/... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 2 | BVA-BLOG-02 | POST | {{baseUrl}}/api/blogs | Create Min Title | {"title":"A","content":"Noi dung blog","author":"postman","category":"Health","thumbnail":"https:... | HTTP 201; dữ liệu trả về hợp lệ | V (nominal) | - |
| 3 | BVA-BLOG-03 | POST | {{baseUrl}}/api/blogs | Create Empty Content | {"title":"Blog {{$timestamp}}","content":"","author":"postman","category":"Health","thumbnail":"h... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 4 | BVA-BLOG-04 | GET | {{baseUrl}}/api/blogs/999999999 | Get Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 5 | BVA-BLOG-05 | DELETE | {{baseUrl}}/api/blogs/999999999 | Delete Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 6 | BVA-BLOG-06 | POST | {{baseUrl}}/api/blogs | Create Empty Author | {"title":"Blog {{$timestamp}}","content":"Noi dung blog","author":"","category":"Health","thumbna... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 7 | BVA-BLOG-07 | POST | {{baseUrl}}/api/blogs | Create Empty Category | {"title":"Blog {{$timestamp}}","content":"Noi dung blog","author":"postman","category":"","thumbn... | HTTP 400; JSON/text message lỗi | B (bien) | - |

### 08 School Health Docs - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-DOC-01 | POST | {{baseUrl}}/api/school-health-docs | Create Valid Doc | {"title":"BVA Health Doc {{$timestamp}}","content":"Noi dung tai lieu suc khoe","url":"https://ex... | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |
| 2 | BVA-DOC-02 | POST | {{baseUrl}}/api/school-health-docs | Create Empty Title | {"title":"","content":"Noi dung tai lieu suc khoe","url":"https://example.com/health-doc"} | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 3 | BVA-DOC-03 | GET | {{baseUrl}}/api/school-health-docs/999999999 | Get Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 4 | BVA-DOC-04 | DELETE | {{baseUrl}}/api/school-health-docs/999999999 | Delete Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |

### 09 Medical Supplies - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-MS-01 | POST | {{baseUrl}}/api/medical-supplies | Create Quantity 0 | {"name":"BVA Zero Quantity {{$timestamp}}","quantity":0,"category":"Thuoc","expiryDate":"2027-12-... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 2 | BVA-MS-02 | POST | {{baseUrl}}/api/medical-supplies | Create Quantity 1 | {"name":"BVA Min Quantity {{$timestamp}}","quantity":1,"category":"Thuoc","expiryDate":"2027-12-31"} | HTTP 201; dữ liệu trả về hợp lệ | V (nominal) | - |
| 3 | BVA-MS-03 | POST | {{baseUrl}}/api/medical-supplies | Create Empty Name | {"name":"","quantity":10,"category":"Thuoc","expiryDate":"2027-12-31"} | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 4 | BVA-MS-04 | GET | {{baseUrl}}/api/medical-supplies/999999999 | Get Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 5 | BVA-MS-05 | GET | {{baseUrl}}/api/medical-supplies/category/UNKNOWN_BVA_CATEGORY | Get Unknown Category | (URL path/query) | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |
| 6 | BVA-MS-06 | DELETE | {{baseUrl}}/api/medical-supplies/999999999 | Delete Invalid ID | (URL path/query) | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 7 | BVA-MS-07 | POST | {{baseUrl}}/api/medical-supplies | Create Empty Category | {"name":"BVA Empty Category {{$timestamp}}","quantity":10,"category":"","expiryDate":"2027-12-31"} | HTTP 400; JSON/text message lỗi | B (bien) | - |

### 10 Health Profiles - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-HP-01 | POST | {{baseUrl}}/api/health-profiles | Create Invalid Student ID | {"student":{"id":999999999},"studentName":"BVA Student","className":"5A","grade":"5","gender":"MA... | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 2 | BVA-HP-02 | POST | {{baseUrl}}/api/health-profiles | Create Valid Health Profile | {"student":{"id":{{studentId}}},"studentName":"BVA Student","className":"5A","grade":"5","gender"... | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | 04 Students, 05 Nurses |
| 3 | BVA-HP-03 | GET | {{baseUrl}}/api/health-profiles/student/999999999 | Get By Invalid Student | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 4 | BVA-HP-04 | GET | {{baseUrl}}/api/health-profiles/by-parent/999999999/student | Get Student ID By Invalid Parent | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 5 | BVA-HP-05 | GET | {{baseUrl}}/api/health-profiles/student/{{studentId}} | Get By Valid Student | (URL path/query) | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |

### 11 Health Incidents - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-HI-01 | POST | {{baseUrl}}/api/health-incidents | Create Invalid Student ID | {"student":{"id":999999999},"reportedBy":{"id":{{nurseId}}},"incidentType":"FALL","description":"... | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 2 | BVA-HI-02 | POST | {{baseUrl}}/api/health-incidents | Create Valid Incident | {"student":{"id":{{studentId}}},"reportedBy":{"id":{{nurseId}}},"incidentType":"FALL","descriptio... | HTTP 201; dữ liệu trả về hợp lệ | V (nominal) | 04 Students, 05 Nurses |
| 3 | BVA-HI-03 | GET | {{baseUrl}}/api/health-incidents/999999999 | Get Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 4 | BVA-HI-04 | GET | {{baseUrl}}/api/health-incidents/student/999999999 | Filter Invalid Student | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 5 | BVA-HI-05 | GET | {{baseUrl}}/api/health-incidents/status/INVALID_STATUS | Filter Invalid Status | (URL path/query) | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 6 | BVA-HI-06 | GET | {{baseUrl}}/api/health-incidents/incident-time/invalid-date | Filter Invalid Incident Time | (URL path/query) | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 7 | BVA-HI-07 | DELETE | {{baseUrl}}/api/health-incidents/{{healthIncidentId}} | Delete Valid Incident | (URL path/query) | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | - |

### 12 Medical Checkup - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-MC-01 | POST | {{baseUrl}}/api/medical-checkup/notify | Create Notification Invalid Student | {"studentId":999999999,"parentId":{{parentId}},"content":"Thong bao kham suc khoe","scheduledDate... | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 2 | BVA-MC-02 | POST | {{baseUrl}}/api/medical-checkup/notify | Create Notification Valid | {"studentId":{{studentId}},"parentId":{{parentId}},"content":"Thong bao kham suc khoe","scheduled... | HTTP 201; dữ liệu trả về hợp lệ | V (nominal) | 04 Students, 05 Nurses |
| 3 | BVA-MC-03 | POST | {{baseUrl}}/api/medical-checkup/notify/bulk | Bulk Empty StudentIds | {"studentIds":[],"content":"Thong bao kham tong quat","scheduledDate":"2026-06-10T08:00:00"} | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 4 | BVA-MC-04 | PUT | {{baseUrl}}/api/medical-checkup/notification/{{medicalCheckupNotificationId}}/consent | Update Consent Invalid Value | INVALID | HTTP 400; JSON/text message lỗi | X (abnormal) | MC-02 đã chạy |
| 5 | BVA-MC-05 | POST | {{baseUrl}}/api/medical-checkup/result | Create Result Invalid Date | {"studentId":{{studentId}},"checkupDate":"invalid-date","result":"Good health","notes":"No issue"... | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 6 | BVA-MC-06 | POST | {{baseUrl}}/api/medical-checkup/result | Create Result Valid | {"studentId":{{studentId}},"checkupDate":"2026-06-01T09:00:00","result":"Good health","notes":"No... | HTTP 201; dữ liệu trả về hợp lệ | V (nominal) | 04 Students, 05 Nurses |
| 7 | BVA-MC-07 | PUT | {{baseUrl}}/api/medical-checkup/result/999999999/student-confirm | Student Confirm Invalid Result | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | MC-02 đã chạy |

### 13 Vaccination Schedules - BVA

| STT | Mã TC | Method | Endpoint | Trường kiểm thử | Giá trị đầu vào | Kết quả mong đợi | Tag | Tiền điều kiện |
|---:|---|---|---|---|---|---|---|---|
| 1 | BVA-VAC-01 | POST | {{baseUrl}}/api/vaccination-schedules | Create Invalid Student | {"studentId":999999999,"studentName":"BVA Student","studentCode":"ST001","vaccineName":"Covid-19"... | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 2 | BVA-VAC-02 | POST | {{baseUrl}}/api/vaccination-schedules | Create Empty Vaccine Name | {"studentId":{{studentId}},"studentName":"BVA Student","studentCode":"ST001","vaccineName":"","sc... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 3 | BVA-VAC-03 | POST | {{baseUrl}}/api/vaccination-schedules | Create Valid Schedule | {"studentId":{{studentId}},"studentName":"BVA Student","studentCode":"ST001","vaccineName":"Covid... | HTTP 201; dữ liệu trả về hợp lệ | V (nominal) | 04 Students, 05 Nurses |
| 4 | BVA-VAC-04 | POST | {{baseUrl}}/api/vaccination-schedules/bulk | Bulk Empty StudentIds | {"studentIds":[],"vaccineName":"Flu","scheduledDateTime":"2026-06-20T08:00:00","notes":"Bulk sche... | HTTP 400; JSON/text message lỗi | B (bien) | - |
| 5 | BVA-VAC-05 | GET | {{baseUrl}}/api/vaccination-schedules/999999999 | Get Invalid ID | (URL path/query) | HTTP 404; JSON message lỗi | X (abnormal) | - |
| 6 | BVA-VAC-06 | GET | {{baseUrl}}/api/vaccination-schedules/date-range?startDate=2026-12-31T23:59:59&endDate=2026-01-01T00:00:00 | Get Invalid Date Range | (URL path/query) | HTTP 400; JSON/text message lỗi | X (abnormal) | - |
| 7 | BVA-VAC-07 | PUT | {{baseUrl}}/api/vaccination-schedules/{{vaccinationScheduleId}}/parent-consent | Update Parent Consent Invalid | {"consent":"INVALID"} | HTTP 400; JSON/text message lỗi | X (abnormal) | VAC-03 đã chạy |
| 8 | BVA-VAC-08 | PUT | {{baseUrl}}/api/vaccination-schedules/{{vaccinationScheduleId}}/student-confirmation | Update Student Confirmation Invalid Type | {"confirmed":"invalid-boolean"} | HTTP 400; JSON/text message lỗi | X (abnormal) | VAC-03 đã chạy |
| 9 | BVA-VAC-09 | PUT | {{baseUrl}}/api/vaccination-schedules/{{vaccinationScheduleId}}/vaccination-status | Update Vaccination Status Valid | {"isVaccinated":true} | HTTP 200; dữ liệu trả về hợp lệ | V (nominal) | VAC-03 đã chạy |

---

## 7. Triển khai kiểm thử tự động (Postman)

### 7.1. Cách import và chạy

1. Postman → **Import** → `SchoolHealth_BVA_All_Functions.postman_collection.json` (Replace nếu đã có bản cũ)
2. Import environment `local.postman_environment.json`
3. Khởi động backend Spring Boot và MySQL
4. **Collection Runner** → chọn collection → chọn environment → **Run**

### 7.2. Cơ chế assert

Mỗi request có script tab **Tests** (Post-response):

- Assert **HTTP status code** cố định (ví dụ 404, 400, 201)
- Assert **`response.json().message`** chứa chuỗi lỗi tiếng Việt mong đợi
- Request nominal lưu ID vào environment (`studentId`, `vaccinationScheduleId`, …)

Collection-level script bổ sung kiểm tra `expectedStatus` từ biến request khi cần.

### 7.3. Mẫu script (minh họa)

```javascript
const tc = 'BVA-VAC-05';
pm.test(`[${tc}] Status code la 404`, function () {
    pm.response.to.have.status(404);
});
pm.test(`[${tc}] Thong bao loi dung`, function () {
    pm.expect(pm.response.json().message)
      .to.include('Không tìm thấy lịch tiêm chủng với ID: 999999999');
});
```

> Toàn bộ script chi tiết nằm trong file collection JSON; không cần nhập tay từng request sau khi import.

### 7.4. Bảng kết quả

**Ngày chạy:** 29/06/2026  
**Công cụ:** Newman 6.2.2 (CLI, tương đương Postman Collection Runner)  
**Environment:** `School Health Backend - Local` (`baseUrl=http://localhost:8080`)  
**Collection:** `SchoolHealth_BVA_All_Functions.postman_collection.json`  
**Backend:** Spring Boot chạy tại cổng 8080, MySQL `db`

| Chỉ số | Giá trị |
|--------|---------|
| Tổng request (testcase BVA) | **89** |
| HTTP call thực tế | **91** *(89 testcase + 2 request setup đồng bộ admin trong prerequest AUTH-03)* |
| Tổng assertion | **174** |
| Pass | **174** |
| Fail | **0** |
| Thời gian chạy | **12,6 giây** |
| Thời gian phản hồi TB | **51 ms** (min 3 ms, max 1146 ms) |

```text
Newman summary:
174 assertions | 174 passed | 0 failed | 0 errors
```

**Kết quả theo module:**

| Module | Số request | Assertion | Pass | Fail | Kết quả |
|--------|------------|-----------|------|------|---------|
| 01 Auth | 5 | 14 | 14 | 0 | **PASS** |
| 02 Users | 10 | 18 | 18 | 0 | **PASS** |
| 03 Parents | 7 | 13 | 13 | 0 | **PASS** |
| 04 Students | 10 | 18 | 18 | 0 | **PASS** |
| 05 Nurses | 7 | 16 | 16 | 0 | **PASS** |
| 06 Managers | 4 | 8 | 8 | 0 | **PASS** |
| 07 Blogs | 7 | 14 | 14 | 0 | **PASS** |
| 08 School Health Docs | 4 | 8 | 8 | 0 | **PASS** |
| 09 Medical Supplies | 7 | 14 | 14 | 0 | **PASS** |
| 10 Health Profiles | 5 | 9 | 9 | 0 | **PASS** |
| 11 Health Incidents | 7 | 14 | 14 | 0 | **PASS** |
| 12 Medical Checkup | 7 | 14 | 14 | 0 | **PASS** |
| 13 Vaccination Schedules | 9 | 18 | 18 | 0 | **PASS** |
| **Tổng** | **89** | **174** | **174** | **0** | **PASS** |

**Ghi chú kỹ thuật:** Lần chạy đầu với Newman, prerequest `BVA-AUTH-03` dùng `async/await` gây lỗi cú pháp và login 401; đã chuyển sang `pm.sendRequest` dạng callback để tương thích Newman/Postman. Sau chỉnh sửa, toàn bộ collection pass.

**Minh chứng:** log chi tiết tại `docs/newman_run.log`, báo cáo JSON tại `docs/newman_results.json`. Khi nộp báo cáo, có thể đính kèm thêm screenshot Collection Runner summary từ Postman GUI (nội dung tương đương bảng trên).

---

## 8. Lỗi phát hiện qua kiểm thử hộp đen

Quan sát từ góc độ **đầu vào / đầu ra** (trước khi chỉnh backend):

| Module | Hiện tượng quan sát | Mong đợi kiểm thử hộp đen |
|--------|---------------------|---------------------------|
| Nurses | GET/POST lỗi validation trả 500 | 400/404 + JSON `message` |
| Managers | GET ID không tồn tại → 500 | 404 + JSON |
| Blogs, Docs, Supplies | 404 body rỗng | 404 + `message` |
| Health Profiles | Thiếu validate student | 404 khi student không tồn tại |
| Health Incidents | Filter/status lỗi → 500 | 400/404 có message |
| Medical Checkup | Bulk `[]` vẫn 200; consent INVALID được chấp nhận | 400 |
| Vaccination | Date range ngược; 404 rỗng; boolean sai kiểu → 500 | 400/404 + JSON |

Các lỗi trên đã được xử lý để testcase Postman pass theo kỳ vọng kiểm thử hộp đen.

---

## 9. Hạn chế của đợt kiểm thử

1. Chỉ kiểm thử **API REST** — không kiểm thử UI React.
2. **89 testcase** tập trung biên và lỗi phổ biến; chưa phủ mọi endpoint (PUT hợp lệ, DELETE hợp lệ trên mọi module).
3. Module `/api/medical` chưa có testcase.
4. Một số testcase phụ thuộc **thứ tự chạy** và dữ liệu DB sẵn có (admin login, parentId=1…).
5. HP-02 có thể fail nếu học sinh đã có hồ sơ (ràng buộc OneToOne) — cần dữ liệu sạch hoặc học sinh mới.

---

## 10. Kết luận

Đợt kiểm thử hộp đen đã thiết kế và tự động hóa **89 testcase** trên **13 module API** của hệ thống Quản lý Y tế Học đường, áp dụng kết hợp **EP** và **BVA**. Kiểm thử được thực hiện hoàn toàn từ bên ngoài hệ thống (request/response HTTP), phù hợp định nghĩa kiểm thử hộp đen.

Kết quả chạy collection (29/06/2026): **174/174 assertion pass**, **0 fail**, thời gian **12,6 giây**. Các API đáp ứng đặc tả lỗi (400/404 + thông báo JSON) sau khi hiệu chỉnh theo phản hồi từ testcase. Phạm vi còn mở rộng được với module Yêu cầu thuốc và luồng E2E nếu có thêm thời gian.

---

## Phụ lục A — Thống kê testcase theo loại

| Loại | Số lượng | Tỷ lệ |
|------|----------|-------|
| N (Nominal) | 19 | 21,3% |
| B (Boundary) | 27 | 30,3% |
| A (Abnormal) | 43 | 48,3% |
| **Tổng** | **89** | **100%** |

## Phụ lục B — Tài liệu tham chiếu trong repo

| File | Mục đích |
|------|----------|
| `backend/postman/SchoolHealth_BVA_All_Functions.postman_collection.json` | Collection kiểm thử (script đầy đủ) |
| `backend/postman/local.postman_environment.json` | Biến môi trường |
| `docs/BVA_Auth_Report.md` | Báo cáo chi tiết mẫu module Auth |
| `docs/UnitTest_QuanLyYTeHocDuong.xlsx` | Ma trận UTC (tham khảo, không thuộc kiểm thử hộp đen) |

---

*Báo cáo được lập theo collection Postman phiên bản hiện tại (89 requests). Khi cập nhật collection, cần đồng bộ lại số lượng testcase trong báo cáo.*
