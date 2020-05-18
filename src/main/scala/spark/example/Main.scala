

package spark.example

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Main {

  
  
  case class People(name:String,age:String,email:String)
  
  def main(args: Array[String]): Unit = {

    /**
     * load data from txtfile with sparkContext
     */
    val conf = new SparkConf().setAppName("Spark example").setMaster("local");
    val sc = new SparkContext(conf);
    

    var file = "file:///C:\\github\\data\\sparkdata.txt"
    var fileRdd = sc.textFile(file)
    fileRdd.foreach(println)
    println("#len:"+ fileRdd.count())

    // String => People class
    val pRdd=fileRdd.map(line=>{
      val lines=line.split(",")
      People(lines(0),lines(1),lines(2))
      
    })
    
    val flat=pRdd.flatMap(p=>{for (i<-0 until 3) yield(p)})
    flat.foreach(p=>println(p.name))
    
    val pair=pRdd.reduce((p1,p2)=>p1)
    println("pair:",pair)
  }

  // read file from fs
  def readFile(): Unit = {
    var file = "C:\\github\\data\\sparkdata.txt"
    val fileSrc = scala.io.Source.fromFile(file)
    fileSrc.getLines().foreach(println)
  }
}