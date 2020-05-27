<span class="configuration">
	<a class="btn btn-sm btn-info" onclick="fileupload()"><i class="glyphicon glyphicon-folder-open">文件上传</i></a>
</span>
<form  enctype="multipart/form-data" action="/${template}/model/importContent.do" accept-charset="UTF-8" method="post">
   	<fieldset>
		<legend contenteditable="true">代码模板配置【${template!''}】
		 </legend>	
        
		<span class="help-block" contenteditable="true">模板内容</span>
		<textarea name="modelContent"  class="form-control"  rows="20">${modelcontent!''}</textarea>
		
		<button type="submit" class="btn btn-primary" contenteditable="true">提交</button>
	</fieldset>
</form>

<script>
function fileupload(){
	$("#myModal").modal({
       		show: true,
       		backdrop:'static'
    })
}

</script>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">上传模板 文件</h4>
           </div>
        	<div class="modal-body">
				<form  enctype="multipart/form-data" action="/${template}/model/import.do" accept-charset="UTF-8" method="post">
        			<div class="file-loading">
         				<input id="file-0c" class="file" name="filename" type="file" data-show-preview="false"  data-theme="fas">
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
