package com.iu33.crudbukulanjutan.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.iu33.crudbukulanjutan.R;
import com.iu33.crudbukulanjutan.helper.MyFunction;
import com.iu33.crudbukulanjutan.model.ModelUser;
import com.iu33.crudbukulanjutan.network.MyRetrofitClient;
import com.iu33.crudbukulanjutan.network.ResApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends MyFunction {

    @BindView(R.id.edtnama)
    EditText edtnama;
    @BindView(R.id.edtalamat)
    EditText edtalamat;
    @BindView(R.id.edtnotelp)
    EditText edtnotelp;
    @BindView(R.id.spinjenkel)
    Spinner spinjenkel;
    @BindView(R.id.edtusername)
    EditText edtusername;
    @BindView(R.id.edtpassword)
    TextInputEditText edtpassword;
    @BindView(R.id.edtpasswordconfirm)
    TextInputEditText edtpasswordconfirm;
    @BindView(R.id.regAdmin)
    RadioButton regAdmin;
    @BindView(R.id.regUserbiasa)
    RadioButton regUserbiasa;
    @BindView(R.id.btnregister)
    Button btnregister;
    String jenkel[] = {"laki-laki", "perempuan"};
    String strnama, strusername, strpassword, strconpassword, stralamat, strlevel, strjenkel, strnohp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        if (regAdmin.isChecked()) {
            strlevel = "Admin";
        } else {
            strlevel = "User Biasa";
        }
        ArrayAdapter adapter = new ArrayAdapter(c, android.R.layout.simple_spinner_item, jenkel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinjenkel.setAdapter(adapter);
        spinjenkel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strjenkel = jenkel[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick({R.id.regAdmin, R.id.regUserbiasa, R.id.btnregister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regAdmin:
                strlevel = "Admin";
                break;
            case R.id.regUserbiasa:
                strlevel = "User Biasa";
                break;
            case R.id.btnregister:
                strnama = edtnama.getText().toString();
                stralamat = edtalamat.getText().toString();
                strnohp = edtnotelp.getText().toString();
                strusername = edtusername.getText().toString();
                strpassword = edtpassword.getText().toString();
                strconpassword = edtpasswordconfirm.getText().toString();
                if (TextUtils.isEmpty(strnama)) {
                    edtnama.setError("Nama Tidak Boleh Kosong");
                    edtnama.requestFocus();
                    myanimation(edtnama);
                } else if (TextUtils.isEmpty(stralamat)) {
                    edtalamat.setError("Alamat Tidak Boleh Kosong");
                    edtalamat.requestFocus();
                    myanimation(edtalamat);
                } else if (TextUtils.isEmpty(strnohp)) {
                    edtnotelp.setError("No Hp Tidak Boleh Kosong");
                    edtnotelp.requestFocus();
                    myanimation(edtnotelp);
                } else if (TextUtils.isEmpty(strusername)) {
                    edtusername.setError("Username Tidak Boleh Kosong");
                    edtusername.requestFocus();
                    myanimation(edtusername);
                } else if (TextUtils.isEmpty(strpassword)) {
                    edtpassword.setError("Password Tidak Boleh Kosong");
                    edtpassword.requestFocus();
                    myanimation(edtpassword);
                } else if (TextUtils.isEmpty(strconpassword)) {
                    edtpasswordconfirm.setError("Konnfirmasi Passwor Tidak Boleh Kosong");
                    edtpasswordconfirm.requestFocus();
                    myanimation(edtpasswordconfirm);
                } else if (!strpassword.equals(strconpassword)) {
                    edtpasswordconfirm.setError("Password Tidak Sama");
                    edtpasswordconfirm.requestFocus();
                    myanimation(edtpasswordconfirm);
                } else {
                    registeruser();
                }
                break;
        }
    }

    private void registeruser() {
        final ProgressDialog dialog = ProgressDialog.show(c, "Proses Register User", "Mohon Tunggu");

        ResApi api = MyRetrofitClient.getInstaceRetrofit();
        retrofit2.Call<ModelUser> modelUserCall =api.registeruser(
                strnama, stralamat, strnohp, strjenkel, strusername, strlevel,strpassword
                );
        //menangkap CallBack
        modelUserCall.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(retrofit2.Call<ModelUser> call, Response<ModelUser> response) {
                dialog.dismiss();
                String result = response.body().getResult();
                String msg = response.body().getMsg();
                if (result.equals("1")) {
                    myToast(msg);
                    myIntent(LoginActivity.class);
                    finish();
                } else {
                    myToast(msg);
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ModelUser> call, Throwable t) {
                dialog.dismiss();
                myToast("Gagal Koneksi :" + t.getMessage());
            }
        });
    }
}
