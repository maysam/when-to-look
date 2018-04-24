package com.codehospital.whentolook

import java.util.*
import kotlin.collections.HashMap

class direction(val azimuth:Double, val altitude:Double)

class SunCalc {
    val J1970 = 2440588
    val J2000 = 2451545
    val deg2rad = Math.PI / 180
    val M0 = 357.5291 * deg2rad
    val M1 = 0.98560028 * deg2rad
    val J0 = 0.0009
    val J1 = 0.0053
    val J2 = -0.0069
    val C1 = 1.9148 * deg2rad
    val C2 = 0.0200 * deg2rad
    val C3 = 0.0003 * deg2rad
    val P = 102.9372 * deg2rad
    val e = 23.45 * deg2rad
    val th0 = 280.1600 * deg2rad
    val th1 = 360.9856235 * deg2rad
    val h0 = -0.83 * deg2rad //sunset angle
    val d0 = 0.53 * deg2rad //sun diameter
    val h1 = -6 * deg2rad //nautical twilight angle
    val h2 = -12 * deg2rad //astronomical twilight angle
    val h3 = -18 * deg2rad //darkness angle
    val msInDay = 1000 * 60 * 60 * 24

    fun dateToJulianDate(date: Date): Double {
        return date.time / msInDay - 0.5 + J1970
    }

    fun julianDateToDate(j: Double): Date {
        val seconds: Long = ((j + 0.5 - J1970) * msInDay).toLong()
        return Date(seconds)
    }

    fun getJulianCycle(J: Double, lw: Double): Long {
        return Math.round(J - J2000 - J0 - lw / (2 * Math.PI))
    }

    fun getApproxSolarTransit(Ht: Double, lw: Double, n: Long): Double {
        return J2000 + J0 + (Ht + lw) / (2 * Math.PI) + n
    }

    fun getSolarMeanAnomaly(Js: Double): Double {
        return M0 + M1 * (Js - J2000)
    }

    fun getEquationOfCenter(M: Double): Double {
        return C1 * Math.sin(M) + C2 * Math.sin(2 * M) + C3 * Math.sin(3 * M)
    }

    fun getEclipticLongitude(M: Double, C: Double): Double {
        return M + P + C + Math.PI
    }

    fun getSolarTransit(Js: Double, M: Double, Lsun: Double): Double {
        return Js + (J1 * Math.sin(M)) + (J2 * Math.sin(2 * Lsun))
    }

    fun getSunDeclination(Lsun: Double): Double {
        return Math.asin(Math.sin(Lsun) * Math.sin(e))
    }

    fun getRightAscension(Lsun: Double): Double {
        return Math.atan2(Math.sin(Lsun) * Math.cos(e), Math.cos(Lsun))
    }

    fun getSiderealTime(J: Double, lw: Double): Double {
        return th0 + th1 * (J - J2000) - lw
    }

    fun getAzimuth(th: Double, a: Double, phi: Double, d: Double): Double {
        val H = th - a
        return Math.atan2(Math.sin(H), Math.cos(H) * Math.sin(phi) -
                Math.tan(d) * Math.cos(phi))
    }

    fun getAltitude(th: Double, a: Double, phi: Double, d: Double): Double {
        val H = th - a
        return Math.asin(Math.sin(phi) * Math.sin(d) +
                Math.cos(phi) * Math.cos(d) * Math.cos(H))
    }

    fun getHourAngle(h: Double, phi: Double, d: Double): Double {
        return Math.acos((Math.sin(h) - Math.sin(phi) * Math.sin(d)) /
                (Math.cos(phi) * Math.cos(d)))
    }

    fun getSunsetJulianDate(w0: Double, M: Double, Lsun: Double, lw: Double, n: Long): Double {
        return getSolarTransit(getApproxSolarTransit(w0, lw, n), M, Lsun)
    }

    fun getSunriseJulianDate(Jtransit: Double, Jset: Double): Double {
        return Jtransit - (Jset - Jtransit)
    }

    fun getSunPosition(J: Double, lw: Double, phi: Double): direction {
        val M = getSolarMeanAnomaly(J)
        val C = getEquationOfCenter(M)
        val Lsun = getEclipticLongitude(M, C)
        val d = getSunDeclination(Lsun)
        val a = getRightAscension(Lsun)
        val th = getSiderealTime(J, lw)

        return direction(azimuth = getAzimuth(th, a, phi, d), altitude = getAltitude(th, a, phi, d))
    }

    fun getDayInfo(date: Date, lat: Double, lng: Double, detailed: Boolean): HashMap<String, Any> {
        val lw = -lng * deg2rad
        val phi = lat * deg2rad
        val J = dateToJulianDate(date)

        val n = getJulianCycle(J, lw)
        val Js = getApproxSolarTransit(0.0, lw, n)
        val M = getSolarMeanAnomaly(Js)
        val C = getEquationOfCenter(M)
        val Lsun = getEclipticLongitude(M, C)
        val d = getSunDeclination(Lsun)
        val Jtransit = getSolarTransit(Js, M, Lsun)
        val w0 = getHourAngle(h0, phi, d)
        val w1 = getHourAngle(h0 + d0, phi, d)
        val Jset = getSunsetJulianDate(w0, M, Lsun, lw, n)
        val Jsetstart = getSunsetJulianDate(w1, M, Lsun, lw, n)
        val Jrise = getSunriseJulianDate(Jtransit, Jset)
        val Jriseend = getSunriseJulianDate(Jtransit, Jsetstart)
        val w2 = getHourAngle(h1, phi, d)
        val Jnau = getSunsetJulianDate(w2, M, Lsun, lw, n)
        val Jciv2 = getSunriseJulianDate(Jtransit, Jnau)
//        val obj =  JSONObject()
//
//        obj.put("name", "foo");
//        obj.put("num", 100);
//        obj.put("balance", 1000.2);
//        obj.put("is_vip", true);

        val info = HashMap<String, Any>()
        info.put("dawn", julianDateToDate(Jciv2))
        info["sunrise"] = OldPeriod(julianDateToDate(Jrise), julianDateToDate(Jriseend))
        info["transit"] = julianDateToDate(Jtransit)
        info["sunset"] = OldPeriod(julianDateToDate(Jsetstart), julianDateToDate(Jset))
        info["dusk"] = julianDateToDate(Jnau)


        if (detailed) {
            val w3 = getHourAngle(h2, phi, d)
            val w4 = getHourAngle(h3, phi, d)
            val Jastro = getSunsetJulianDate(w3, M, Lsun, lw, n)
            val Jdark = getSunsetJulianDate(w4, M, Lsun, lw, n)
            val Jnau2 = getSunriseJulianDate(Jtransit, Jastro)
            val Jastro2 = getSunriseJulianDate(Jtransit, Jdark)


            val morningTwilight = HashMap<String, Any>()
            info["morningTwilight"] = morningTwilight
            morningTwilight.put("astronomical", OldPeriod(julianDateToDate(Jastro2), julianDateToDate(Jnau2)))
            morningTwilight["nautical"] = OldPeriod(julianDateToDate(Jnau2), julianDateToDate(Jciv2))
            morningTwilight["civil"] = OldPeriod(julianDateToDate(Jciv2), julianDateToDate(Jrise))
            info["nightTwilight"] = OldPeriod(julianDateToDate(Jset), julianDateToDate(Jnau))
            info["nautical"] = OldPeriod(julianDateToDate(Jnau), julianDateToDate(Jastro))
            info["astronomical"] = OldPeriod(julianDateToDate(Jastro), julianDateToDate(Jdark))
        }
        return info
    }


    fun getSunPosition( date:Date, lat:Double, lng:Double): direction {
        return getSunPosition( dateToJulianDate(date), -lng * deg2rad, lat * deg2rad )
    }
}


class OldPeriod(start: Date, end: Date)

class location(name:String, lat:Double, lng: Double)

val locations = listOf<location>(location(name="tehran",lat= 35.7537583,lng= 51.4783184),location(name= "kabaa",lat= 21.4226577,lng= 39.8262454))
//
//val date = Date()
//
//
//var lat = locations[0].lat
//var lng = locations[0].lng
//
//var di = SunCalc.getDayInfo(date, lat, lng)
//var sunrisePos = SunCalc.getSunPosition(di.sunrise.start, lat, lng)
//var sunsetPos = SunCalc.getSunPosition(di.sunset.end, lat, lng)
//
//console.log(di)
//console.log(sunrisePos)
//console.log(sunsetPos)
//
//var lat = locations[1].lat
//var lng = locations[1].lng
//
//var di = SunCalc.getDayInfo(date, lat, lng)
//var sunrisePos = SunCalc.getSunPosition(di.sunrise.start, lat, lng)
//var sunsetPos = SunCalc.getSunPosition(di.sunset.end, lat, lng)
//
//console.log(di)
//console.log(sunrisePos)
//console.log(sunsetPos)