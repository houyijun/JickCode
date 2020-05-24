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
	
	<div class="preview">Jnode List</div>
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

</script>
