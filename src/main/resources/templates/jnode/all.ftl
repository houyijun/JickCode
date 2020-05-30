	<input class="hidden type="text" id="template" value="${template}"></input>
    
    <div class="view pull-right">
		<span class="configuration">
		<a class="btn btn-sm btn-info" href="/${template}/jnode/new" ><i class="glyphicon glyphicon-plus">Create</i></a>
		
		<a class="btn btn-sm btn-info" href="/${template}/jnode/download"><i class="glyphicon glyphicon-download">Export</i></a>
		<a class="btn btn-sm btn-info hidden" href="/${template}/jnode/import"><i class="glyphicon glyphicon-upload">Import</i></a>
		<a class="btn btn-sm btn-info" onclick="showImport();"><i class="glyphicon glyphicon-upload">Import</i></a>
		
	</span>
	</div>
	<span class="help-block h4">JNode配置【${template}】</span>
	
<div class="view">
  <table class="table" contenteditable="false">
      <thead>
        <tr>
          <th>#</th>
          <th>名称</th>
          <th>状态</th>
        </tr>
      </thead>
      <tbody>
      
      <#list jnodelist as node>
      <tr>
			<td>${node_index+1}</td>
            <td>${node!''}</td>
            <td>
            
            <ul class="list-unstyled list-inline">
            <li><span><a href="/${template}/jnode/edit/${node}"> <i  class="glyphicon glyphicon-pencil text-info" ></i></a></span>
            </li>
            
            <li><span> <i onclick="showRename('${node}');" class="glyphicon glyphicon-plus text-info" ></i></span>
            
            </li>
            <li>                 
            <span><a href="/${template}/jnode/downsingle?node=${node}"><i class="glyphicon glyphicon-download" ></i></a> </span> 
             </li>
            <li>
               		<span><a onclick="deljnode('${node}');"><i class="glyphicon glyphicon-trash text-danger" ></i></a></span>   
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

function showImport(){
	$("#importModal").modal({
       		show: true,
       		backdrop:'static'
    })
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
 
 
 <!-- import模态框（Modal） -->
<div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="importModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">上传Jnode模板</h4>
           </div>
        	<div class="modal-body">
				<form  enctype="multipart/form-data" action="/${template}/jnode/import.do" class="form-horizontal" accept-charset="UTF-8" method="post">
        			<div class="control-group">
						<h5>选择文件...</h5>
        				<div class="file-loading">
         					<input id="file-0c" class="file" name="filename" type="file"  data-show-preview="false" data-theme="fas">
  						</div>
					</div>
        			
         		<div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="submit button" class="btn btn-primary" >提交</button>
           		</div>
			</form>
			
     		</div> 
          
       </div><!-- /.modal-content -->
   </div><!-- /.modal -->
 </div>

<link href="/fileinput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
<link href="/fileinput/themes/explorer-fas/theme.css" media="all" rel="stylesheet" type="text/css"/>
<script src="/fileinput/js/plugins/piexif.js" type="text/javascript"></script>
<script src="/fileinput/js/plugins/sortable.js" type="text/javascript"></script>
<script src="/fileinput/js/fileinput.js" type="text/javascript"></script>
<script src="/fileinput/js/locales/fr.js" type="text/javascript"></script>
<script src="/fileinput/js/locales/es.js" type="text/javascript"></script>
<script src="/fileinput/themes/fas/theme.js" type="text/javascript"></script>
<script src="/fileinput/themes/explorer-fas/theme.js" type="text/javascript"></script>   