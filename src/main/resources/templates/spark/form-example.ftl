 <form class="form-horizontal">
	<div class="control-group">
	<label class="control-label">Path</label>
	<div class="controls">
		<input type="text" name="myname" class="spark-data" value="c:\\github\data">
	</div>
	</div>
    <div class="control-group">
	<label class="control-label">JDBC type</label>
		<div class="controls">
			<input type="text" name="myname2" class="spark-data" value="jdbc">
		</div>
	</div>
</form>
     		
     		
 <!-- code example  
 val  myname="${node.props.myname}"
val ${node.name}= spark.load("${node.props.myname}")
 
 -->