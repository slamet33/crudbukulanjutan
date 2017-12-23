package com.iu33.crudbukulanjutan.model;

import com.google.gson.annotations.SerializedName;

public class DataItem {

	@SerializedName("id_kategori")
	private String idKategori;

	@SerializedName("nama_kategori")
	private String namaKategori;

	public void setIdKategori(String idKategori){
		this.idKategori = idKategori;
	}

	public String getIdKategori(){
		return idKategori;
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
			"DataItem{" +
			"id_kategori = '" + idKategori + '\'' + 
			",nama_kategori = '" + namaKategori + '\'' + 
			"}";
		}
}