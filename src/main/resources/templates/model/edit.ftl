
<form  enctype="multipart/form-data" action="/model/import.do" accept-charset="UTF-8" method="post">
   	<fieldset>
		<legend contenteditable="true">${modelname!''}
		 </legend>
		 <input type="text" placeholder="Type something…" class="hidden" name="name" value="${modelname}">
		
        <h3>Uploading model file</h3>
        <div class="file-loading">
         <input id="file-0c" class="file" name="filename" type="file" data-show-preview="false"  data-theme="fas">
  		</div>
        
		<span class="help-block" contenteditable="true">Model Content:</span>
		<textarea   class="form-control"  rows="20">${modelcontent!''}</textarea>
		
		<button type="submit" class="btn" contenteditable="true">提交</button>
	</fieldset>
</form>

<link href="/fileinput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
<link href="/fileinput/themes/explorer-fas/theme.css" media="all" rel="stylesheet" type="text/css"/>
 <script src="/fileinput/js/plugins/piexif.js" type="text/javascript"></script>
    <script src="/fileinput/js/plugins/sortable.js" type="text/javascript"></script>
    <script src="/fileinput/js/fileinput.js" type="text/javascript"></script>
    <script src="/fileinput/js/locales/fr.js" type="text/javascript"></script>
    <script src="/fileinput/js/locales/es.js" type="text/javascript"></script>
 <script src="/fileinput/themes/fas/theme.js" type="text/javascript"></script>
 <script src="/fileinput/themes/explorer-fas/theme.js" type="text/javascript"></script>    
