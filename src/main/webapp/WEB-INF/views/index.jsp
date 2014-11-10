<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tree Application</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"> </script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="<c:url value="/resources/dist/jstree.js" />"></script>
<script src="<c:url value="/resources/static/treeapp.js" />"></script>
<link href="<c:url value="/resources/static/treeapp.css" />" rel="stylesheet">
<link href="<c:url value="/resources/dist/themes/default/style.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" />

<script>

var contextPath = '${pageContext.request.contextPath}';
$(function() {
	
	var json = ${nodes};
	loadJSTree(json);
	
	$("#refresh_id").button()
    .click(function(event) {
      location.reload();
    });
});



(function poll() {
setTimeout(function() {
    $.ajax({
        url: contextPath+'/poll',
        type: "GET",
        success: function(data) {
        	var html = new Array(); 
        	for(var i = 0; i<data.length; i++) {
        		html.push("<p class=\"italic\">Node '" + data[i].nodeName + "' :: " + data[i].eventType + "</p>");
        	}
        	$('#broadcast_id').html(html).show();
        },
        dataType: "json",
        complete: poll
        //timeout: 20000
    });
}, 5000);
})();

function loadJSTree(dataJson) {
	$('#jsTree_Id')
	 .jstree({
	    'core' : {
	    	'check_callback' : true,
	    	'data' : dataJson
		 },
		 
	    'plugins' : ["themes", "ui", "contextmenu","types","dnd"],
	    'contextmenu' : {
           items: function($node) {
           	var tree = $("#jsTree_Id").jstree(true);

           	if($node.parent != '#' && $node.parent != 'root-id') {
           		console.dir("Do not display context menu on child");
           		return;
           	}
               return {
                   create : {
                       "label" : contextMenuLabel($node),
                       "action" : function(obj) {
                       	if($node.parent == '#') {
                       		var url = contextPath+'/createFactory';
                       		createFactory($node, url);
	                       
                       	} else if($node.parent == 'root-id') {
                       		var num = userInputForChildrenNum();
                       		if(num){
                       			var url = contextPath+'/createChildren';
                           		createChildren($node, url, num);
                       		}
                       		
                       	}
                       }
                   },
                   remove: {
                   	"label" : "Remove",
                   	"action" : function() {
                   		if($node.id != 'root-id') {
                   			var deleteUrl = contextPath+'/deleteNode';
                   			deleteFactory($node, deleteUrl);
                   		}
                   	}
                   	
                   }
               };
           }
       }
       
	 });
}
</script>
</head>
<body>

<div id="header" style="background-color:#99B2CC" class="header row">
	<h1 style="margin-bottom:0;" align="center">Tree Factory</h1>
</div>


<div class="body row scroll-y">
<div id="error_id">
</div>
	
<div id="broadcast_id" style="background-color:#F0F2F4;height:500px;width:500px;float:right;overflow: auto"></div>

<div id="content" style="background-color:#EEEEEE;height:500px;width:750px;float:left;">

	<div id="jsTree_Id" style="float: left;"></div>
	<div id="refresh_div_id" style="float: right;">
		<!-- <label id="update_count" style="color: red;"></label> -->
		<input type="submit" id="refresh_id" value="Refresh" style="background:#909192;"/>
	</div>
</div>
</div>
<div id="footer" style="background-color:#99B2CC" class="footer row">
</div>
</body>
</html>