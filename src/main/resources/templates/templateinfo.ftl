	<input class="hidden type="text" id="template" value="${template}"></input>
	<span class="configuration">
		<a class="btn btn-sm btn-info" href="/${template}/jnode/all"><i class="glyphicon glyphicon-asterisk">节点模板</i></a>
		<a class="btn btn-sm btn-info" href="/${template}/model/all"><i class="glyphicon glyphicon-asterisk">代码模板</i></a>
	</span>


<div class="view">
     <div class="tabbable" id="tabs-template"> <!-- Only required for left/right tabs -->
          <ul class="nav nav-tabs">
                    <li class="active"><a href="#panel-jnode" data-toggle="tab" contenteditable="true">Jnode配置</a></li>
                    <li class=""><a href="#panel-model" data-toggle="tab" contenteditable="true">代码模板</a></li>
          </ul>
          <div class="tab-content">
            <div class="tab-pane active" id="panel-jnode" contenteditable="true">
                <p>第一部分内容.</p>
                <#include "/jnode/all.ftl">
            </div>
            <div class="tab-pane" id="panel-model" contenteditable="true">
               <p>第二部分内容.</p>
               <#include "/model/all.ftl">
            </div>
          </div>
     </div>
 </div>
              
<script>

</script>
