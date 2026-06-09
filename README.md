# Dokumentasi Proyek RESTful API - Web Services

**Mata Kuliah:** Web Services  
**Pertemuan ke:** 5  
**Topik:** Tugas Membuat API dengan RESTful (10 Services)  
**Nama Proyek:** `module5`  
**Base URL:** `http://localhost:67`

---

## Peringatan Penting

Proyek ini menggunakan **Spring Boot versi 4.0.6** (masih dalam tahap milestone/SNAPSHOT dan **tidak stabil**).  
Jika Anda mengalami error seperti `ClassNotFoundException: org.hibernate.dialect.MySQL8Dialect`, **downgrade ke versi stabil**:

1. Buka `pom.xml`, ubah `parent` version menjadi:
   ```xml
   <version>3.2.5</version>
   ```
2. Hapus dependensi `spring-boot-starter-data-jpa-test` dan `spring-boot-starter-webmvc-test` (tidak standar).
3. Ganti `spring-boot-starter-webmvc` dengan `spring-boot-starter-web`.
4. Reload Maven.

---

## Daftar Isi
1. [Deskripsi Proyek](#deskripsi-proyek)
2. [Struktur Proyek](#struktur-proyek)
3. [Persiapan Lingkungan](#persiapan-lingkungan)
4. [Konfigurasi Database](#konfigurasi-database)
5. [Entitas yang Telah Disediakan (User)](#entitas-yang-telah-disediakan-user)
6. [Tugas Mahasiswa: Melengkapi 4 Entitas](#tugas-mahasiswa-melengkapi-4-entitas)
7. [Cara Menjalankan Aplikasi](#cara-menjalankan-aplikasi)
8. [Pengujian API dengan Postman](#pengujian-api-dengan-postman)
9. [Tugas Praktikum Lanjutan](#tugas-praktikum-lanjutan)
10. [Kesimpulan](#kesimpulan)

---

## Deskripsi Proyek

Proyek ini adalah aplikasi **Spring Boot** yang menyediakan **RESTful API** untuk manajemen berbagai entitas. Saat ini, kode awal hanya menyediakan entitas **User** lengkap dengan operasi CRUD dan dua custom query (`findByName` dan `findByEmailContaining`). Mahasiswa diwajibkan untuk **melengkapi 4 entitas lainnya**:

- **Product** – Manajemen produk
- **Order** – Manajemen pesanan
- **Category** – Manajemen kategori produk
- **Review** – Manajemen ulasan produk

Setiap entitas harus memiliki **CRUD lengkap** (GET, POST, PUT, DELETE) serta minimal **satu custom query**.

---

## Struktur Proyek

```
module5/
├── src/main/java/com/wsb/module5/
│   ├── controller/
│   │   └── UserController.java          => sudah ada
│   ├── model/
│   │   └── User.java                    => sudah ada
│   ├── repository/
│   │   └── UserRepository.java          => sudah ada (dengan custom query)
│   ├── service/
│   │   └── UserService.java             => sudah ada
│   └── Module5Application.java          (main class)
├── src/main/resources/
│   └── application.properties           => konfigurasi database
└── pom.xml                              => perlu diperbaiki (lihat peringatan)
```

> **Catatan:** Mahasiswa harus menambahkan package `model`, `repository`, `service`, `controller` untuk entitas **Product**, **Order**, **Category**, **Review** dengan pola yang sama seperti User.

---

## Persiapan Lingkungan

| Alat/Bahan | Versi Minimal |
|------------|---------------|
| Java JDK | 11 atau 17 |
| MySQL Server | 8.0 |
| IDE (VS Code, IntelliJ, Eclipse) | - |
| Postman / cURL | - |
| Maven | 3.6+ |

---

## Konfigurasi Database

File `application.properties` (sudah disediakan):

```properties
spring.application.name=module5
server.port=67

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/restful_api_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

> **Yang harus dilakukan:**  
> - Isi `spring.datasource.password` dengan password MySQL Anda.  
> - Database `restful_api_db` akan otomatis dibuat.

---

## Entitas yang Telah Disediakan (User)

### Model (`User.java`)
```java
@Entity @Table(name = "users") @Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String phone;
}
```

### Repository (`UserRepository.java`)
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByEmailContaining(@Param("keyword") String keyword);
}
```

### Service (`UserService.java`)
```java
@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    // CRUD standar + getUsersByName + searchUsersByEmailKeyword
}
```

### Controller (`UserController.java`)
```java
@RestController @RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService userService;
    // GET, POST, PUT, DELETE, GET /search/name, GET /search/email-keyword
}
```

### Endpoint User
| Method | Endpoint | Fungsi |
|--------|----------|--------|
| GET | `/api/users` | Ambil semua user |
| GET | `/api/users/{id}` | Ambil user by ID |
| POST | `/api/users` | Tambah user baru |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Hapus user |
| GET | `/api/users/search/name?name=...` | Cari user berdasarkan nama persis |
| GET | `/api/users/search/email-keyword?keyword=...` | Cari user berdasarkan email mengandung kata kunci |

---

## Tugas Mahasiswa: Melengkapi 4 Entitas

Ikuti pola User untuk membuat entitas berikut:

### 6.1. Product (Manajemen Produk)

**Model** (`Product.java`):
```java
@Entity @Table(name = "products") @Data @NoArgsConstructor @AllArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
}
```

**Repository** – tambahkan custom query, misal:  
`List<Product> findByPriceBetween(Double min, Double max);`

**Controller** – endpoint:
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`
- `GET /api/products/search/price?min=...&max=...` (custom)

---

### 6.2. Order (Manajemen Pesanan)

**Model**:
```java
@Entity @Table(name = "orders") @Data @NoArgsConstructor @AllArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private String status; // PENDING, PAID, SHIPPED, DELIVERED
    private LocalDateTime orderDate;
}
```

**Custom query contoh:** `List<Order> findByUserId(Long userId)`

---

### 6.3. Category (Manajemen Kategori)

**Model**:
```java
@Entity @Table(name = "categories") @Data @NoArgsConstructor @AllArgsConstructor
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
}
```

**Custom query:** `Category findByName(String name)`

---

### 6.4. Review (Manajemen Ulasan)

**Model**:
```java
@Entity @Table(name = "reviews") @Data @NoArgsConstructor @AllArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long userId;
    private Integer rating; // 1-5
    private String comment;
    private LocalDateTime createdAt;
}
```

**Custom query:** `List<Review> findByProductId(Long productId)`

---

## Cara Menjalankan Aplikasi

1. **Import proyek** ke IDE.
2. **Pastikan MySQL** berjalan.
3. **Sesuaikan password** di `application.properties`.
4. **Perbaiki `pom.xml`** (lihat peringatan di atas) jika diperlukan.
5. **Build**:
   ```bash
   mvn clean install
   ```
6. **Jalankan**:
   ```bash
   mvn spring-boot:run
   ```
   Aplikasi berjalan di `http://localhost:67`.

---

## Pengujian API dengan Postman

Contoh pengujian untuk User (endpoint lain serupa):

| Operasi | Method | URL | Body (JSON) |
|----------|--------|-----|--------------|
| GET all | GET | `http://localhost:67/api/users` | - |
| POST | POST | `http://localhost:67/api/users` | `{"name":"Budi","email":"budi@mail.com","phone":"0812"}` |
| Search name | GET | `http://localhost:67/api/users/search/name?name=Budi` | - |

### Contoh cURL
```bash
curl -X GET http://localhost:67/api/users
curl -X POST http://localhost:67/api/users -H "Content-Type: application/json" -d '{"name":"Budi","email":"budi@mail.com"}'
```

---

## Tugas Praktikum Lanjutan (Nilai Tambahan)

1. **Dokumentasi API dengan Swagger** – tambahkan dependensi `springdoc-openapi`.
2. **Validasi Input** – gunakan `@NotBlank`, `@Min`, lalu `@Valid` di controller.
3. **Global Exception Handling** – buat `@ControllerAdvice`.
4. **Keamanan** – Spring Security (basic auth atau JWT).

---

## Kesimpulan

Setelah menyelesaikan praktikum ini, mahasiswa diharapkan mampu:
- Mengembangkan RESTful API dengan Spring Boot.
- Menerapkan CRUD dan custom query dengan Spring Data JPA.
- Menguji API menggunakan Postman/cURL.
- Menambahkan fitur validasi, error handling, dan keamanan.