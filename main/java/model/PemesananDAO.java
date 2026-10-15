/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import model.MakeUpPengantin;
import model.MakeUpWisuda;
import model.Pelanggan;
import model.PemesananLayananMUA;

/**
 *
 * @author HP VICTUS
 */

public class PemesananDAO  {

    private static final String jenis_makeup = "Make Up Wisuda";

    private static final DateTimeFormatter format_input =
            DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter format_tampil =
            DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private static final String sql_select =
            "SELECT p.id_pemesanan, p.tanggal_pemesanan, p.jenis_makeup, p.total_harga, p.status_pembayaran, "
            + "p.jumlah_orang_dirias, p.ada_retouch_kit, p.jumlah_sesi_rias, p.include_sanggul, "
            + "pl.nama_pelanggan, pl.alamat, pl.noHP "
            + "FROM pemesanan p JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan";

    public void tambah(PemesananLayananMUA pesanan) throws SQLException {
        String sqlPelanggan =
                "INSERT INTO pelanggan (nama_pelanggan, alamat, noHP) VALUES (?, ?, ?)";
        String sqlPemesanan =
                "INSERT INTO pemesanan (id_pemesanan, id_pelanggan, tanggal_pemesanan, jenis_makeup, "
                + "total_harga, status_pembayaran, jumlah_orang_dirias, ada_retouch_kit, jumlah_sesi_rias, include_sanggul) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection koneksi = Koneksi.getConnection()) {
            koneksi.setAutoCommit(false);
            try {
                int idPelanggan;
                try (PreparedStatement psPelanggan =
                             koneksi.prepareStatement(sqlPelanggan, Statement.RETURN_GENERATED_KEYS)) {
                    Pelanggan pelanggan = pesanan.getPelanggan();
                    psPelanggan.setString(1, pelanggan.getNamaPelanggan());
                    psPelanggan.setString(2, pelanggan.getAlamat());
                    psPelanggan.setString(3, pelanggan.getNoHP());
                    psPelanggan.executeUpdate();

                    try (ResultSet kunci = psPelanggan.getGeneratedKeys()) {
                        kunci.next();
                        idPelanggan = kunci.getInt(1);
                    }
                }

                try (PreparedStatement psPemesanan = koneksi.prepareStatement(sqlPemesanan)) {
                    psPemesanan.setString(1, pesanan.getIdPesanan());
                    psPemesanan.setInt(2, idPelanggan);
                    psPemesanan.setObject(3, LocalDate.parse(pesanan.getTanggalPesanan(), format_input));
                    psPemesanan.setString(4, pesanan.getJenisMakeup());
                    psPemesanan.setDouble(5, pesanan.getTotalHarga());
                    psPemesanan.setString(6, pesanan.getStatus());

                    if (pesanan instanceof MakeUpWisuda) {
                        MakeUpWisuda wisuda = (MakeUpWisuda) pesanan;
                        psPemesanan.setInt(7, wisuda.getJumlahOrangDirias());
                        psPemesanan.setBoolean(8, wisuda.isAdaRetouchKit());
                        psPemesanan.setNull(9, Types.INTEGER);
                        psPemesanan.setNull(10, Types.TINYINT);
                    } else if (pesanan instanceof MakeUpPengantin) {
                        MakeUpPengantin pengantin = (MakeUpPengantin) pesanan;
                        psPemesanan.setNull(7, Types.INTEGER);
                        psPemesanan.setNull(8, Types.TINYINT);
                        psPemesanan.setInt(9, pengantin.getJumlahSesiRias());
                        psPemesanan.setBoolean(10, pengantin.isIncludeSanggul());
                    } else {
                        throw new SQLException("Jenis pesanan belum didukung: " + pesanan.getJenisMakeup());
                    }
                    psPemesanan.executeUpdate();
                }

                koneksi.commit();
            } catch (SQLException | RuntimeException e) {
                koneksi.rollback();
                throw e;
            }
        }
    }

    public List<PemesananLayananMUA> ambilSemua() throws SQLException {
        String sql = sql_select + " ORDER BY CAST(SUBSTRING(p.id_pemesanan, 2) AS UNSIGNED)";
        List<PemesananLayananMUA> hasil = new ArrayList<>();

        try (Connection koneksi = Koneksi.getConnection();
             PreparedStatement ps = koneksi.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(ubahKeObjek(rs));
            }
        }
        return hasil;
    }

    public PemesananLayananMUA cariById(String idPemesanan) throws SQLException {
        String sql = sql_select + " WHERE p.id_pemesanan = ?";

        try (Connection koneksi = Koneksi.getConnection();
             PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, idPemesanan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return ubahKeObjek(rs);
                }
            }
        }
        return null;
    }

    public boolean updateStatus(String idPemesanan, String statusBaru) throws SQLException {
        String sql = "UPDATE pemesanan SET status_pembayaran = ? WHERE id_pemesanan = ?";

        try (Connection koneksi = Koneksi.getConnection();
             PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setString(2, idPemesanan);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapus(String idPemesanan) throws SQLException {
        String sqlCariPelanggan = "SELECT id_pelanggan FROM pemesanan WHERE id_pemesanan = ?";
        String sqlHapusPemesanan = "DELETE FROM pemesanan WHERE id_pemesanan = ?";
        String sqlHapusPelanggan =
                "DELETE FROM pelanggan WHERE id_pelanggan = ? "
                + "AND NOT EXISTS (SELECT 1 FROM pemesanan WHERE id_pelanggan = ?)";

        try (Connection koneksi = Koneksi.getConnection()) {
            koneksi.setAutoCommit(false);
            try {
                int idPelanggan;
                try (PreparedStatement ps = koneksi.prepareStatement(sqlCariPelanggan)) {
                    ps.setString(1, idPemesanan);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            return false; 
                        }
                        idPelanggan = rs.getInt("id_pelanggan");
                    }
                }

                try (PreparedStatement ps = koneksi.prepareStatement(sqlHapusPemesanan)) {
                    ps.setString(1, idPemesanan);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = koneksi.prepareStatement(sqlHapusPelanggan)) {
                    ps.setInt(1, idPelanggan);
                    ps.setInt(2, idPelanggan);
                    ps.executeUpdate();
                }

                koneksi.commit();
                return true;
            } catch (SQLException e) {
                koneksi.rollback();
                throw e;
            }
        }
    }

    public String generateIdBaru() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(id_pemesanan, 2) AS UNSIGNED)) AS terakhir FROM pemesanan";
        int terakhir = 0;

        try (Connection koneksi = Koneksi.getConnection();
             PreparedStatement ps = koneksi.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                terakhir = rs.getInt("terakhir"); 
            }
        }
        return "P" + String.format("%02d", terakhir + 1);
    }

    private PemesananLayananMUA ubahKeObjek(ResultSet rs) throws SQLException {
        Pelanggan pelanggan = new Pelanggan(
                rs.getString("nama_pelanggan"),
                rs.getString("alamat"),
                rs.getString("noHP"));

        String id = rs.getString("id_pemesanan");
        String tanggal = rs.getObject("tanggal_pemesanan", LocalDate.class).format(format_tampil);
        String status = rs.getString("status_pembayaran");

        PemesananLayananMUA pesanan;
        if (jenis_makeup.equals(rs.getString("jenis_makeup"))) {
            pesanan = new MakeUpWisuda(id, tanggal, status, pelanggan,
                    rs.getInt("jumlah_orang_dirias"), rs.getBoolean("ada_retouch_kit"));
        } else {
            pesanan = new MakeUpPengantin(id, tanggal, status, pelanggan,
                    rs.getInt("jumlah_sesi_rias"), rs.getBoolean("include_sanggul"));
        }
        pesanan.setTotalHarga(rs.getDouble("total_harga"));
        return pesanan;
    }
}
