package com.mycompany.maintoko;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SistemToko {
    private String namaToko;
    private GudangUtama gudang;
    private HashMap<String, Pelanggan> daftarPelanggan;
    private HashMap<String, Produk> daftarProduk;
    private ArrayList<Transaksi> riwayatTransaksi;

    public SistemToko(String namaToko) {
        this.namaToko = namaToko;
        this.gudang = new GudangUtama("WH-01", "Gudang Utama");
        this.daftarPelanggan = new HashMap<>();
        this.daftarProduk = new HashMap<>();
        this.riwayatTransaksi = new ArrayList<>();
        System.out.println("=== SISTEM E-COMMERCE: " + this.namaToko + " AKTIF ===");
    }

    public void tambahPelanggan(String id, String nama) {
        daftarPelanggan.put(id, new Pelanggan(id, nama));
    }

    public void tambahProduk(String kode, String nama, int stok) {
        Produk produkBaru;
        if (kode.equalsIgnoreCase("OBJ-A")) {
            produkBaru = new ProdukElektronik(kode, nama, stok, 8500000.0, 24, true);
        } else if (kode.equalsIgnoreCase("OBJ-B")) {
            produkBaru = new ProdukFashion(kode, nama, stok, 185000.0, "Katun Combed 30s", "All Size");
        } else {
            produkBaru = new ProdukKonsumsi(kode, nama, stok, 25000.0, 30, 250);
        }
        daftarProduk.put(kode, produkBaru);
    }

    public void jalankanTransaksiDariFile(String namaFile) throws FileNotFoundException {
        File file = new File(namaFile);
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + namaFile + "' tidak ditemukan.");
        }

        Scanner scanner = new Scanner(file);
        String isiMentah = "";
        while (scanner.hasNextLine()) {
            isiMentah = isiMentah + scanner.nextLine().trim();
        }
        scanner.close();

        String[] daftarBlok = isiMentah.split("\\|");

        for (String blok : daftarBlok) {
            if (blok.trim().isEmpty()) {
                continue;
            }
            try {
                eksekusiPesanan(blok.trim());
            } catch (TransaksiException e) {
                System.out.println("[PESANAN DITOLAK] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[DATA CORRUPT] Gagal memproses blok '" + blok + "': " + e.getMessage());
            }
        }
    }

    private void eksekusiPesanan(String blok) throws TransaksiException {
        String[] token = blok.split(";");
        if (token.length < 3) {
            throw new TransaksiException("Format data tidak lengkap: " + blok);
        }

        String idPelanggan = token[0].split(":")[1].trim();
        String kodeProduk = token[1].split(":")[1].trim();
        int qty = Integer.parseInt(token[2].split(":")[1].trim());

        if (!daftarPelanggan.containsKey(idPelanggan)) {
            throw new TransaksiException("Pelanggan '" + idPelanggan + "' tidak terdaftar di " + this.namaToko + ".");
        }
        if (!daftarProduk.containsKey(kodeProduk)) {
            throw new TransaksiException("Produk '" + kodeProduk + "' tidak tersedia di katalog " + this.namaToko + ".");
        }

        Pelanggan pelanggan = daftarPelanggan.get(idPelanggan);
        Produk produk = daftarProduk.get(kodeProduk);

        produk.kurangiStok(qty);
        gudang.catatBarangKeluar(kodeProduk, produk.getNama(), qty);

        double subtotal = produk.getHargaSatuan() * qty;
        double biayaLayanan = produk.hitungBiayaLayanan(qty);
        double ongkir = 0.0;
        double diskonProduk = 0.0;
        double diskonMember = pelanggan.hitungDiskonMember(subtotal);
        String metodeKirim = "Ambil di Toko";

        if (produk instanceof DapatDikirim) {
            DapatDikirim kurir = (DapatDikirim) produk;
            metodeKirim = kurir.getMetodePengiriman();
            ongkir = kurir.hitungOngkir(qty);
        }

        if (produk instanceof DapatDidiskon) {
            DapatDidiskon promo = (DapatDidiskon) produk;
            diskonProduk = promo.hitungDiskonProduk(qty, subtotal);
        }

        double totalDiskon = diskonProduk + diskonMember;
        double totalAkhir = (subtotal + biayaLayanan + ongkir) - totalDiskon;

        pelanggan.catatTransaksiBelanja(totalAkhir, 10);

        Transaksi trx = new Transaksi(pelanggan, produk, qty, subtotal, biayaLayanan, ongkir, totalDiskon, metodeKirim);
        riwayatTransaksi.add(trx);
        trx.cetakStruk();
    }

    public void simpanLaporanAkhir() {
        try {
            FileOutputStream fos = new FileOutputStream("dump_sys.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.gudang);
            oos.writeObject(this.daftarPelanggan);
            oos.writeObject(this.daftarProduk);
            oos.writeObject(this.riwayatTransaksi);
            oos.close();
            System.out.println("[SERIALIZATION SUKSES] Status sistem tersimpan ke dump_sys.dat");
        } catch (IOException e) {
            System.out.println("[SERIALIZATION GAGAL] " + e.getMessage());
        }

        try {
            FileWriter writer = new FileWriter("laporan.txt");
            writer.write("<laporan_toko nama=\"" + this.namaToko + "\">\n");
            for (Transaksi trx : riwayatTransaksi) {
                writer.write("  <transaksi>"
                        + "<invoice>" + trx.getNomorInvoice() + "</invoice>"
                        + "<aktor>" + trx.getPelanggan().getId() + "</aktor>"
                        + "<nama>" + trx.getPelanggan().getNama() + "</nama>"
                        + "<tier>" + trx.getPelanggan().getTierMember() + "</tier>"
                        + "<produk>" + trx.getProduk().getKode() + "</produk>"
                        + "<qty>" + trx.getQty() + "</qty>"
                        + "<total>" + (long) trx.getTotalBayar() + "</total>"
                        + "</transaksi>\n");
            }
            writer.write("</laporan_toko>\n");
            writer.close();
            System.out.println("[FILE I/O SUKSES] Laporan tag berhasil ditulis ke laporan.txt");
        } catch (IOException e) {
            System.out.println("[FILE I/O GAGAL] " + e.getMessage());
        }
    }
}