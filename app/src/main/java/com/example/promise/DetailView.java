package com.example.promise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailView extends AppCompatActivity {
    Button btn1;
    TextView name, date, time, txt1;
    AlertDialog alertDialog;
    String pname, pdate, pstatus, phour, pmin, pstate, ptext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        btn1 = (Button)findViewById(R.id.btn1);
        name = (TextView)findViewById(R.id.name);
        date = (TextView)findViewById(R.id.Date);
        time = (TextView)findViewById(R.id.Time);
        txt1 = (TextView)findViewById(R.id.txt1);

        Intent intent = getIntent();
        pname = intent.getStringExtra("pname");
        pdate = intent.getStringExtra("pdate");
        pstatus = intent.getStringExtra("pstatus");
        phour = intent.getStringExtra("phour");
        pmin = intent.getStringExtra("pmin");
        pstate = intent.getStringExtra("pstate");
        ptext = intent.getStringExtra("ptext");

        name.setText(pname);
        date.setText(pdate);
        time.setText(pstatus+phour+"시"+pmin+"분");
        txt1.setText(ptext);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                alertDialog.setTitle("약속 생성");
                alertDialog.setMessage("약속을 생성하시겠습니까?");
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
                        Intent intent = new Intent(DetailView.this, ChangePromise.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }
}
