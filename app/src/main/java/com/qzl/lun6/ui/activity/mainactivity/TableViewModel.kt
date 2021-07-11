package com.qzl.lun6.ui.activity.mainactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qzl.lun6.logic.Repository
import com.qzl.lun6.logic.model.MyData
import com.qzl.lun6.logic.model.course.Course
import com.qzl.lun6.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TableViewModel : ViewModel() {

    //年份代码
    private val yearCodeLiveData: MutableLiveData<String> =
        MutableLiveData<String>().apply { value = "" }

    /*//选课
    val courseList = ArrayList<Course>()

    val courseListLiveData = Transformations.switchMap(yearCodeLiveData) {
        Repository.requestCourse(it)
    }

    //////////////////////////////
    //校历
    val dateList = ArrayList<Calendar>()

    val dateListLiveData = Transformations.switchMap(yearCodeLiveData) {
        Repository.requestCalendar(it)
    }*/
    ////////////////////////////////

    val myData = MyData(ArrayList<Course>(), ArrayList<Calendar>())

    val myDataLiveData = Transformations.switchMap(yearCodeLiveData) {
        Repository.requestData(it)
        //IT 202001 202002
    }

    /**
     * 请求数据
     * @param termCode 学期代码  202001 202002
     */
    fun requestData(termCode: String? = null) {
        "requestData".log()
        yearCodeLiveData.value = termCode ?: yearCodeLiveData.value
    }

    /**
     * 加载数据
     */
    fun loadData() {
        "loadCourse".log()
        viewModelScope.launch(Dispatchers.IO) {
            myData.courses.addAll(Repository.loadCourse())
            myData.dates.addAll(Repository.loadCalendar())
        }
    }

    fun saveData() {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.deleteAllCourse()
            Repository.saveCourse(myData.courses)
            Repository.saveCalendar(myData.dates)
            super.onCleared()
        }
    }

}