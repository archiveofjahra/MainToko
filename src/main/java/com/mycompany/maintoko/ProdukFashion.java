package com.mycompany.maintoko;

public class ProdukFashion extends Produk implements DapatDikirim, DapatDidiskon {
    private String bahan;
    private String ukuran;

    public ProdukFashion(String kode, String nama, int stok, double hargaSatuan, String bahan, String ukuran) {
        super(kode, nama, stok, hargaSatuan);
        this.bahan = bahan;
        this.ukuran = ukuran;
    }

    @Override
    public String getKategori() {
        return "Fashion & Apparel (" + bahan + " - Ukuran " + ukuran + ")";
    }

    @Override
    public double hitungBiayaLayanan(int qty) {
        return 2500.0 * qty;
    }

    @Override
    public String getMetodePengiriman() {
        return "Kurir Reguler (Pouch Polymailer)";
    }

    @Override
    public double hitungOngkir(int qty) {
        return 12000.0;
    }

    @Override
    public double hitungDiskonProduk(int qty, double subtotal) {
        if (qty >= 2) {
            return subtotal * 0.10;
        }
        return 0.0;
    }
}