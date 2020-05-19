<!DOCTYPE html>
<html>
<head lang="en">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>JickCode</title>

<meta name="keywords" content="Spark,JickCode">
<meta name="description"
	content="JickCode is a draggable Spark code generator">
<link rel="shortcut icon" href="/favicon.png">

<!-- Le styles -->
<link href="/css/bootstrap.min.css" rel="stylesheet">

<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script src="/spark/uuid.js"></script>

<link type="text/css" rel="stylesheet" href="/spark/main.css" />

</head>
<body class="edit" style="min-height: 488px; cursor: auto;">
	<#include "/top.ftl">
	
	<div class="container" style="margin-top:60px;">
		<div class="row">
			<#include "${divname}">
		</div>
	</div>
</body>
</html>