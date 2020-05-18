	<span class="configuration">
		<a class="btn btn-sm btn-default" href="/svg/new" rel="table-hover">New</a>
		<a class="btn btn-sm btn-default" href="#" rel="table-sm">Close</a>
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
            <span><a onclick="del('${node}');">Delete</a></span></td>
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
