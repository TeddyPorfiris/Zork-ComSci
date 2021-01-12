//THIS IS WHERE YOU UNDERSTAND WHAT THE COMMANDS MEAN. YOU CAN ALSO ACCESS THINGS LIKE INVENTORY, ITEMS IN ROOMS, ETC. THIS IS WHERE ALL THE GAME STUFF HAPPENS. YOU WILL WRITE MOST OF YOUR CODE HERE.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Michael Kolling Version: 1.1 Date: March 2000
 * 
 * This class is the main class of the "Zork" application. Zork is a very
 * simple, text based adventure game. Users can walk around some scenery. That's
 * all. It should really be extended to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * routine.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates the commands that
 * the parser returns.
 */
public class Game {
  private Parser parser;
  private Room currentRoom; //each room has an inventory
  private Inventory inventory; //player's inventory 
  private Scanner in = new Scanner(System.in);

  // This is a MASTER object that contains all of the rooms and is easily
  // accessible.
  // The key will be the name of the room -> no spaces (Use all caps and
  // underscore -> Great Room would have a key of GREAT_ROOM
  // In a hashmap keys are case sensitive.
  // masterRoomMap.get("GREAT_ROOM") will return the Room Object that is the Great
  // Room (assuming you have one).
  private HashMap<String, Room> masterRoomMap;
  private HashMap<String, Item> masterItemMap;
  private HashMap<String, GameCharacter> masterCharacterMap;

	private void initItems(String fileName) throws Exception{
		Scanner itemScanner;
    masterItemMap = new HashMap<String, Item>();
    
		try {
			
			itemScanner = new Scanner(new File(fileName));
			while (itemScanner.hasNext()) {
				Item item = new Item(); //creates new item object
				String itemName = itemScanner.nextLine().split(":")[1].trim(); //gets item name by parsing items dat file 
				item.setName(itemName); //sets the name of this new item object that we created to what we defined above
      
        String itemDesc = itemScanner.nextLine().split(":")[1].trim(); //gets item description by parsing items dat file 
        item.setDescription(itemDesc); //sets the description of this new item object that we created to what we defined above
        	
				Boolean openable = Boolean.valueOf(itemScanner.nextLine().split(":")[1].trim()); //gets boolean value if item is openable or not in items dat file 
        item.setOpenable(openable); //sets if the new item object we created above is openable

        int weight = Integer.parseInt(itemScanner.nextLine().split(":")[1].trim()); //gets item description by parsing items dat file 
        item.setWeight(weight); //sets the weight of this new item object that we created to what we defined above

        masterItemMap.put(itemName.toUpperCase().replaceAll(" ", "_"), item); //hashmap of all items
        //System.out.println(masterItemMap);
				
				String temp = itemScanner.nextLine(); 
				String itemType = temp.split(":")[0].trim();
				String name = temp.split(":")[1].trim(); 
        if (itemType.equals("Room")) //adding items from items dat file to their respective rooms
          if (name.equals("RANDOM")){  //if the name of the room is RANDOM (i.e. we want to initialize the item to a random room)
            int randomRoomIndex = (int)(Math.random() * masterRoomMap.size()); //generate random index of the hashmap that contains all the rooms
            ArrayList<String> roomKeys = new ArrayList<String>(masterRoomMap.keySet()); //turn hashmap into ArrayList format
            masterRoomMap.get(roomKeys.get(randomRoomIndex)).getInventory().addItem(item); //get a random room by taking a room name from this arrayList at the random index and then adding an item to it
          }else 
					  masterRoomMap.get(name).getInventory().addItem(item); //adding item to respective room (name is the name of the room)
				else //adding item to respective item that can hold other items (name is the name of the item that can hold other items)
					masterItemMap.get(name).addItem(item);
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	private void initCharacters(String fileName) throws Exception{
		Scanner characterScanner;
    masterCharacterMap = new HashMap<String, GameCharacter>();
    
		try {
			
			characterScanner = new Scanner(new File(fileName));
			while (characterScanner.hasNext()) {
				GameCharacter character = new GameCharacter(); //creates new GameCharacter object
        String characterName = characterScanner.nextLine().split(":")[1].trim(); //gets character name by parsing characters dat file
				character.setName(characterName); //sets the name of this new character object that we created to what we defined above
      
        //set character description of new GameCharacter object
        String characterDesc = characterScanner.nextLine().split(":")[1].trim();  
        character.setDescription(characterDesc);
        
        //set character initial text of new GameCharacter object
        String initialText = characterScanner.nextLine().split(":")[1].trim(); 
        character.setInitialText(initialText);

        //set character dialogue of new GameCharacter object
        character.setDialogue();

        //add character's dialogue within dialogue hashmap of the new GameCharacter object

        //parsing the character dat file to get the respective answers and responses of the dialogue
        String answerOne = characterScanner.nextLine().split(":")[1].trim(); 
        String answerTwo = characterScanner.nextLine().split(":")[1].trim();
        String responseOne = characterScanner.nextLine().split(":")[1].trim();
        String responseTwo = characterScanner.nextLine().split(":")[1].trim();

        //adding the answers and responces to the dialogue hashmap. The answers are the keys and the responses are the items. For a given answer (key), a call to the hashmap is made to produce the corresponding response (item)
        character.addDialogue(answerOne, responseOne);
        character.addDialogue(answerTwo, responseTwo);
         
        masterCharacterMap.put(characterName.toUpperCase().replaceAll(" ", "_"), character); //hashmap of all charaters (character name is key and GameCharacter object is item)

        String roomName = characterScanner.nextLine().split(":")[1].trim(); 
        if (roomName.equals("RANDOM")){  //if the name of the room is RANDOM (i.e. we want to initialize the character to a random room)
          int randomRoomIndex = (int)(Math.random() * masterRoomMap.size()); //generate random index of the hashmap that contains all the rooms
          ArrayList<String> roomKeys = new ArrayList<String>(masterRoomMap.keySet()); //turn hashmap into ArrayList format
          masterRoomMap.get(roomKeys.get(randomRoomIndex)).getCharacters().addCharacter(character); //get a random room by taking a room name from this arrayList at the random index and then adding a character to it
        }else 
          masterRoomMap.get(roomName).getCharacters().addCharacter(character); //adding character to respective room (name is the name of the room)
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
  
  private void initRooms(String fileName) throws Exception {
    masterRoomMap = new HashMap<String, Room>();
    Scanner roomScanner;
    try {
      HashMap<String, HashMap<String, String>> exits = new HashMap<String, HashMap<String, String>>();
      roomScanner = new Scanner(new File(fileName));
      while (roomScanner.hasNext()) {
        Room room = new Room();
        // Read the Name
        String roomName = roomScanner.nextLine();
        room.setRoomName(roomName.split(":")[1].trim());
        // Read the Description
        String roomDescription = roomScanner.nextLine();
        room.setDescription(roomDescription.split(":")[1].replaceAll("<br>", "\n").trim());
        // Read the Exits
        String roomExits = roomScanner.nextLine();
        // An array of strings in the format E-RoomName
        String[] rooms = roomExits.split(":")[1].split(",");
        HashMap<String, String> temp = new HashMap<String, String>();
        for (String s : rooms) {
          temp.put(s.split("-")[0].trim(), s.split("-")[1]);
        }

        exits.put(roomName.substring(10).trim().toUpperCase().replaceAll(" ", "_"), temp);

        // This puts the room we created (Without the exits in the masterMap)
        masterRoomMap.put(roomName.toUpperCase().substring(10).trim().replaceAll(" ", "_"), room);

        // Now we better set the exits.
      }

      for (String key : masterRoomMap.keySet()) {
        Room roomTemp = masterRoomMap.get(key);
        HashMap<String, String> tempExits = exits.get(key);
        for (String s : tempExits.keySet()) {
          // s = direction
          // value is the room.

          String roomName2 = tempExits.get(s.trim());
          Room exitRoom = masterRoomMap.get(roomName2.toUpperCase().replaceAll(" ", "_"));
          roomTemp.setExit(s.trim().charAt(0), exitRoom);
        }
      }

      roomScanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the game and initialise its internal map.
   */


  //CHANGE ROOM YOU START OFF HERE
  public Game() {
    try {
        initRooms("data/roomsOne.dat");
        currentRoom = masterRoomMap.get("HANGAR");
        inventory = new Inventory();

        initItems("data/itemsOne.dat");
        initCharacters("data/charactersOne.dat");
    

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    parser = new Parser();
  }

  //parsing new dat files that contain data about items, characters, and rooms that are a part of the second stage of the game
  private void stageTwo(){
    try {
      initRooms("data/roomsTwo.dat");
      currentRoom = masterRoomMap.get("CRASH_SITE");
      initItems("data/itemsTwo.dat");
      initCharacters("data/charactersTwo.dat");
      playStageTwo();

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    parser = new Parser();
  }
  /**
   * Main play routine. Loops until end of play.
   */
  public void play() {
    printWelcome();
    // Enter the main command loop. Here we repeatedly read commands and
    // execute them until the game is over.
    boolean finished = false;
    while (!finished) {
      Command command = parser.getCommand();
      finished = processCommand(command);
    }
    System.out.println("Thank you for playing.  Good bye.");
    in.close();
  }

  //begin to take commands from new map 
  public void playStageTwo() {
    System.out.println(currentRoom.longDescription());
    // Enter the main command loop. Here we repeatedly read commands and
    // execute them until the game is over.
    boolean finished = false;
    while (!finished) {
      Command command = parser.getCommand();
      finished = processCommand(command);
    }
    System.out.println("Thank you for playing.  Good bye.");
  }
  /**
   * Print out the opening message for the player.
   */

  //CHANGE WELCOME MESSAGE
  private void printWelcome() {
    System.out.println();
    System.out.println("Welcome to Space Survival!");
    System.out.println("Space Survival is an action adventure game full of mystery and strategy.");
    System.out.println("Type 'help' if you need help.");
    System.out.println();
    System.out.println("You are aboard a space station located on the outer rims of the Milky Ray.");
    System.out.println("You have just woken up, and you are currently gathering some equipment from around the ship to take with you before you take your fighter jet and go on your assigned scouting mission.");
    System.out.println("Your task: Walk around the ship and collect the following items: Meal pack, laser gun, medical kit, tarp. Return to the hangar once these items are collected.");
    System.out.println();
    System.out.println(currentRoom.longDescription());
  }

  /**
   * Given a command, process (that is: execute) the command. If this command ends
   * the game, true is returned, otherwise false is returned.
   */

  //CHANGE RESULTS BASED OFF OF COMMANDS
  private boolean processCommand(Command command) {
    if (command.isUnknown()) {
      System.out.println("I don't know what you mean...");
      return false;
    }
    String commandWord = command.getCommandWord();
    if (commandWord.equals("help"))
      printHelp();
    else if (commandWord.equals("go"))
      goRoom(command);
    else if (commandWord.equals("quit")) {
      if (command.hasSecondWord())
        System.out.println("Quit what?");
      else
        return true; // signal that we want to quit
    } else if (commandWord.equals("eat")) {
      System.out.println("Do you really think you should be eating at a time like this?");
    }else if (commandWord.equals("jump")){
      System.out.println("jump up and down");

    }else if (commandWord.equals("take")) {
      if (!command.hasSecondWord())
        System.out.println("Take what?");
      else
        takeItem(command.getSecondWord());
    }else if (commandWord.equals("drop")) {
    if (!command.hasSecondWord())
      System.out.println("Drop what?");
    else
      dropItem(command.getSecondWord());
    }else if (commandWord.equals("i")){
      System.out.println("You are carrying the following: " + inventory + "\nInventory weight: " + inventory.getTotalWeight());
    }else if (commandWord.equals("open")){
      if (!command.hasSecondWord())
        System.out.println("Open what?");
      else
        openItem(command.getSecondWord());
    }else if (commandWord.equals("talk")){
      if (!command.hasSecondWord() || !command.hasThirdWord())
        System.out.println("Talk to who?");
      else
        conversation(command.getThirdWord());
    }else if (commandWord.equals("proceed")){
      if (!command.hasSecondWord() || !command.hasThirdWord())
       System.out.println("Proceed from where?");
      else
        proceed(command.getThirdWord());
    }else if (commandWord.equals("weigh")) {
      if (!command.hasSecondWord())
        System.out.println("Weigh what?");
      else
        itemWeight(command.getSecondWord());
    }



    return false;
  }



  private void itemWeight(String secondWord) {
    Item item = inventory.contains(secondWord);
    if (item != null)
      System.out.println(item.getWeight());
    else
    System.out.println("You don't have that item in your inventory.");
  }

  private void proceed(String thirdWord) {
    if (!currentRoom.getRoomName().equalsIgnoreCase(thirdWord)) //if you are trying to proceed from a room you are not in currently
      System.out.println("You can't proceed from a room your not currently in!");
    else{
      //in order to proceed from first part of game, you have to be in the hangar and have the following items:
      if (thirdWord.equalsIgnoreCase("hangar") && inventory.inInventory("mealKit") && inventory.inInventory("blaster") && inventory.inInventory("medKit") && inventory.inInventory("tarp")){
        spaceFight();
        stageTwo();
        
      }else
        System.out.println("You haven't found all your items in the ship to proceed yet!");
    }
  }

  private void spaceFight() {
    //creating hashmaps full of command option
    HashMap<String, String> spaceFightPart1Choices = new HashMap<String, String>(); 
    spaceFightPart1Choices.put("go north", "The enemies have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("go south", "The enemies have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("go east", "The enemies have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("go west", "The enemies have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("request backup", "There's no time to request backup!");
    spaceFightPart1Choices.put("fire", "Nice! You destroyed some of their ships");

    HashMap<String, String> spaceFightPart2Choices = new HashMap<String, String>(); 
    spaceFightPart2Choices.put("go north", "Good job! Dodging the bullets allowed both enemy ships to blow themselves up in the cross fire!");
    spaceFightPart2Choices.put("go south", "Good job! Dodging the bullets allowed both enemy ships to blow themselves up in the cross fire!");
    spaceFightPart2Choices.put("go east", "You got shot and died! The end!");
    spaceFightPart2Choices.put("go west", "You got shot and died! The end!");
    spaceFightPart2Choices.put("request backup", "There's no time to request backup!");
    spaceFightPart2Choices.put("fire", "You got shot and died! The end!");

    System.out.println("You are on your flight patrol but the enemy fleet start attacking you. What do you do?");
    
    //get answer from user
    String answerOne = in.nextLine().toLowerCase();
    String responseOne = spaceFightPart1Choices.get(answerOne); 
    int wrongAnswersOne = 0;
    
    while (wrongAnswersOne < 3)
    if (responseOne != null){ //if the answer the user inputs is one of the keys in the hashMap
      if (answerOne != "fire"){ //if the answer the user inputs is not the correct answer
        System.out.println(responseOne);
        wrongAnswersOne++;
      }else{
        System.out.println(responseOne);
        System.out.println("Oh no! Enemy ships are shooting from east and west! What do you do?");
      }

    }else{ //if the answer the user inputs is not one of the keys in the hashMap
      System.out.println("I don't know what you mean");
      wrongAnswersOne++;
    }

    if (wrongAnswersOne >= 3)
      System.out.println("You're out of time. The enemy fleet destroyed your ship and you died. The End");

  }

  private void conversation(String thirdWord) {
    
    //check if character we want to talk to is in this room by first accessing the room that we are currently in through the masterRoomMap, then getting all the characters in that room, then checking if the person we want to talk to is one of those chracters
    GameCharacter speakingCharacter = masterRoomMap.get(currentRoom.getRoomName().toUpperCase().replaceAll(" ", "_")).getCharacters().contains(thirdWord);
    
    if (speakingCharacter != null){ //character we want to talk to is in room
      System.out.println(speakingCharacter.getInitialText()); //print out the conversation's initial dialogue
      String userAnswer = in.nextLine(); //the answer the user inputs after the character asks a question in the initial dialogue

      //produce a response (item) based off of the user's answer (key) within dialogue hashmap
      while (speakingCharacter.getDialogue().get(userAnswer) == null){ //continue asking for a response to the initial dialogue question until it is a proper response
        System.out.println("I don't know what you mean.");
        userAnswer = in.nextLine();
      }

      System.out.println(speakingCharacter.getDialogue().get(userAnswer));
    
    }else //character we want to talk to is not in room
      System.out.println(thirdWord + " is not in this room.");
    
    
  }

  private void openItem(String itemName) {
    Item item = inventory.contains(itemName);
    if (item != null) //item is in inventory
      System.out.println(item.displayContents());
    else //item is not in inventory
      System.out.println("You are not carrying a " + itemName);
  }

  private void takeItem(String itemName) {
      Inventory temp = currentRoom.getInventory(); //room inventory
      Item item = temp.removeItem(itemName);//this is the item the player picks up from the room. Item can have a value of null (if it is null then that means that the item they command to take is not in the room inventory)
      boolean isInInventory = false;
      boolean noSpace = false;

      if (item != null){ //if the player picks up an item that is in the room inventory 
        if (inventory.getTotalWeight() + item.getWeight() > 10){
          System.out.println("You don't have enough space in your inventory! Remove some items to be able to add this one.");
          temp.addItem(item);
          noSpace = true;
        }else{
          if (inventory.addItem(item)) //adding item that was in room inventory to player's inventory
            System.out.println("You have taken the " + itemName);
          else //some items cannot be picked up.
            System.out.println("You were unable to take the " + itemName); 
        }
      }
      else{ //if the player commands to pick up an item that is not in the room inventory 
        for (int i=0; i<inventory.getInventory().size(); i++){ //go through the player's inventory to see if requested item is within any items that can contain other items
          temp = inventory.getInventory().get(i).getContents(); //the contents of item that can hold other items
          if (temp != null){ //if the item in the inventory we are currently focusing on can hold other items
            item = temp.removeItem(itemName);
            if (item != null){
              if (inventory.getTotalWeight() + item.getWeight() > 10){
                System.out.println("You don't have enough space in your inventory! Remove some items to be able to add this one.");
                temp.addItem(item);
                noSpace = true;
              }else{
                if (inventory.addItem(item)){ //adding item that was in the item that can hold other item's inventory to player's inventory
                  System.out.println("You have taken the " + itemName);
                  isInInventory = true;
                }else //some items cannot be picked up.
                  System.out.println("You were unable to take the " + itemName); 
              }
            }
          
          }
        }

        if (!isInInventory && !noSpace) //if requested item is not in room or in any of the items that can store other items
          System.out.println("The item " + itemName + " isn't here."); 
      }
  }

  
  private void dropItem(String itemName) {
      Item item = inventory.removeItem(itemName);

      if (item != null){
        if (currentRoom.getInventory().addItem(item))
          System.out.println("you have dropped the " + itemName);
        else  System.out.println("You were unable to drop the " + itemName);
      }
      else
        System.out.println("You are not carrying a " + itemName + "."); 
  }





  // implementations of user commands:
  /**
   * Print out some help information. Here we print some stupid, cryptic message
   * and a list of the command words.
   */
  private void printHelp() {
    System.out.println("You are lost. You are alone. You wander");
    System.out.println("around at Monash Uni, Peninsula Campus.");
    System.out.println();
    System.out.println("Your command words are:");
    parser.showCommands();
  }

  /**
   * Try to go to one direction. If there is an exit, enter the new room,
   * otherwise print an error message.
   */

  //CREATE THIRD WORD FOR COMMAND BY GOING TO COMMAND FILE AND ADDING THIRD WORD
  private void goRoom(Command command) {
    if (!command.hasSecondWord()) {
      // if there is no second word, we don't know where to go...
      System.out.println("Go where?");
      return;
    }
    String direction = command.getSecondWord();
    // Try to leave current room.
    Room nextRoom = currentRoom.nextRoom(direction);
    if (nextRoom == null)
      System.out.println("There is no door!");
    else {
      currentRoom = nextRoom;
      System.out.println(currentRoom.longDescription());
    }
  }
}
