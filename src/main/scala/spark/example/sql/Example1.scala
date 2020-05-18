package spark.example.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType

object Example1 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .master("local")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val file = "file:///C:\\github\\data\\sparkdata.txt"
    val df = spark.read.csv(file).toDF("name","age","email")
    df.createOrReplaceTempView("people")
    val sql = spark.sql("select name,sum(age) as sumage from people group by name")
    sql.show()   
    //sql.rdd.repartition(1).saveAsTextFile("file:///C:\\github\\data\\tmp.txt")
  }
}