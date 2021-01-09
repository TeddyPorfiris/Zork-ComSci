import java.util.HashMap;
import java.util.Scanner;

public class GameCharacter {
    private String name;
    private String description;
    private String initialText;
    private HashMap<String, String> dialogue;

	public GameCharacter() {
		
	}


	public void setName(String name) {
		this.name = name;
    }
    
	public void setDescription(String description) {
		this.description = description;
    }

    public void setInitialText(String initialText){
        this.initialText = initialText;
    }

    public void setDialogue(){
        dialogue = new HashMap<String, String>();
    }
    

    public GameCharacter(String name, String description, String initialText, HashMap dialogue){
        super();
        this.name = name;
        this.description = description;
        this.initialText = initialText;
        this.dialogue = new HashMap<String, String>();
    }

    public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
    }

    public String getInitialText(){
        return initialText;
    }

    public HashMap getDialogue() {
		return dialogue;
    }

    public void addDialogue(String answer, String response) {
        dialogue.put(answer, response);
    }
    
	
}
