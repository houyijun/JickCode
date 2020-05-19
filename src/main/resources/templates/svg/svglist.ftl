	<span class="configuration">
		<a class="btn btn-sm btn-danger" href="/svg/new" rel="table-hover"><i class="glyphicon glyphicon-plus">New</i></a>
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
      
      <#list svglist as node>
      <tr>
			<td>${node_index+1}</td>
            <td>${node!''}</td>
            <td><span><a href="/svg/edit/${node}">Edit</a></span>
            <span><a onclick="del('${node}');">Delete</a></span>
            <span>Export:</span>
            <#list codetypes as codetype>
            <span><a href="/file/export/${node}/${codetype}">${codetype}</a></span>
            </#list>
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
        			url:"/svg/delete",
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
