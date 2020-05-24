<input class="hidden type="text" id="template" value="${template}"></input>
<div class="view">
<span><a class="text-primary" href="/${template}/model/edit/${modelname}">Modify</a></span>
<span><a href="/${template}/model/download?name=${modelname}">Download</a></span>  
</div>
<label contenteditable="true">${modelname!''}</label>
<span class="help-block" contenteditable="true">Model Content:</span>
<textarea   class="form-control"  rows="20">${modelcontent!''}</textarea>
		