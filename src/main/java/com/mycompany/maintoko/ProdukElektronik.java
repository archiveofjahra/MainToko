package com.mycompany.maintoko;

public class ProdukElektronik extends Produk implements DapatDikirim, DapatDidiskon {
    private int garansiBulan;
    private boolean asuransiWajib;

    public ProdukElektronik(String kode, String nama, int stok, double hargaSatuan, int garansiBulan, boolean asuransiWajib) {
        super(kode, nama, stok, hargaSatuan);
        this.garansiBulan = garansiBulan;
        this.asuransiWajib = asuransiWajib;
    }

    @Override
    public String getKategori() {
        String infoAsuransi = asuransiWajib ? "Berasuransi" : "Standar";
        return "Elektronik & Komputer (Garansi " + garansiBulan + " Bulan - " + infoAsuransi + ")";
    }

    @Override
    public double hitungBiayaLayanan(int qty) {
        double tarifAsuransi = asuransiWajib ? 0.015 : 0.005;
        return (this.hargaSatuan * qty) * tarifAsuransi;
    }

    @Override
    public String getMetodePengiriman() {
        return "Kargo Prioritas + Packing Kayu";
    }

    @Override
    public double hitungOngkir(int qty) {
        return 35000.0 * qty;
    }

    @Override
    public double hitungDiskonProduk(int qty, double subtotal) {
        if (qty >= 3) {
            return subtotal * 0.05;
        }
        return 0.0;
    }
}