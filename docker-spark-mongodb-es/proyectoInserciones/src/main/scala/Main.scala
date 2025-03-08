import org.apache.spark.sql.SparkSession


object Main {




  def main(args: Array[String]){

    //create spark session
    val sparkSession: SparkSession = SparkSession.
      builder.
      config("spark.master", "local").
      getOrCreate()

    //read csv
    val csv=sparkSession.read.format("csv").option("header", "true").load(DataPersistUtils.CSV_PATH)

    println("CSV FILE READ SUCCESSFULLY")

    //insert the csv into HDFS
    DataPersistUtils.HDFSInsertions(csv)

    //read csv file from HDFS
    val csv_df=DataPersistUtils.getHDFSFile(sparkSession,"traffic_violations.csv")

    //insert the csv file into ElasticSearch
    DataPersistUtils.ElasticSearchInsertions(csv_df,"index/traffic-violations")

    //read the records from ElasticSearch
    val csv_df_ES =  DataPersistUtils.getElasticSearchFile_1(sparkSession,"index/traffic-violations")
    //val csv_df_ES =  getElasticSearchFile_2(sparkSession,"index/traffic-violations")

    //insert the csv into MongoDB
    DataPersistUtils.MongoDBInsertions(csv_df_ES,"TrafficViolations")

    //get MongoDB records
    val csv_df_MongoDB = DataPersistUtils.getMongoDBRecords(sparkSession,"TrafficViolations")

    val df1=csv_df.orderBy("personal_injury","make","model","color")
    val df2=csv_df_MongoDB.orderBy("personal_injury","make","model","color")

    //compare the first row between the two dataframes in order to see if they are equal
    if(
      df1.first().get(0) == df2.first().get(0) &&
      df1.first().get(1) == df2.first().get(1) &&
      df1.first().get(2) == df2.first().get(2) &&
      df1.first().get(3) == df2.first().get(3)

    ) println(" *** DATAFRAMES ARE EQUAL ***")
    else println(" *** DATAFRAMES ARE NOT EQUAL ***")

    //close spark session
    sparkSession.close()

  }
}