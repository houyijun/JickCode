
<form  enctype="multipart/form-data" action="/model/import.do" accept-charset="UTF-8" method="post">
   	<fieldset>
		<legend contenteditable="true">Edit Model...</legend>
		<label contenteditable="true">名称</label>
		<input type="text" placeholder="Type something…" name="name" value="${modelname!''}">
		
		<span>文件:<input type="file" name="filename"/>
        <input type="submit" value="上传"/></span>
        
		<span class="help-block" contenteditable="true">Model Content:</span>
		<textarea   class="form-control"  rows="20">${modelcontent!''}</textarea>
		
		<button type="submit" class="btn" contenteditable="true">提交</button>
	</fieldset>
</form>