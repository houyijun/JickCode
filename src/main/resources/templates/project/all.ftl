	<input class="hidden type="text" id="template" value="${template}"></input>
	<span class="configuration">
		<a class="btn btn-sm btn-info" onclick="show()"><i class="glyphicon glyphicon-plus">Create</i></a>
	</span>
	<div class="preview">Project List...</div>
<div class="view">
  <table class="table" contenteditable="false">
      <thead>
        <tr>
          <th>#</th>
          <th>Jnode Name</th>
          <th>Export Code</th>
        </tr>
      </thead>
      <tbody>
      
      <#list svglist as node>
      <tr>
			<td><a href="javascript:del('${node}');"><i class="glyphicon glyphicon-trash text-danger" ></i></a>${node_index+1}</td>
            <td><a href="/${template}/project/edit/${node}">${node!''}</a></td>
            <td>
            <ul class="list-unstyled list-inline">
            <li>
            <span><a class="text-muted" href="/${template}/project/export/${node}"><i class="glyphicon glyphicon-download" ></i></a></span>
            </li>
            
            </ul>
            </td>
      <tr>
	  </#list>

      </tbody>
    </table>
	</div>
</div>

<script>
function del(node){
	var template=$("#template").val();
    if(confirm("确定要删除吗？")) {
        $.ajax({
        			url:"/"+template+"/project/delete",
        			type:"post",
        			dataType:"json",
        			data:{node:node},
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
	var template=$("#template").val();
	var name=$("#myModal input[name=name]").val();
	console.log(name);
	 $.ajax({
        			url:"/"+template+"/project/add.do",
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
				<input type="text" name="name" class="spark-data btn form-control"  width="60px">
     		</div>
  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="button" class="btn btn-primary" onclick="submit()">提交</button>
           </div>
       </div><!-- /.modal-content -->
   </div><!-- /.modal -->
 </div>