/**
 * Javascript methods for treeapp
 */

function removeChildrenNodes(children){
	for(var i=0; i<children.length; i++) {
		$('#jsTree_Id').jstree("delete_node", children[i]);
	}
}
	
function createChildrenNodes(parentNode, childData) {
	console.log("createChildrenNodes");
	$('#jsTree_Id').jstree("create_node", parentNode, childData);
	
} 

function createFactory(rootNode,createFactoryUrl) {
	$.ajax({
			 type: "POST",
			 url: createFactoryUrl,
			 dataType : "json",
			 data: {'parentId' : rootNode.id},
			 success: function(newData) {
				createChildrenNodes(rootNode, newData[0]);
			 }
		 })
}

function createChildren(factoryNode, createChildrenUrl, num) {
	var tree = $("#jsTree_Id").jstree(true);
	$.ajax({
			 type: "POST",
			 url: createChildrenUrl,
			 dataType : "json",
			 data: {
				 	'parentId' : factoryNode.id,
				 	'parentText' : factoryNode.text,
				 	'numOfChildren' : num
				 },
			 success: function(newData) {
				 var len = newData.length;
				 var children = tree.get_children_dom(factoryNode);
				 removeChildrenNodes(children);
				 for(var i=0; i<len; i++) {
					// console.dir("newData = " + newData[i].text);
					createChildrenNodes(factoryNode, newData[i]);
				 }
				
			 },
			error: function() {
				var html = '<p><font style="color: red;">Error creating child nodes</font></p>';
				$('#error_id').html(html).show();
				console.log("errrror");
			}
		 })
}

function deleteFactory(factoryNode, deleteNodeUrl){
	$.ajax({
		 type: "POST",
		 url: deleteNodeUrl,
		 dataType : "json",
		 data: {
			 'nodeId' : factoryNode.id,
			 'nodeText' : factoryNode.text
			},
		 success: function(newData) {
			 console.dir("deleted successfully");
			$('#jsTree_Id').jstree("delete_node", factoryNode);
		 }
		 })
}

function contextMenuLabel(treeNode){
   	if(treeNode.parent == '#') {
   		return "Create Factory";
   	} else {
   		return "Generate";
   	}
}

function userInputForChildrenNum() {
	var num = prompt("Enter the number of children (1-15) : ");
		if(!num) {
			return;
		} else if(num < 1 || num > 15) {
			return alert("Please enter a value between 1-15");
		} 
	return num;
}
	


    
    
