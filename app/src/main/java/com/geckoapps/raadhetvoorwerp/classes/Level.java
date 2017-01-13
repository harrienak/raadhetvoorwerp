package com.geckoapps.raadhetvoorwerp.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Level {

	private int nummer;
	private String answer;
	private String plaatje;
	
	private ArrayList<String> letters;
	private final int max_letters = 18;
	
	private String[] alfabet = {"a", "b", "c" , "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	private ArrayList<String> woorden;
	
	public String[] woord_letters;
	public String[] correct_letters;
	public int[] woord_positie;
	public int[] origin_positie;
	private int numberOfLetters;
	
	private ArrayList<String> removeHelp;
	
	
	public Level(int nummer, String answer, String plaatje) {
		super();
		this.nummer = nummer;
		this.answer = answer;
		this.plaatje = plaatje;

		
		letters = new ArrayList<String>();

		woorden = new ArrayList<String>();
		String[] w  =  answer.split(" ");
		for (int i = 0; i < w.length; i++) {
			woorden.add( w[i]);
		}
		
		for (int i = 0; i < answer.length(); i++) {
			if( !answer.substring(i, i+1).equals(" ")){
				letters.add( answer.substring(i, i +1) );
			}
		}
		numberOfLetters = letters.size();
		resetData();
		
		removeHelp = new ArrayList<String>();
		for (int i = letters.size(); i < max_letters; i++) {
			Random r = new Random();
			String l= alfabet[ r.nextInt(alfabet.length)];
			letters.add( l );
			removeHelp.add(l);
		}
		
		Collections.shuffle(this.letters);
		Collections.shuffle(this.removeHelp);
	}
	
	public void resetData(){
		woord_letters = new String[numberOfLetters];
		correct_letters = new String[numberOfLetters];
		woord_positie = new int[numberOfLetters];
		origin_positie = new int[20];
		for (int i = 0; i < woord_letters.length; i++) {
			correct_letters[i] = letters.get(i);
			woord_letters[i] = "";
			woord_positie[i] = -1;
		}
	}

	public String[] getWoord_letters() {
		return woord_letters;
	}
	public void setWoord_letters(String[] woord_letters) {
		this.woord_letters = woord_letters;
	}
	public int[] getWoord_positie() {
		return woord_positie;
	}
	public void setWoord_positie(int[] woord_positie) {
		this.woord_positie = woord_positie;
	}



	public ArrayList<String> getRemoveHelp() {
		return removeHelp;
	}

	public int getAantal(){
		return 1;
	}
	

	public ArrayList<String> getWoorden() {
		return woorden;
	}

	public void setWoorden(ArrayList<String> woorden) {
		this.woorden = woorden;
	}

	public ArrayList<String> getLetters() {
		return letters;
	}



	public void setLetters(ArrayList<String> letters) {
		this.letters = letters;
	}



	public int getNummer() {
		return nummer;
	}

	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getPlaatje() {
		return plaatje;
	}


	public boolean lettersAreClickable() {
		boolean canClick = false;
		for (int i = 0; i < woord_letters.length; i++) {
			if(woord_letters[i].length() == 0){
				canClick = true;
			}
		}
		
		return canClick;
	}
	
	
	
	

}
