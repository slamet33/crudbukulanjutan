package com.iu33.crudbukulanjutan.model;

import com.google.gson.annotations.SerializedName;


public class DataBukuItem {

	@SerializedName("id_buku")
	private String idBuku;

	@SerializedName("id_kategori")
	private String idKategori;

	@SerializedName("foto_buku")
	private String fotoBuku;

	@SerializedName("level")
	private String level;

	@SerializedName("insert_time")
	private String insertTime;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("buku")
	private String buku;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("password")
	private String password;

	@SerializedName("nama")
	private String nama;

	@SerializedName("jenkel")
	private String jenkel;

	@SerializedName("no_telp")
	private String noTelp;

	@SerializedName("username")
	private String username;

	@SerializedName("nama_kategori")
	private String namaKategori;

	public void setIdMakanan(String idBuku){
		this.idBuku= idBuku;
	}

	public String getIdBuku(){
		return idBuku;
	}

	public void setIdKategori(String idKategori){
		this.idKategori = idKategori;
	}

	public String getIdKategori(){
		return idKategori;
	}

	public void setFotoBuku(String fotoBuku){
		this.fotoBuku= fotoBuku;
	}

	public String getFotoBuku(){
		return fotoBuku;
	}

	public void setLevel(String level){
		this.level = level;
	}

	public String getLevel(){
		return level;
	}

	public void setInsertTime(String insertTime){
		this.insertTime = insertTime;
	}

	public String getInsertTime(){
		return insertTime;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setBuku(String Buku){
		this.buku= buku;
	}

	public String getBuku(){
		return buku;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setJenkel(String jenkel){
		this.jenkel = jenkel;
	}

	public String getJenkel(){
		return jenkel;
	}

	public void setNoTelp(String noTelp){
		this.noTelp = noTelp;
	}

	public String getNoTelp(){
		return noTelp;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setNamaKategori(String namaKategori){
		this.namaKategori = namaKategori;
	}

	public String getNamaKategori(){
		return namaKategori;
	}

	@Override
 	public String toString(){
		return 
			"DataBukuItem{" +
			"id_buku= '" + idBuku+ '\'' +
			",id_kategori = '" + idKategori + '\'' + 
			",foto_buku= '" + fotoBuku+ '\'' +
			",level = '" + level + '\'' + 
			",insert_time = '" + insertTime + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",buku = '" + buku+ '\'' +
			",alamat = '" + alamat + '\'' + 
			",password = '" + password + '\'' + 
			",nama = '" + nama + '\'' + 
			",jenkel = '" + jenkel + '\'' + 
			",no_telp = '" + noTelp + '\'' + 
			",username = '" + username + '\'' + 
			",nama_kategori = '" + namaKategori + '\'' + 
			"}";
		}
}