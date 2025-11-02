# TP6DPBO2425C1
Saya Arya Purnama Sauri dengan NIM 2408521 mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain Pemrograman Berbasis Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

# Desain Program

## Class App (Main)
Berfungsi untuk:
- Sebagai inisialisasi menu dan game panel
- Setup JFrame
- Setup Icon
- Run Program

## Class Logic
Berfungsi untuk:
- Mengatur state game (mulai, berakhir, ulang dsb)
- Mengatur physics (gravitasi, kecepatan dll)
- Mengatur game loop
- Mengatur collision
- Mengatur poin
- Mengatur input

## Class View
Berfungsi untuk:
- Render semua objek game (background, pipes, player dll)
- Mengatur UI
- Mengatur VFX (white flash saat mati)

## Class MenuPanel
Berfungsi untuk:
- Mengatur menu main
- Mengatur tombol play & exit

## Game Objects
Terdiri dari 3 class, yaitu:

### A) Class Player
- Membuat objek player
### B) Class Pipe
- Membuat objek pipe
### C) Class Ground
- Membuat objek ground

## Class SoundManager
Berfungsi untuk:
- Load sound effects
- Putar jump sfx
- Putar collision sfx
- Putar scoring sfx

# Penjelasan Alur Program
## 1. Jalankan Program
## 2. Menampilkan Tampilan Menu
Terdapat 2 tombol yaitu :
- START (untuk memulai permainan)
- EXIT (untuk menutup permainan)
## 3. Menekan Tombol
### A) START (alur bermain)
      1. Program menunggu input pemain (pause)
      2. Tekan input untuk memulai permainan (W/UP/SPACE/LMB)
      3. Player akan mulai bergerak
      3. Gravitasi akan menarik player kebawah
      4. Pemain menekan input untuk melompat (terbang)
      5. Player terus maju ke sisi kanan
      6. Pipa - pipa akan mulai bermunculan dari sisi kanan ke kiri
**Kondisi Permainan:**
- Jika player melewati pipa : ++**TAMBAH POIN**++
- Jika player menyentuh tanah: --**GAME OVER**--
- Jika player menyentuh langit: --**GAME OVER**--
- Jika player menyentuh pipa: --**GAME OVER**--
### B) EXIT
      1. Program akan berhenti

# Dokumentasi
https://github.com/user-attachments/assets/f4c21ce8-f27f-4353-aad2-39224bc186de
