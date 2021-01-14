import java.util.ArrayList;


public class SetCharacter {
    private ArrayList<GameCharacter> characters;
    
    public SetCharacter(){
        characters = new ArrayList<GameCharacter>();
    }

    public boolean addCharacter(GameCharacter character){
        return characters.add(character); //returns true if character is added
    }

    public ArrayList<GameCharacter>getCharacters(){
        return characters;
    }

    public GameCharacter contains(String name){
        for (int i=0; i<characters.size(); i++){
            if (name.equals(characters.get(i).getName()))
                return characters.get(i);
        }

        return null;
    }

    //displays inventory
    public String toString(){

        if (characters.size() == 0)
            return "No one is in this room.";

        String msg = "";
        for (GameCharacter i : characters){
            msg+= i.getName() + "\n";
        }

        return msg;

    }
}
