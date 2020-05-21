<form  action="/model/uploadJnode" accept-charset="UTF-8" method="post">
   	<fieldset>
		<legend contenteditable="true">表单项</legend>
		<label contenteditable="true">名称</label>
		<input type="text" placeholder="Type something…" name="name" value="${jnodename}">
		
		<span class="help-block" contenteditable="true">属性框内容</span>
		<textarea id="propdialog" name="propdialog" class="form-control" rows="8"  placeholder="添加属性配置模态框">${dialog}</textarea>
		
		<span class="help-block" contenteditable="true">模板内容</span>
		<textarea id="data" name="data" class="form-control" rows="8"  placeholder="Add a description…">${ftl}</textarea>
		<button type="submit" class="btn" contenteditable="true">提交</button>
	</fieldset>
</form>