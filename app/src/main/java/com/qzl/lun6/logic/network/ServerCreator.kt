package com.qzl.lun6.logic.network


import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


object ServerCreator {
    private const val BASE_URL = "https://jwcjwxt2.fzu.edu.cn:81/"/*"http://192.168.31.186/"*/

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(myClient())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    private fun myClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)//3s超时
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .cookieJar(LocalCookieJar())
            .followRedirects(true)
            .build()

    //CookieJar是用于保存Cookie的
    internal class LocalCookieJar : CookieJar {

        private val cookieStore = HashMap<String, MutableList<Cookie>>()

        init {
            // TODO: 2021/6/29 从本地活得cookie
        }


        override fun loadForRequest(arg0: HttpUrl): List<Cookie> {
            val cookies = cookieStore[arg0.host()]
            return cookies ?: ArrayList()
            //Cookie.Builder().value().build()
        }

        override fun saveFromResponse(arg0: HttpUrl, cookies: List<Cookie>) {

            if (cookieStore[arg0.host()] == null) {
                cookieStore[arg0.host()] = cookies.toMutableList()
            } else {
                for (c in cookies) {
                    cookieStore[arg0.host()]!!.removeAll {
                        it.name() == c.name()
                    }
                }
                cookieStore[arg0.host()]!!.addAll(cookies)
            }

            // TODO: 2021/6/29 把cookie存到本地

        }
    }
}