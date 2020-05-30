	<input class="hidden type="text" id="template" value="${template}"></input>
	
	<div class="view pull-right">
		<span class="configuration">
		<a class="btn btn-sm btn-info" onclick="show()"><i class="glyphicon glyphicon-plus">Create</i></a>
		<a class="btn btn-sm btn-info" onclick="showImport()"><i class="glyphicon glyphicon-open">Import</i></a>
	</span>
	</div>
	<span class="help-block h4">工程列表【${template}】</span>
	
<div class="view">
  <table class="table" contenteditable="false">
      <thead>
        <tr>
          <th>#</th>
          <th>工程名称</th>
          <th>状态</th>
        </tr>
      </thead>
      <tbody>
      
      <#list svglist as node>
      <tr>
			<td>${node_index+1}</td>
            <td>${node!''}</td>
            <td>
            <ul class="list-unstyled list-inline">
             <li>
            <span><a class="text-muted" href="/${template}/project/edit/${node}"><i class="glyphicon glyphicon-pencil" ></i></a></span>
            </li>
            <li>
            <span><a class="text-muted" href="/${template}/project/export/${node}"><i class="glyphicon glyphicon-eye-open" ></i></a></span>
            </li>
            <li>
            <span><a class="text-muted" href="/${template}/project/download/${node}"><i class="glyphicon glyphicon-save" ></i></a></span>
            </li>
            <li>
               		<span><a onclick="del('${node}');"><i class="glyphicon glyphicon-trash text-danger" ></i></a></span>   
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

function showImport(){
	$("#importModal").modal({
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
<!-- create 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">创建</h4>
           </div>
        	<div class="modal-body">
				<div class="control-group">	
					<div class="controls">
                       <label class="control-label">名称:</label>
						<input type="text" name="name" class="spark-data form-control">                   
					</div>
				</div>
				
     		</div>
  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="button" class="btn btn-primary" onclick="submit()">提交</button>
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
               <h4 class="modal-title" id="myModalLabel">上传</h4>
           </div>
        	<div class="modal-body">
				<form  enctype="multipart/form-data" action="/${template}/project/import.do" class="form-horizontal" accept-charset="UTF-8" method="post">
        			<div class="control-group">
						<label class="control-label">名称</label>
						<div class="controls">
							<input type="text" name="name" class="form-control">
						</div>
					</div>
        			<div class="control-group">
						<label class="control-label">上传文件</label>
						<div class="controls">
							<div class="file-loading">
         						<input id="file-0c" class="file" name="filename" type="file" data-show-preview="false"  data-theme="fas">
  							</div>
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