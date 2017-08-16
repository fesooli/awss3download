package br.com.automatic.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWords {

	private Map<String, List<String>> keysWords;
	
	public KeyWords() {
		keysWords = new HashMap<String, List<String>>();
		List<String> dispositivos = new ArrayList<String>();
		dispositivos.add("tv");
		dispositivos.add("luz quarto fellipe");
		dispositivos.add("luz quarto celia");
		keysWords.put("ligar", dispositivos);
		keysWords.put("desligar", dispositivos);
	}

	public Map<String, List<String>> getKeysWords() {
		return keysWords;
	}

	public void setKeysWords(Map<String, List<String>> keysWords) {
		this.keysWords = keysWords;
	}
}
