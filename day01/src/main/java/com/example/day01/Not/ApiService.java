package com.example.day01.Not;

import com.example.day01.ReanBean;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    //登录
    String Base_url="https://www.wanandroid.com/";
    @POST("user/login")
    @FormUrlEncoded
            Observable<ResponseBody>getBean(@Field("username")String username,@Field("password") String password);

    //验证码
    String Base_yan="http://yun918.cn/";
    @GET("study/public/index.php/verify")
            Observable<ReanBean>getData();
    String s ="222";
    //注册
    String Base_Url1="https://www.wanandroid.com/";
    @POST("user/register")
    @FormUrlEncoded
    Observable<ResponseBody>getBody(@Field("username")String username,@Field("password") String password ,@Field("repassword") String repassword);

}
