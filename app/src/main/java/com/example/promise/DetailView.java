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
    Button btn1,btn2, btn3, btn4;
    TextView name, date, time, txt1, txt2;
    AlertDialog alertDialog;
    String pid, pname, pdate, pstatus, phour, pmin, pstate, ptext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        btn1 = (Button)findViewById(R.id.btn1); //확인버튼
        btn2 = (Button)findViewById(R.id.btn2); //승인버튼
        btn3 = (Button)findViewById(R.id.btn3); //수정버튼
        btn4 = (Button)findViewById(R.id.btn4); //삭제버튼
        name = (TextView)findViewById(R.id.name);   //이름
        date = (TextView)findViewById(R.id.Date);   //날짜
        time = (TextView)findViewById(R.id.Time);   //시간
        txt1 = (TextView)findViewById(R.id.txt1);   //내용
        txt2 = (TextView)findViewById(R.id.txt2);

        //전 액티비티에서 값들 받오오는 부분
        Intent intent = getIntent();
        pid = intent.getStringExtra("id");
        pname = intent.getStringExtra("pname");
        pdate = intent.getStringExtra("pdate");
        pstatus = intent.getStringExtra("pstatus");
        phour = intent.getStringExtra("phour");
        pmin = intent.getStringExtra("pmin");
        pstate = intent.getStringExtra("pstate");
        ptext = intent.getStringExtra("ptext");

        //받아온 값들을 UI에 설정
        name.setText(pname);
        date.setText(pdate);
        time.setText(pstatus+phour+"시"+pmin+"분");
        txt1.setText(ptext);

        if(pstate.equals("0")) {
            txt2.setText("승인X");
        }
        else{
            txt2.setText("승인O");
        }

        //확인 버튼 클릭 시
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                alertDialog.setTitle("약속 확인");
                alertDialog.setMessage("약속리스트 화면으로 되돌아 가시겠습니까?");
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
                        Intent intent = new Intent(DetailView.this, MainActivity.class);

                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //약속 승인 버튼 클릭 시
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pstate.equals("0")){
                    alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                    alertDialog.setTitle("약속 승인");
                    alertDialog.setMessage("약속을 승인하시겠습니까?");
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
                            Intent intent = new Intent(DetailView.this, AgreementPromise.class);
                            intent.putExtra("ptext", ptext);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        //약속수정 버튼 클릭 시
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                alertDialog.setTitle("약속 수정");
                alertDialog.setMessage("약속수정 화면으로 가시겠습니까?");
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

        //약속삭제 버튼 클릭 시
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                alertDialog.setTitle("약속 삭제");
                alertDialog.setMessage("약속을 삭제하시겠습니까?");
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
                        Intent intent = new Intent(DetailView.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }
}
