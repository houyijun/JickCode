<input class="hidden type="text" id="template" value="${template}"></input>
<input id="svgname" value="${svgname}" class="hidden"></input>
 	<div class="view">
    	<ul class="btn-group nav pull-right">
        	<li>
        	<span class="btn"  onclick="postsvg('${svgname}',dragData)"><i class="glyphicon glyphicon-ok text-warn">提交</i></span>
        	<span class="btn"  onclick="javascript:window.location.href='/${template}/project/exportcode/${svgname}'"><i class="glyphicon glyphicon-export text-warn">生成代码</i></span>
        	<span class="btn btn-info hidden"  onclick="getsvg('${svgname}')">Load</span>
        	</li>
        </ul>
     </div>
      
      
     <div class="view">
        <ul class="shuiguo btn-group">
         <li class="btn btn-primary" style="background:white;color:black;border:none;"><span> ${svgname}:</span></li>
        <#list names as name>
           <li draggable="true" class="btn btn-primary" data-name="${name}">${name}</li>
        </#list>
        </ul>
     </div>
        
        <div id="d1" style="width:auto;">
            <svg>                
            </svg>
        </div>
        <#noparse>
        <script type="text/javascript">
            var dragData = [];
            var mapData = {};//new Object();
            //重置拖拽后流程图展示
            function reload(isend) {
            	loadMap();
                $(function() {
                    var html = "";
                    var g = `
                    <defs>
                        <marker id="markerArrow1" markerWidth="10" markerHeight="10" refX="0" refY="3" orient="auto" markerUnits="strokeWidth">
                            <path d="M0,1 L0,5 L3,3 z" fill="#CCCCCC"></path> 
                        </marker>
                        <marker id="markerArrow2" markerWidth="10" markerHeight="10" refX="0" refY="3" orient="auto" markerUnits="strokeWidth">
                            <path d="M0,1 L0,5 L3,3 z" fill="#cccdff"></path> 
                        </marker>
                        <marker id="markerArrow3" markerWidth="10" markerHeight="10" refX="3" refY="2.6" orient="auto" markerUnits="strokeWidth">
                            <path fill="#f00" d="m6.75848,4.22161c-0.13193,0.12924 -0.3468,0.12924 -0.47903,0l-3.03436,-2.97252c-0.13193,-0.12953 -0.13223,-0.33974 0,-0.46927c0.13163,-0.12953 0.3465,-0.12953 0.47933,0l3.03406,2.97223c0.13193,0.13012 0.13253,0.34003 0,0.46956l0,0l0,0zm-0.00361,-2.974l-3.03406,2.97223c-0.13253,0.12983 -0.3471,0.12983 -0.47933,0c-0.13223,-0.12924 -0.13223,-0.33915 0.0003,-0.46927l3.03406,-2.97193c0.13253,-0.12953 0.3474,-0.12953 0.47903,-0.0003c0.13253,0.12953 0.13193,0.33974 0,0.46927l0,0l0,0z"/>
                        </marker>
                    </defs>
                    `;
                    if($('svg').siblings()) { //清除$('svg')范围外的所有元素
                        var prev = $('svg').siblings();
                        for(var i = 0; i < prev.length; i++) {
                            prev[i].remove();
                        }
                    }
                    console.log(dragData);
                    for(var i = 0; i < dragData.length; i++) { //循环dragData，重置流程图所有dom节点
                        if(dragData[i] != undefined) {
                            var data = dragData[i];
                            html +=
                                `
                            <div class = "${data.name}" data-drag="1" data-id = "${data.id}" data-inx = "${data.inx}" data-iny = "${data.iny}" data-label = "${data.label}" ondragstart = "insideDrag(this)"  draggable = "true" style = "transform:translate(${data.x}px,${data.y}px)" >
                                <span class = "${data.icon}" data-id = "${data.id}"></span>
                                <span onclick="shownode(${data.id})" data-id = "${data.id}">${data.label}</span>
                                <div class = "output">
                                    <span class = "circle" title = "输出" onmousedown = "noDrag(this)" onmouseup = "addDrag(this)" onmouseleave = "draw(this)" onmouseenter = "noMove()" data-id = "${data.id}"></span>
                                </div>
                                <div class="pull-right"><i onclick="removeNode(${data.id});" class="glyphicon glyphicon-remove text-danger" ></i></div>
                            </div>  
                        `
                            if(data.link.length > 0) {
                                for(var j = 0; j < data.link.length; j++) {
                                    g +=
                                        `
                                        <g id="${data.link[j].name}">
                                            <path style="cursor:pointer" d = "M${data.outx} ${data.outy} Q${data.link[j].mx1} ${data.link[j].my1} ${data.link[j].mx2} ${data.link[j].my2} T${data.link[j].dx} ${data.link[j].dy}" stroke = "#CCCCCC" fill = "none" stroke-width="4" marker-end="url(#markerArrow1)"/>
                                        </g>
                                        `
                                }
                            }
                        }
                    }
                    $('svg').before(html);
                    $('svg').html(g);
                    if(isend) {
                        $('svg').on('mouseenter', "path", function() {
                            $(this).attr({
                                "stroke": "#cccdff",
                                "marker-end": "url(#markerArrow2)",
                                "marker-mid": "url(#markerArrow3)"
                            })
                        }).on('mouseleave', "path", function() {
                            $(this).attr({
                                "stroke": "#cccccc",
                                "marker-end": "url(#markerArrow1)",
                                "marker-mid": ""
                            })
                        }).on('click', "path", function() {
                            var $p = $(this).parent();
                            var id = $p[0].id;
                            for(var i = 0; i < dragData.length; i++) {
                                var data = dragData[i];
                                for(var j = 0; j < data.link.length; j++) {
                                    if(id == data.link[j].name) {
                                        data.link.splice(j, 1)
                                    }
                                }
                                for(var j = 0; j < data.linked.length; j++) {
                                    if(id == data.linked[j].name) {
                                        data.linked.splice(j, 1)
                                    }
                                }
                            }
                            $p.remove()
                        });
                    } else {
                        $('svg').off('mouseenter mouseleave', "path");
                    }
                   
                    console.log($('svg').siblings());
                })
            }
            //reload();
            document.getElementById('d1').ondragover = function(e) {
                e.preventDefault(); //流程图展示区阻止默认事件
            }
            var dWidth = Number($('#d1').css('width').slice(0, -2)); //流程图展示区域宽度
            console.log(dWidth);
            var dHeight = Number($('#d1').css('height').slice(0, -2)); //流程图展示区域高度
            console.log(dHeight);
            var dClient = $("#d1").offset().top; //流程图展示区偏移位置top
            var dLeft = $("#d1").offset().left; //流程图展示区偏移位置left
            console.log('顶部位置', dClient);
            console.log('左边位置', dLeft);

            //模块拖进流程图后，初始化拖拽方法
            /*
             * word：模块名称
             * name:模块数据名称
             * type:拖拽事件类型，用于判断来执行不同拖拽事件，"outside"：拖拽完成，"inside"：开始拖拽
             * id:模块id
             */
            function drag(word, name, type, id) {
                console.log(type);
                console.log(name);
                //在可拖动元素放置在 <div> 元素中时执行事件ondrop
                document.getElementById('d1').ondrop = function(e) {
                	console.log("#drag in svg");
                    var sTop = $(document).scrollTop(); //文档滚动条偏移量top
                    var sLeft = $(document).scrollLeft(); //文档滚动条偏移量left
                    console.log('e.target', e.target.dataset.id);
                    var x, y;
                    console.log('e.clientX', e.clientX);
                    console.log('e.clientY', e.clientY);
                    if((dWidth - e.clientX + dLeft + 65) - sLeft >= 132) {
                        x = e.clientX - 65 - dLeft + sLeft;
                    } else {
                        x = dWidth - 133;
                    }
                    if((e.clientX - dLeft) < 65) {
                        x = 1;
                    }
                    if((dHeight - e.clientY + dClient + 15) - sTop >= 33) {
                        y = e.clientY - 15 - dClient + sTop;
                    } else {
                        y = dHeight - 33;
                    }
                    if(e.clientY - 15 - dClient + sTop < 0) {
                        y = 1;
                    }
                    if(type == "outside") {
                        console.log('mouse down!');
                        var myid=dragData.length;
                        dragData.push({
                            id: myid,
                            label: word,
                            name: name,
                            x: x, //模块相对展示区域的位移x
                            y: y, //模块相对展示区域的位移y
                            outx: x + 68, //模块输出点位置x/贝塞尔曲线起点x
                            outy: y + 30, //模块输出点位置y/贝塞尔曲线起点y
                            inx: x + 65, //模块输入点位置x
                            iny: y - 1, //模块输入点位置y
                            link: [], //存放由该模块连接的关联线数据数组
                            linked: [], //存放由其他模块连接该模块的关联线数据数组
                            dx: 0,
                            dy: 0,
                            mx1: 0,
                            my1: 0,
                            mx2: 0,
                            my2: 0,
                            style: name,
                            draw: false,
                            props:new Object(),
                            icon: name + "Icon"
                        });
                        console.log(dragData);
                        newnode(name,myid);
                        reload(1);
                    }
                    if(type == "inside") {
                        console.log(word, name, type, id);
                        for(var i = 0; i < dragData.length; i++) {
                            if(id == dragData[i].id) {
                                dragData[i].x = x;
                                dragData[i].y = y;
                                dragData[i].outx = dragData[i].x + 68;
                                dragData[i].outy = dragData[i].y + 30;
                                dragData[i].inx = dragData[i].x + 65;
                                dragData[i].iny = dragData[i].y - 1;
                                console.log('dragData[i].link', dragData[i].link);
                                for(let j = 0; j < dragData[i].link.length; j++) {
                                    dragData[i].link[j].linkId = parseFloat(dragData[i].link[j].name.split("|")[1]);
                                }
                                for(var k = 0; k < dragData[i].linked.length; k++) {
                                    console.log('dragData[i].linked[k]', dragData[i].linked[k]);
                                    for(let j = 0; j < dragData.length; j++) {
                                        if(dragData[i].linked[k].linkedNum == dragData[j].id) {
                                            console.log('ID is same errors');
                                            for(let m = 0; m < dragData[j].link.length; m++) {
                                                if(dragData[i].linked[k].name == dragData[j].link[m].name) {
                                                    console.log("same name error");
                                                    dragData[j].link[m].dx = dragData[i].inx;
                                                    dragData[j].link[m].dy = dragData[i].iny-10;
                                                    dragData[j].link[m].mx1 = dragData[j].outx;
                                                    dragData[j].link[m].my1 = dragData[j].link[m].dy > dragData[j].outy ? dragData[j].outy + (dragData[j].link[m].dy - dragData[j].outy) / 3 : dragData[j].outy - (dragData[j].link[m].dy - dragData[j].outy) / 3;
                                                    dragData[j].link[m].mx2 = dragData[j].outx + (dragData[j].link[m].dx - dragData[j].outx) / 2,
                                                    dragData[j].link[m].my2 = dragData[j].outy + (dragData[j].link[m].dy - dragData[j].outy) / 2
                                                }
                                            }
                                        }
                                    }
                                }
                                if(dragData[i].link.length > 0) {
                                    for(var j = 0; j < dragData[i].link.length; j++) {
                                        dragData[i].link[j].mx1 = dragData[i].outx;
                                        dragData[i].link[j].my1 = dragData[i].link[j].dy > dragData[i].outy ? dragData[i].outy + (dragData[i].link[j].dy - dragData[i].outy) / 3 : dragData[i].outy - (dragData[i].link[j].dy - dragData[i].outy) / 3;
                                        dragData[i].link[j].mx2 = dragData[i].outx + (dragData[i].link[j].dx - dragData[i].outx) / 2,
                                        dragData[i].link[j].my2 = dragData[i].outy + (dragData[i].link[j].dy - dragData[i].outy) / 2
                                    }
                                }
                            }
                        }
                        reload(1);
                    }
                }
            }
            var shuiguo = $('.shuiguo li');
            var isondrag = 0;
            console.log(shuiguo);
            for(var i = 0; i < shuiguo.length; i++) {
                console.log(shuiguo[i]);
                shuiguo[i].ondragstart = function() {
                    console.log('finished:'+this.dataset.name);
                    drag(this.innerHTML, this.dataset.name, 'outside');
                }
            }

            function insideDrag(item) {
                console.log(item);
                if(item.getAttribute('draggable')) {
                    drag(item.dataset.label, item.className, 'inside', item.dataset.id);
                }
            }
            
            function loadMap(){
            	mapData={};
            	for(var i = 0; i < dragData.length; i++) { 
            		mapData[dragData[i].id]=dragData[i];
            	}
            }

			function removeNode(dataId){
				console.log("##delete "+dataId);
				for(var i = 0; i < dragData.length; i++) { 
                    if(dragData[i] != undefined) {
                       var data = dragData[i];
                       if (data.id==dataId){
                       	console.log("#name=",data.name);
                       	dragData.splice(i, 1);
                       	reload(1);
                       	return; 
                       }
                    }
                }
			}
			
            function noDrag(item) {
                event.preventDefault();
                event.stopPropagation();
                console.log(item.parentNode.parentNode);
                var parent = item.parentNode.parentNode;
                parent.setAttribute('draggable', false);
                for(var i = 0; i < dragData.length; i++) {
                    for(var d = 0; d < dragData[i].link.length; d++) {
                        if(!~dragData[i].link[d].name.indexOf("|")) {
                            dragData[i].link.splice(d, 1)
                        }
                    }
                    if(parent.dataset.id == dragData[i].id) {
                        dragData[i].draw = true;
                        dragData[i].link.push({
                            name: parent.dataset.id + parent.className,
                            dx: 0,
                            dy: 0,
                            mx1: 0,
                            my1: 0,
                            mx2: 0,
                            my2: 0
                        });
                        $('body').on('mouseup', function(e) {
                            for(var j = 0; j < dragData.length; j++) {
                                if(parent.dataset.id == dragData[j].id) {
                                    console.log('mouse page up');
                                    dragData[j].draw = false;
                                    var $dom = $(e.target).data("drag") ? $(e.target) : $(e.target).closest("div[data-drag]");
                                    if($dom.length) {
                                        if($dom.data("drag") && $dom[0].dataset.id != dragData[j].id) { //判断是否关联另外模块，非自己
                                            $('svg').unbind('mousemove');
                                            var name = dragData[j].link[dragData[j].link.length - 1].name + "|" + $dom[0].dataset.id + $dom[0].className;
                                            var isontbe = 0; //判断是否存在关联
                                            for(let i = 0; i < dragData.length; i++) {
                                                if($dom[0].dataset.id == dragData[i].id) {
                                                    for(let c = 0; c < dragData[i].linked.length; c++) {
                                                        if(name == dragData[i].linked[c].name) {
                                                            isontbe = 1
                                                        }
                                                    }
                                                    if(!isontbe) { //不存在时候存入linked
                                                        dragData[i].linked.push({
                                                            name: name,
                                                            linkedNum: parseFloat(name)
                                                        })
                                                    }
                                                }
                                            }
                                            if(!isontbe) { //不存在时候生成link数据
                                                dragData[j].link[dragData[j].link.length - 1].name = name;
                                                dragData[j].link[dragData[j].link.length - 1].dx = Number($dom[0].dataset.inx);
                                                dragData[j].link[dragData[j].link.length - 1].dy = Number($dom[0].dataset.iny)-10;
                                            } else {
                                                dragData[j].link.splice(dragData[j].link.length - 1, 1);
                                            }
                                        } else {
                                            dragData[j].link.splice(dragData[j].link.length - 1, 1);
                                        }
                                    } else {
                                        dragData[j].link.splice(dragData[j].link.length - 1, 1);
                                    }
                                    $('svg').unbind('mousemove');
                                    reload(1);
                                }
                            }
                            $('body').unbind('mouseup');
                        })
                        //reload();
                    }
                }
            }

            function addDrag(item) {
                var parent = item.parentNode.parentNode;
                parent.setAttribute('draggable', true);
                for(var i = 0; i < dragData.length; i++) {
                    if(parent.dataset.id == dragData[i].id) {
                        dragData[i].draw = false;
                        console.log(dragData[i]);
                    }
                }
            }

            function draw(item) {
                var parent = item.parentNode.parentNode;
                parent.setAttribute('draggable', true);
                for(var i = 0; i < dragData.length; i++) {
                    if(parent.dataset.id == dragData[i].id) {
                        if(dragData[i].draw == true) {
                            $('svg').mousemove(function(e) {
                                console.log(parent.dataset.id);
                                for(var i = 0; i < dragData.length; i++) {
                                    if(parent.dataset.id == dragData[i].id) {
                                        console.log(dragData[i]);
                                        if(dragData[i].link[dragData[i].link.length - 1]) {
                                            dragData[i].link[dragData[i].link.length - 1].dx = e.offsetX;
                                            dragData[i].link[dragData[i].link.length - 1].dy = e.offsetY-10;
                                            dragData[i].link[dragData[i].link.length - 1].mx1 = dragData[i].outx;
                                            dragData[i].link[dragData[i].link.length - 1].my1 = dragData[i].dy > dragData[i].outy ? dragData[i].outy + (dragData[i].dy - dragData[i].outy) / 3 : dragData[i].outy - (dragData[i].dy - dragData[i].outy) / 3;
                                            dragData[i].link[dragData[i].link.length - 1].mx2 = dragData[i].outx + (dragData[i].dx - dragData[i].outx) / 2,
                                            dragData[i].link[dragData[i].link.length - 1].my2 = dragData[i].outy + (dragData[i].dy - dragData[i].outy) / 2
                                        }
                                        //////////////////////////////////////////////
                                        dragData[i].dx = e.offsetX;
                                        dragData[i].dy = e.offsetY-10;
                                        dragData[i].mx1 = dragData[i].outx;
                                        if(dragData[i].dy > dragData[i].outy) {
                                            dragData[i].my1 = dragData[i].outy + (dragData[i].dy - dragData[i].outy) / 3;
                                        } else {
                                            dragData[i].my1 = dragData[i].outy - (dragData[i].dy - dragData[i].outy) / 3;
                                        }
                                        dragData[i].mx2 = dragData[i].outx + (dragData[i].dx - dragData[i].outx) / 2
                                        dragData[i].my2 = dragData[i].outy + (dragData[i].dy - dragData[i].outy) / 2
                                    }
                                }
                                reload();
                            })
                        } else {
                            $('svg').unbind('mousemove');
                        }

                    }
                }
            }
            
            function postsvg(svgname,dragData){
            	loadMap();
            	var template=$("#template").val();
				$.each(mapData,function(name,value) {
					console.log("#name:"+name+",value:"+value);
     				saveSourceFileDialog(name);
				});
				var target= new Object();
				target.chart=dragData;
				console.log("#target:",target);
            	$.ajax({
        			url:"/"+template+"/project/postSvg",
        			type:"post",
        			dataType:"json",
        			data:{svgName:svgname,svg:JSON.stringify(target)},
        			success:function(res){
                    	console.log(res);    
                    	alert(res.msg);              
            		}
    			});
            }
            
            
            // using json to construct properties values and decode properties and show in canvas 
			function saveSourceFileDialog(nodeid){
				loadMap();
				var mydata=new Object();
				$("#spark-canvas #"+nodeid+"  .spark-data").each(function(){
  				  	mydata[$(this).attr('name')]=$(this).val();
  				  	dragData[nodeid].props[$(this).attr('name')]=$(this).val();
  				  	mapData[nodeid].props[$(this).attr('name')]=$(this).val();
  				});
			}
			
			function loadNodeProperties(nodeid,mapData){
				loadMap();
				var mydata=mapData[nodeid];
				$("#spark-canvas #"+nodeid+"  .spark-data").each(function(){
					console.log("##=",mydata.props[$(this).attr('name')]);
					$(this).val(mydata.props[$(this).attr('name')]);
  				});
			}
			
            
            function getsvg(svgname){
            	var template=$("#template").val();
            	$.ajax({
        			url:"/"+template+"/project/getSvg",
        			type:"get",
        			dataType:"json",
        			data:{svgName:svgname},
        			success:function(res){
        				if (res==""){
        					console.log("svg content is null");
        					return;
        				}	
        				console.log(res.data);
        				var svg=eval('(' + res.data + ')');    
        				if (svg.chart == null || svg.chart == undefined || svg.chart == '') {
        					console.log("#svg is null");
        					return;
        				}    				
                    	dragData=svg.chart;
                    	reload(1);
                    	$("#spark-canvas").empty();
                    	 //load node property dialog 
                    	for(var i = 0; i < dragData.length; i++) {
                    		newnode(dragData[i].label,dragData[i].id);
                    		loadNodeProperties(dragData[i].id,dragData);
                   	 	}
            		}
    			});
            }
            
            function shownode(id){
            	$("#"+id).modal({
            		show: true,
            		backdrop:'static'
       		 	})
            	//$("#"+id).css({"display":"block","top":"10px","left":"100px","width":"300px","position":"absolute"});
             }
			
			function newnode(label,id){
            	var newid=id;//Math.uuid();
            	var name=label;
				var cav = $("#spark-model").find("."+name).last().clone();
				cav.attr("id",newid);
				var html=cav.prop("outerHTML");
				$("#spark-canvas").append(html);
				$("#spark-canvas").find("."+name).last().attr("id",""+newid);
				$("#"+newid).css({"display":"none"});
			}
			
			function testModal(){
				$('#myModal').modal({
            		show: true,
            		backdrop:'static'
       		 	})
			}
			
            function noMove() {
                $('svg').unbind('mousemove');
            }
            $('svg').mouseup(function(e) {
                console.log(e.target);
                $('svg').unbind('mousemove');
                for(var i = 0; i < dragData.length; i++) {
                    dragData[i].draw = false;
                }
                console.log('mouse up!');
            })
            
            $(document).ready(function(){
 				console.log("#init func");
 				var id = $("#svgname").val();
				getsvg(id);				
 
			});

        </script>
        </#noparse>
        	
	<div id="spark-canvas"></div>
	
	<div id="spark-model" style="display:none;">
<#list modals?keys as key>
	 <!-- 模态框（Modal） -->
	<div class="modal fade ${key}" id="myModal"  role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   		<div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
               <h4 class="modal-title" id="myModalLabel">模态框（Modal）标题</h4>
           </div>
        	<div class="modal-body">
				<!-- user defined form  -->				
           			${modals[key]?default("")}        		
     		</div>  		
           <div class="modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
           </div>
       	</div>
   		</div><!-- .modal -->
 	</div><!-- .modal finish -->
 </#list>
    	
	</div>
	
	
   
