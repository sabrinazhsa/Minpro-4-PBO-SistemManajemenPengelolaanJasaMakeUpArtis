/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HP VICTUS
 */

public abstract class PemesananLayananMUA implements UntukDiskon {
    public abstract double hitungTotalBiaya();
    private String idPesanan;
    private String tanggalPesanan;
    private String jenisMakeup;
    private double totalHarga;
    private String status;
    private Pelanggan pelanggan;

    public PemesananLayananMUA(String idPesanan, String tanggalPesanan,
            String jenisMakeup, double totalHarga, String status,
            Pelanggan pelanggan) {

        this.idPesanan = idPesanan;
        this.tanggalPesanan = tanggalPesanan;
        this.jenisMakeup = jenisMakeup;
        this.totalHarga = totalHarga;
        this.status = status;
        this.pelanggan = pelanggan;
    }
    
    public String getIdPesanan() { 
        return idPesanan; 
    }
    
    public void setIdPesanan(String idPesanan) { 
        this.idPesanan = idPesanan; 
    }
    
    public String getTanggalPesanan() { 
        return tanggalPesanan; 
    }
    
    public void setTanggalPesanan(String tanggalPesanan) { 
        this.tanggalPesanan = tanggalPesanan; 
    }
    
    public String getJenisMakeup() { 
        return jenisMakeup; 
    }
    
    public void setJenisMakeup(String jenisMakeup) { 
        this.jenisMakeup = jenisMakeup; 
    }
    
    public double getTotalHarga() { 
        return totalHarga; 
    }
    
    public void setTotalHarga(double totalHarga) { 
        this.totalHarga = totalHarga; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public Pelanggan getPelanggan() { 
        return pelanggan; 
    }

    public double hitungTotalBiaya(double diskonPersen) {
        if (diskonPersen < 0 || diskonPersen > 100) {
            System.out.println("Diskon tidak valid, dianggap 0%.");
            diskonPersen = 0;
        }
        double totalSebelumDiskon;
        totalSebelumDiskon = this.hitungTotalBiaya();
        double potongan = totalSebelumDiskon * (diskonPersen / 100);
        return totalSebelumDiskon - potongan;
    }

    public void tampilkanDetailPesanan() {
        System.out.println("ID Pesanan: " + idPesanan);
        System.out.println("Tanggal   : " + tanggalPesanan);
        System.out.println("Jenis Make Up     : " + jenisMakeup);
        System.out.println("Total Harga:   " + totalHarga);
        System.out.println("Status Pembayaran : " + status);
    }
    
}