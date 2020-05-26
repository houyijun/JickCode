	<span class="configuration">
		<a class="btn btn-sm btn-info" onclick="show()"><i class="glyphicon glyphicon-plus">创建模板</i></a>
	</span>
	<div class="preview"><h2>模板列表</h2></div>

<div class="box box-element ui-draggable" style="display: block;"> 
	<div class="view">
         <ul class="thumbnails">
            <#list templates as template>
             <li class="span4">
               <div class="thumbnail">
                 <div class="caption">
                     <span><a href="/${template}/project/all">${template}</a></span>
                                      
                     <span class="pull-right"> 
                        <i onclick="showRename('${template}');" class="hidden glyphicon glyphicon-edit text-info" ></i>
                        <a href="/${template}/info"> <i  class="glyphicon glyphicon-th-list text-info" >配置...</i></a>
                             
                        <i onclick="del('${template}');" class="glyphicon glyphicon-trash text-danger" ></i>
                             
                            
                     </span>
                 
                 </div>
                 
               </div>
            </li>
            </#list>
        </ul>
     </div>
</div>

<script>

function del(template){
    if(confirm("确定要删除吗？")) {
        $.ajax({
        			url:"/delete",
        			type:"post",
        			dataType:"json",
        			data:{template:template},
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
	var name=$("#myModal input[name=modelname]").val();
	 $.ajax({
       		url:"/template/add",
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


function showRename(oldName){
	$("#renameModal input[name=oldName]").val(oldName)
	$("#renameModal").modal({
       		show: true,
       		backdrop:'static'
    })
}

function rename(){
	var newName=$("#renameModal input[name=newName]").val();
	var oldName=$("#renameModal input[name=oldName]").val();
	console.log(oldName,newName);
	 $.ajax({
       		url:"/template/rename",
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
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">创建</h4>
           </div>
        	<div class="modal-body"><span>名称</span>
				
				<input type="text" name="modelname" class="spark-data btn form-control"  width="60px">
     		</div>
  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="button" class="btn btn-primary" onclick="submit()">提交</button>
           </div>
       </div><!-- /.modal-content -->
   </div><!-- /.modal -->
 </div>
 
 
 <!-- 模态框（Modal） -->
<div class="modal fade" id="renameModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">重命名</h4>
           </div>
        	<div class="modal-body"><span>新名称</span>
				<input type="text" name="oldName" class="hidden">
				<input type="text" name="newName" class="spark-data btn form-control"  width="60px">
     		</div>
  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="button" class="btn btn-primary" onclick="rename()">提交</button>
           </div>
       </div><!-- /.modal-content -->
   </div><!-- /.modal -->
 </div>
