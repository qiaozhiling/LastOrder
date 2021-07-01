package com.qzl.lun6.logic.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.utils.exception.LoginDataException
import com.qzl.lun6.utils.exception.NetException
import internet.FzuServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset
import java.util.*


object NetUtils {

    ///真id
    private var ID: String = ""

    fun setID(id: String?) {
        ID = id ?: ID
    }

    fun getID() = ID

    ///用户学期
    private var USER_TERMS: List<String>? = null

    fun getUserTerms() = USER_TERMS

    fun setUserTerms(list: List<String>) {
        USER_TERMS = list
    }

    ///校历
    private var SCHOOL_TERMS: Map<String, String>? = null

    private fun getSchoolTerns() = SCHOOL_TERMS

    fun setSchoolTerns(map: Map<String, String>) {
        SCHOOL_TERMS = map
    }

    //校历查询参数
    private var SCHOOLYEARS = mapOf<String, String>()

    fun getSchoolYear() = SCHOOLYEARS as Map<String, String>

    fun setSchoolYear(map: Map<String, String>) {
        SCHOOLYEARS = map
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //请求部分
    private val mServer = ServerCreator.create(FzuServer::class.java)

    /**
     * 1 验证码图片
     * @throws NetException("请检查网络")("教务处连接超时")
     */
    suspend fun getVerifyCode(): Bitmap = try {
        val pic = mServer.verifyCode().byteStream()
        BitmapFactory.decodeStream(pic)
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException -> throw NetException("请检查网络")
            is SocketTimeoutException -> throw NetException("教务处连接超时")
            else -> throw e
        }
    }

    /**
     * 2
     * 发送登入请求 获取cookie
     * @param user 9位学号 不能错
     * @throws  LoginDataException 1账号密码错误,2网络错误,3Body为空
     */
    private suspend fun sendLoginCheck(user: String, password: String, verifyCode: String): String {
        //todo 优化逻辑
        val fieldMap = mutableMapOf<String, String>().apply {
            put("muser", user)
            put("passwd", password)
            put("Verifycode", verifyCode)
        }

        val responseBody = mServer.loginCheck(fieldMap)

        return when {
            responseBody.contains("不存在该用户") -> throw LoginDataException("不存在该用户")
            responseBody.contains("密码错误") -> throw LoginDataException("密码错误")
            responseBody.contains("验证码验证失败") -> throw LoginDataException("验证码验证失败")
            else -> responseBody
        }
    }

    /**
     * 3 验证sso
     * @throws Exception
     */
    private suspend fun getSsoLogin(token: String): String = mServer.ssoLogin(token)

    /**
     * 4 再验证Loginchk_xs
     */
    private suspend fun getLoginCHK(queryMap: Map<String, String>): String {

        val response = mServer.LoginCHK(queryMap)
        //todo 更改id储存逻辑
        // TODO: 2021/6/29 此处为真id
        AnalysisUtils.saveIDFromResponse(response)
        return response.body() ?: ""
    }


    /**
     *  总登入方法
     *  @throws LoginDataException 1账号密码错误,2网络错误,3Body为空,4正在登入中...
     *  @throws NetException 1"请检查网络",2"教务处连接超时"
     */
    suspend fun login(user: String, passwd: String, verifyCode: String) {
        return withContext(Dispatchers.IO) {
            try {

                //发送登入
                val loginCheck = sendLoginCheck(user, passwd, verifyCode)
                //解析获得token
                val token = AnalysisUtils.getTokenFromHtml(loginCheck)
                //解析获得查询
                val queries = AnalysisUtils.getQueryInfoFromHtml(loginCheck)
                //验证sso
                getSsoLogin(token)

                //再验证Loginchk_xs
                //登入成功请求到主页 不包含名字信息
                /*val b =*/
                //b.log()
                getLoginCHK(queries)

            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> throw NetException("请检查网络")
                    is SocketTimeoutException -> throw NetException("教务处连接超时")
                    else -> throw e
                }
            }
        }
    }

    /**
     * 6 我的选课html
     * @param formMap 查询表单
     * ctl00\$ContentPlaceHolder1\$DDL_xnxq 年份/学期代码 202001 202002
     * ctl00\$ContentPlaceHolder1\$BT_submit "确定"
     *
     *
     */
    suspend fun getCourseList(formMap: Map<String, String>) = mServer.xkjg_list(formMap)
    /*suspend fun getCourseList(year: String? = null): List<Course> {

        val oP = getLessonPara()

        // lessons -> html原始数据
        val lessons = if (year != null && oP != null) {
            //有其他参数
            val formMap = mutableMapOf<String, String>().apply {
                //表单参数
                put("ctl00\$ContentPlaceHolder1\$DDL_xnxq", year)
                put("ctl00\$ContentPlaceHolder1\$BT_submit", "确定")
                putAll(oP)
            }
            mServer.xkjg_list(formMap)

        } else {
            mServer.xkjg_list()
        }


        //储存参数
        AnalyzeUtils.getParasFromHtml(lessons)
        //储存可选学期
        AnalyzeUtils.getUserTernsFromHtm(lessons)


        return AnalysisUtils.getCourseFromHtml(lessons)
    }*/

    /**
     * 7校历 返回学期校历html
     */
    suspend fun getSchoolCalendar(yearValue: String? = null): String = decode(mServer.xl(yearValue))


    /**
     *  @throws Exception("BODY IS NULL")
     *  判断Response<String>是否为null 返回html<String>
     */
    private fun <T> getBody(response: Response<T>?): T =
        with<Response<T>?, T>(response) {
            this?.body() ?: throw Exception("BODY IS NULL")
        }

    /**
     * body用gbk解码
     */
    private fun decode(responseBody: ResponseBody): String {
        val source: BufferedSource = responseBody.source()
        val buffer = source.buffer()
        val gbk = Charset.forName("gbk")
        return buffer.clone().readString(gbk)
    }

}