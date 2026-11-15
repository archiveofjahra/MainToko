package com.mycompany.maintoko;

public class ProdukKonsumsi extends Produk implements DapatDikirim {
    private int masaSimpanHari;
    private int beratGram;

    public ProdukKonsumsi(String kode, String nama, int stok, double hargaSatuan, int masaSimpanHari, int beratGram) {
        super(kode, nama, stok, hargaSatuan);
        this.masaSimpanHari = masaSimpanHari;
        this.beratGram = beratGram;
    }

    @Override
    public String getKategori() {
        return "Produk Konsumsi (" + beratGram + "gr - Masa Simpan " + masaSimpanHari + " Hari)";
    }

    @Override
    public double hitungBiayaLayanan(int qty) {
        return 1500.0;
    }

    @Override
    public String getMetodePengiriman() {
        return "Kurir Instan / Same-Day (Food Grade Box)";
    }

    @Override
    public double hitungOngkir(int qty) {
        return 15000.0;
    }
}