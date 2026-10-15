/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import model.Pelanggan;
import model.PemesananLayananMUA;
import model.MakeUpPengantin;
import model.MakeUpWisuda;
import model.PemesananDAO;
import java.sql.SQLException;

/**
 *
 * @author HP VICTUS
 */
public class LayananCRUD {

    private final PemesananDAO model = new PemesananDAO();

    public String generateIdPesanan() {
        try {
            return model.generateIdBaru();
        } catch (SQLException e) {
            tampilkanError("Membuat ID pesanan", e);
            return null;        
        }
    }
    
    public void tambahPemesanan(PemesananLayananMUA pesanan) {
        if (pesanan == null) {
            System.out.println("Data pesanan tidak boleh kosong!");
            return;
        }

        try {
            if (model.cariById(pesanan.getIdPesanan()) != null) {
                System.out.println("ID Pesanan [" + pesanan.getIdPesanan() + "] sudah digunakan. Gunakan ID lain!");
                return;
            }
            model.tambah(pesanan);
            System.out.println("Pesanan dengan ID [" + pesanan.getIdPesanan() + "] berhasil ditambahkan!");
        } catch (SQLException e) {
            tampilkanError("Menyimpan pesanan", e);
        }   
    }

    public void tampilkanSemuaPemesanan() {
        List<PemesananLayananMUA> daftarPesanan;
        try {
            daftarPesanan = model.ambilSemua();
        } catch (SQLException e) {
            tampilkanError("Mengambil data pesanan", e);
            return;
        }
 
        if (daftarPesanan.isEmpty()) {
            System.out.println("Belum ada pesanan yang masuk.");
            return;
        }
 
        NumberFormat formatRupiah = NumberFormat.getNumberInstance(new Locale("in", "ID"));
 
        System.out.println("\n--- DAFTAR PESANAN ---");
 
        for (int i = 0; i < daftarPesanan.size(); i++) {
 
            PemesananLayananMUA p = daftarPesanan.get(i);
            Pelanggan pelanggan = p.getPelanggan();
         
        System.out.println("   -----------------------------------------------------------------");
        System.out.println((i + 1) + ". ID Pesanan          : " + p.getIdPesanan());
        System.out.println("   Nama Pelanggan      : " + pelanggan.getNamaPelanggan());
        System.out.println("   Alamat Pelanggan    : " + pelanggan.getAlamat());
        System.out.println("   Nomor HP            : " + pelanggan.getNoHP());
        System.out.println("   Jenis Makeup        : " + p.getJenisMakeup());
        System.out.println("   Tanggal Pengerjaan  : " + p.getTanggalPesanan());
        System.out.println("   Total Harga         : Rp" + formatRupiah.format(p.getTotalHarga()));
        System.out.println("   Status Pembayaran   : " + p.getStatus());
        System.out.println("   -----------------------------------------------------------------");
    }
}

    public PemesananLayananMUA cariPemesanan(String idPesananCari) {
        if (idPesananCari == null) {
            return null;
        }
        try {
            return model.cariById(idPesananCari);
        } catch (SQLException e) {
            tampilkanError("Mencari pesanan", e);
            return null;
        }
    }

    public void updatePemesanan(String idPesananCari, String statusBaru) {
        PemesananLayananMUA p = cariPemesanan(idPesananCari);

        if (p == null) {
            System.out.println("Pesanan dengan ID [" + idPesananCari + "] tidak ditemukan.");
            return;
        }
        if (statusBaru == null || statusBaru.trim().isEmpty()) {
        System.out.println("Tidak ada perubahan, status tetap \"" + p.getStatus() + "\".");
        return;
    }
        try {
            boolean berhasil = model.updateStatus(idPesananCari, statusBaru);
            if (berhasil) {
                System.out.println("Status pesanan [" + idPesananCari + "] berhasil diperbarui menjadi \"" + statusBaru + "\"!");
            } else {
                System.out.println("Pesanan dengan ID [" + idPesananCari + "] tidak ditemukan.");
            }
        } catch (SQLException e) {
            tampilkanError("Mengubah status pesanan", e);
        }
    }

    public void hapusPemesanan(String idPesananCari) {
         try {
            boolean berhasil = model.hapus(idPesananCari);
            if (berhasil) {
                System.out.println("Pesanan [" + idPesananCari + "] berhasil dihapus!");
            } else {
                System.out.println("Pesanan dengan ID [" + idPesananCari + "] tidak ada!!!");
            }
        } catch (SQLException e) {
            tampilkanError("Menghapus pesanan", e);
        }
    }
 
    private void tampilkanError(String aksi, SQLException e) {
        System.out.println("Gagal " + aksi + ". Terjadi kesalahan database: " + e.getMessage());
    }
}
 