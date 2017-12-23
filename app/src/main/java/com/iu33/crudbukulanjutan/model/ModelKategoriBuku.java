package com.iu33.crudbukulanjutan.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ModelKategoriBuku{

	@SerializedName("DataKategori")
	private List<DataItem> dataKategori;

	public void setDataKategori(List<DataItem> dataKategori){
		this.dataKategori = dataKategori;
	}

	public List<DataItem> getDataKategori(){
		return dataKategori;
	}

	@Override
 	public String toString(){
		return 
			"ModelKategoriBuku{" + 
			"dataKategori = '" + dataKategori + '\'' + 
			"}";
		}
}