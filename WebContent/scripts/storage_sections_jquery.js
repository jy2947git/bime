
function getSections(storageId){
	
	  jQuery.ajax({
		type:"GET",
		url:"sections/list.json",
		data:"",
		contentType:"application/json; charset=urtf-8",
		dataType:"json",
		success:function(results){
		  //alert(results);
		  loadSections(results);
		  },
		error:function(textStatus, errorThrown){ 
			  alert("Sorry failed to load the sections " + textStatus + " " + errorThrown);
			  }
		
		
	});
}

function deleteSection(sectionId){
	  jQuery.ajax({
		type:"POST",
		url:"section/"+sectionId+"/delete.json",
		data:"{}",
		contentType:"application/json; charset=urtf-8",
		dataType:"json",
		success:function(data){loadSections(data);},
		failure:function(){alert("Sorry failed to load the sections");}
		
		
	});
}

function saveSection(sectionId, sectionName, sectionType){
//	if(!confirm("section/"+sectionId+"/form.json?sectionName="+sectionName+"&sectionType="+sectionType)){
//		return;
//	}
	  jQuery.ajax({
		type:"POST",
		url:"section/"+sectionId+"/form.json?sectionName="+sectionName+"&sectionType="+sectionType,
		data:"{}",
		contentType:"application/json; charset=urtf-8",
		dataType:"json",
		success:function(data){loadSections(data);},
		failure:function(){alert("Sorry failed to load the sections");}
		
		
	});
}

function loadSections(data){
	//alert(data);
	var tbody =   jQuery("#sectionbody").html("");
	for(var i =0; i<data.length;i++){
		var section = data[i];
		//alert(section.name);
		var rowText = "<tr id=\"section" + section.id+"\"><td>"+section.name+"</td><td>"+section.type+"</td><td><input id=\"edit\" type=\"image\" src=\""+imagePath+"pencil.png\" onclick=\"editSection("+section.id+")\"/> &nbsp;&nbsp;<input type=\"image\" src=\""+imagePath+"cross.png\" onclick=\"deleteSection("+ section.id+")\"/></td></tr>";
		//alert(rowText);
		//jQuery(rowText).appendTo(tbody);
		jQuery('#sectionbody').append(rowText);
	}
}

function addStorageSection(){
	saveSection(jQuery("#editSectionId").val(),jQuery("#editSectionName").val(),jQuery("#editSectionType").val());
	clearStorageSection();
}

function saveStorageSection(){
	saveSection(jQuery("#editSectionId").val(),jQuery("#editSectionName").val(),jQuery("#editSectionType").val());
	clearStorageSection();
}

function clearStorageSection(){
	//clear the id, name and type
	jQuery("#editSectionId").val("0");
	jQuery("#editSectionName").val("");
	jQuery("#editSectionType").val("");
	jQuery("#saveChangeButton").attr("disabled", "true");
	jQuery("#addNewButton").removeAttr("disabled");
}

function editSection(editSectionId){
		
	//populate with value from row
	jQuery("#editSectionId").val(editSectionId);
	jQuery("#editSectionName").val(jQuery("#section"+editSectionId).find("td").eq(0).html());
	jQuery("#editSectionType").val(jQuery("#section"+editSectionId).find("td").eq(1).html());
	//disable the add-new button
	jQuery("#addNewButton").attr("disabled", "true");
	jQuery("#saveChangeButton").removeAttr("disabled");
	jQuery("#clearButton").removeAttr("disabled");
}