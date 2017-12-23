package com.iu33.crudbukulanjutan.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ModelBuku {

	@SerializedName("DataBuku")
	private List<DataBukuItem> dataBuku;

	public void setDataBuku(List<DataBukuItem> dataBuku){
		this.dataBuku = dataBuku;
	}

	public List<DataBukuItem> getDataBuku(){
		return dataBuku;
	}

	@Override
 	public String toString(){
		return 
			"ModelBuku{" +
			"dataBuku = '" + dataBuku + '\'' +
			"}";
		}
}