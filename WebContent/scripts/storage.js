function init() {
  fillTable();
}

var sectionCache = { };
var viewed = -1;

function fillTable() {
	  InventoryManager.getSectionsOfStorage(storageId,function(sections) {
	    // Delete all the rows except for the "pattern" row
	    dwr.util.removeAllRows("sectionbody", { filter:function(tr) {
	      return (tr.id != "pattern");
	    }});
	    // Create a new set cloned from the pattern row
	    var section, id;
	   
	    for (var i = 0; i < sections.length; i++) {
	      section = sections[i];
	      id = section.id;
	      dwr.util.cloneNode("pattern", { idSuffix:id });
	      dwr.util.setValue("tableName" + id, section.name);
	      dwr.util.setValue("tableType" + id, section.type);
	     
	      $("pattern" + id).style.display = '';//compatible to both firefox and IE "table-row";
	     
	      sectionCache[id] = section;
	    }
	  });
	}



function fillSectionsData(sections) {

  }

function editClicked(eleid) {
  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
  var section = sectionCache[eleid.substring(4)];
  viewed=section.id;
  //alert('section ' + section.id + ' ' + section.name + ' ' + section.type);
  dwr.util.setValues({editSectionId:section.id,editSectionName:section.name, editSectionType:section.type});
}

function deleteClicked(eleid) {
  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
  var section = sectionCache[eleid.substring(6)];
  if (confirm("Are you sure you want to delete " + section.id + "?")) {
    dwr.engine.beginBatch();
    InventoryManager.deleteStorageSection(section.id);
    fillTable();
    dwr.engine.endBatch();
  }
}

function saveStorageSection() {

  dwr.engine.beginBatch();
  InventoryManager.updateStorageSection(dwr.util.getValue('editSectionId'),dwr.util.getValue('editSectionName'),dwr.util.getValue('editSectionType'));
  fillTable();
  
  dwr.engine.endBatch();
  clearStorageSection();
}

function clearStorageSection() {
  viewed = -1;
  dwr.util.setValues({editSectionId:0,editSectionName:null, editSectionType:null});
}

function addStorageSection(){
	dwr.engine.beginBatch();
	InventoryManager.saveStorageSection(storageId,{id:null,name:dwr.util.getValue('editSectionName'),type:dwr.util.getValue('editSectionType')});
	fillTable();
	
	dwr.engine.endBatch();
	clearStorageSection();
}
