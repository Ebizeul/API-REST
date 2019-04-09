package com.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blo.VilleFranceBLO;
import com.dao.DAOFactory;
import com.dao.VilleFranceDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
//@RequestMapping("/path")
public class TestController {
	
	private DAOFactory daoFactory = new DAOFactory();
	private VilleFranceDAO villeFranceDAO = daoFactory.getVilleFranceDao();
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseBody
	public String get(@RequestParam(required = false, value = "value") String value
			, @RequestParam(required = false, value = "filtre") String filtre) {
		
		List<VilleFranceBLO> villes = this.villeFranceDAO.lister();
		ObjectMapper objectMapper = new ObjectMapper();
		String affichageVille = null;
		
		if(value.equals("villesFrance")) {
			try {
				affichageVille = objectMapper.writeValueAsString(villes);
			} catch(JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if(filtre != null) {
				List<String> nomsCommunes = new ArrayList<String>();
				List<String> codesCommunes = new ArrayList<String>();
				
				if(filtre.equals("nom")) {
					for(int i=0 ; i<villes.size() ; i++) {
						nomsCommunes.add("Nom commune : " + villes.get(i).getNomCommune());
					}
				} else if(filtre.equals("codeCo")) {
					for(int i=0 ; i<villes.size() ; i++) { 
						codesCommunes.add("Code commune : " + villes.get(i).getCodeCommune());
					}
				} 
				
				if(nomsCommunes.size() > 0) {
					try {
						affichageVille = objectMapper.writeValueAsString(nomsCommunes);
					} catch(JsonProcessingException e) {
						e.printStackTrace();
					}
				} else if(codesCommunes.size() > 0) {
					try {
						affichageVille = objectMapper.writeValueAsString(codesCommunes);
					} catch(JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		return affichageVille;
	}
	
	@RequestMapping(value="/test", method=RequestMethod.POST)
	@ResponseBody
	public void post(@RequestParam(required = false, value = "codeCo") String codeCommune
			, @RequestParam(required = false, value = "nom") String nom) {
		VilleFranceBLO villeFranceBLO = new VilleFranceBLO();
		villeFranceBLO.setCodeCommune(codeCommune);
		villeFranceBLO.setNomCommune(nom);
		villeFranceBLO.setCodePostal(codeCommune);
		villeFranceBLO.setLibelleAcheminement(nom);
		villeFranceBLO.setLigne5("");
		villeFranceBLO.setLatitude("11");
		villeFranceBLO.setLongitude("66");
		this.villeFranceDAO.creer(villeFranceBLO);
	}
	
	@RequestMapping(value="/test", method=RequestMethod.PUT)
	@ResponseBody
	public void put(@RequestParam(required = false, value = "ucodeCo") String codeCo
			, @RequestParam(required = false, value = "nouveauNom") String nouveauNom) {
		VilleFranceBLO villeFranceBLO = new VilleFranceBLO();
		villeFranceBLO.setCodeCommune(codeCo);
		VilleFranceBLO villeUpdate = this.villeFranceDAO.trouver(villeFranceBLO).get(0);
		villeUpdate.setNomCommune(nouveauNom);
		this.villeFranceDAO.modifier(villeUpdate);		
	}
	
	@RequestMapping(value="/test", method=RequestMethod.DELETE)
	@ResponseBody
	public void delete(@RequestParam(required = false, value = "dcodeCo") String codeCo) {
		VilleFranceBLO villeFranceBLO = new VilleFranceBLO();
		villeFranceBLO.setCodeCommune(codeCo);
		VilleFranceBLO villeDelete = this.villeFranceDAO.trouver(villeFranceBLO).get(0);
		this.villeFranceDAO.supprimer(villeDelete);
	}

}
