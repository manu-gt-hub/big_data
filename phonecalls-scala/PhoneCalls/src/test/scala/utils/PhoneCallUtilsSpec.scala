import com.phoneCalls.utils.{PhoneCallConstants, PhoneCallUtils}

import scala.util.matching.Regex

class PhoneCallUtilsSpec extends SparkUnitTest{

  import sparkSession.implicits._

  val keyValPattern: Regex = "(([0-9]{2}:){2}[0-9]{2}),(([0-9]{3}-){2}[0-9]{3})".r

  val arrayPhoneCalls= keyValPattern.findAllIn(PhoneCallConstants.test.TEST_STRING).toArray
  val rawPhoneCalls = sparkSession.sparkContext.parallelize(arrayPhoneCalls).toDF("raw")
  val result = PhoneCallUtils.parsePhoneCalls(rawPhoneCalls,sparkSession)

  "PhoneCallUtils#parsePhoneCall" should "return the expected result " in {
    assert(result.first().get(0) == 0, "hours must be 0")
    assert(result.first().get(1) == 1, "minutes must be 1")
    assert(result.first().get(2) == 7, "seconds must be 7")
  }

  val result3 =  PhoneCallUtils.calculateCostsPerCall(0,5,0)

  "PhoneCallUtils#calculateCostsPerCall" should "return the expected result " in {
    assert(result3 == 750.0, "cost must be 750.0")
  }

  val result4 =  PhoneCallUtils.applyPromotion(result,sparkSession)

  "PhoneCallUtils#applyPromotion" should "return the expected result " in {
    assert(result4.count() == 1, "rows must be 1")
  }

}
