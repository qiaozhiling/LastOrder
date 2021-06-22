package internet

import internet.NetUtils.getCookie
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface FzuServer {

    /**
     * 1验证码图片 https://jwcjwxt2.fzu.edu.cn:82/plus/verifycode.asp
     */
    @GET("https://jwcjwxt2.fzu.edu.cn:82/plus/verifycode.asp")
    suspend fun verifyCode(
        //@Header("Cookie") cookie: List<String> = getCookie()
    ): ResponseBody

    /**
     * 2登入接口 https://jwcjwxt2.fzu.edu.cn:82/logincheck.asp
     */
    @POST("https://jwcjwxt2.fzu.edu.cn:82/logincheck.asp")
    @Headers("referer:https://jwch.fzu.edu.cn/")
    @FormUrlEncoded
    suspend fun loginCheck(
        @FieldMap fieldMap: Map<String, String>,
        //@Header("Cookie") cookie: List<String> = getCookie()
    ): ResponseBody

    /**
     * 3 验证SSOLogin https://jwcjwxt2.fzu.edu.cn/Sfrz/SSOLogin
     */
    @POST("https://jwcjwxt2.fzu.edu.cn/Sfrz/SSOLogin")
    @Headers("X-Requested-With:XMLHttpRequest")
    @FormUrlEncoded
    suspend fun ssoLogin(
        @Field("token") token: String,
        //@Header("Cookie") cookie: List<String> = getCookie(),
        //@HeaderMap headerMap: Map<String, String>
    ): String

    /**
     *  4 loginchk_xs https://jwcjwxt2.fzu.edu.cn:81/loginchk_xs.aspx
     */
    @GET("loginchk_xs.aspx")
    suspend fun loginchk_xs(
        @QueryMap queryMap: Map<String, String>,
        //@HeaderMap map: Map<String, String>
        //@Header("Cookie") cookie: List<String> = getCookie(),
        //@HeaderMap headerMap: Map<String, String>
    ): Response<String>

    /**
     *  5我的选课 https://jwcjwxt2.fzu.edu.cn:81/student/xkjg/wdxk/xkjg_list.aspx
     */
    @POST("student/xkjg/wdxk/xkjg_list.aspx")
    @FormUrlEncoded
    suspend fun xkjg_list(
        @FieldMap form: Map<String, String> = mapOf(),
        @Query("id") id: String = NetUtils.getID()
        //@HeaderMap headerMap: Map<String, String>
        //@Query("id") id: String = NetUtils.getID()
        //@Header("Cookie") cookie: List<String> = getCookie(),
    ): String


    /**
     * 6 校历 https://jwcjwxt1.fzu.edu.cn/xl.asp
     */
    @POST("https://jwcjwxt1.fzu.edu.cn/xl.asp")
    @FormUrlEncoded
    suspend fun xl(
        @Field("xq") tern: String? = null
    ): ResponseBody
}

