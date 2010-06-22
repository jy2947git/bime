package com.focaplo.myfuse.webapp.controller.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.webapp.controller.BimeFormController;

@Controller
public class StorageSectionController extends BimeFormController{

	public class Section{
		private String name;
		private  String type;
		private  Long id;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Section(Long id, String name, String type) {
			super();
			this.name = name;
			this.type = type;
			this.id = id;
		}
		
	}
	@RequestMapping(value="/inventory/*/{storageId}/sections/list.*", method=RequestMethod.GET)
	@ResponseBody
	public String getListOfSectionsOfStorage(@PathVariable("storageId") Long storageId, Model model) throws JsonGenerationException, JsonMappingException, IOException{

		return this.listWithJson(storageId);
	}
	
	private String listWithJson(Long storageId) throws JsonGenerationException, JsonMappingException, IOException{
		List<Section> sections = this.list(storageId);
		//JSON the list
		ObjectMapper mapper = new ObjectMapper();
		String json =  mapper.writeValueAsString(sections);
		//System.out.println(json);
		return json;
	}
	private List<Section> list(Long storageId){
		List<StorageSection> sss  = this.inventoryManager.getSectionsOfStorage(storageId);
		List<Section> ssss = new ArrayList();
		for(StorageSection ss:sss){
			ss.setStorage(null);
			Section s = new Section(ss.getId(),ss.getName(),ss.getType());
			ssss.add(s);
		}
		return ssss;
	}
	@RequestMapping(value="/inventory/*/{storageId}/section/{sectionId}/delete.*", method=RequestMethod.POST)
	@ResponseBody
	public String deleteSectionsFromStorage(@PathVariable("storageId") Long storageId, @PathVariable("sectionId") Long sectionId, Model model) throws JsonGenerationException, JsonMappingException, IOException{
		this.inventoryManager.deleteStorageSection(sectionId);
		return this.listWithJson(storageId);
	
	}
	@RequestMapping(value="/inventory/*/{storageId}/section/{sectionId}/form.*", method=RequestMethod.POST)
	@ResponseBody
	public String saveSectionToStorage(@PathVariable("storageId") Long storageId, @PathVariable("sectionId") Long sectionId, @RequestParam("sectionName") String sectionName, @RequestParam("sectionType") String sectionType, Model model, Locale locale) throws JsonGenerationException, JsonMappingException, IOException{
		StorageSection section = null;
		if(sectionId!=null && sectionId>0){
			
			this.inventoryManager.updateStorageSection(sectionId, sectionName, sectionType);
		}else{
			Storage s = (Storage) this.inventoryManager.get(Storage.class, storageId);
			section = new StorageSection();
			section.setAlias(s.getAlias() + " " + this.getText("section", locale) + " " + sectionName);
			section.setCreatedDate(new Date());
			section.setName(sectionName);
			section.setType(sectionType);
			section.setUpdatedByUser(this.getLoginUser().getUsername());
			
			this.inventoryManager.saveNewStorageSection(storageId, section);
		}
		
		return this.listWithJson(storageId);
		
	}
}
