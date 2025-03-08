package com.phoneCalls.utils

import java.util.Calendar

import org.apache.spark.sql.functions.{col, split, _}
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType}
import org.apache.spark.sql.{DataFrame, SparkSession}



object PhoneCallUtils {

  /**
    * This function will return the duration of the call
    * @param h hours
    * @param m minutes
    * @param s seconds
    * @return time in milliseconds
    *
    * */
  def calculateDuration (h:Int,m:Int,s:Int):Long = {
    //get time in milliseconds from Calendar instance
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR, h)
    cal.set(Calendar.MINUTE, m)
    cal.set(Calendar.SECOND, s)
    cal.getTimeInMillis
  }

  val calculateDurationUDF = udf((hours: Int,minutes:Int,seconds:Int) => calculateDuration(hours,minutes,seconds))

  /**
    * This function will return the cost of each call
    * @param h hours
    * @param m minutes
    * @param s seconds
    * @return minutesMultip calculated cost of the phone call
    *
    * */
  def calculateCostsPerCall (h:Int,m:Int,s:Int):Double = {
    val minutesMultip = if (s == 0) m else m + 1
    //calculate duration and costs
    if(m<5)
      ((m * 60) + s) * PhoneCallConstants.values.MULT_LESS_THAN_5
    else
      minutesMultip * PhoneCallConstants.values.MULT_AT_LEAST_5
  }

  val calculateCostUDF = udf((hours: Int,minutes:Int,seconds:Int) => calculateCostsPerCall(hours,minutes,seconds))

   /**
    * This function will parse the phone call through the string given
    * @param values the phone call given as string
    * @return dfReturn phone call parsed
    *
    * */
  def parsePhoneCalls(values: DataFrame,sparkSession: SparkSession): DataFrame ={
    //make a new data frame with columns calculated
    val dfReturn=values.
      withColumn("hours", split(col("raw"), ":").getItem(0).cast(IntegerType)).
      withColumn("minutes", split(col("raw"), ":").getItem(1).cast(IntegerType)).
      withColumn("seconds", split(split(col("raw"), ":").getItem(2),",").getItem(0).cast(IntegerType)).
      withColumn("phoneNumber", split(col("raw"), ",").getItem(1)).
      withColumn("durationInMillis",calculateDurationUDF(col("hours"),col("minutes"),col("seconds")).cast(LongType))
        .drop("raw")

    dfReturn

  }
  /**
    * This function will apply the promotion to the current values
    * @param df the phone calls as data frame
    * @return promotionApplied dataframe with phone numbers and costs calculated
    *
    * */
  def applyPromotion(df:DataFrame,sparkSession: SparkSession): DataFrame=
  {
    import sparkSession.implicits._

    //make a new dataframe phoneNUmber | time duration in milliseconds
    val totalDurationDF=df.rdd.
     map(i => (i(3).asInstanceOf[String], i(4).asInstanceOf[Long])).
     reduceByKey((a,b) => a + b).toDF("phoneNumber","groupedTotalDuration")

    //get the max duration
    val maxDuration= totalDurationDF.agg(max(totalDurationDF("groupedTotalDuration"))).head()(0)

    //get the number associated to it
    val maxPhoneNumbers=totalDurationDF.filter(i=>i.get(1) == maxDuration)

    //get the min phone number in case there is more than one
    val maxphoneNum = maxPhoneNumbers.agg(min(maxPhoneNumbers("phoneNumber"))).head()(0).toString

    //create a dataframe filtering and getting only those rows where the phoneNumber of promotion is not the same
    val promotionApplied=df.filter(i=>i.get(3) != maxphoneNum).withColumn("cost",calculateCostUDF(col("hours"),col("minutes"),col("seconds")).cast(DoubleType))

    promotionApplied
  }

  /**
    * This function will get the final cost of the bill with the applied promotion
    * @param df the data frame
    * @return billCost as the cost of the bill
    *
    * */
  def getBillAmount(df:DataFrame): Double=
  {
    //summarize the column "cost" of the data frame
    val billCost=df.rdd.
      map(i => (i(5).asInstanceOf[Double])).
      reduce((a,b) => a + b)

    billCost
  }

}
