package com.iu33.crudbukulanjutan.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.iu33.crudbukulanjutan.R;
import com.iu33.crudbukulanjutan.adapter.ListBukuAdapter;
import com.iu33.crudbukulanjutan.helper.MyConstant;
import com.iu33.crudbukulanjutan.helper.SessionManager;
import com.iu33.crudbukulanjutan.model.DataBukuItem;
import com.iu33.crudbukulanjutan.model.DataItem;
import com.iu33.crudbukulanjutan.model.ModelBuku;
import com.iu33.crudbukulanjutan.model.ModelKategoriBuku;
import com.iu33.crudbukulanjutan.model.ModelUser;
import com.iu33.crudbukulanjutan.network.MyRetrofitClient;
import com.iu33.crudbukulanjutan.network.ResApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.iu33.crudbukulanjutan.helper.MyConstant.STORAGE_PERMISSION_CODE;

public class BukuActivity extends SessionManager implements ListBukuAdapter.OnItemClicked, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.listbuku)
    RecyclerView listbuku;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;

    List<DataItem> listdatakategoribuku;
    List<DataBukuItem> listdatabuku;
    RecyclerView.LayoutManager layoutManager;
    Dialog dialog, dialog2;
    TextInputEditText edtnamabuku;
    EditText edtidbuku;
    Button btnuploadbuku, btninsert, btnreset, btnupdate, btndelete;
    ImageView imgpreview;
    Uri filepath;
    Bitmap bitmap;
    String strpath, striduser, strnamabuku, stridbuku, strtime, strkategoribuku, strkategori;
    boolean fc = false;
    Spinner spincarikategoribuku;
    Spinner spinnercarikategori, spincariupdatekategori;
    private Target mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku);
        ButterKnife.bind(this);

        requeststoragepermission();

        refreshlayout.setOnRefreshListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        spincarikategoribuku = (Spinner) findViewById(R.id.spincaribuku);
        getDatakategori();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(BukuActivity.this);
                dialog.setContentView(R.layout.tambahbuku);
                dialog.setTitle("Data Buku");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                //inisialisai
                edtidbuku = (TextInputEditText) dialog.findViewById(R.id.edtnamabuku);
                btnuploadbuku = (Button) dialog.findViewById(R.id.btnuploadbuku);
                imgpreview = (ImageView) dialog.findViewById(R.id.imgupload);
                btninsert = (Button) dialog.findViewById(R.id.btninsert);
                btnreset = (Button) dialog.findViewById(R.id.btnreset);

                spinnercarikategori = (Spinner) dialog.findViewById(R.id.spincarikategori);
                btnuploadbuku.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showfilechooser(MyConstant.REQ_FILE_CHOOSE);
                        fc = true;
                    }
                });
                getDataKategori2(spinnercarikategori);

                btninsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strnamabuku = edtnamabuku.getText().toString();
                        if (TextUtils.isEmpty(strnamabuku)) {
                            edtnamabuku.setError("Nama Buku Harus Diisi");
                        } else if (fc == false) {
                            myToast("Gambar Harus Dipilih");
                        } else if (imgpreview.getDrawable() == null) {
                            myToast("Gambar Harus Dipilih");
                        } else {
                            insertdatabuku(strkategori);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void insertdatabuku(String strkategori) {
        try {
            strpath = getPath(filepath);
            striduser = sessionManager.getIdUser();
        } catch (Exception e) {
            myToast("Gambar Terlalu besar\n Silahkan Pilih Gambar Yang Lebih Kecil");
            e.printStackTrace();
        }
        strtime = currentDate();
        try {
            new MultipartUploadRequest(c, MyConstant.UPLOAD_URL)
                    .addFileToUpload(strpath, "image")
                    .addParameter("vsiduser", striduser)
                    .addParameter("vsnamabuku", strnamabuku)
                    .addParameter("vstimeinsert", strtime)
                    .addParameter("vskategori", strkategori)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
            getDataBuku(strkategoribuku);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getPath(Uri filepath) {
        Cursor cursor = getContentResolver().query(filepath, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + "= ?", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void getDataKategori2(final Spinner spinnercarikategori) {
        showProgressDialog("Proses GetData Buku");
        String iduser = sessionManager.getIdUser();

        ResApi api = MyRetrofitClient.getInstaceRetrofit();
        Call<ModelKategoriBuku> modelUserCall = api.getDataCarikategoriBuku();
        modelUserCall.enqueue(new Callback<ModelKategoriBuku>() {
            @Override
            public void onResponse(Call<ModelKategoriBuku> call, Response<ModelKategoriBuku> response) {
                hideProgressDialog();
                listdatakategoribuku = new ArrayList<DataItem>();

                listdatakategoribuku = response.body().getDataKategori();
                final String[] items = new String[listdatakategoribuku.size()];
                final String[] itemsnama = new String[listdatakategoribuku.size()];

                for (int i = 0; i < listdatakategoribuku.size(); i++) {
                    items[i] = listdatakategoribuku.get(i).getIdKategori().toString();
                    itemsnama[i] = listdatakategoribuku.get(i).getNamaKategori().toString();
                }
                ArrayAdapter adapter = new ArrayAdapter(BukuActivity.this, android.R.layout.simple_spinner_dropdown_item, itemsnama);
                spinnercarikategori.setAdapter(adapter);
                strkategori = items[0].toString();

                spinnercarikategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        strkategori = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ModelKategoriBuku> call, Throwable t) {
                hideProgressDialog();
                myToast("gagal" + t.getMessage());

            }
        });
    }

    private void showfilechooser(int reqFileChoose) {
        Intent intentgallery = new Intent();
        intentgallery.setType("image/*"); //akan menampilkan seluruh gambar digaleri
        intentgallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentgallery, "Select Picture"), reqFileChoose);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstant.REQ_FILE_CHOOSE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imgpreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    private void requeststoragepermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void getDatakategori() {
        ResApi api = MyRetrofitClient.getInstaceRetrofit();
        Call<ModelKategoriBuku> modelUserCall = api.getDataCarikategoriBuku();
        modelUserCall.enqueue(new Callback<ModelKategoriBuku>() {
            @Override
            public void onResponse(Call<ModelKategoriBuku> call, Response<ModelKategoriBuku> response) {
                listdatakategoribuku= new ArrayList<DataItem>();
                hideProgressDialog();
                listdatakategoribuku = response.body().getDataKategori();
                final String[] items = new String[listdatakategoribuku.size()];
                final String[] itemsnama = new String[listdatakategoribuku.size()];
                for (int i = 0; i < listdatakategoribuku.size(); i++) {
                    //Storing names to string array
                    items[i] = listdatakategoribuku.get(i).getIdKategori().toString();
                    itemsnama[i] = listdatakategoribuku.get(i).getNamaKategori().toString();

                }
                ArrayAdapter adapter = new ArrayAdapter(BukuActivity.this, android.R.layout.simple_spinner_dropdown_item, itemsnama);
                spincarikategoribuku.setAdapter(adapter);
                strkategoribuku = items[0].toString();

                spincarikategoribuku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //cara 1 untuk menampilakan brdasarkan nama
                        strkategoribuku= parent.getItemAtPosition(position).toString();
                        //tvhargajual.setText("");
                        //cara 2 untuk menampilkan brdasarkan posisi
                        //strnmkota = items[position];

                        getDataBuku(strkategoribuku);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });
            }

            @Override
            public void onFailure(Call<ModelKategoriBuku> call, Throwable t) {

            }

        });
    }

    private void getDataBuku(String strkategoribuku) {
        final ProgressDialog dialog = ProgressDialog.show(BukuActivity.this, "Proses Register User", "Mohon Bersabar");
        String iduser = sessionManager.getIdUser();
        ResApi api = MyRetrofitClient.getInstaceRetrofit();
        Call<ModelBuku> modelUserCall = api.getdatabuku(
                iduser, strkategoribuku);
        modelUserCall.enqueue(new Callback<ModelBuku>() {
            @Override
            public void onResponse(Call<ModelBuku> call, Response<ModelBuku> response) {
                dialog.dismiss();
                hideProgressDialog();
                listdatabuku = response.body().getDataBuku();
                String[] id_buku = new String[listdatabuku.size()];
                String[] namabuku = new String[listdatabuku.size()];
                String[] fotomakanan = new String[listdatabuku.size()];
                for (int i = 0; i < listdatabuku.size(); i++) {
                    id_buku[i] = listdatabuku.get(i).getIdBuku().toString();
                    namabuku[i] = listdatabuku.get(i).getBuku().toString();
                    fotomakanan[i] = listdatabuku.get(i).getBuku().toString();
                    striduser = id_buku[i];
                }
                ListBukuAdapter adapter = new ListBukuAdapter(c, listdatabuku);
                listbuku.setAdapter(adapter);
                adapter.setOnClick(BukuActivity.this);
            }

            @Override
            public void onFailure(Call<ModelBuku> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mn_logout) {
            sessionManager.logout();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRefresh() {
        getDataBuku(strkategoribuku);
        refreshlayout.setRefreshing(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                myToast("Permission granted now you can read the storage");
            } else {
                //Displaying another toast if permission is not granted
                myToast("Oops you just denied the permission");
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        dialog2 = new Dialog(BukuActivity.this);
        dialog2.setTitle("Update Data Buku");
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setContentView(R.layout.updatebuku);
        dialog2.show();
//        inisialisasi
        edtnamabuku = (TextInputEditText) dialog2.findViewById(R.id.edtnamabuku);
        edtidbuku = (EditText) dialog2.findViewById(R.id.edtidbuku);
        btnuploadbuku = (Button) dialog2.findViewById(R.id.btnuploadbuku);
        imgpreview = (ImageView) dialog2.findViewById(R.id.imgupload);
        btnupdate = (Button) dialog2.findViewById(R.id.btnupdate);
        btndelete = (Button) dialog2.findViewById(R.id.btndelete);
        spinnercarikategori = (Spinner) dialog2.findViewById(R.id.spincarikategori);

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imgpreview.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(c)
                .load(MyConstant.IMAGE_URL + listdatabuku.get(position).getFotoBuku().toString())
                .into(mTarget);

        getDataKategori2(spincariupdatekategori);
        edtnamabuku.setText(listdatabuku.get(position).getBuku());
        edtidbuku.setText(listdatabuku.get(position).getIdBuku());
        btnuploadbuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfilechooser(MyConstant.REQ_FILE_CHOOSE);
            }
        });
        spinnercarikategori.setPrompt(listdatakategoribuku.get(position).getNamaKategori());
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stridbuku = edtnamabuku.getText().toString();
                hapusdatabuku();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    strpath = getPath(filepath);
                    striduser = sessionManager.getIdUser();

                } catch (Exception e) {
//                    myToast("gambar terlalu besar \n silahkan pilih gambar yang lebih kecil");
                    e.printStackTrace();
                }

                strnamabuku = edtnamabuku.getText().toString();
                stridbuku = edtidbuku.getText().toString();
                if (TextUtils.isEmpty(strnamabuku)) {
                    edtnamabuku.setError("Nama Buku Tidak Boleh Kosong");
                    edtnamabuku.requestFocus();
                    myanimation(edtnamabuku);
                } else if (imgpreview.getDrawable() == null) {
                    myToast("gambar harus dipilih");
                } else if (strpath == null) {
                    myToast("gambar harus diganti");

                } else {
                    /**
                     * Sets the maximum time to wait in milliseconds between two upload attempts.
                     * This is useful because every time an upload fails, the wait time gets multiplied by
                     * {@link UploadService#BACKOFF_MULTIPLIER} and it's not convenient that the value grows
                     * indefinitely.
                     */

                    try {
                        new MultipartUploadRequest(c, MyConstant.UPLOAD_UPDATE_URL)
                                .addFileToUpload(strpath, "image")
                                .addParameter("vsidbuku", stridbuku)
                                .addParameter("vsnamabuku", strnamabuku)
                                .addParameter("vsidkategori", strkategori)
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)

                                .startUpload();


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        myToast(e.getMessage());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        myToast(e.getMessage());
                    }

                    dialog2.dismiss();
                }
            }


        });
    }

    private void hapusdatabuku() {
        showProgressDialog(("Proses Get Data Makanan"));
        ResApi api = MyRetrofitClient.getInstaceRetrofit();
        Call<ModelUser> modelbukucall = api.deleteData(
                stridbuku
        );
        modelbukucall.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
                String result = response.body().getResult();
                String msg = response.body().getMsg();
                if (result.equals("1")) {
                    myToast(msg);
                    dialog2.dismiss();
                    getDataBuku(strkategoribuku);
                } else {
                    myToast(msg);
                    dialog2.setCancelable(true);
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ModelUser> call, Throwable t) {myToast((t.getMessage()));}
        });
    }
}
