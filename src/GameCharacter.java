import java.util.HashMap;
import java.util.Scanner;

public class GameCharacter {
    private String name;
    private String description;
    private String initialText;
    private HashMap<String, String> dialogue;

	public GameCharacter() {
		
	}


    //set name of character
	public void setName(String name) {
		this.name = name;
    }
    
    //set description of character
	public void setDescription(String description) {
		this.description = description;
    }

    //set initial dialogue of character
    public void setInitialText(String initialText){
        this.initialText = initialText;
    }

    //set dialogue hashmap of character (every key is an answer, and every item is the response to the respective answer)
    public void setDialogue(){
        dialogue = new HashMap<String, String>();
    }
    

    //charactrer constructor
    public GameCharacter(String name, String description, String initialText, HashMap dialogue){
        super();
        this.name = name;
        this.description = description;
        this.initialText = initialText;
        this.dialogue = new HashMap<String, String>();
    }

    //get name of character
    public String getName() {
		return name;
	}

    //get description of character
	public String getDescription() {
		return description;
    }

    //get initial dialogue of character
    public String getInitialText(){
        return initialText;
    }

    //get dialogue hashmap of character
    public HashMap getDialogue() {
		return dialogue;
    }

    //add dialogue to dialogue hashmap (add answer key and response item)
    public void addDialogue(String answer, String response) {
        dialogue.put(answer, response);
    }
    
	
}
