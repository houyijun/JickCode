package codegen.spark.jnode.source;

/**
 * example:
 * val conf = new SparkConf().setAppName("IpLocation").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
    val getConnection=()=>{
      DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata?characterEncoding=UTF-8", "root", "123568")
    }
    val jdbcRDD: JdbcRDD[(Int, String, Any)] = new JdbcRDD(
      sc,
      getConnection,
      "select * from logs where id<? and id>=?",
      1,
      5,
      3,//分区数目
      rs => {
        val id = rs.getInt(1)
        val name = rs.getString(2)
        val age = rs.getInt(3)
        (id, name, age)
      }
    )
    //触发 action
    println(jdbcRDD.collect().toBuffer)
    sc.stop()
 * 
 * @author houyiju
 *
 */
public class JNodeSourceJDBC {
	private String connStr="";
	private String user;
	private String pass;
	private String sql;
	private String readDataCode="";

}
