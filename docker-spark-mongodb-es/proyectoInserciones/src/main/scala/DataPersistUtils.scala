import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.types.{BooleanType, StringType, StructField, StructType}

object DataPersistUtils {

  val CSV_PATH ="../data/slice_violations.csv"

  case class TrafficViolation(
    personal_injury: Boolean,
    make: String,
    model:String,
    color:String
  )

  val SCHEMA = StructType(
    Seq(
      StructField("personal_injury", BooleanType, true),
      StructField("make", StringType, true),
      StructField("model", StringType, true),
      StructField("color", StringType, true))
  )

  def ElasticSearchInsertions (df: DataFrame, index: String) = {

    df.write
      .format("org.elasticsearch.spark.sql")
      .option("es.nodes.wan.only",ConfigFactory.load().getString("elastic.WanOnly.value"))
      .option("es.port",ConfigFactory.load().getString("elastic.port.value"))
      .option("es.net.ssl",ConfigFactory.load().getString("elastic.SSL.value"))
      .option("es.nodes", ConfigFactory.load().getString("elastic.nodes.value"))
      .mode("Overwrite")
      .save(index)
  }


  def getElasticSearchFile_2 (sparkSession: SparkSession,index: String):DataFrame = {

    val reader = sparkSession.read
      .format("org.elasticsearch.spark.sql")
      .option("es.nodes.wan.only","true")
      .option("es.port",ConfigFactory.load().getString("elastic.port.value"))
      .option("es.net.ssl",ConfigFactory.load().getString("elastic.SSL.value"))
      .option("es.nodes", ConfigFactory.load().getString("elastic.nodes.value"))

    val df = reader.schema(SCHEMA).load(index).select("personal_injury","make","model","color")

    df

  }

  def getElasticSearchFile_1 (sparkSession: SparkSession,index: String):DataFrame = {


    val myquery = "{\"query\": {\"match_all\": {}}}"
    val df = sparkSession.read.format("org.elasticsearch.spark.sql")
      .option("query", myquery)
      .option("pushdown", "true")
      .schema(SCHEMA)
      .load(index)
      .limit(200000) // instead of size
      .select("personal_injury","make","model","color") // instead of fields

    df

  }

  def MongoDBInsertions (df: DataFrame,collection:String) = {

    df.write
      .format("com.mongodb.spark.sql.DefaultSource")
      .option("uri", "mongodb://localhost/local."+collection)
      .mode("Overwrite")
      .save()
  }

  def getMongoDBRecords (sparkSession: SparkSession,collection: String):DataFrame = {

    val df = sparkSession.read.format("com.mongodb.spark.sql.DefaultSource")
      .option("uri", "mongodb://localhost/local."+collection)
      .schema(SCHEMA)
      .load()
      .limit(200000)
      .select("personal_injury","make","model","color")
    df
  }

  def HDFSInsertions (df: DataFrame) = {

    df.write.mode(SaveMode.Overwrite).csv("hdfs://localhost:8020"+ "/datalake/traffic_violations.csv")
  }

  def getHDFSFile (sparkSession: SparkSession,file: String):DataFrame  = {

    sparkSession.read.format("csv")
      .schema(SCHEMA)
      .load("hdfs://localhost:8020"+ "/datalake/"+file)
      .select("personal_injury","make","model","color")
  }

}
