/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;
import java.sql.Connection;

/**
 *
 * @author HP VICTUS
 */
public class TesKoneksi {
    public static void main(String[] args) {
        System.out.println("Mengecek koneksi ke database...");
        Connection conn = Koneksi.getConnection();

        if (conn != null) {
            System.out.println("STATUS: BERHASIL! Database siap digunakan.");
        } else {
            System.out.println("STATUS: GAGAL! Periksa URL, USER, PASS, dan pastikan service MySQL menyala.");
        }
    }
}
