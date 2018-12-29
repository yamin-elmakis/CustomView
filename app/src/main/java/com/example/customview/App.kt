package com.example.customview

import android.app.Application
import lib.yamin.easylog.EasyLog
import lib.yamin.easylog.EasyLogFormatter
import java.util.*

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        EasyLog.showLogs(true)
        EasyLog.setFormatter(object : EasyLogFormatter() {
            override fun format(classname: String, methodName: String, lineNumber: Int): String {
                return String.format(Locale.getDefault(), "[%d] %s.%s() => ", lineNumber, classname, methodName)
            }
        })
    }
}