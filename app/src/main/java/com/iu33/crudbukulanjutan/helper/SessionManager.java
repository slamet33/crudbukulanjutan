package com.iu33.crudbukulanjutan.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.iu33.crudbukulanjutan.R;
import com.iu33.crudbukulanjutan.activity.LoginActivity;

/**
 * Created by hp on 12/10/2017.
 */

public class SessionManager extends MyFunction {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getApplicationContext());
        setTheme(R.style.MyAppTheme);
    }
    public SessionManager sessionManager;
    SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public static final String pref_name ="crudpref";
    private static final String is_login = "islogin";
    public static final String kunci_email = "keyemail";
//    method untuk Session
    public SessionManager(Context context){
        c = context;
        pref=context.getSharedPreferences(pref_name, 0);
        //login value menjadi true
        editor = pref.edit();
    }
    public void createSession(String email){
        editor.putBoolean(is_login, true);
        editor.putString(kunci_email, email);
        editor.commit();
    }
    public void setIdUser(String idUser){
        editor.putBoolean(is_login, true);
        editor.putString("iduser", idUser);
        editor.commit();
    }
    public SessionManager(){}

    public String getIdUser() {
        return pref.getString("iduser", "");
    }
    public void logout(){

        /*hapus semua data dan kunci*/
        editor.clear();
        editor.commit();

        //gmail logout


        /*pergi ke loginactivity*/
        Intent i = new Intent(c, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }
}