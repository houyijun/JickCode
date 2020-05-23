	<span class="configuration">
		<a class="btn btn-sm btn-info" href="/jnode/new" rel="table-hover"><i class="glyphicon glyphicon-plus">Create</i></a>
		
	</span>
	<ul class="btn-group nav pull-right">
       	<li>
		<span class="configuration">
		<a class="btn btn-sm btn-info" href="/jnode/download" rel="table-hover"><i class="glyphicon glyphicon-download">Export</i></a>
		
		<a class="btn btn-sm btn-info" href="/jnode/import" rel="table-hover"><i class="glyphicon glyphicon-upload">Import</i></a>
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
			<td>${node_index+1}</td>
            <td><a href="/jnode/uploadform/${node}">${node!''}</a></td>
            <td>
            
            <ul class="list-unstyled list-inline">
            <li><a onclick="deljnode('${node}');"><i class="glyphicon glyphicon-remove text-danger" ></i></a>  </li>
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
    if(confirm("确定要删除吗？")) {
        $.ajax({
        			url:"/jnode/delete",
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
