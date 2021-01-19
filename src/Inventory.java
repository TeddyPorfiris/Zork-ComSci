import java.util.ArrayList;

public class Inventory {
    //arrayList of item or hashmap of item
    //has max weight
    private ArrayList<Item> items;

    //constructor (creates an arrayList of Item objects. the arrayList holds all the items that are in a specific room)
    public Inventory(){
        items = new ArrayList<Item>();
    }

    //add item to room (to arrayList of items)
    public boolean addItem(Item item){
        return items.add(item); //returns true if item is added
    }

    //removes item from room (from arrayList of items) and returns the Item object that was removed
    public Item removeItem(String name){
        //checking if item is in their inventory and if it is then it removes the item (the player cannot remove something they don't have in their inventory!)
        for (int i=0; i<items.size(); i++){
            if (name.equals(items.get(i).getName()))
                return items.remove(i);
        }

        //if we get through the for loop, then the item must not be in their inventory
        return null;
    }

    //return inventory of items (returns arrayList of Item objects)
    public ArrayList<Item> getInventory(){
        return items;
    }

    //checks if the item is in your inventory. if it is then it returns the item. if not then it returns null.
    public Item contains(String name){
        for (int i=0; i<items.size(); i++){
            if (name.equals(items.get(i).getName()))
                return items.get(i);
        }

        return null;
    }
    
    //return true if item is in inventory. return false otherwise.
    public Boolean inInventory(String name){
        for (int i=0; i<items.size(); i++){
            if (name.equals(items.get(i).getName()))
                return true;
        }

        return false;
    }

    //returns total weight of all of player's inventory
    public int getTotalWeight(){
        int totalWeight = 0;
        for (int i=0; i<items.size(); i++){ //goes through all items in player inventory, gets the weight for each item, and adds it to totalWeight variable
            totalWeight += items.get(i).getWeight();
        }

        return totalWeight;
    }

    //displays inventory
    public String toString(){

        if (items.size() == 0)
            return "No items.\n";

        String msg = "";
        for (Item i : items){
            msg+= i.getName() + "\n";
        }
        
        return msg;

    }

}

