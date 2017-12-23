package com.iu33.crudbukulanjutan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.iu33.crudbukulanjutan.R;
import com.iu33.crudbukulanjutan.helper.SessionManager;
import com.iu33.crudbukulanjutan.model.ModelUser;
import com.iu33.crudbukulanjutan.network.MyRetrofitClient;
import com.iu33.crudbukulanjutan.network.ResApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends SessionManager {

    @BindView(R.id.regUsername)
    EditText regUsername;
    @BindView(R.id.regPass)
    EditText regPass;
    @BindView(R.id.regAdmin)
    RadioButton regAdmin;
    @BindView(R.id.regUserbiasa)
    RadioButton regUserbiasa;
    @BindView(R.id.regBtnLogin)
    Button regBtnLogin;
    @BindView(R.id.regBtnRegister)
    Button regBtnRegister;
    //Variable ?
    String strlevel,strusername,strpassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //Kondoisi Memilih Admin/Userbiasa saat login
        if (regAdmin.isChecked()) {
            strlevel="admin";
        } else{
            strlevel="user biasa";
        }
    }

    @OnClick({R.id.regAdmin, R.id.regUserbiasa, R.id.regBtnLogin, R.id.regBtnRegister})
    public void onViewClicked(View view) {
        strusername = regUsername.getText().toString();
        strpassword = regPass.getText().toString();

        switch (view.getId()) {
            case R.id.regAdmin:
                strlevel = "admin";
                break;
            case R.id.regUserbiasa:
                strlevel = "user biasa";
                break;
            case R.id.regBtnLogin:
                //TextUtils Adalah seperangkat utility function yang bisa di gunakan untuk melakukan manipulasi pada String Object
                if (TextUtils.isEmpty(strusername)) {
                    regUsername.setError("Username Harus Diisi");
                } else if(TextUtils.isEmpty(strpassword)) {
                    regPass.setError("Password Harus Diisi");
                } else if (strpassword.length() < 8) {
                    regPass.setError("Minimal Password 8 Karakter");
                } else{
                    loginuser();
                }
                break;
            case R.id.regBtnRegister:
                myIntent(RegisterActivity.class);
                break;
        }
    }

    private void loginuser() {
        showProgressDialog("Proses Login User");
        //Membuat resapi ssekaligus
        ResApi api = MyRetrofitClient.getInstaceRetrofit();
        //memanggil model
        retrofit2.Call<ModelUser> modelUserCall = api.loginuser(
                strusername, strpassword,strlevel
        );
        modelUserCall.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(retrofit2.Call<ModelUser> call, Response<ModelUser> response) {
                hideProgressDialog();
                String result = response.body().getResult();
                String msg = response.body().getMsg();
                if(result.equals("1")){
                    myIntent(BukuActivity.class);
                    String iduser = response.body().getUser().getIdUser();
                    sessionManager.createSession(strusername);
                    sessionManager.setIdUser(iduser);
                    finish();
                } else{
                    myToast(msg);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ModelUser> call, Throwable t) {
                hideProgressDialog();
                myToast("Gagal Koneksi : "+t.getMessage() );
            }
        });
    }
}
