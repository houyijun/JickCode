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
            <td><a href="/project/edit/${node}">${node!''}</a></td>
            <td>
            <ul class="list-unstyled list-inline">
            <li>
            <#list codetypes as codetype>
            <span><a class="text-muted" href="/project/export/${node}/${codetype}"><i class="glyphicon glyphicon-new-window" ></i>${codetype}</a></span>
            </#list>
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
    if(confirm("确定要删除吗？")) {
        $.ajax({
        			url:"/project/delete",
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
	var name=$("#myModal input[name=name]").val();
	console.log(name);
	 $.ajax({
        			url:"/project/add.do",
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