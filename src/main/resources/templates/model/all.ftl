	<span class="configuration">
		<a class="btn btn-sm btn-info" onclick="show()"><i class="glyphicon glyphicon-plus">创建</i></a>
	</span>
	<div class="preview">Model List</div>
<div class="view">
  <table class="table" contenteditable="false">
      <thead>
        <tr>
          <th>#</th>
          <th>Model Name</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
      
      <#list nodes as node>
      <tr>
			<td>${node_index+1}</td>
            <td>${node!''}</td>
            <td><span><a href="/model/edit/${node}">Edit</a></span>
            <span><a onclick="del('${node}');">Delete</a></span>  
             <span><a href="/model/download?name=${node}">Download</a></span>          
            </td>
      <tr>
	  </#list>

      </tbody>
    </table>
	</div>
</div>

<script>
function del(node){
    if(confirm("确定要删除吗？")) {
        $.ajax({
        			url:"/model/delete",
        			type:"post",
        			dataType:"json",
        			data:{name:node},
        			success:function(res){
                    	console.log(res);
                    	window.location.reload();                  
            		}
    			});
    } 
}

function show(){
	$("#myModal").modal({
       		show: true,
       		backdrop:'static'
    })
}

function submit(){
	var name=$("#myModal input[name=modelname]").val();
	console.log(name);
	 $.ajax({
        			url:"/model/add",
        			type:"post",
        			dataType:"json",
        			data:{name:name},
        			success:function(res){
                    	console.log(res);
                    	if (res.code=="0"){
                    	window.location.reload();
                    	}else{
                    	alert(res.msg);
                    	}                  
            		}
    			});
}

</script>


 <!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">创建</h4>
           </div>
        	<div class="modal-body"><span>名称</span>
				<input type="text" name="modelname" class="spark-data btn form-control"  width="60px">
     		</div>
  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="button" class="btn btn-primary" onclick="submit()">提交</button>
           </div>
       </div><!-- /.modal-content -->
   </div><!-- /.modal -->
 </div>
 
