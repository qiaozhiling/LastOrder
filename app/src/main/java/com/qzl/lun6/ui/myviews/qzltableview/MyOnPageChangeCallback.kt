package com.qzl.lun6.ui.myviews.qzltableview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2


class MyOnPageChangeCallback(
    private val selfViewPager: ViewPager2,
    private val linkViewPager: ViewPager2
) :
    ViewPager2.OnPageChangeCallback() {

    private var _pos = 0
    private var lastOffset = 0f
    private val _currentItem = MutableLiveData<Int>()
    val currentItem = _currentItem as LiveData<Int>

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     *                 Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
/*

        Log.i("aaposition", position.toString())
        Log.i("aapositionOffset", positionOffset.toString())
        Log.i("aapositionOffsetPixels", positionOffsetPixels.toString())


        val selfWidth = selfViewPager.width + selfViewPager.marginLeft + selfViewPager.marginRight
        val linkWidth = linkViewPager.width + linkViewPager.marginLeft + linkViewPager.marginRight

        val marginX: Float = if (selfViewPager.currentItem == position) {
            -1f * linkWidth * (positionOffset - lastOffset)
        } else {
            linkWidth * (positionOffset - lastOffset)
        }

        linkViewPager.beginFakeDrag()
        if (linkViewPager.fakeDragBy(marginX))
            linkViewPager.endFakeDrag()

        lastOffset = positionOffset

        */
/* if (linkViewPager.scrollX != marginX) {
             linkViewPager.scrollTo(marginX, 0)
         }*//*


           Log.i("aaselfWidth", selfWidth.toString())
           Log.i("aalinkWidth", linkWidth.toString())
           Log.i("aamarginX", marginX.toString())
           Log.i("aapositionOffset", positionOffset.toString())
*/

        //太难了
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        this._pos = position
        _currentItem.value = position
    }

    override fun onPageScrollStateChanged(state: Int) {

        if (state == ViewPager2.SCROLL_STATE_IDLE) {
            linkViewPager.setCurrentItem(_pos, false)
        }
    }

}