
import Utilities._
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.elasticsearch.spark.sql._

case class Data(num: Int,brand: String)
case class Location(lat: Double, lon: Double)


object TwitterStreamMain{

  val KEYWORDS = Array("renault", "citroen", "volkswagen", "opel", "skoda", "fiat")
  val warehouseLocation="spark-warehouse"

  def main(args: Array[String]) {

    setupLogging()

    setupTwitter()

    val sparkSession = SparkSession.builder
      .master("local[*]")
      .appName("TwitterStream")
      .getOrCreate()

    val ssc = new StreamingContext(sparkSession.sparkContext,Seconds(1))
    val esConf = Map("es.nodes" -> "0.0.0.0", "es.port" -> "9200", "es.nodes.wan.only" -> "true")


    val tweets = TwitterUtils.createStream(ssc,None,KEYWORDS)
    val data = tweets.map { status =>
      val hashtags = status.getHashtagEntities
      val intersect=hashtags.map(_.getText().toLowerCase()).intersect(KEYWORDS.map(i=>i.toLowerCase().replace("#","").trim))

      println("HASHTAGS: "+hashtags.map(_.getText()+" ").mkString)
      println("FILTERED HASHTAGS: "+intersect.map(_+" ").mkString)

      (intersect)
    }
    data.foreachRDD{ rdd =>

      if (!rdd.isEmpty()) {
        val filteredHashtags = rdd.filter(x => x.nonEmpty)
        import sparkSession.implicits._


        val df = filteredHashtags.map(x => Data(1,x(0))).toDF()
        df.createOrReplaceTempView("tweets")


        df.saveToEs("tweets/cars", esConf)

      }
    }
    // Kick it all off
    ssc.checkpoint("~/code/spark-course/streaming-course/checkpoint/")
    ssc.start()
    ssc.awaitTermination()
  }
}