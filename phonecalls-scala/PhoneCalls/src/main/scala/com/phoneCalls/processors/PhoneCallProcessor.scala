package com.phoneCalls.processors

import com.phoneCalls.utils.PhoneCallUtils
import com.phoneCalls.utils.PhoneCallUtils.applyPromotion
import org.apache.spark.sql.SparkSession

import scala.util.matching.Regex

object PhoneCallProcessor {

    /**
    * This function will return the cost the whole bill
    * @param phoneCalls the phone calls
    * @return cost cost of the bill
    *
    * */
  def getBillCost(phoneCalls: String)(sparkSession: SparkSession): Double = {
    import sparkSession.implicits._
    //declare a Reg expression
    val keyValPattern: Regex = "(([0-9]{2}:){2}[0-9]{2}),(([0-9]{3}-){2}[0-9]{3})".r

    //find occurences
    val arrayPhoneCalls= keyValPattern.findAllIn(phoneCalls).toArray
    val rawPhoneCalls = sparkSession.sparkContext.parallelize(arrayPhoneCalls).toDF("raw")

    //data frame well formatted
    val phoneCallsFormatted = PhoneCallUtils.parsePhoneCalls(rawPhoneCalls,sparkSession)
    phoneCallsFormatted.persist()

    //apply the promotion
    val dataFrameBill = applyPromotion(phoneCallsFormatted,sparkSession)

    //get the bill cost
    val billCost = PhoneCallUtils.getBillAmount(dataFrameBill)

    billCost

  }
}
