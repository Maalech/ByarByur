package com.byarbyur.myapplication.model;

public class Pesanan {
    private String usahaid;
    private String userid;
    private String nama;
    private String alamat;
    private  String status;
    private String contact;
    private String tanggal;
    private String waktu;

    public Pesanan(){

    }

    public Pesanan(String usahaid, String userid, String nama, String alamat, String status, String contact, String tanggal, String waktu) {
        this.usahaid = usahaid;
        this.userid = userid;
        this.nama = nama;
        this.alamat = alamat;
        this.status = status;
        this.contact = contact;
        this.tanggal = tanggal;
        this.waktu = waktu;
    }

    public String getUsahaid() {
        return usahaid;
    }

    public void setUsahaid(String usahaid) {
        this.usahaid = usahaid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
