	<span class="configuration">
		<a class="btn btn-sm btn-info" onclick="show()"><i class="glyphicon glyphicon-plus">Create</i></a>
	</span>
	<div class="preview">模板列表</div>

<div class="box box-element ui-draggable" style="display: block;"> 
	<div class="view">
         <ul class="thumbnails">
            <#list templates as template>
             <li class="span4" onclick="window.location.href='/${template}/info'">
               <div class="thumbnail">
                 <div class="caption">
                     <h4>${template}</h4>
                  </div>
                </div>
               </div>
            </li>
            </#list>
        </ul>
     </div>
</div>

<script>
function show(){
	$("#myModal").modal({
       		show: true,
       		backdrop:'static'
    })
}

function submit(){
	var name=$("#myModal input[name=modelname]").val();
	console.log(name);
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
 
