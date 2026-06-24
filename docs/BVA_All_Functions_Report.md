# Bao Cao Kiem Thu Day Du Cac Chuc Nang Theo Phuong Phap BVA

## 1. Thong Tin Chung

- Du an: Quan Ly Y Te Hoc Duong
- Phuong phap: BVA - Boundary Value Analysis
- Cong cu: Postman Collection Runner
- Backend: Spring Boot
- Base URL: `http://localhost:8080`
- Collection: `backend/postman/SchoolHealth_Backend.postman_collection.json`
- Environment: `backend/postman/local.postman_environment.json`

## 2. Muc Tieu Bao Cao

Bao cao nay trinh bay day du cac chuc nang API hien co trong Postman Collection va de xuat test case theo phuong phap BVA.

Muc tieu:

- Xac dinh cac truong dau vao co bien can kiem thu.
- Kiem tra gia tri nho hon bien, tai bien va lon hon bien.
- Kiem tra cac gia tri rong, null, sai dinh dang, ID khong ton tai.
- Kiem tra cac gia tri enum hop le va khong hop le.
- Ho tro viet script auto test Postman va lap bao cao unit/system test.

## 3. Nguyen Tac Thiet Ke Test BVA

Cong thuc BVA co ban:

```text
min - 1, min, min + 1, max - 1, max, max + 1
```

Trong project nay, nhieu truong khong co gioi han max ro rang trong backend. Vi vay, bao cao ap dung cac nhom bien sau:

| Nhom bien | Cach kiem thu |
| --- | --- |
| Chuoi bat buoc | `""`, 1 ky tu, chuoi hop le |
| So nguyen duong | `-1`, `0`, `1`, gia tri hop le |
| ID | `0`, `-1`, ID ton tai, ID khong ton tai |
| Email | rong, sai format, dung format |
| Phone | 9 chu so, 10 chu so, 11 chu so, khong bat dau bang `0` |
| Enum | rong/null, gia tri hop le, gia tri khong nam trong enum |
| Date/DateTime | rong, sai format, dung format, khoang ngay bat dau > ket thuc |
| Boolean | `true`, `false`, null/sai kieu |
| Array | mang rong, 1 phan tu, nhieu phan tu, ID khong ton tai |

## 4. Danh Sach Chuc Nang Trong Collection

| STT | Module | So request | Chuc nang chinh |
| --- | --- | ---: | --- |
| 1 | Auth | 1 | Login |
| 2 | Users | 4 | Create, Get All, Get By ID, Update, Delete |
| 3 | Students | 6 | CRUD, Get By Parent |
| 4 | Parents | 6 | Create, Get, Link Student, Confirm Form, Delete |
| 5 | Nurses | 5 | CRUD |
| 6 | Managers | 5 | CRUD |
| 7 | Blogs | 5 | CRUD |
| 8 | School Health Docs | 5 | CRUD |
| 9 | Medical Supplies | 6 | CRUD, Get By Category |
| 10 | Health Profiles | 6 | CRUD, Get By Student, Get Student By Parent |
| 11 | Health Incidents | 12 | CRUD va cac filter |
| 12 | Medical Checkup | 11 | Notifications, Results, Consent, Confirmation |
| 13 | Vaccination Schedules | 16 | CRUD, Bulk, filter, consent, confirmation, vaccination status |

## 5. Bien Moi Truong Can Dung

| Bien | Y nghia |
| --- | --- |
| `baseUrl` | URL backend local |
| `accessToken` | Token sau khi login |
| `userId` | ID user |
| `studentId` | ID hoc sinh |
| `parentId` | ID phu huynh |
| `nurseId` | ID nhan vien y te |
| `managerId` | ID quan ly |
| `blogId` | ID blog |
| `schoolHealthDocId` | ID tai lieu suc khoe |
| `medicalSupplyId` | ID vat tu y te |
| `medicalSupplyCategory` | Danh muc vat tu y te |
| `healthProfileId` | ID ho so suc khoe |
| `healthIncidentId` | ID su co suc khoe |
| `reportedById` | ID nguoi bao cao |
| `medicalCheckupNotificationId` | ID thong bao kham suc khoe |
| `medicalCheckupResultId` | ID ket qua kham suc khoe |
| `vaccinationScheduleId` | ID lich tiem |
| `formId` | ID form can xac nhan |
| `incidentTime` | Thoi gian su co |

## 6. BVA Cho Auth

### 6.1. Chuc Nang Login

Endpoint: `POST /api/auth/login`

| ID | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- |
| BVA-AUTH-01 | `email` | `""` | min - 1 | Dang nhap that bai, `400`/`401` |
| BVA-AUTH-02 | `email` | `a@b.co` | min hop le | Neu tai khoan ton tai thi `200` |
| BVA-AUTH-03 | `email` | `abc` | sai format | Dang nhap that bai |
| BVA-AUTH-04 | `password` | `""` | min - 1 | Dang nhap that bai |
| BVA-AUTH-05 | `password` | dung mat khau | hop le | `200`, co token |
| BVA-AUTH-06 | `password` | sai mat khau | ngoai mien | `401`/`400` |

## 7. BVA Cho Users

Endpoint chinh: `/api/user`, `/api/users`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-USER-01 | Create User | `email` | `""` | min - 1 | Loi validation |
| BVA-USER-02 | Create User | `email` | `postman.user{{$timestamp}}@school.com` | hop le | Tao thanh cong |
| BVA-USER-03 | Create User | `email` | `abc` | sai format | Loi validation |
| BVA-USER-04 | Create User | `password` | `""` | min - 1 | Loi validation |
| BVA-USER-05 | Create User | `password` | `123456` | min hop le theo collection | Tao thanh cong |
| BVA-USER-06 | Create User | `fullName` | `""` | min - 1 | Loi validation |
| BVA-USER-07 | Create User | `fullName` | `"A"` | min | Tao thanh cong |
| BVA-USER-08 | Create User | `role` | null/khong gui | min - 1 | Loi validation |
| BVA-USER-09 | Create User | `role` | `PARENT` | enum hop le | Tao thanh cong |
| BVA-USER-10 | Create User | `role` | `INVALID_ROLE` | enum khong hop le | Loi parse/validation |
| BVA-USER-11 | Get By ID | `userId` | `0` | duoi mien ID | Khong tim thay |
| BVA-USER-12 | Get By ID | `userId` | ID ton tai | hop le | `200` |
| BVA-USER-13 | Update User | `fullName` | `""` | min - 1 | Loi hoac khong cap nhat |
| BVA-USER-14 | Delete User | `userId` | ID khong ton tai | ngoai mien | `404`/`400`/`500` tuy backend |

## 8. BVA Cho Students

Endpoint chinh: `/api/students`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-STU-01 | Create Student | `email` | `""` | min - 1 | Loi validation |
| BVA-STU-02 | Create Student | `email` | `student{{$timestamp}}@school.com` | hop le | Tao thanh cong |
| BVA-STU-03 | Create Student | `password` | `""` | min - 1 | Loi validation |
| BVA-STU-04 | Create Student | `fullName` | `""` | min - 1 | Loi validation |
| BVA-STU-05 | Create Student | `role` | `STUDENT` | enum hop le | Tao thanh cong |
| BVA-STU-06 | Create Student | `role` | `PARENT` | sai role | Loi nghiep vu |
| BVA-STU-07 | Create Student | `code` | `""` | min - 1 | Loi neu bat buoc |
| BVA-STU-08 | Create Student | `code` | `ST{{$timestamp}}` | hop le/unique | Tao thanh cong |
| BVA-STU-09 | Create Student | `dateOfBirth` | `2014-01-01` | ngay hop le | Tao thanh cong |
| BVA-STU-10 | Create Student | `dateOfBirth` | `2014/01/01` | sai format | Loi parse |
| BVA-STU-11 | Create Student | `parentId` | `0` | ID khong ton tai | Loi lien ket |
| BVA-STU-12 | Create Student | `parentId` | ID parent ton tai | hop le | Tao thanh cong |
| BVA-STU-13 | Get Student By ID | `studentId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-STU-14 | Get Students By Parent | `parentId` | ID khong ton tai | ngoai mien | Mang rong hoac loi |
| BVA-STU-15 | Update Student | `studentClass` | `""` | min - 1 | Loi/khong cap nhat |
| BVA-STU-16 | Delete Student | `studentId` | ID ton tai | hop le | Xoa thanh cong |

## 9. BVA Cho Parents

Endpoint chinh: `/api/parent`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-PAR-01 | Create Parent | `email` | `""` | min - 1 | Loi validation |
| BVA-PAR-02 | Create Parent | `email` | `parent{{$timestamp}}@school.com` | hop le | Tao thanh cong |
| BVA-PAR-03 | Create Parent | `role` | `PARENT` | enum hop le | Tao thanh cong |
| BVA-PAR-04 | Create Parent | `role` | `STUDENT` | sai role | Loi nghiep vu |
| BVA-PAR-05 | Create Parent | `phoneNumber` | 9 chu so | min - 1 | Loi validation neu co |
| BVA-PAR-06 | Create Parent | `phoneNumber` | 10 chu so | min | Tao thanh cong |
| BVA-PAR-07 | Create Parent | `phoneNumber` | 11 chu so | max + 1 | Loi validation neu co |
| BVA-PAR-08 | Get Parent By ID | `parentId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-PAR-09 | Link Student To Parent | `parentId` | ID khong ton tai | ngoai mien | Loi lien ket |
| BVA-PAR-10 | Link Student To Parent | `studentId` | ID khong ton tai | ngoai mien | Loi lien ket |
| BVA-PAR-11 | Confirm Parent Form | `formId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-PAR-12 | Delete Parent | `parentId` | ID ton tai | hop le | Xoa thanh cong |

## 10. BVA Cho Nurses

Endpoint chinh: `/api/nurses`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-NUR-01 | Create Nurse | `role` | null/khong gui | min - 1 | `400`, role bat buoc |
| BVA-NUR-02 | Create Nurse | `role` | `SCHOOL_NURSE` | hop le | `200` |
| BVA-NUR-03 | Create Nurse | `role` | `PARENT` | enum sai nghiep vu | `400` |
| BVA-NUR-04 | Create Nurse | `phoneNumber` | 9 chu so | min - 1 | Loi neu validation kich hoat |
| BVA-NUR-05 | Create Nurse | `phoneNumber` | 10 chu so | min | Tao thanh cong |
| BVA-NUR-06 | Create Nurse | `phoneNumber` | 11 chu so | max + 1 | Loi neu validation kich hoat |
| BVA-NUR-07 | Create Nurse | `email` | sai format | sai format | Loi validation neu `@Valid` |
| BVA-NUR-08 | Get Nurse By ID | `nurseId` | ID khong ton tai | ngoai mien | Backend hien tai co the tra `500` |
| BVA-NUR-09 | Update Nurse | `email` | email trung | unique duplicate | Loi cap nhat |
| BVA-NUR-10 | Delete Nurse | `nurseId` | ID ton tai | hop le | `200` |

## 11. BVA Cho Managers

Endpoint chinh: `/api/managers`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-MAN-01 | Create Manager | `role` | null/khong gui | min - 1 | Loi validation |
| BVA-MAN-02 | Create Manager | `role` | `MANAGER` | hop le | Tao thanh cong |
| BVA-MAN-03 | Create Manager | `role` | `PARENT` | sai role | Loi nghiep vu |
| BVA-MAN-04 | Create Manager | `email` | `manager{{$timestamp}}@school.com` | hop le/unique | Tao thanh cong |
| BVA-MAN-05 | Create Manager | `email` | email da ton tai | duplicate | Loi unique |
| BVA-MAN-06 | Create Manager | `phoneNumber` | 9 chu so | min - 1 | Loi neu validation kich hoat |
| BVA-MAN-07 | Create Manager | `phoneNumber` | 10 chu so | min | Tao thanh cong |
| BVA-MAN-08 | Get Manager By ID | `managerId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-MAN-09 | Update Manager | `fullName` | `""` | min - 1 | Loi/khong cap nhat |
| BVA-MAN-10 | Delete Manager | `managerId` | ID ton tai | hop le | Xoa thanh cong |

## 12. BVA Cho Blogs

Endpoint chinh: `/api/blogs`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-BLOG-01 | Create Blog | `title` | `""` | min - 1 | `400` |
| BVA-BLOG-02 | Create Blog | `title` | `"A"` | min | `200` |
| BVA-BLOG-03 | Create Blog | `content` | `""` | min - 1 | `400` |
| BVA-BLOG-04 | Create Blog | `content` | `"A"` | min | `200` |
| BVA-BLOG-05 | Create Blog | `author` | `""` | min - 1 | `400` |
| BVA-BLOG-06 | Create Blog | `author` | `"A"` | min | `200` |
| BVA-BLOG-07 | Create Blog | `category` | `""` | min - 1 | `400` |
| BVA-BLOG-08 | Create Blog | `category` | `"H"` | min | `200` |
| BVA-BLOG-09 | Create Blog | `publishDate` | sai format | sai format | Loi parse |
| BVA-BLOG-10 | Get Blog By ID | `blogId` | ID khong ton tai | ngoai mien | `404` |
| BVA-BLOG-11 | Update Blog | `title` | `""` | min - 1 | `400` |
| BVA-BLOG-12 | Delete Blog | `blogId` | ID khong ton tai | ngoai mien | `404` |

## 13. BVA Cho School Health Docs

Endpoint chinh: `/api/school-health-docs`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-DOC-01 | Create Doc | `title` | `""` | min - 1 | Loi validation neu bat buoc |
| BVA-DOC-02 | Create Doc | `title` | `"A"` | min | Tao thanh cong |
| BVA-DOC-03 | Create Doc | `content` | `""` | min - 1 | Loi validation neu bat buoc |
| BVA-DOC-04 | Create Doc | `content` | `"A"` | min | Tao thanh cong |
| BVA-DOC-05 | Create Doc | `url` | `""` | min - 1 | Loi neu bat buoc |
| BVA-DOC-06 | Create Doc | `url` | `https://example.com/health-doc` | hop le | Tao thanh cong |
| BVA-DOC-07 | Create Doc | `url` | `abc` | sai format URL | Loi neu backend validate |
| BVA-DOC-08 | Get Doc By ID | `schoolHealthDocId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-DOC-09 | Update Doc | `title` | `""` | min - 1 | Loi/khong cap nhat |
| BVA-DOC-10 | Delete Doc | `schoolHealthDocId` | ID ton tai | hop le | Xoa thanh cong |

## 14. BVA Cho Medical Supplies

Endpoint chinh: `/api/medical-supplies`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-MS-01 | Create Supply | `name` | `""` | min - 1 | `400`, ten khong duoc de trong |
| BVA-MS-02 | Create Supply | `name` | `"A"` | min | `201` |
| BVA-MS-03 | Create Supply | `name` | ten da ton tai | duplicate | `400` |
| BVA-MS-04 | Create Supply | `quantity` | `-1` | min - 2 | `400` |
| BVA-MS-05 | Create Supply | `quantity` | `0` | min - 1 | `400` |
| BVA-MS-06 | Create Supply | `quantity` | `1` | min | `201` |
| BVA-MS-07 | Create Supply | `category` | `""` | min - 1 | `400` |
| BVA-MS-08 | Create Supply | `category` | `"A"` | min | `201` |
| BVA-MS-09 | Create Supply | `expiryDate` | sai format | sai format | Loi parse |
| BVA-MS-10 | Update Supply | `quantity` | `0` | min - 1 | Khong cap nhat hoac loi tuy logic |
| BVA-MS-11 | Update Supply | `quantity` | `1` | min | `200` |
| BVA-MS-12 | Get By ID | `medicalSupplyId` | ID khong ton tai | ngoai mien | `404` |
| BVA-MS-13 | Get By Category | `category` | category khong ton tai | ngoai mien | Mang rong |
| BVA-MS-14 | Delete Supply | `medicalSupplyId` | ID khong ton tai | ngoai mien | `400` |

Body update nen dung:

```json
{
  "name": "Supply Updated {{$timestamp}}",
  "quantity": 80,
  "category": "Vat tu",
  "expiryDate": "2028-12-31"
}
```

## 15. BVA Cho Health Profiles

Endpoint chinh: `/api/health-profiles`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-HP-01 | Create Profile | `student.id` | `0` | ID khong ton tai | Loi lien ket |
| BVA-HP-02 | Create Profile | `student.id` | ID student ton tai | hop le | Tao thanh cong |
| BVA-HP-03 | Create Profile | `studentName` | `""` | min - 1 | Loi neu bat buoc |
| BVA-HP-04 | Create Profile | `studentName` | `"A"` | min | Tao thanh cong |
| BVA-HP-05 | Create Profile | `className` | `""` | min - 1 | Loi neu bat buoc |
| BVA-HP-06 | Create Profile | `grade` | `""` | min - 1 | Loi neu bat buoc |
| BVA-HP-07 | Create Profile | `dateOfBirth` | sai format | sai format | Loi parse |
| BVA-HP-08 | Get By Student | `studentId` | ID khong ton tai | ngoai mien | Khong tim thay/mang rong |
| BVA-HP-09 | Get Student ID By Parent | `parentId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-HP-10 | Update Profile | `healthProfileId` | ID khong ton tai | ngoai mien | Loi update |
| BVA-HP-11 | Delete Profile | `healthProfileId` | ID ton tai | hop le | Xoa thanh cong |

## 16. BVA Cho Health Incidents

Endpoint chinh: `/api/health-incidents`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-HI-01 | Create Incident | `student.id` | `0` | ID khong ton tai | Loi lien ket |
| BVA-HI-02 | Create Incident | `student.id` | ID ton tai | hop le | Tao thanh cong |
| BVA-HI-03 | Create Incident | `reportedBy.id` | `0` | ID khong ton tai | Loi lien ket |
| BVA-HI-04 | Create Incident | `incidentType` | `FALL` | enum hop le | Tao thanh cong |
| BVA-HI-05 | Create Incident | `incidentType` | `INVALID` | enum sai | Loi parse/validation |
| BVA-HI-06 | Create Incident | `description` | `""` | min - 1 | Loi neu bat buoc |
| BVA-HI-07 | Create Incident | `description` | `"A"` | min | Tao thanh cong |
| BVA-HI-08 | Create Incident | `incidentTime` | sai format | sai format | Loi parse |
| BVA-HI-09 | Create Incident | `incidentTime` | `2026-05-25T08:00:00` | hop le | Tao thanh cong |
| BVA-HI-10 | Create Incident | `status` | `REPORTED` | enum hop le | Tao thanh cong |
| BVA-HI-11 | Create Incident | `status` | `INVALID` | enum sai | Loi parse/validation |
| BVA-HI-12 | Get By ID | `healthIncidentId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-HI-13 | Filter By Student | `studentId` | ID khong ton tai | ngoai mien | Mang rong |
| BVA-HI-14 | Filter By Status | `status` | `REPORTED` | hop le | `200` |
| BVA-HI-15 | Filter By Status | `status` | `INVALID` | enum sai | Loi hoac mang rong |
| BVA-HI-16 | Filter By Incident Time | `incidentTime` | sai format | sai format | Loi parse |
| BVA-HI-17 | Update Incident | `status` | `MONITORING` | enum hop le | Cap nhat thanh cong |
| BVA-HI-18 | Delete Incident | `healthIncidentId` | ID ton tai | hop le | Xoa thanh cong |

## 17. BVA Cho Medical Checkup

Endpoint chinh: `/api/medical-checkup`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-MC-01 | Create Notification | `studentId` | `0` | ID khong ton tai | Loi lien ket |
| BVA-MC-02 | Create Notification | `studentId` | ID ton tai | hop le | Tao thong bao |
| BVA-MC-03 | Create Notification | `parentId` | `0` | ID khong ton tai | Loi lien ket |
| BVA-MC-04 | Create Notification | `content` | `""` | min - 1 | Loi neu bat buoc |
| BVA-MC-05 | Create Notification | `content` | `"A"` | min | Tao thong bao |
| BVA-MC-06 | Create Notification | `scheduledDate` | sai format | sai format | Loi parse |
| BVA-MC-07 | Create Notification | `status` | `PENDING` | enum hop le | Tao thong bao |
| BVA-MC-08 | Create Notification | `status` | `INVALID` | enum sai | Loi parse/validation |
| BVA-MC-09 | Create Bulk | `studentIds` | `[]` | min - 1 | Loi hoac khong tao |
| BVA-MC-10 | Create Bulk | `studentIds` | 1 ID | min | Tao thanh cong |
| BVA-MC-11 | Create Bulk | `studentIds` | ID khong ton tai | ngoai mien | Loi lien ket |
| BVA-MC-12 | Get Notifications By Parent | `parentId` | ID khong ton tai | ngoai mien | Mang rong |
| BVA-MC-13 | Get Notifications By Student | `studentId` | ID khong ton tai | ngoai mien | Mang rong |
| BVA-MC-14 | Get Notifications By Status | `status` | `PENDING` | enum hop le | `200` |
| BVA-MC-15 | Update Consent | body text | `APPROVED` | enum hop le | Cap nhat thanh cong |
| BVA-MC-16 | Update Consent | body text | `INVALID` | enum sai | Loi validation |
| BVA-MC-17 | Create Result | `checkupDate` | sai format | sai format | Loi parse |
| BVA-MC-18 | Create Result | `result` | `""` | min - 1 | Loi neu bat buoc |
| BVA-MC-19 | Create Result | `abnormal` | `false` | boolean hop le | Tao thanh cong |
| BVA-MC-20 | Create Result | `abnormal` | sai kieu | sai kieu | Loi parse |
| BVA-MC-21 | Update Result | `medicalCheckupResultId` | ID khong ton tai | ngoai mien | Loi update |
| BVA-MC-22 | Student Confirm Result | `medicalCheckupResultId` | ID ton tai | hop le | Xac nhan thanh cong |

## 18. BVA Cho Vaccination Schedules

Endpoint chinh: `/api/vaccination-schedules`

| ID | Chuc nang | Truong | Gia tri dau vao | Loai bien | Ket qua mong doi |
| --- | --- | --- | --- | --- | --- |
| BVA-VAC-01 | Create Schedule | `studentId` | `0` | ID khong ton tai | Loi lien ket |
| BVA-VAC-02 | Create Schedule | `studentId` | ID ton tai | hop le | Tao lich |
| BVA-VAC-03 | Create Schedule | `studentName` | `""` | min - 1 | Loi neu bat buoc |
| BVA-VAC-04 | Create Schedule | `studentName` | `"A"` | min | Tao lich |
| BVA-VAC-05 | Create Schedule | `studentCode` | `""` | min - 1 | Loi neu bat buoc |
| BVA-VAC-06 | Create Schedule | `vaccineName` | `""` | min - 1 | Loi neu bat buoc |
| BVA-VAC-07 | Create Schedule | `vaccineName` | `"A"` | min | Tao lich |
| BVA-VAC-08 | Create Schedule | `scheduledDateTime` | sai format | sai format | Loi parse |
| BVA-VAC-09 | Create Schedule | `scheduledDateTime` | `2026-06-15T08:00:00` | hop le | Tao lich |
| BVA-VAC-10 | Create Schedule | `vaccinated` | `false` | boolean hop le | Tao lich |
| BVA-VAC-11 | Create Schedule | `vaccinated` | sai kieu | sai kieu | Loi parse |
| BVA-VAC-12 | Create Schedule | `parentConsent` | `PENDING` | enum hop le | Tao lich |
| BVA-VAC-13 | Create Schedule | `parentConsent` | `INVALID` | enum sai | Loi parse/validation |
| BVA-VAC-14 | Create Bulk | `studentIds` | `[]` | min - 1 | Loi hoac khong tao |
| BVA-VAC-15 | Create Bulk | `studentIds` | 1 ID | min | Tao lich bulk |
| BVA-VAC-16 | Get By ID | `vaccinationScheduleId` | ID khong ton tai | ngoai mien | Khong tim thay |
| BVA-VAC-17 | Get By Student | `studentId` | ID khong ton tai | ngoai mien | Mang rong |
| BVA-VAC-18 | Get Parent Consent | `parentConsent` | `PENDING` | enum hop le | `200` |
| BVA-VAC-19 | Get Parent Consent | `parentConsent` | `INVALID` | enum sai | Loi hoac mang rong |
| BVA-VAC-20 | Get Date Range | `startDate` > `endDate` | bien sai | Loi hoac mang rong |
| BVA-VAC-21 | Get Date Range | dung format | hop le | `200` |
| BVA-VAC-22 | Update Parent Consent | `consent` | `APPROVED` | enum hop le | Cap nhat thanh cong |
| BVA-VAC-23 | Update Parent Consent | `consent` | `INVALID` | enum sai | Loi validation |
| BVA-VAC-24 | Update Student Confirmation | `confirmed` | `true` | boolean hop le | Cap nhat thanh cong |
| BVA-VAC-25 | Update Student Confirmation | `confirmed` | sai kieu | sai kieu | Loi parse |
| BVA-VAC-26 | Update Vaccination Status | `isVaccinated` | `true` | boolean hop le | Cap nhat thanh cong |
| BVA-VAC-27 | Delete Schedule | `vaccinationScheduleId` | ID ton tai | hop le | Xoa thanh cong |

## 19. Mau Script Postman Chung Cho BVA

### 19.1. Kiem Tra Status Code Loi

```javascript
pm.test("BVA: status code is expected error", function () {
    pm.expect([400, 404, 500]).to.include(pm.response.code);
});
```

### 19.2. Kiem Tra Response Thanh Cong La JSON

```javascript
pm.test("BVA: response is valid JSON", function () {
    pm.expect(function () {
        pm.response.json();
    }).to.not.throw();
});
```

### 19.3. Kiem Tra Field Bat Buoc

```javascript
const json = pm.response.json();

pm.test("BVA: response has required fields", function () {
    pm.expect(json).to.have.property("id");
});
```

### 19.4. Kiem Tra ID Khong Ton Tai

```javascript
pm.test("BVA: not found or handled error", function () {
    pm.expect([400, 404, 500]).to.include(pm.response.code);
});
```

### 19.5. Kiem Tra Mang Rong Cho Filter

```javascript
const list = pm.response.json();

pm.test("BVA: response is array", function () {
    pm.expect(list).to.be.an("array");
});
```

## 20. Ma Tran Uu Tien Thuc Thi

| Muc uu tien | Nhom test | Ly do |
| --- | --- | --- |
| Cao | Required field rong | De gay loi nhat trong form nhap lieu |
| Cao | ID khong ton tai | Anh huong CRUD va lien ket bang |
| Cao | Enum sai | De gay loi parse request |
| Cao | Quantity `0`, `1` | Bien quan trong cua Medical Supplies |
| Trung binh | Email/phone sai format | Phu thuoc validation backend |
| Trung binh | Date/DateTime sai format | De gay loi parse JSON |
| Trung binh | Array rong trong bulk API | Anh huong tao hang loat |
| Thap | Chuoi rat dai | Backend chua khai bao max ro rang |

## 21. Nhan Xet Chung

Qua phan tich BVA cho toan bo collection, cac diem can chu y gom:

- Nhieu API co truong ID lien ket voi bang khac, vi vay can test ID ton tai va ID khong ton tai.
- Cac module user role nhu Student, Parent, Nurse, Manager can test role hop le va role sai.
- Cac API bulk can test mang rong, mot phan tu va ID khong ton tai.
- Cac API filter can test gia tri co du lieu, khong co du lieu va sai format.
- Mot so controller co the chua kich hoat validation bang `@Valid`, nen ket qua voi email/phone sai format co the khong on dinh.
- Mot so API hien tra `500` khi khong tim thay du lieu, can ghi nhan theo hanh vi backend hien tai neu chua sua code.

## 22. Ket Luan

Bao cao nay bao phu day du cac chuc nang trong Postman Collection theo phuong phap BVA. Cac test case duoc thiet ke dua tren:

- Body request hien co trong collection.
- Cac bien environment dang su dung.
- Cac truong bat buoc, enum, ID, ngay thang, boolean va array.
- Hanh vi backend da quan sat trong qua trinh chay Postman.

Khi thuc thi, nen chay cac test happy path truoc de tao du lieu va luu environment, sau do chay cac test BVA negative rieng de tranh anh huong den flow CRUD chinh.
