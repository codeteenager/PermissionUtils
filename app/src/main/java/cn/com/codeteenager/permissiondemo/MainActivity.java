package cn.com.codeteenager.permissiondemo;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.com.codeteenager.permissionlibrary.PermissionFail;
import cn.com.codeteenager.permissionlibrary.PermissionHelper;
import cn.com.codeteenager.permissionlibrary.PermissionSuccess;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                PermissionHelper.with(MainActivity.this).requestCode(REQUEST_CODE).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}).request();
            }
        });
    }

    @PermissionSuccess(requestCode = REQUEST_CODE)
    private void success() {
        Toast.makeText(this, "申请", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUEST_CODE)
    private void fali() {
        Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions);
    }
}
