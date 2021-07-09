package com.qzl.lun6.ui.fragment.table

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import com.qzl.lun6.R
import com.qzl.lun6.databinding.FragmentTableBinding
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.logic.model.course.Exam
import com.qzl.lun6.logic.model.course.TP
import com.qzl.lun6.logic.model.course.Transfer
import com.qzl.lun6.ui.activity.AddCourseActivity
import com.qzl.lun6.ui.activity.TableSettingActivity
import com.qzl.lun6.ui.activity.mainactivity.MainActivity
import com.qzl.lun6.ui.fragment.BaseFragment
import com.qzl.lun6.utils.log
import com.qzl.lun6.utils.setStatusBarColor
import com.qzl.lun6.utils.toast
import jsc.kit.wheel.base.WheelItem
import jsc.kit.wheel.dialog.ColumnWheelDialog
import java.util.*

class TableFragment : BaseFragment<FragmentTableBinding>() {

    private val viewModel by lazy { (activity as MainActivity).viewModel }

    private val requestCodeTableSetting = 1
    private val requestCodeAddCourse = 2

    private var currentTerm: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //菜单
        binding.toolbarTable.apply {
            inflateMenu(R.menu.table_menu)
            //点击事件
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add_table_menu -> {
                        val intent = Intent(context, AddCourseActivity::class.java)
                        startActivityForResult(intent, requestCodeAddCourse)
                    }
                    R.id.setting_table_menu -> {
                        val intent = Intent(context, TableSettingActivity::class.java)
                        startActivityForResult(intent, requestCodeTableSetting)
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }

        //数据绑定
        binding.tableviewTable.setData(viewModel.myData.dates, viewModel.myData.courses)

        //菜单周数显示绑定
        binding.tableviewTable.currentItem.observe(viewLifecycleOwner) {
            val t = "第${it + 1}周"
            binding.weekIndicatorTable.text = t
        }

        //周数显示点击事件
        binding.weekIndicatorTable.setOnClickListener {
            showWheel()
        }

        //下拉刷新事件
        binding.swipeTable.setOnRefreshListener {
            viewModel.requestData()
        }

        //显示当前周
        binding.tableviewTable.setCurrentItem()

        // 数据监听绑定
        // TODO: 2021/7/6 绑定时会触发请求？？？？？？
        viewModel.myDataLiveData.observe(viewLifecycleOwner) {
            val myData = it.getOrNull()

            if (myData != null) {
                viewModel.myData.courses.apply {
                    clear()
                    addAll(myData.courses)
                }
                viewModel.myData.dates.apply {
                    clear()
                    addAll(myData.dates)
                }

                binding.tableviewTable.notifyDataChange()
                "获取课程成功".toast()
            } else {
                it.exceptionOrNull()?.printStackTrace()
                "获取课程失败，显示本地缓存".toast()
            }

            binding.swipeTable.isRefreshing = false
        }

        /* //课程数据绑定
         viewModel.courseListLiveData.observe(viewLifecycleOwner) { result ->
             val courses = (result as Result<List<Course>>).getOrNull()
             if (courses != null) {
                 viewModel.courseList.clear()
                 viewModel.courseList.addAll(courses)
                 //binding.tableviewTable.notifyDataChange()
                 "获取课程成功".toast()
             } else {
                 result.exceptionOrNull()?.printStackTrace()
                 "获取课程失败".toast()
             }
         }

         //校历数据绑定
         viewModel.dateListLiveData.observe(viewLifecycleOwner) {
             val dates = (it as Result<List<Calendar>>).getOrNull()
             if (dates != null) {
                 viewModel.dateList.clear()
                 viewModel.dateList.addAll(dates)
                 binding.tableviewTable.notifyDataChange()

             } else {
                 "刷新失败,获取日期失败".toast()
                 it.exceptionOrNull()?.printStackTrace()
             }
             binding.swipeTable.isRefreshing = false//取消刷新动画
         }*/
    }


    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.white)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == requestCodeAddCourse && resultCode == RESULT_OK -> {
                val sDW = data?.getIntExtra("sDW", 2)!!
                val startSection = data.getIntExtra("startSection", 1)
                val endSection = data.getIntExtra("endSection", 1)
                val startWeek = data.getIntExtra("startWeek", 1)
                val endWeek = data.getIntExtra("endWeek", 1)
                val week = data.getIntExtra("week", 1)
                val courseName = data.getStringExtra("courseName")!!
                val teacher = data.getStringExtra("teacher")!!
                val place = data.getStringExtra("place")!!
                val remark = data.getStringExtra("remark")!!
                val course = Course(
                    courseName,
                    teacher,
                    listOf(TP(startWeek, endWeek, week, sDW, startSection, endSection, place)),
                    Exam(""),
                    Transfer(""),
                    remark
                )
                viewModel.myData.courses.add(course)
                binding.tableviewTable.notifyDataChange()
            }

            requestCode == requestCodeTableSetting && resultCode == RESULT_OK -> {
                "setting".toast()
            }
        }
    }

    private fun showWheel() {

        val dialog: ColumnWheelDialog<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem> =
            ColumnWheelDialog(context!!)
        dialog.show()
        dialog.setTitle("选择菜单")
        dialog.setCancelButton("取消", null)
        dialog.setOKButton("确定") { _, item0, _, _, _, _ ->
            val a = item0!!.showText.replace(Regex("[第周]"), "").toInt()
            binding.tableviewTable.setCurrentItem(a)
            false
        }

        dialog.setItems(
            initItems(),
            null,
            null,
            null,
            null
        )
    }

    private fun initItems(): Array<WheelItem?> {
        val items = arrayOfNulls<WheelItem>(22)
        for (i in 0..21) {
            items[i] = WheelItem("第${i}周")
        }
        return items
    }

    val kc =
        "\n" +
                "\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head id=\"Head1\"><title>\n" +
                "\n" +
                "</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link href=\"../../../images/skin.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                "    <style type=\"text/css\">\n" +
                "    body {\n" +
                "\t    margin-left: 0px;\n" +
                "\t    margin-top: 0px;\n" +
                "\t    margin-right: 0px;\n" +
                "\t    margin-bottom: 0px;\n" +
                "\t    background-color: #EEF2FB;\n" +
                "\t    font-family:Arial, 宋体, Helvetica, sans-serif;\n" +
                "\t    font-size:12px;\n" +
                "    }\n" +
                "    body table{ font-size:12px; font-family:Arial, 宋体, Helvetica, sans-serif}\n" +
                "    .sys_text{ width:160px;}\n" +
                "    body textarea{ width:200px; height:80px; font-size:12px;}\n" +
                "    .STYLE1 {color: #FF0000;}\n" +
                "    .STYLE2 {color: #CCCCCC}    \n" +
                "    </style>\n" +
                "    \n" +
                "<script language=\"JavaScript\" type=\"text/javascript\" src=\"../../../js/popMenu.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <form method=\"post\" action=\"./xkjg_list.aspx?id=20216161555598155\" id=\"form1\">\n" +
                "<div class=\"aspNetHidden\">\n" +
                "<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"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\" />\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"aspNetHidden\">\n" +
                "\n" +
                "\t<input type=\"hidden\" name=\"__VIEWSTATEGENERATOR\" id=\"__VIEWSTATEGENERATOR\" value=\"9C837C5E\" />\n" +
                "\t<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\"rHRK9t2Ns9zoQqOQpCdjKYQ4JYzZZLPlRSTomf52qiTjKrSwCcg4QdNtWup1YvC/YVtcuVRnwbt+8rZMRi6UzEKt9w9PWSh2Q6pn/MLeOvf0hmrWuUVWlgkrdnhaoLBzd+9w2/NaA2G1UhXFF2WPXLCuFnz/MOZWwjAgFxX2cS7/xAxcUowgM/EpbU4=\" />\n" +
                "</div>\n" +
                "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "      <tr>\n" +
                "        <td width=\"17\" valign=\"top\" background=\"~/images/mail_leftbg.gif\"><img id=\"Image1\" src=\"../../../images/left-top-right.gif\" style=\"height:29px;width:17px;\" /></td>\n" +
                "        <td valign=\"top\" background=\"~/images/content-bg.gif\" width=\"99%\">\n" +
                "        <table width=\"100%\" height=\"31\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"left_topbg\" id=\"table2\">\n" +
                "          <tr>\n" +
                "            <td height=\"31\"><div class=\"titlebt\"><span id=\"LB_bt\">我的选课</span></div></td>\n" +
                "          </tr>\n" +
                "        </table>\n" +
                "        </td>\n" +
                "        <td width=\"16\" valign=\"top\" background=\"~/images/mail_rightbg.gif\"><img id=\"Image2\" src=\"../../../images/nav-right-bg.gif\" style=\"height:29px;width:16px;\" /></td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "    \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<table  border=\"0\" cellpadding=\"0\" cellspacing=\"0\"  align=\"center\" width=\"98%\">\n" +
                "  <tr>     \n" +
                "    <td align=\"center\" style=\"height:30px;\">\t\t\n" +
                "        学年学期：<select name=\"ctl00\$ContentPlaceHolder1\$DDL_xnxq\" id=\"ContentPlaceHolder1_DDL_xnxq\" style=\"width:100px;\">\n" +
                "\t<option selected=\"selected\" value=\"202002\">202002</option>\n" +
                "\t<option value=\"202001\">202001</option>\n" +
                "\n" +
                "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"submit\" name=\"ctl00\$ContentPlaceHolder1\$BT_submit\" value=\"确定\" id=\"ContentPlaceHolder1_BT_submit\" />\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "  <tr>     \n" +
                "    <td align=\"center\">\t\n" +
                "        <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; border-collapse:collapse;\" align=\"left\">\t\n" +
                "            <table id=\"ContentPlaceHolder1_DataList_xxk\" cellspacing=\"0\" style=\"height:60px;width:100%;border-collapse:collapse;\">\n" +
                "\t<tr>\n" +
                "\t\t<td colspan=\"2\">\n" +
                "                <tr style=\"height:30px; background:#efefef; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" >\n" +
                "                    <td align=\"center\" style=\"width:60px; padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">修读类别</td>\n" +
                "                    <td align=\"center\" style=\"width:200px;padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">课程名称</td>\n" +
                "                    <td align=\"center\" style=\"width:60px; padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">教学大纲<br />授课计划</td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"width:60px; padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">缴费情况</td>\n" +
                "                    <td align=\"center\" style=\"width:30px; padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">学分</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">选修类型</td>                 \n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">考试类别</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">任课教师</td>\n" +
                "                    <td align=\"center\" style=\"width:200px;padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">上课时间地点</td>\n" +
                "                    <td align=\"center\" style=\"width:100px; padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">考试时间地点</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">备注</td>\n" +
                "                    <td align=\"center\" style=\"width:150px;padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray; border-top:1px solid gray;\">调课信息</td>\n" +
                "                </tr>\n" +
                "                </td>\n" +
                "\t</tr><tr>\n" +
                "\t\t<td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">大学英语（三）</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=00900011&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200200900011035&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_0\">2</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_0\">公共必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_0\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">林群</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-16&nbsp;星期2:1-2节&nbsp;铜盘A315<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">2021年05月09日<br>09:00-10:00<br>铜盘A508   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td><td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">中国近现代史纲要</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=02700006&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200202700006150&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_1\">3</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_1\">公共必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_1\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">陈亮</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-16&nbsp;星期2:5-6节&nbsp;铜盘B209<br>01-16&nbsp;星期4:7-8节(双)&nbsp;铜盘B209<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">2021年06月17日<br>15:50-16:30<br>铜盘B209   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td>\n" +
                "\t</tr><tr>\n" +
                "\t\t<td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">形势与政策（二）</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=02700014&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200202700014113&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_2\">0</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_2\">公共必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_2\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">唐筱霞</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">05-08&nbsp;星期4:5-6节&nbsp;铜盘B210<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td><td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">体育（二）</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=02800009&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200202800009161&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_3\">1</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_3\">公共必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_3\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">官钟威</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-16&nbsp;星期1:3-4节&nbsp;铜盘田径场<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td>\n" +
                "\t</tr><tr>\n" +
                "\t\t<td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">军事理论</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=06700001&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200206700001062&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_4\">2</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_4\">公共必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_4\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">张文娟</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-16&nbsp;星期5:7-8节&nbsp;铜盘B109<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">2021年07月02日<br>15:50-16:50<br>铜盘A309   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td><td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">离散数学</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=00300238&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200200300238003&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_5\">4</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_5\">学科必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_5\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">林政宽,李小燕</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-16&nbsp;星期2:3-4节&nbsp;铜盘B409<br>01-16&nbsp;星期5:3-4节&nbsp;铜盘B409<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td>\n" +
                "\t</tr><tr>\n" +
                "\t\t<td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">人工智能导论</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=00300561&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200200300561001&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_6\">2</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_6\">学科必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_6\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">牛玉贞,何炳蔚,赵铁松,黄捷</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-08&nbsp;星期6:1-4节&nbsp;铜盘B311<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">第1-2周牛玉贞；第3-4周何炳蔚；第5-6周赵铁松；第7-8周黄捷。</td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">05周 星期6:1-4节&nbsp;&nbsp;调至&nbsp;&nbsp;06周 星期6:5-8节&nbsp;&nbsp;铜盘B311</td>                    \n" +
                "                </tr>                        \n" +
                "                </td><td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">高等数学A（中）</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=00310524&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200200310524014&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_7\">5</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_7\">学科必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_7\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">钟展良</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-14&nbsp;星期1:1-2节&nbsp;铜盘B205<br>01-14&nbsp;星期3:3-4节&nbsp;铜盘B205<br>01-14&nbsp;星期5:1-2节&nbsp;铜盘B205<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">2021年05月09日<br>14:30-16:30<br>铜盘A109   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td>\n" +
                "\t</tr><tr>\n" +
                "\t\t<td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">Python程序设计</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=00310838&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200200310838006&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_8\">2</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_8\">学科必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_8\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">吴伶</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-12&nbsp;星期1:5-6节&nbsp;铜盘B505<br>13-16&nbsp;星期1:5-6节&nbsp;铜盘A203<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td><td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">大学物理A(上)</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=01101155&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200201101155061&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_9\">3</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_9\">学科必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_9\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">苏万钧</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">01-16&nbsp;星期2:7-8节&nbsp;铜盘B112<br>01-16&nbsp;星期5:5-6节(单)&nbsp;铜盘B112<br>   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td>\n" +
                "\t</tr><tr>\n" +
                "\t\t<td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">大学物理实验A（上）</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=01101157&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200201101157030&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_10\">1.5</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_10\">学科必修</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_10\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">林丽华</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">星期四上午，物理实验中心</td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td><td>               \n" +
                "                <tr style=\"height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;\" onmouseover=\"c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa'\" onmouseout=\"this.style.backgroundColor=c\">\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">本专业</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">超星尔雅：对话大国工匠 致敬劳动模范</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><a href=javascript:pop1('/pyfa/jxdg/TeachingProgram_view.aspx?kcdm=03601409&id=20216161555598155');>教学大纲</a><br /><a href=javascript:pop1('/pyfa/skjh/TeachingPlan_view.aspx?kkhm=20200203601409215&id=20216161555598155');>授课计划</a></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xf_11\">1</span></td>\n" +
                "\t\t  \t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_xxlxmc_11\">劳动教育类</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"><span id=\"ContentPlaceHolder1_DataList_xxk_LB_kslbmc_11\">正常考考试</span></td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">王琳等</td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>                              <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">   </td>\n" +
                "                    <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\">人文社科类,开课学校：中国劳动关系学院，注册要求详见选课通知</td>\n" +
                "\t\t\t        <td align=\"center\" style=\"padding-right: 1px; padding-left: 1px; padding-bottom: 1px; padding-top: 1px; border-right:1px solid gray; border-bottom:1px solid gray; border-left:1px solid gray;\"></td>                    \n" +
                "                </tr>                        \n" +
                "                </td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "\n" +
                "        </table>\n" +
                "    </td>\n" +
                "  </tr> \n" +
                "  \n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "    </form>\n" +
                "</body>\n" +
                "</html>\n"

}