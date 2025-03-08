
import org.apache.spark.sql.SparkSession
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.LoggerFactory

class SparkUnitTest extends FlatSpec with Matchers {

  def logger = LoggerFactory.getLogger(getClass)

  val sparkSession = SparkSession.builder
    .master("local")
    .appName("SparkSession")
    .getOrCreate()

}