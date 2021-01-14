import java.util.ArrayList;

public class SetAnimal {
    
    private ArrayList<Animal> animals;

    public SetAnimal(){
        animals = new ArrayList<Animal>();
    }

    public Animal eat(String name){
        //checking if animal is in room and if it is then it removes animal (the player cannot remove something that isn't in the room!)
        for (int i=0; i<animals.size(); i++){
            if (name.equals(animals.get(i).getName()))
                return animals.remove(i);
        }

        //if we get through the for loop, then the item must not be in their inventory
        return null;
    }

    public ArrayList<Animal> getAnimals(){
        return animals;
    }

    public boolean addAnimal(Animal animal){
        return animals.add(animal); //returns true if animal is added
    } 

    public Animal removeAnimal(Animal animal){
        //checking if item is in room and if it is then it removes the animal (the player cannot remove something that isn't in the room they're currently in!)
        for (int i=0; i<animals.size(); i++){
            if (animal == animals.get(i))
                return animals.remove(i);
        }

        //if we get through the for loop, then the animal must not be in the room
        return null;
    }

    //this method checks if the animal is in the current room. if it is then it returns the animal. if not then it returns null.
    public Animal contains(String name){
        for (int i=0; i<animals.size(); i++){
            if (name.equals(animals.get(i).getName()))
                return animals.get(i);
        }

        return null;
    }

    //displays inventory
    public String toString(){

        if (animals.size() == 0)
            return "No animals are in this room.\n";

        String msg = "";
        for (Animal i : animals){
            if (!i.isDead())
                msg+= i.getName() + "\n";
            else //let users know that an animal is dead if it has been killed
                msg+= "dead " + i.getName() + "\n";
        }

        return msg;

    }
}
