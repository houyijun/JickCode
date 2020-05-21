	<span class="configuration">
		<a class="btn btn-sm btn-danger" href="/model/new" rel="table-hover"><i class="glyphicon glyphicon-plus">New</i></a>
	</span>
	<div class="preview">SVG List</div>
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
      
      <#list nodes as node>
      <tr>
			<td>${node_index+1}</td>
            <td>${node!''}</td>
            <td><span><a href="/model/edit/${node}">Edit</a></span>
            <span><a onclick="del('${node}');">Delete</a></span>            
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

</script>
