 
 
 <!-- 模态框（Modal） -->
<div class="modal fade sourceFile" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">模态框（Modal）标题</h4>
           </div>
        	<div class="modal-body">
				<form class="form-horizontal">
					<div class="control-group">
						<label class="control-label">Path</label>
						<div class="controls">
							<input type="text" name="myname" class="spark-data" value="c:\\github\data">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">JDBC type</label>
						<div class="controls">
							<input type="text" name="myname2" class="spark-data" value="jdbc">
						</div>
					</div>
				</form>
     		</div>
  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
               <button type="button" class="btn btn-primary">提交更改</button>
           </div>
       </div><!-- /.modal-content -->
   </div><!-- /.modal -->
 </div>