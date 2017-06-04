package com.example.dell.actplus;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Loginpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        setUpDefalutFragment();
    }
    private void setUpDefalutFragment() {
        FragmentManager fragmentManager = this.getFragmentManager();
        login login = new login();
        fragmentManager.beginTransaction().replace(R.id.login_fragment, login).commit();
    }
}
