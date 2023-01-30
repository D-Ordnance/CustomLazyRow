package com.deeosoft.customtanglazycolumn

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity

class Book(var author: String, var pages: String){
    fun calculateBookPrice(){

    }
}

class Invoice{
    private val databasePersistence = DatabasePersistence()

    val persistenceManager: PersistenceManager = PersistenceManager(databasePersistence)
//    persistenceManager.make()


}

interface InvoicePersistence{
    fun save()
}

class PrintPersistence: InvoicePersistence{
    override fun save() {
        println("printing logic called ...")
    }
}

class DatabasePersistence: InvoicePersistence{
    override fun save() {
        println("Saving to database ...")
    }
}

class PersistenceManager(var invoicePersistence: InvoicePersistence){
    fun make(){
        invoicePersistence.save()
    }
}

class Dashboard: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        PersistenceManager()
    }
}

// Given a task to log to an analytics platform
// write a class that is open to extension and close to modification
//


interface AnalyticLogger{
    fun log(title: String, msg: String)
}

class FirebaseLogger(context: Context): AnalyticLogger{

    //implement the firebase logger
    override fun log(title: String, msg: String) {

        println("logging to the firebase with title $title and message $msg")
    }
}

class AppFlyerLogger(context: Context): AnalyticLogger{
    //implement the app flyer  logger
    override fun log(title: String, msg: String) {
        println("logging to the apps flyer with the ")
    }
}

class AnalyticsManager{
    fun log(logger: AnalyticLogger, title: String, msg: String){
        logger.log(title, msg)
    }
}


class LoggerClass: ComponentActivity(){

    //Classes, routine, modules and function should be open for extension and closed for modification.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseLogger = FirebaseLogger(this)
        val appFlyerLogger = AppFlyerLogger(this)
        val analyticsManager = AnalyticsManager()
        analyticsManager.log(firebaseLogger,"Open/Close Principle", "Class should be open for extension and close for modification")
        analyticsManager.log(appFlyerLogger,"Open/Close Principle", "Class should be open for extension and close for modification")
    }
}