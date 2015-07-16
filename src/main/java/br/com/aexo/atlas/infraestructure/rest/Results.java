package br.com.aexo.atlas.infraestructure.rest;

public class Results {

	public static Class<RepresentationResultView> representation(){
		return RepresentationResultView.class;
	}

	public static Class<StatusResultView> status() {
		return StatusResultView.class;
	}
	
}
