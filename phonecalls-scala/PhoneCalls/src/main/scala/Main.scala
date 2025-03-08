import com.phoneCalls.processors.PhoneCallProcessor
import com.phoneCalls.utils.PhoneCallConstants
import org.apache.spark.sql.SparkSession

object Main {

  def main(args: Array[String]){

      val sparkSession: SparkSession = SparkSession.builder.config("spark.master", "local").getOrCreate()
      //call to the function
      val cost=PhoneCallProcessor.getBillCost(
        PhoneCallConstants.test.STRING_CALL_VALUES
      )(sparkSession)

      sparkSession.close()

      println(cost)
  }
}