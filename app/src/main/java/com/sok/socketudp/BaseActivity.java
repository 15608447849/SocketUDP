package com.sok.socketudp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    public void toClient(View view){
            startActivity(new Intent(this,ClientActivity.class));
    }
    public void toServer(View view){
        startActivity(new Intent(this,ServerActivity.class));
    }
}
