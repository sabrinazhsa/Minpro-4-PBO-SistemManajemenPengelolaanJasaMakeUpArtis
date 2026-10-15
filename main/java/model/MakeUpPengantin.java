/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author HP VICTUS
 */

public class MakeUpPengantin extends PemesananLayananMUA {
 
    private static final double hargaPerSesi = 500000;
    private static final double hargaSanggul = 200000;
 
    private int jumlahSesiRias;
    private boolean includeSanggul;
 
    public MakeUpPengantin(String idPesanan, String tanggalPesanan, String status, Pelanggan pelanggan,
                            int jumlahSesiRias, boolean includeSanggul) {
        super(idPesanan, tanggalPesanan, "Make Up Pengantin", 0, status, pelanggan);
        this.jumlahSesiRias = jumlahSesiRias;
        this.includeSanggul = includeSanggul;
        this.setTotalHarga(hitungTotalBiaya());
    }
 
    @Override
    public double hitungTotalBiaya() {
        double total = hargaPerSesi * jumlahSesiRias;
        if (includeSanggul) {
            total += hargaSanggul;
        }
        return total;
    }
    
 
    @Override
    public void tampilkanDetailPesanan() {
        super.tampilkanDetailPesanan();
        System.out.println("Jumlah Sesi Rias : " + jumlahSesiRias + " Sesi");
        System.out.println("Sanggul/Hairdo   : " + (includeSanggul ? "Ya (+Rp200.000)" : "Tidak"));
        System.out.println("Total Biaya      : Rp " + (long) getTotalHarga());
    }
 
    public int getJumlahSesiRias() { return jumlahSesiRias; }
    public void setJumlahSesiRias(int jumlahSesiRias) {
        this.jumlahSesiRias = jumlahSesiRias;
        this.setTotalHarga(hitungTotalBiaya());
    }
    public boolean isIncludeSanggul() { return includeSanggul; }
    public void setIncludeSanggul(boolean includeSanggul) {
        this.includeSanggul = includeSanggul;
        this.setTotalHarga(hitungTotalBiaya());
    }
}