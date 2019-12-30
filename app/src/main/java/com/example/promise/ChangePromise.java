package com.example.promise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChangePromise extends AppCompatActivity {
    Button btn1;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_promise);

        btn1 = (Button)findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(ChangePromise.this).create();
                alertDialog.setTitle("약속 수정");
                alertDialog.setMessage("수정한 약속을 상대방에게 보내겠습니까?");
                alertDialog.setCancelable(false);

                //취소 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                //확인 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ChangePromise.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }
}
