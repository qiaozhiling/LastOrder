package com.qzl.lun6.ui.fragment.table

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.qzl.lun6.logic.Repository
import com.qzl.lun6.logic.model.course.Course
import java.util.*
import kotlin.collections.ArrayList

class TableViewModel : ViewModel() {
    //选课
    private val courseLiveData = MutableLiveData<String>().apply { value = "" }

    val courseList = ArrayList<Course>()

    val courseListLiveData = Transformations.switchMap(courseLiveData) {
        Repository.getNewCourse(it)
    }

    //////////////////////////////
    //校历
    private val dateLiveData = MutableLiveData<String>().apply { value = "" }

    val dateListLiveData = Transformations.switchMap(dateLiveData) {
        Repository.getNewCalendar(it)
    }

    val dateList = ArrayList<Calendar>()
    ////////////////////////////////

    /**
     * @param termCode 学期代码  202001 202002
     */
    fun getCourse(termCode: String? = null) {
        dateLiveData.value = termCode ?: courseLiveData.value
        courseLiveData.value = termCode ?: courseLiveData.value
    }


    override fun onCleared() {


        super.onCleared()
    }
}