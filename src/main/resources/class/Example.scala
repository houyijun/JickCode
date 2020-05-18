package spark.example

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Main {
     
  def main(args: Array[String]): Unit = {
    
    val conf = new SparkConf().setAppName("Spark example").setMaster("local");
    val spark = new SparkContext(conf);    

    test Code 
    
  }

}

