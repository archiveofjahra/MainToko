package com.mycompany.maintoko;

import java.io.Serializable;

public class Pelanggan implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nama;
    private String tierMember;
    private int poinReward;
    private double totalBelanja;

    public Pelanggan(String id, String nama) {
        this.id = id;
        this.nama = nama;
        this.tierMember = "Regular";
        this.poinReward = 0;
        this.totalBelanja = 0.0;
    }

    public Pelanggan(String id, String nama, double saldoBelanjaAwal) {
        this.id = id;
        this.nama = nama;
        this.tierMember = "Regular";
        this.poinReward = 0;
        this.totalBelanja = 0.0;
        catatTransaksiBelanja(saldoBelanjaAwal);
    }

    public double hitungDiskonMember(double subtotal) {
        if (this.tierMember.equals("Gold")) {
            return subtotal * 0.05;
        } else if (this.tierMember.equals("Silver")) {
            return subtotal * 0.02;
        }
        return 0.0;
    }

    public void catatTransaksiBelanja(double nominal) {
        if (nominal > 0) {
            this.totalBelanja = this.totalBelanja + nominal;
            this.poinReward = this.poinReward + (int) (nominal / 10000);
            perbaruiTierMember();
        }
    }

    public void catatTransaksiBelanja(double nominal, int bonusPoin) {
        catatTransaksiBelanja(nominal);
        this.poinReward = this.poinReward + bonusPoin;
    }

    private void perbaruiTierMember() {
        if (this.totalBelanja >= 20000000) {
            this.tierMember = "Gold";
        } else if (this.totalBelanja >= 5000000) {
            this.tierMember = "Silver";
        }
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getTierMember() {
        return tierMember;
    }

    public int getPoinReward() {
        return poinReward;
    }

    public double getTotalBelanja() {
        return totalBelanja;
    }
}