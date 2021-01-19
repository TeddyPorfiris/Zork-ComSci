import java.util.ArrayList;


public class SetCharacter {
    private ArrayList<GameCharacter> characters;
    
    //constructor (creates an arrayList of GameCharacter objects. the arrayList holds all the characters that are in a specific room)
    public SetCharacter(){
        characters = new ArrayList<GameCharacter>();
    }

    //add character to room (to arrayList of characters)
    public boolean addCharacter(GameCharacter character){
        return characters.add(character); //returns true if character is added
    }

    //return arrayList of characters in room
    public ArrayList<GameCharacter>getCharacters(){
        return characters;
    }

    //check if character is in room (AKA in arrayList) by searching via the character's name. return the character if they are in the room. if not return null.
    public GameCharacter contains(String name){
        for (int i=0; i<characters.size(); i++){
            if (name.equals(characters.get(i).getName()))
                return characters.get(i);
        }

        return null;
    }

    //cleanly displays whoever is in the room.
    public String toString(){

        if (characters.size() == 0)
            return "No one is in this room.\n";

        String msg = "";
        for (GameCharacter i : characters){
            msg+= i.getName() + "\n";
        }

        return msg;

    }
}
