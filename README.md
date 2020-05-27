Project Name: JickCode
平时经常要写spark代码，时间长了都要去网上搜索相关的示例代码，然后开始coding。时间长了，也觉得挺麻烦的。
后来想，不如开发一个Spark代码模板系统，图形化的方式拖拽编辑RDD节点，最后自动导出代码，不是更方便吗。

然后就开始编了，一开始要定制化很多节点模板，比如Source，Sink，Map，FlatMap等运算的节点，都是硬编码实现。
这样也实在是太麻烦了不是？ 有没有办法让用户自己来编写各个运算节点的代码输出规则呢。

然后JickCode就变成现在这样了。

【JNode配置】
JNode要配置两个东西，一个是modal对话框，定义该JNode节点有哪些属性，在html上如何展示的，样式如下：
 <form class="form-horizontal">
	<div class="control-group">
	  <label class="control-label">Code format</label>
		<div class="controls">
			<input type="text" name="codename" class="spark-data" value="ss">
		</div>
	</div>
</form>
另一个是代码模板，兼容freemaker语法。${node}表示本节点。
示例如下：
val  myname="${node.props.myname}"
val ${node.name}= spark.load("${node.props.myname}")

【代码模板配置】
每种代码模板包含一个输出代码的字符串样式，必须要有  ${code_generated} ，这代表了这个svg图整个的输出代码。可以添加自己的文本。比如：
package myspark
import org.apache.spark.*
/**
  Spark示例代码模板
*/
object SparkMain {
  def main(args: Array[String]): Unit = {
    // spark code模板  
    ${code_generated} 
  }
}  
【创建模板】
支持创建多种模板，每种模板配置自己的JNode和代码模板。


