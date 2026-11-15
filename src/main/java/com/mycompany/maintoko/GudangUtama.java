package com.mycompany.maintoko;

import java.io.Serializable;

public class GudangUtama implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kodeGudang;
    private String lokasi;
    private int totalUnitKeluar;

    public GudangUtama(String kodeGudang, String lokasi) {
        this.kodeGudang = kodeGudang;
        this.lokasi = lokasi;
        this.totalUnitKeluar = 0;
    }

    public void catatBarangKeluar(String kodeProduk, String namaProduk, int qty) {
        this.totalUnitKeluar = this.totalUnitKeluar + qty;
        System.out.println("   [GUDANG " + kodeGudang + " - " + lokasi + "] Mengeluarkan "
                + qty + " unit '" + namaProduk + "' (" + kodeProduk + ")");
    }

    public int getTotalUnitKeluar() {
        return totalUnitKeluar;
    }
}