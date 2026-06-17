# TimezoneMembership

## Informasi Proyek

**Anggota Kelompok:**

| Nama | NRP |
|---|---|
| Aryaka Leorgi Epridaka | 5025231117 |
| Muhammad Risyad Himawan Putra | 5025231205 |


**Tautan Sumber Daya:**
* **Source Code GitHub:** https://github.com/AryakaLeorgi/TimezoneMembership
* **Link Project Requirement:** https://drive.google.com/file/d/1nmoltS56WvYHeTYYmfm3NbdxOTZ_FyRd/view?usp=sharing
* **Link PPT:** https://canva.link/qsfw5yh1cqmy3fp
* **Link Aplikasi:** https://github.com/AryakaLeorgi/TimezoneMembership/releases/download/apk/TimezoneMembership.apk
* **Link Infografis:** https://canva.link/o43x2oh4pywauij
* **Link YouTube:** https://youtu.be/Qy8J54NoRQU

---

Aplikasi Android untuk manajemen keanggotaan (membership) dan program loyalitas **Timezone**.
Dibangun menggunakan **Jetpack Compose** dengan arsitektur **MVVM** (Model-View-ViewModel) dan
penyimpanan data lokal menggunakan **Room Database**.

---

## Daftar Isi

- [Gambaran Umum](#gambaran-umum)
- [Fitur Utama](#fitur-utama)
- [Technology Stack](#technology-stack)
- [Arsitektur Aplikasi](#arsitektur-aplikasi)
- [Struktur Proyek](#struktur-proyek)
- [Data Model](#data-model)
- [Business Logic](#business-logic)
- [Alur Navigasi](#alur-navigasi)
- [Halaman Aplikasi](#halaman-aplikasi)
- [Desain dan Theming](#desain-dan-theming)
- [Persyaratan Sistem](#persyaratan-sistem)
- [Cara Menjalankan](#cara-menjalankan)
- [Konfigurasi Proyek](#konfigurasi-proyek)

---

## Gambaran Umum

**TimezoneMembership** adalah aplikasi simulasi manajemen keanggotaan untuk pusat hiburan Timezone.
Aplikasi ini menyediakan dua peran pengguna utama:

- **Member**: Pengguna biasa yang dapat melakukan top-up saldo Tizo, bermain mesin arcade (simulasi),
  menukarkan reward points dengan hadiah, dan melihat riwayat transaksi.
- **Admin**: Administrator yang memiliki dashboard untuk melihat seluruh daftar member terdaftar,
  menambahkan member baru secara manual, dan mengelola data keanggotaan.

Seluruh data disimpan secara lokal di perangkat menggunakan Room Database (SQLite). Tidak ada
koneksi jaringan atau backend server yang diperlukan.

---

## Fitur Utama

### Sistem Autentikasi
- Login dengan email dan password
- Registrasi akun baru (Member atau Admin)
- Fitur "Remember Me" untuk auto-login
- Validasi input lengkap (format email, panjang password, format nomor telepon)
- Penanganan error untuk email duplikat dan kredensial salah

### Dashboard Admin
- Tampilan jumlah total member terdaftar
- Daftar seluruh member dengan informasi saldo Tizo dan reward points
- Navigasi ke detail setiap member
- Tombol FAB untuk menambah member baru secara manual

### Profil dan Kartu Member
- Kartu keanggotaan digital dengan desain gradient
- Menampilkan nama, email, nomor telepon, dan ID member
- Placeholder QR code untuk identifikasi member
- Tampilan saldo Tizo dan reward points secara real-time

### Top-Up Saldo Tizo
- 6 tier paket top-up dengan harga berbeda (Rp100.000 hingga Rp2.000.000)
- Setiap top-up memberikan sejumlah Tizo dan bonus reward points
- Dialog konfirmasi sebelum pemrosesan
- Feedback melalui Snackbar setelah transaksi berhasil atau gagal

### Simulasi Bermain
- 3 kategori mesin: Mesin Ringan, Mesin Sedang, dan Mesin Berat
- Setiap kategori memiliki biaya Tizo dan reward points yang berbeda
- Validasi saldo sebelum bermain (mesin yang tidak terjangkau akan di-disable)
- Pencatatan otomatis ke riwayat transaksi

### Penukaran Reward (Redeem)
- 3 kategori hadiah: Gratis Bermain, Merchandise, dan Diskon Top-Up
- Total 9 jenis hadiah yang dapat ditukarkan
- Navigasi dengan tab untuk setiap kategori
- Validasi kecukupan poin sebelum penukaran

### Riwayat Transaksi
- Daftar kronologis seluruh transaksi (Top-Up, Bermain, Redeem)
- Badge visual berbeda untuk setiap tipe transaksi
- Menampilkan mutasi Tizo dan poin untuk setiap transaksi
- Format tanggal dan waktu dalam locale Indonesia

---

## Technology Stack

| Komponen             | Teknologi                                        |
|----------------------|--------------------------------------------------|
| Bahasa               | Kotlin                                           |
| UI Framework         | Jetpack Compose dengan Material 3                |
| Arsitektur           | MVVM (Model-View-ViewModel)                      |
| Database Lokal       | Room Database (SQLite)                           |
| Navigasi             | Navigation Compose                               |
| State Management     | StateFlow dan MutableStateFlow (Kotlin Coroutines) |
| Annotation Processor | KSP (Kotlin Symbol Processing)                   |
| Build System         | Gradle (Kotlin DSL) dengan Version Catalog       |
| Min SDK              | API 24 (Android 7.0 Nougat)                      |
| Target SDK           | API 36                                           |

### Dependency Utama

| Library                         | Versi         | Fungsi                                  |
|---------------------------------|---------------|-----------------------------------------|
| AGP (Android Gradle Plugin)     | 9.1.1         | Build system Android                    |
| Kotlin                          | 2.2.10        | Compiler bahasa Kotlin                  |
| Compose BOM                     | 2024.09.00    | Bill of Materials untuk Jetpack Compose |
| Room                            | 2.7.1         | ORM untuk SQLite                        |
| Navigation Compose              | 2.9.0         | Navigasi deklaratif antar screen        |
| Lifecycle ViewModel Compose     | 2.10.0        | Integrasi ViewModel dengan Compose      |
| KSP                             | 2.2.10-2.0.2  | Annotation processing untuk Room        |

---

## Arsitektur Aplikasi

Aplikasi ini mengikuti pola arsitektur **MVVM** (Model-View-ViewModel) dengan pemisahan tanggung
jawab yang jelas antar layer:

```
+--------------------------------------------------+
|                    UI Layer                       |
|    (Composable Screens + Navigation Graph)        |
+--------------------------------------------------+
                       |
                       | mengamati StateFlow
                       v
+--------------------------------------------------+
|               ViewModel Layer                     |
|   (MemberViewModel + TransactionViewModel)        |
+--------------------------------------------------+
                       |
                       | memanggil suspend functions
                       v
+--------------------------------------------------+
|              Repository Layer                     |
|            (TimezoneRepository)                   |
+--------------------------------------------------+
                       |
                       | mengakses DAO
                       v
+--------------------------------------------------+
|                Data Layer                         |
|  (Room Database + DAO + Entity Models)            |
+--------------------------------------------------+
```

### Penjelasan Setiap Layer

**UI Layer** -- Terdiri dari Composable functions yang merender tampilan pengguna. Setiap screen
mengamati (observe) data dari ViewModel melalui `collectAsState()` dan memanggil fungsi ViewModel
ketika ada interaksi pengguna.

**ViewModel Layer** -- Bertindak sebagai penghubung antara UI dan data. Menyimpan state UI dalam
`StateFlow`, menangani logika presentasi, dan mendelegasikan operasi data ke Repository. Menggunakan
`viewModelScope` untuk menjalankan coroutine.

**Repository Layer** -- Mengenkapsulasi seluruh logika akses data dan business rules. Mengoordinasikan
operasi antara multiple DAO dalam satu transaction (misalnya update saldo member sekaligus insert
catatan transaksi).

**Data Layer** -- Terdiri dari Room Database, DAO interfaces, dan Entity data classes. Menangani
penyimpanan dan pengambilan data dari SQLite secara langsung.

---

## Struktur Proyek

```
TimezoneMembership/
|-- app/
|   |-- build.gradle.kts              # Konfigurasi build module app
|   |-- proguard-rules.pro            # Aturan ProGuard
|   |-- src/
|       |-- main/
|           |-- AndroidManifest.xml    # Manifest aplikasi
|           |-- java/com/example/timezonemembership/
|           |   |-- MainActivity.kt   # Entry point aplikasi
|           |   |-- data/
|           |   |   |-- dao/
|           |   |   |   |-- MemberDao.kt        # DAO untuk operasi tabel members
|           |   |   |   |-- TransactionDao.kt   # DAO untuk operasi tabel transactions
|           |   |   |-- database/
|           |   |   |   |-- TimezoneDatabase.kt  # Konfigurasi Room Database (Singleton)
|           |   |   |-- model/
|           |   |   |   |-- Constants.kt         # Konstanta business rules (tier, mesin, hadiah)
|           |   |   |   |-- Member.kt            # Entity data class untuk member
|           |   |   |   |-- Transaction.kt       # Entity data class untuk transaksi
|           |   |   |-- repository/
|           |   |       |-- TimezoneRepository.kt # Repository untuk akses data terpusat
|           |   |-- ui/
|           |   |   |-- navigation/
|           |   |   |   |-- NavGraph.kt          # Konfigurasi navigation graph dan routes
|           |   |   |-- screen/
|           |   |   |   |-- AddMemberScreen.kt   # Halaman tambah member (Admin)
|           |   |   |   |-- AuthScreen.kt        # Halaman login dan registrasi
|           |   |   |   |-- HistoryScreen.kt     # Halaman riwayat transaksi
|           |   |   |   |-- HomeScreen.kt        # Dashboard Admin
|           |   |   |   |-- MemberDetailScreen.kt # Profil dan kartu member
|           |   |   |   |-- PlayScreen.kt        # Simulasi bermain mesin arcade
|           |   |   |   |-- RedeemScreen.kt      # Penukaran reward points
|           |   |   |   |-- TopUpScreen.kt       # Top-up saldo Tizo
|           |   |   |-- theme/
|           |   |       |-- Color.kt             # Definisi palet warna
|           |   |       |-- Theme.kt             # Konfigurasi Material Theme
|           |   |       |-- Type.kt              # Konfigurasi Typography
|           |   |-- viewmodel/
|           |       |-- MemberViewModel.kt       # ViewModel utama (auth, member, transaksi)
|           |       |-- TransactionViewModel.kt  # ViewModel untuk riwayat transaksi
|           |-- res/                              # Resource Android (drawable, mipmap, values, xml)
|-- build.gradle.kts                              # Konfigurasi build root project
|-- settings.gradle.kts                           # Konfigurasi settings Gradle
|-- gradle/
|   |-- libs.versions.toml                        # Version Catalog untuk dependency management
|   |-- wrapper/                                  # Gradle Wrapper
|-- gradlew                                       # Script Gradle Wrapper (Unix)
|-- gradlew.bat                                   # Script Gradle Wrapper (Windows)
```

---

## Data Model

### Entity: Member

Tabel `members` menyimpan data pengguna aplikasi.

| Kolom          | Tipe      | Keterangan                                          |
|----------------|-----------|-----------------------------------------------------|
| `id`           | `Int`     | Primary key, auto-generated                         |
| `name`         | `String`  | Nama lengkap pengguna                               |
| `email`        | `String`  | Alamat email (unik, digunakan untuk login)          |
| `phone`        | `String`  | Nomor telepon                                       |
| `tizoBalance`  | `Int`     | Saldo Tizo saat ini (default: 0)                    |
| `rewardPoints` | `Int`     | Jumlah reward points saat ini (default: 0)          |
| `password`     | `String`  | Password pengguna (disimpan sebagai plain text)     |
| `role`         | `String`  | Peran pengguna: `"MEMBER"` atau `"ADMIN"`           |
| `isRemembered` | `Boolean` | Flag untuk fitur "Remember Me" (default: `false`)   |

### Entity: Transaction

Tabel `transactions` mencatat seluruh riwayat transaksi member. Memiliki foreign key ke tabel
`members` dengan `onDelete = CASCADE`.

| Kolom             | Tipe     | Keterangan                                                      |
|-------------------|----------|-----------------------------------------------------------------|
| `id`              | `Int`    | Primary key, auto-generated                                     |
| `memberId`        | `Int`    | Foreign key ke tabel `members`                                  |
| `transactionType` | `String` | Tipe transaksi: `"TOPUP"`, `"PLAY"`, atau `"REDEEM"`           |
| `description`     | `String` | Deskripsi transaksi (contoh: "Top-Up Tier 1 (Rp100.000)")      |
| `tizoMutation`    | `Int`    | Perubahan saldo Tizo (positif = kredit, negatif = debit)        |
| `pointMutation`   | `Int`    | Perubahan reward points (positif = kredit, negatif = debit)     |
| `timestamp`       | `Long`   | Waktu transaksi dalam milliseconds (default: `currentTimeMillis`) |

### Relasi Antar Entity

```
members (1) ----< (N) transactions
   |                     |
   | id (PK)             | memberId (FK)
   |                     | onDelete: CASCADE
```

Ketika sebuah record member dihapus, seluruh transaksi terkait juga akan dihapus secara otomatis
(cascade delete).

---

## Business Logic

### Konstanta dan Aturan Bisnis

Seluruh aturan bisnis terdefinisi secara terpusat di `Constants.kt` sebagai Single Source of Truth.

#### Tier Top-Up

| Tier   | Harga (Rp) | Tizo yang Didapat | Reward Points |
|--------|------------|-------------------|---------------|
| Tier 1 | 100.000    | 100               | 10            |
| Tier 2 | 200.000    | 200               | 25            |
| Tier 3 | 300.000    | 300               | 40            |
| Tier 4 | 500.000    | 500               | 75            |
| Tier 5 | 1.000.000  | 1.000             | 200           |
| Tier 6 | 2.000.000  | 2.000             | 500           |

#### Kategori Mesin

| Kategori     | Deskripsi                   | Biaya Tizo | Reward Points |
|--------------|-----------------------------|------------|---------------|
| Mesin Ringan | Capit boneka kecil, dsb     | 5          | 2             |
| Mesin Sedang | Arcade standard, balapan    | 10         | 3             |
| Mesin Berat  | VR, mesin tiket besar       | 15         | 4             |

#### Hadiah Penukaran (Redeem)

**Kategori Gratis Bermain:**

| Hadiah                       | Biaya Poin | Keterangan       |
|------------------------------|------------|------------------|
| 1x Voucher Mesin Sedang     | 30         | Senilai 10 Tizo  |
| 1x Voucher Mesin Berat      | 45         | Senilai 15 Tizo  |
| 1x Voucher Mesin Premium/VR | 80         | Senilai 30 Tizo  |

**Kategori Merchandise:**

| Hadiah                            | Biaya Poin |
|-----------------------------------|------------|
| Gantungan Kunci / Snack Ringan   | 500        |
| Boneka Timezone / Tumbler Eksklusif | 1.500    |
| Kaos Timezone / Tas Selempang    | 3.000      |
| TWS / Smartwatch Entry-Level     | 10.000     |

**Kategori Diskon:**

| Hadiah              | Biaya Poin | Keterangan      |
|---------------------|------------|-----------------|
| Voucher Diskon 10%  | 150        | Max Rp300.000   |
| Voucher Diskon 25%  | 300        | Max Rp500.000   |

### Alur Proses Transaksi

Semua operasi transaksi (top-up, bermain, redeem) dijalankan dalam Room `@Transaction` untuk
menjamin atomicity. Setiap operasi mengikuti pola yang sama:

1. Ambil data member terkini dari database
2. Validasi kelayakan (contoh: cek kecukupan saldo)
3. Update saldo/poin member
4. Catat record transaksi baru
5. Return `true` jika berhasil, `false` jika gagal

---

## Alur Navigasi

Aplikasi menggunakan Navigation Compose dengan konfigurasi route berikut:

```
splash --> [Cek Auto-Login]
              |
              +-- Jika ada remembered user --> memberDetail/{id} atau adminDashboard
              |
              +-- Jika tidak ada -----------> auth
                                                |
                                                +-- Login berhasil (MEMBER) --> memberDetail/{id}
                                                +-- Login berhasil (ADMIN) ---> adminDashboard
                                                +-- Register -----------------> Kembali ke Login
```

### Daftar Route

| Route                      | Halaman            | Parameter     | Keterangan                      |
|----------------------------|--------------------|---------------|---------------------------------|
| `splash`                   | Splash Screen      | -             | Layar loading awal              |
| `auth`                     | Auth Screen        | -             | Login dan registrasi            |
| `adminDashboard`           | Home Screen        | -             | Dashboard admin                 |
| `addMember`                | Add Member Screen  | -             | Form tambah member (admin)      |
| `memberDetail/{memberId}`  | Member Detail      | `memberId`    | Profil dan kartu member         |
| `topUp/{memberId}`         | Top-Up Screen      | `memberId`    | Halaman top-up saldo            |
| `play/{memberId}`          | Play Screen        | `memberId`    | Simulasi bermain                |
| `redeem/{memberId}`        | Redeem Screen      | `memberId`    | Penukaran reward points         |
| `history/{memberId}`       | History Screen     | `memberId`    | Riwayat transaksi               |

### Alur Navigasi per Role

**Member:**
```
Auth --> MemberDetail --> TopUp
                      --> Play
                      --> Redeem
                      --> History
```

**Admin:**
```
Auth --> AdminDashboard --> AddMember
                        --> MemberDetail --> TopUp / Play / Redeem / History
```

---

## Halaman Aplikasi

### 1. Auth Screen (`AuthScreen.kt`)

Halaman autentikasi yang terdiri dari dua sub-halaman:

**Login Page:**
- Input email dan password dengan validasi
- Checkbox "Ingat Akun Saya" untuk fitur auto-login
- Pesan error jika kredensial salah
- Banner sukses setelah registrasi berhasil

**Register Page:**
- Form registrasi dengan 5 field: nama, email, nomor telepon, password, dan konfirmasi password
- Validasi komprehensif: format email via regex, nomor telepon harus diawali "08" atau "+62" dengan
  panjang 10-15 digit, password minimal 6 karakter, dan pencocokan konfirmasi password
- Opsi checkbox untuk mendaftar sebagai Admin (untuk keperluan demo)
- Pengecekan duplikasi email di database

### 2. Home Screen / Admin Dashboard (`HomeScreen.kt`)

Dashboard utama untuk pengguna dengan role Admin:

- Card statistik menampilkan total member terdaftar
- Informasi nama admin yang sedang login
- `LazyColumn` berisi daftar seluruh member (role = MEMBER)
- Setiap item member menampilkan nama, email, saldo Tizo, dan reward points
- Floating Action Button (FAB) untuk navigasi ke halaman tambah member
- Tombol logout di top app bar
- Empty state jika belum ada member terdaftar

### 3. Add Member Screen (`AddMemberScreen.kt`)

Form untuk admin menambah member atau admin baru:

- Input nama, email, nomor telepon, dan password
- Validasi input yang sama dengan halaman registrasi
- Checkbox pemilihan role (Member atau Admin)
- Feedback melalui Snackbar setelah operasi selesai

### 4. Member Detail Screen (`MemberDetailScreen.kt`)

Halaman profil dan kartu keanggotaan digital:

- Kartu member dengan desain gradient (warna biru tua ke biru muda)
- Avatar dengan inisial nama member
- Informasi kontak (email dan nomor telepon)
- Placeholder QR code dengan ID member
- Dua card saldo: Saldo Tizo dan Reward Points
- Menu navigasi ke 4 layanan: Top-Up, Bermain, Redeem, dan Riwayat
- Perbedaan tampilan berdasarkan role (Member vs Admin)
- Tombol logout untuk pengguna yang melihat profilnya sendiri

### 5. Top-Up Screen (`TopUpScreen.kt`)

Halaman pembelian saldo Tizo:

- Card informasi saldo terkini (Tizo dan reward points)
- Grid 2 kolom menampilkan 6 tier paket top-up
- Setiap card tier menampilkan nama tier, harga dalam Rupiah, jumlah Tizo, dan bonus poin
- Dialog konfirmasi dengan detail transaksi sebelum eksekusi
- Snackbar untuk notifikasi hasil transaksi

### 6. Play Screen (`PlayScreen.kt`)

Halaman simulasi bermain mesin arcade:

- Card informasi saldo terkini
- 3 card mesin dalam format list vertikal
- Setiap card mesin menampilkan nama, deskripsi, biaya Tizo, dan reward points
- Mesin yang tidak terjangkau (saldo tidak cukup) akan disabled secara visual
- Indikator "Saldo Kurang" pada mesin yang tidak terjangkau
- Dialog konfirmasi sebelum bermain

### 7. Redeem Screen (`RedeemScreen.kt`)

Halaman penukaran reward points:

- Card saldo reward points dengan background gradient
- `ScrollableTabRow` dengan 3 tab kategori: Gratis Bermain, Merchandise, dan Diskon
- `LazyColumn` menampilkan daftar hadiah per kategori
- Setiap item hadiah menampilkan ikon kategori, nama, deskripsi (jika ada), dan biaya poin
- Item yang tidak terjangkau akan disabled dengan indikator "Kurang"
- Dialog konfirmasi sebelum penukaran

### 8. History Screen (`HistoryScreen.kt`)

Halaman riwayat seluruh transaksi member:

- `LazyColumn` menampilkan transaksi terurut dari yang terbaru
- Setiap card transaksi menampilkan: ikon dan badge tipe transaksi (TOP-UP, BERMAIN, REDEEM),
  deskripsi, timestamp dalam format Indonesia, dan mutasi Tizo serta poin
- Kode warna: hijau untuk TOP-UP, biru untuk BERMAIN, emas untuk REDEEM
- Empty state dengan instruksi jika belum ada transaksi

---

## Desain dan Theming

### Palet Warna

Aplikasi menggunakan light theme dengan palet warna yang terinspirasi dari branding Timezone:

| Peran Warna         | Nama Variabel       | Kode Hex    | Kegunaan                        |
|---------------------|---------------------|-------------|---------------------------------|
| Primary             | `BluePrimary`       | `#184594`   | Warna utama (Royal Blue)        |
| Primary Light       | `BluePrimaryLight`  | `#4A74C4`   | Varian terang primary           |
| Primary Dark        | `BluePrimaryDark`   | `#0E2D6B`   | Varian gelap primary            |
| Secondary           | `GoldSecondary`     | `#EDDF0C`   | Aksen emas/kuning               |
| Tertiary            | `TealAccent`        | `#14D9D6`   | Aksen teal (untuk Tizo)         |
| Background          | `LightBackground`   | `#F7F9FC`   | Latar belakang utama            |
| Surface             | `LightSurface`      | `#FFFFFF`   | Permukaan card                  |
| On Surface          | `OnLightSurface`    | `#083B3B`   | Teks utama (Dark Teal)          |
| On Surface Variant  | `OnLightSurfaceVariant` | `#5A6A7A` | Teks sekunder               |
| Success             | `SuccessGreen`      | `#2E7D32`   | Indikator sukses                |
| Error               | `ErrorRed`          | `#D32F2F`   | Indikator error/debit           |
| Warning             | `WarningOrange`     | `#EF6C00`   | Indikator peringatan            |

### Typography

Menggunakan font system `SansSerif` dengan konfigurasi Material 3 Typography yang lengkap,
mencakup scale dari `displayLarge` (36sp) hingga `labelSmall` (10sp).

### Elemen Desain

- **Kartu Member**: Gradient vertikal dari `BluePrimaryDark` ke `BluePrimaryLight`
- **Rounded Corners**: Konsisten menggunakan `RoundedCornerShape` (12dp - 24dp)
- **Elevation**: Penerapan shadow bertingkat (0dp - 6dp) untuk hierarki visual
- **Edge-to-Edge**: Mendukung tampilan edge-to-edge melalui `enableEdgeToEdge()`
- **Content Animation**: Penggunaan `animateContentSize()` pada komponen interaktif

---

## Persyaratan Sistem

### Untuk Pengembangan

- **Android Studio** versi terbaru (disarankan Ladybug atau yang lebih baru)
- **JDK 11** atau yang lebih baru
- **Gradle** 8.x (disertakan melalui Gradle Wrapper)
- **Android SDK** dengan API level 36

### Untuk Perangkat Pengguna

- **Android 7.0 (API 24)** atau yang lebih baru
- Tidak memerlukan koneksi internet
- Tidak memerlukan permission khusus

---

## Cara Menjalankan

### Melalui Android Studio

1. Clone atau unduh repository ini
2. Buka proyek di Android Studio
3. Tunggu proses sync Gradle selesai
4. Hubungkan perangkat fisik atau jalankan emulator Android (minimum API 24)
5. Klik tombol **Run** atau tekan `Shift + F10`

### Melalui Command Line

```bash
# Di direktori root proyek (TimezoneMembership/)

# Build APK debug
./gradlew assembleDebug

# Install ke perangkat yang terhubung
./gradlew installDebug
```

Pada Windows, gunakan `gradlew.bat` sebagai pengganti `./gradlew`.

### Akun Demo

Setelah aplikasi terpasang, Anda perlu membuat akun terlebih dahulu melalui halaman registrasi.
Tidak ada akun default yang sudah tersedia di database.

- Untuk membuat akun Admin: centang checkbox "Daftar sebagai Admin (Demo)" pada halaman registrasi
- Untuk membuat akun Member: biarkan checkbox tidak dicentang

---

## Konfigurasi Proyek

### Build Configuration

| Parameter                    | Nilai                             |
|------------------------------|-----------------------------------|
| `applicationId`              | `com.example.timezonemembership`  |
| `minSdk`                     | 24                                |
| `targetSdk`                  | 36                                |
| `compileSdk`                 | 36                                |
| `versionCode`                | 1                                 |
| `versionName`                | 1.0                               |
| `sourceCompatibility`        | Java 11                           |
| `targetCompatibility`        | Java 11                           |
| `Compose`                    | Enabled                           |

### Database Configuration

| Parameter              | Nilai                       |
|------------------------|-----------------------------|
| Nama Database          | `timezone_membership_db`    |
| Versi Database         | 2                           |
| Export Schema           | `false`                     |
| Migration Strategy     | `fallbackToDestructiveMigration()` |

Perlu diperhatikan bahwa strategi migrasi menggunakan `fallbackToDestructiveMigration()`, yang
berarti seluruh data akan dihapus jika terjadi perubahan skema database. Ini hanya cocok untuk
lingkungan pengembangan dan demo, bukan untuk aplikasi produksi.

### Dependency Management

Proyek menggunakan Gradle **Version Catalog** (`gradle/libs.versions.toml`) untuk manajemen
dependency terpusat. Seluruh versi library didefinisikan di satu tempat sehingga memudahkan
pemeliharaan dan update.

---

## Catatan Pengembangan

### Keterbatasan yang Disengaja

- **Penyimpanan password**: Password disimpan sebagai plain text di database lokal. Untuk aplikasi
  produksi, harus menggunakan hashing (misalnya bcrypt atau Argon2).
- **Tidak ada backend server**: Seluruh data disimpan lokal. Tidak ada sinkronisasi antar perangkat.
- **Simulasi pembayaran**: Top-up tidak terhubung ke payment gateway nyata. Saldo langsung
  ditambahkan tanpa proses pembayaran.
- **QR Code**: Hanya berupa placeholder visual, belum menghasilkan QR code yang sebenarnya.
- **Destructive migration**: Perubahan skema database akan menghapus seluruh data.

### Pola Desain yang Diterapkan

- **Singleton Pattern**: Pada `TimezoneDatabase` menggunakan companion object dengan `@Volatile`
  dan `synchronized` block untuk thread-safe singleton.
- **Repository Pattern**: `TimezoneRepository` mengenkapsulasi akses data dan menyediakan API
  bersih untuk ViewModel.
- **Observer Pattern**: Menggunakan `Flow` dan `StateFlow` untuk reaktivitas data dari database
  hingga UI.
- **Single Source of Truth**: Seluruh konstanta business rules terdefinisi di `Constants.kt`.

---

*Proyek ini dikembangkan sebagai tugas mata kuliah Pengembangan Platform Bergerak (PPB) -- Semester 6.*
