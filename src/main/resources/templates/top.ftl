<div class="navbar navbar-inverse navbar-fixed-top navbar-layoutit">
	<div class="navbar-header">
		<button data-target="navbar-collapse" data-toggle="collapse"
			class="navbar-toggle" type="button">
			<span class="glyphicon-bar"></span> <span class="glyphicon-bar"></span>
			<span class="glyphicon-bar"></span>
		</button>
		<a class="navbar-brand" href="/"><span style="color: #9da5e4;"> <img
			src="/favicon.png"> Jick Code </span><span class="label label-default hidden"><small>0.0.1</small></span>
		</a>
	</div>
	<div class="collapse navbar-collapse">

		<ul class="nav pull-right hidden" style="padding-top:15px;padding-right:10px;">
			<li style="color:gray;">
				<span>
					Contact:<i class="glyphicon glyphicon-envelope"></i>househou@263.net
				</span>
			</li>
		</ul>
		<ul class="nav" id="menu-layoutit" style="padding-top:15px;">
			<li class="menu-spark" id="menu-spark">			
				<span id="menu-project" class="menu" onClick="location.href='/project/all'">Projects</span>
				<span id="menu-jnode" class="menu" onClick="location.href='/jnode/all'">Jnodes</span>
				<span id="menu-model" class="menu" onClick="location.href='/model/all'">Models</span>
				
		
				<div class="btn-group hidden" data-toggle="buttons-radio">
					<button type="button" id="edit"
						class="active btn btn-xs btn-primary">
						<i class="glyphicon glyphicon-plus"></i> 编辑
					</button>
					<button type="button" class="btn btn-xs btn-primary">
						<i class="glyphicon glyphicon-list"></i> 配置
					</button>
					<button type="button" class="btn btn-xs btn-primary">
						<i class="glyphicon glyphicon-cog"></i> 下载
					</button>
				</div>
			</li>
		</ul>
	</div>
	<!--/.navbar-collapse -->
</div>
<input id="menu" value="<#if menu??>${menu}</#if>" class="hidden"></input>
<!--/.navbar-fixed-top -->