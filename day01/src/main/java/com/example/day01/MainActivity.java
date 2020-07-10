package com.example.day01;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.day01.Not.ApiService;
import com.google.gson.Gson;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user;
    private EditText psw;
    private Button den;
    private Button zhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        user = (EditText) findViewById(R.id.user);
        psw = (EditText) findViewById(R.id.psw);
        den = (Button) findViewById(R.id.den);
        zhu = (Button) findViewById(R.id.zhu);

        den.setOnClickListener(this);
        zhu.setOnClickListener(this);
        Intent intent = getIntent();
        String user1 = intent.getStringExtra("aaa");
        String user2 = intent.getStringExtra("bbb");
        user.setText(user1);
        psw.setText(user2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.den:
                Retrofit1();
                break;
            case R.id.zhu:
                startActivity(new Intent(MainActivity.this,ZhuceActivity.class));
                break;
        }
    }

    private void Retrofit1() {
        String use = user.getText().toString();
        final String s = psw.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<ResponseBody> bean = apiService.getBean(use, s);
        bean.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                          Log.i("111",string);
                            Bean json = new Gson().fromJson(string, Bean.class);
                            if (json.getErrorCode()==0){
                                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

}
