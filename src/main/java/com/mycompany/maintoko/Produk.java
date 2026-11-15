package com.mycompany.maintoko;

import java.io.Serializable;

public abstract class Produk implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String kode;
    protected String nama;
    protected int stok;
    protected double hargaSatuan;

    public static int jumlahProdukTerdaftar = 0;

    public Produk(String kode, String nama, int stok, double hargaSatuan) {
        this.kode = kode;
        this.nama = nama;
        this.stok = stok;
        this.hargaSatuan = hargaSatuan;
        jumlahProdukTerdaftar++;
    }

    public void kurangiStok(int jumlah) throws TransaksiException {
        if (jumlah <= 0) {
            throw new TransaksiException("Kuantitas pembelian tidak valid (" + jumlah + "). Minimal beli 1 unit.");
        }
        if (this.stok < jumlah) {
            throw new TransaksiException("Stok '" + this.nama + "' (" + this.kode + ") tidak mencukupi. Tersedia: " + this.stok + ", Diminta: " + jumlah);
        }
        this.stok = this.stok - jumlah;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public int getStok() {
        return stok;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public abstract String getKategori();
    public abstract double hitungBiayaLayanan(int qty);
}