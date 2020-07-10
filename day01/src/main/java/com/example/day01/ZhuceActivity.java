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

public class ZhuceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_user;
    private EditText et_password;
    private EditText et_psw;
    private EditText et_phone;
    private EditText yanzhen;
    private Button et_zhu;
    private Button but_yan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        initView();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);
        et_psw = (EditText) findViewById(R.id.et_psw);
        et_phone = (EditText) findViewById(R.id.et_phone);
        yanzhen = (EditText) findViewById(R.id.yanzhen);
        et_zhu = (Button) findViewById(R.id.et_zhu);
        et_zhu.setOnClickListener(this);
        but_yan = (Button) findViewById(R.id.but_yan);
        but_yan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_zhu:
                RetrofitData();
                break;
            case R.id.but_yan:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiService.Base_yan)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Observable<ReanBean> data = apiService.getData();
                data.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ReanBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ReanBean reanBean) {
                                String beanData = reanBean.getData();
                                yanzhen.setText(beanData);

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
        }
    }

    public void RetrofitData() {
        final String us = et_user.getText().toString();
        final String ps = et_password.getText().toString();
        String ph = et_phone.getText().toString();
        String yan = yanzhen.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.Base_Url1)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<ResponseBody> body = apiService.getBody(us, ps, et_psw.getText().toString());
        body.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            Log.i("111", string);
                            Bean bean = new Gson().fromJson(string, Bean.class);
                            int errorCode = bean.getErrorCode();
                            String errorMsg = bean.getErrorMsg();
                            if(errorCode==0){
                            Toast.makeText(ZhuceActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ZhuceActivity.this, MainActivity.class);
                                intent.putExtra("aaa",us);
                                intent.putExtra("bbb",ps);
                                startActivity(intent);
                                finish();
                            }else {
                                    if(errorMsg!=null){
                                        Toast.makeText(ZhuceActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
                                    }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ZhuceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
