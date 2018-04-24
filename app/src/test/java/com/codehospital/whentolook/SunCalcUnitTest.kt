package com.codehospital.whentolook

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SunCalcUnitTest {
    @Test
    fun julianDateToDate_isCorrect() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                dateFormat.timeZone = TimeZone.getTimeZone("IRDT")
        assertEquals(dateFormat.parse("2018-04-23 16:06:45").time/1000, SunCalc().julianDateToDate(2458232.171357161).time/1000)
        val dateFormat1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
        dateFormat1.timeZone = TimeZone.getTimeZone("IRDT")
        assertEquals(dateFormat1.parse("2018-04-23 16:06:45.258").time, SunCalc().julianDateToDate(2458232.171357161).time)
    }
}
