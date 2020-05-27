	<div class="h4">JNode配置【${template}】 </div>
	<input class="hidden type="text" id="template" value="${template}"></input>
	<span class="configuration">
		<a class="btn btn-sm btn-info" href="/${template}/jnode/new" rel="table-hover"><i class="glyphicon glyphicon-plus">Create</i></a>
	</span>
	<ul class="btn-group nav pull-right">
       	<li>
		<span class="configuration">
		<a class="btn btn-sm btn-info" href="/${template}/jnode/download" rel="table-hover"><i class="glyphicon glyphicon-download">Export</i></a>
		
		<a class="btn btn-sm btn-info" href="/${template}/jnode/import" rel="table-hover"><i class="glyphicon glyphicon-upload">Import</i></a>
		</span>
       	</li>
    </ul>
	
<div class="view">
  <table class="table" contenteditable="false">
      <thead>
        <tr>
          <th>#</th>
          <th>Jnode Name</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
      
      <#list jnodelist as node>
      <tr>
			<td><a onclick="deljnode('${node}');"><i class="glyphicon glyphicon-trash text-danger" ></i></a>${node_index+1}</td>
            <td><a href="/${template}/jnode/edit/${node}">${node!''}</a></td>
            <td>
            
            <ul class="list-unstyled list-inline">
            <li><span> <i onclick="showRename('${node}');" class="glyphicon glyphicon-plus text-info" ></i></span>
            
            </li>
            <li>
                 
            <span><a href="/${template}/jnode/downsingle?node=${node}"><i class="glyphicon glyphicon-download" ></i></a> </span> 
            
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
function deljnode(node){
	var template=$("#template").val();
    if(confirm("确定要删除吗？")) {
        $.ajax({
        			url:"/"+template+"/jnode/delete",
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


function showRename(oldName){
	$("#renameModal input[name=oldName]").val(oldName)
	$("#renameModal").modal({
       		show: true,
       		backdrop:'static'
    })
}

function clone(){
	var template=$("#template").val();
	var newName=$("#renameModal input[name=newName]").val();
	var oldName=$("#renameModal input[name=oldName]").val();
	console.log(oldName,newName);
	 $.ajax({
       		url:"/"+template+"/jnode/clone",
       		type:"post",
       		dataType:"json",
       		data:{oldName:oldName,newName:newName},
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
<div class="modal fade" id="renameModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">复制</h4>
           </div>
        	<div class="modal-body"><span>新名称</span>
				<input type="text" name="oldName" class="hidden">
				<input type="text" name="newName" class="spark-data btn form-control"  width="60px">
     		</div>
  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="button" class="btn btn-primary" onclick="clone()">提交</button>
           </div>
       </div><!-- /.modal-content -->
   </div><!-- /.modal -->
 </div>