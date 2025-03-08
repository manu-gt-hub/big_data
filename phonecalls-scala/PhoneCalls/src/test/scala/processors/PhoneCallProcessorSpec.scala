import com.phoneCalls.processors.PhoneCallProcessor
import com.phoneCalls.utils.PhoneCallConstants

class PhoneCallProcessorSpec extends SparkUnitTest
{

  val result = PhoneCallProcessor.getBillCost(PhoneCallConstants.test.TEST_STRING)(sparkSession)

  "PhoneCallProcessor#getBillCost" should "return the expected result " in {
    assert(result == 900.0, "result must be 900.0")
  }


}
