	<span class="configuration">
		<a class="btn btn-sm btn-danger" href="/jnode/new" rel="table-hover"><i class="glyphicon glyphicon-plus">New</i></a>
	</span>
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
            <td>${node!''}</td>
            <td><span><a href="/jnode/uploadform/${node}">Edit</a></span>
            <span><a onclick="deljnode('${node}');">Delete</a></span></td>
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
