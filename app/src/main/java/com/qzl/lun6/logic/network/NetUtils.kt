package internet

import com.qzl.lun6.logic.network.AnalyzeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Response
import java.io.InputStream
import java.net.UnknownHostException
import java.nio.charset.Charset


object NetUtils {

    private var COOKIE: MutableList<String> = mutableListOf()

    fun getCookie() = COOKIE.map {
        it.split(";")[0]
    }

    fun addCookie(cookie: String) {
        this.COOKIE.add(cookie)
    }

    ///
    private var ID: String = ""

    fun setID(id: String?) {
        this.ID = id ?: ID
    }

    fun getID() = ID

    ///
    private val mServer = ServerCreator.create(FzuServer::class.java)

    ///查询课表参数
    //__VIEWSTATE
    //__EVENTVALIDATION
    //__VIEWSTATEGENERATOR

    private var LESSONPARAMETER: Map<String, String>? = null

    private fun getLessonPara() = LESSONPARAMETER

    fun setLessonPara(map: Map<String, String>) {
        LESSONPARAMETER = map
    }

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
    /**
     * 1 验证码图片 获取cookie
     * TODO 改成返回Bitmap
     */
    suspend fun getVerifyCode(): InputStream = try {
        mServer.verifyCode().byteStream()
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException -> throw UnknownHostException("请检查网络")
            else -> throw e
        }
    }

    /**
     * 2
     * 发送登入请求 获取cookie
     * @param user 9位学号 不能错
     * @throws  Exception 1账号密码错误,2网络错误,3Body为空,4学号错误
     */
    private suspend fun sendLoginCheck(user: String, passwd: String, verifyCode: String): String {

        if (user.length != 9) {
            throw Exception("学号长度错误")
        }

        val fieldMap = mutableMapOf<String, String>().apply {
            put("muser", user)
            put("passwd", passwd)
            put("Verifycode", verifyCode)
        }

        val responseBody = mServer.loginCheck(fieldMap)

        val s = decode(responseBody)

        return when {
            s.contains("不存在该用户，请确认是否输入错误，用户名前请不要加字母！！") -> throw Exception("不存在该用户")
            s.contains("密码错误，请重新登录，或与学院教学办联系！") -> throw Exception("密码错误")
            s.contains("验证码验证失败！") -> throw Exception("验证码验证失败")
            else -> s
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
    private suspend fun getLoginchk_xs(queryMap: Map<String, String>): String {

        val response = mServer.loginchk_xs(queryMap = queryMap)

        AnalyzeUtils.saveIDFromResponse(response)
        return response.body() ?: ""
    }

    /**
     *  总登入方法
     */
    suspend fun login(user: String, passwd: String, verifyCode: String) {
        return withContext(Dispatchers.IO) {
            try {
                //发送登入
                val a = sendLoginCheck(user, passwd, verifyCode)
                //解析获得token
                val token = AnalyzeUtils.getTokenFromString(a)
                //验证sso
                getSsoLogin(token)
                //解析获得查询
                val queries = AnalyzeUtils.getQueryInfo(a)

                //
                getLoginchk_xs(queries)

            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException -> throw UnknownHostException("请检查网络")
                    else -> throw e
                }
            }

        }
    }

    /**
     * 6 我的选课
     * @param year 查询年份
     * TODO 返回课程bean
     */
    suspend fun getLessonList(year: String? = null): String {

        val oP = getLessonPara()

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

        return lessons
    }

    /**
     * 7校历
     * TODO 返回校历bean
     */
    suspend fun getXl(yearValue: String? = null): String {

        val xl = mServer.xl(yearValue)

        return decode(xl)
    }


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