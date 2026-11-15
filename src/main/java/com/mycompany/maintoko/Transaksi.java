package com.mycompany.maintoko;

import java.io.Serializable;

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nomorUrut = 1;

    private String nomorInvoice;
    private Pelanggan pelanggan;
    private Produk produk;
    private int qty;
    private double subtotal;
    private double biayaLayanan;
    private double ongkir;
    private double totalDiskon;
    private double totalBayar;
    private String metodeKirim;

    public Transaksi(Pelanggan pelanggan, Produk produk, int qty, double subtotal,
                     double biayaLayanan, double ongkir, double totalDiskon, String metodeKirim) {
        this.nomorInvoice = "INV-2026-" + nomorUrut;
        nomorUrut++;
        this.pelanggan = pelanggan;
        this.produk = produk;
        this.qty = qty;
        this.subtotal = subtotal;
        this.biayaLayanan = biayaLayanan;
        this.ongkir = ongkir;
        this.totalDiskon = totalDiskon;
        this.metodeKirim = metodeKirim;
        this.totalBayar = (subtotal + biayaLayanan + ongkir) - totalDiskon;
    }

    public String getNomorInvoice() {
        return nomorInvoice;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public Produk getProduk() {
        return produk;
    }

    public int getQty() {
        return qty;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    private String formatRupiah(double nominal) {
        long nilai = (long) nominal;
        if (nilai == 0) {
            return "Rp 0";
        }
        String hasil = "";
        int hitung = 0;
        while (nilai > 0) {
            if (hitung > 0 && hitung % 3 == 0) {
                hasil = "." + hasil;
            }
            hasil = (nilai % 10) + hasil;
            nilai = nilai / 10;
            hitung++;
        }
        return "Rp " + hasil;
    }

    public void cetakStruk() {
        System.out.println();
        System.out.println("==========================================================");
        System.out.println("INVOICE     : " + nomorInvoice);
        System.out.println("Pelanggan   : " + pelanggan.getNama() + " (" + pelanggan.getId() + ") | Tier: " + pelanggan.getTierMember());
        System.out.println("Produk      : " + produk.getNama() + " (" + produk.getKode() + ") x " + qty);
        System.out.println("Kategori    : " + produk.getKategori());
        System.out.println("Ekspedisi   : " + metodeKirim);
        System.out.println("Subtotal    : " + formatRupiah(subtotal));
        System.out.println("Biaya Lay.  : " + formatRupiah(biayaLayanan) + " | Ongkir: " + formatRupiah(ongkir) + " | Diskon: -" + formatRupiah(totalDiskon));
        System.out.println("TOTAL BAYAR : " + formatRupiah(totalBayar) + " (Sisa Stok: " + produk.getStok() + " | Poin: " + pelanggan.getPoinReward() + ")");
        System.out.println("==========================================================");
        System.out.println();
    }
}