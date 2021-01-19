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
  private int hunger = 0;
  private int time = 0;
  private boolean firstNightComplete = false;

  // This is a MASTER object that contains all of the rooms and is easily
  // accessible.
  // The key will be the name of the room -> no spaces (Use all caps and
  // underscore -> Great Room would have a key of GREAT_ROOM
  // In a hashmap keys are case sensitive.
  // masterRoomMap.get("GREAT_ROOM") will return the Room Object that is the Great
  // Room (assuming you have one).
  private HashMap<String, Room> masterRoomMap;
  private HashMap<String, Item> masterItemMap;
  private HashMap<String, Animal> masterAnimalMap;
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

        boolean pickUpable = Boolean.valueOf(itemScanner.nextLine().split(":")[1].trim()); //gets item description by parsing items dat file 
        item.setPickUpable(pickUpable); //sets if this new item object that we created to what we defined above can be picked

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


  
  private void initAnimals(String fileName) throws Exception{
		Scanner animalScanner;
    masterAnimalMap = new HashMap<String, Animal>();
    
		try {
			
			animalScanner = new Scanner(new File(fileName));
			while (animalScanner.hasNext()) {
        Animal animal = new Animal(); //creates new animal object
        
				String animalName = animalScanner.nextLine().split(":")[1].trim(); //gets animal name by parsing items dat file 
				animal.setName(animalName); //sets the name of this new animal object that we created to what we defined above
      
        String animalDesc = animalScanner.nextLine().split(":")[1].trim(); //gets animal description by parsing items dat file 
        animal.setDescription(animalDesc); //sets the description of this new animal object that we created to what we defined above

        boolean isDead = Boolean.valueOf(animalScanner.nextLine().split(":")[1].trim()); //checks if animal is dead by parsing items dat file 
        animal.setIsDead(isDead); //sets if this new animal object that we created to what we defined above is dead or not

        int nutrition = Integer.parseInt(animalScanner.nextLine().split(":")[1].trim()); //gets animal nutritional value by parsing items dat file 
        animal.setNutrition(nutrition); //sets the nutritional value of this new animal object that we created to what we defined above


        masterAnimalMap.put(animalName.toUpperCase().replaceAll(" ", "_"), animal); //hashmap of all animals


				
				String temp = animalScanner.nextLine(); 
        String name = temp.split(":")[1].trim(); 
        
        if (name.equals("RANDOM")){  //if the name of the room is RANDOM (i.e. we want to initialize the item to a random room)
          int randomRoomIndex = (int)(Math.random() * masterRoomMap.size()); //generate random index of the hashmap that contains all the rooms
          ArrayList<String> roomKeys = new ArrayList<String>(masterRoomMap.keySet()); //turn hashmap into ArrayList format
          masterRoomMap.get(roomKeys.get(randomRoomIndex)).getAnimals().addAnimal(animal); //get a random room by taking a room name from this arrayList at the random index and then adding an animal to it
        }else 
          masterRoomMap.get(name).getAnimals().addAnimal(animal); //adding animal to respective room (name is the name of the room)

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
  private boolean stageTwo(){
    try {
      initRooms("data/roomsTwo.dat");
      currentRoom = masterRoomMap.get("CRASH_SITE");
      inventory = new Inventory();
      initItems("data/itemsTwo.dat");
      initCharacters("data/charactersTwo.dat");
      initAnimals("data/animals.dat");
      return playStageTwo();

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
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
  }

  //begin to take commands from new map 
  public boolean playStageTwo() {
    planetFirstDayDescription();
    System.out.println(currentRoom.longDescriptionPlanet());
    System.out.println("\nHunger level: 0/15");
    System.out.println("\nTime: 12:00pm");
    // Enter the main command loop. Here we repeatedly read commands and
    // execute them until the game is over.
    boolean finished = false;
    while (!finished) {
      Command command = parser.getCommand();
      return processCommand(command);
    }
    System.out.println("Thank you for playing.  Good bye.");
    return false;
  }
  /**
   * Print out the opening message for the player.
   */

  //CHANGE WELCOME MESSAGE
  private void printWelcome() {
    System.out.println();
    System.out.println("Welcome to Space Survival!");
    System.out.println("Space Survival is an action adventure game full of adventure and strategy.");
    System.out.println("Type 'help' if you need help.");
    System.out.println();
    System.out.println("You are aboard the ICS space station located on the outer rims of the Milky Way.");
    System.out.println("You have just woken up, and you are currently gathering equipment from around the ship to take with you before you take your fighter jet and go on your assigned scouting mission.");
    System.out.println("Your task: Walk around the ship and collect the following items: water jug, blaster, med kit, tarp. Return to the hangar once these items are collected to be able to proceed.");
    System.out.println();
    System.out.println(currentRoom.longDescription());
  }

  private void planetFirstDayDescription(){
    System.out.println("DAY 1");
    System.out.println("You wake up inside your broken ship. You've crash-landed on one the abandoned ICS military grounds.");
    System.out.println("Besides the equipment you gathered at the station, in your space jet, you also have climbing gear and a torch. However, you noticed as you were crashing that your water jug and tarp flew out of a broken window. It seems to be midday.");
    System.out.println("You need to set up a base before 10:00pm that has a fire and a roof. You also need to collect all the items that you previously packed because they fell out of your inventory in the crash. Drop off all items at the crash site to set up your base");
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
    else if (commandWord.equals("go")){
      hunger++;
      time++;
      return goRoom(command);
    }else if (commandWord.equals("quit")) {
      if (command.hasSecondWord())
        System.out.println("Quit what?");
      else
        return true; // signal that we want to quit
    }else if (commandWord.equals("take")) {
      if (!command.hasSecondWord())
        System.out.println("Take what?");
      else
        if (command.hasThirdWord())
          takeItem(command.getSecondWord() + " " + command.getThirdWord());
        else
          takeItem(command.getSecondWord());
    }else if (commandWord.equals("drop")) {
    if (!command.hasSecondWord())
      System.out.println("Drop what?");
    else
      if (command.hasThirdWord())
        dropItem(command.getSecondWord() + " " + command.getThirdWord());
      else
        dropItem(command.getSecondWord());
    }else if (commandWord.equals("i")){
      System.out.println("You are carrying the following:\n" + inventory + "\nInventory weight: " + inventory.getTotalWeight() + "\n");
    }else if (commandWord.equals("open")){
      if (!command.hasSecondWord())
        System.out.println("Open what?");
      else
        if (command.hasThirdWord())
          openItem(command.getSecondWord() + " " + command.getThirdWord());
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
        return proceed(command.getThirdWord());
    }else if (commandWord.equals("weigh")) {
      if (!command.hasSecondWord())
        System.out.println("Weigh what?");
      else
        if (command.hasThirdWord())
          itemWeight(command.getSecondWord() + " " + command.getThirdWord());
        else
          itemWeight(command.getSecondWord());
    }else if (commandWord.equals("fire")){
      if (!command.hasSecondWord() || !command.hasThirdWord())
        System.out.println("Fire at what?");
      else
        fire(command.getThirdWord());
    }else if (commandWord.equals("eat")) {
      if (!command.hasSecondWord())
        System.out.println("eat what?");
      else
        eat(command.getSecondWord());
    }else if (commandWord.equals("request")) {
      if (!command.hasSecondWord())
        System.out.println("Request what?");
      else{
        request(command.getSecondWord());
      }
    }else if (commandWord.equals("read")) {
      if (!command.hasSecondWord())
        System.out.println("Read what?");
      else{
        if (command.hasThirdWord())
          read(command.getSecondWord() + " " + command.getThirdWord());
        else
          read(command.getSecondWord());
      }
    }else if (commandWord.equals("sleep"))
      sleep();


    return false;
  }


  private void sleep() {
    if (currentRoom.getRoomName().equalsIgnoreCase("crash site")){ //if the player tries to sleep at the crash site
      if (firstNightComplete){ //if the first day has been completed, the player can sleep whenever they want.
        System.out.println("Good morning world!");
        time = 38; //set time to 7:00am
        System.out.println(currentRoom.longDescriptionPlanet());
        System.out.println("Hunger level: " + hunger + "/15");
        System.out.println("Time: 7:00am");
        
      }else{ //if the first day, has not been completed, they need to have set up their base before they can sleep.
        if (currentRoom.getInventory().inInventory("climbing gear") && currentRoom.getInventory().inInventory("water jug") && currentRoom.getInventory().inInventory("blaster") && currentRoom.getInventory().inInventory("tarp") && currentRoom.getInventory().inInventory("med kit") && currentRoom.getInventory().inInventory("torch") && currentRoom.getInventory().inInventory("rocks") && currentRoom.getInventory().inInventory("wood")){
          firstNightComplete = true;
          System.out.println("Good night!\n\n");
          System.out.println("Good morning world! Spend the day today exploring the planet. The ICS starfleet used this planet to train its military before it was abandoned. Maybe there is a location on the planet that still has funcitoning ships you can use to escape! Remember to get back to the crash site before 10:00pm or else you'll die in the wild!");
          time = 38; //set time to 7:00am
          System.out.println(currentRoom.longDescriptionPlanet());
          System.out.println("Hunger level: " + hunger + "/15");
          System.out.println("Time: 7:00am");
        }else
          System.out.println("You can't sleep yet! You have to set up your base first!");
        
      } 
    }else //if the player tries to sleep somewhere other than the crash site
      if (currentRoom.getRoomName().equalsIgnoreCase("hangar") || currentRoom.getRoomName().equalsIgnoreCase("dorm") || currentRoom.getRoomName().equalsIgnoreCase("cafeteria") || currentRoom.getRoomName().equalsIgnoreCase("storage") || currentRoom.getRoomName().equalsIgnoreCase("navigation") || currentRoom.getRoomName().equalsIgnoreCase("weapons") || currentRoom.getRoomName().equalsIgnoreCase("washroom"))
        System.out.println("No time to sleep now!");
      else
        System.out.println("You can only sleep at your base at the crash site");
  }

  private void read(String secondWord) {

    if (inventory.contains(secondWord) != null){ //if the item the player is trying to read is in their inventory
      if (secondWord.equals("suspiciousCode")) //if the user is trying to read the suspicous code
        System.out.println("Code: 8864"); 
      else //if the user is not trying to read the suspicous code
        System.out.println("I don't understand what you mean...");
    }else //if the item the player is trying to read is NOT in their inventory
      System.out.println("You can't read something that isn't in your inventory!");
  }

  private void request(String secondWord) {
    if (secondWord.equalsIgnoreCase("backup")){
      if (currentRoom.getRoomName().equalsIgnoreCase("hangar") || currentRoom.getRoomName().equalsIgnoreCase("dorm") || currentRoom.getRoomName().equalsIgnoreCase("cafeteria") || currentRoom.getRoomName().equalsIgnoreCase("storage") || currentRoom.getRoomName().equalsIgnoreCase("navigation") || currentRoom.getRoomName().equalsIgnoreCase("weapons") || currentRoom.getRoomName().equalsIgnoreCase("washroom"))
        System.out.println("Why do you need to request backup? You're already at the space station!");
      else
        System.out.println("You are too far away from the space station for your distress signal to be recieved.");
    }else
      System.out.println("I don't know what you mean...");
  }

  private void eat(String secondWord) {
    if (currentRoom.getAnimals().contains(secondWord) != null){ //if the room player is currently in contains the animal they want to eat
        Animal deadAnimal = currentRoom.getAnimals().contains(secondWord); //the dead animal
        if (deadAnimal.isDead()){ //if the animal we want to eat is dead
          System.out.println("Yummy!");

          //reduce hunger based off of the nutrition the animal we just ate provides
          if (hunger < deadAnimal.getNutrition())
            hunger = 0;
          else
            hunger -= deadAnimal.getNutrition();

            System.out.println("Hunger lever: " + hunger + "/15");
          //remove the animal we just ate from the room
          currentRoom.getAnimals().removeAnimal(deadAnimal); 

        }else //if animal we want to eat is not dead
          System.out.println("You are trying to eat a live animal! Kill it first with your blaster.");

    }else
      System.out.println("You are trying to eat something that isn't there!");
  }

  private void fire(String thirdWord) {
    if (inventory.contains("blaster") != null){
      if (currentRoom.getAnimals().contains(thirdWord) != null){ //if animal we are firing at is in room
        Animal deadAnimal = currentRoom.getAnimals().contains(thirdWord); //get animal we want to fire at
        deadAnimal.setIsDead(true); //set that the animal is dead to true
        System.out.println("The " + thirdWord + " has been shot and killed.");

      }else if (thirdWord.equalsIgnoreCase("trees")){
        System.out.println("The trees have been blasted down. Their wood is now salvageable.");
        currentRoom.getInventory().removeItem("trees");
        Item wood = new Item();
        wood.setName("wood");
        wood.setDescription("Large strips of wood.");
        wood.setOpenable(false);
        wood.setWeight(2);
        wood.setPickUpable(true);
        currentRoom.getInventory().addItem(wood);
      }else
        System.out.println("Why are you firing at that?");
    }else
      System.out.println("You don't have your blaster with you. You cannot fire at anything without it!");
  }

  private void itemWeight(String secondWord) {
    Item item = inventory.contains(secondWord);
    if (item != null)
      System.out.println(item.getWeight());
    else
    System.out.println("You don't have that item in your inventory.");
  }
  
  private boolean proceed(String thirdWord) {
    //in order to proceed from first part of game, you have to be in the hangar and have the following items:
    if (thirdWord.equalsIgnoreCase("hangar") && inventory.inInventory("water jug") && inventory.inInventory("blaster") && inventory.inInventory("med kit") && inventory.inInventory("tarp") && currentRoom.getRoomName().equalsIgnoreCase("hangar")){
      return spaceFight();
    }else
      System.out.println("You can't proceed just yet!");
    
    return false;
  }

  private boolean spaceFight() {
    //creating hashmaps full of command option
    HashMap<String, String> spaceFightPart1Choices = new HashMap<String, String>(); 
    spaceFightPart1Choices.put("go north", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("go south", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("go east", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("go west", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart1Choices.put("request backup", "There's no time to request backup!");
    spaceFightPart1Choices.put("fire", "\nNice! You destroyed some of their ships\n");

    HashMap<String, String> spaceFightPart2Choices = new HashMap<String, String>(); 
    spaceFightPart2Choices.put("go north", "\nGood job! Dodging the bullets allowed both alien ships to blow themselves up in the cross fire!\n");
    spaceFightPart2Choices.put("go south", "\nGood job! Dodging the bullets allowed both alien ships to blow themselves up in the cross fire!\n");
    spaceFightPart2Choices.put("go east", "You can't go east! You'll run into the bullets!");
    spaceFightPart2Choices.put("go west", "You can't go west! You'll run into the bullets!");
    spaceFightPart2Choices.put("request backup", "There's no time to request backup!");
    spaceFightPart2Choices.put("fire", "You can't fire in two directions at once!");

    HashMap<String, String> spaceFightPart3Choices = new HashMap<String, String>(); 
    spaceFightPart3Choices.put("go north", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart3Choices.put("go south", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart3Choices.put("go east", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart3Choices.put("go west", "The aliens have surrounded you! There's nowhere to go!");
    spaceFightPart3Choices.put("request backup", "There's no time to request backup!");
    spaceFightPart3Choices.put("fire", "You don't have enough fire power to take out the whole alien fleet!");
    spaceFightPart3Choices.put("use self-destruct button", "\nYou escape the aliens while blowing them up but you and parts of your ship crashland on an unknown planet.\n");


    
    System.out.println("You are on your space jet performing your scouting mission. All of the sudden, an alien fleet approaches and begins to attack you! What do you do?");
    boolean spaceFightPart1 = rightAnswer(spaceFightPart1Choices, "fire", null);
    if (spaceFightPart1){
      System.out.println("Oh no! Alien ships are shooting from east and west! What do you do?");
      boolean spaceFightPart2 = rightAnswer(spaceFightPart2Choices, "go north", "go south");
      if (spaceFightPart2){
        System.out.println("The aliens are closing in on you! One giant blast can take out the whole fleet but you don't have enough fire power to create an explosion like that. What do you do?");
        boolean spaceFightPart3 = rightAnswer(spaceFightPart3Choices, "use self-destruct button", null);
        if (spaceFightPart3)
          return stageTwo();
      }
    }
    return true;

  }

  private boolean rightAnswer(HashMap<String, String> spaceFightChoices, String correctAnswer1, String correctAnswer2) {
    boolean isCorrect = false; 

    for (int i=0; i<3; i++){ //the user has three tries to get the right answer.
      String answer = in.nextLine().toLowerCase();
      String response = spaceFightChoices.get(answer);
      if (response != null){ //if the answer the user inputs is one of the keys in the hashMap
        if (!answer.equals(correctAnswer1) && !answer.equals(correctAnswer2)){//if the answer the user inputs is not the correct answer
          System.out.println(response);
        }else{
          System.out.println(response);
          isCorrect = true;
          break; //break from loop if correct answer is given (we don't have to continue prompting for answers if correct answer is given)
        }
      }else{ //if the answer the user inputs is not one of the keys in the hashMap
        if (answer.equalsIgnoreCase("help")){
          printHelp();
          System.out.println();
          i--;
        }
        else
          System.out.println("That's not the best thing to do right now!");
      }
    }
    if (isCorrect)
      return true;
    else{
      System.out.println("You're out of time. The enemy fleet destroyed your ship and you died. The End");
      return false;
    }
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
        if (item.pickUpable()){ 
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
        }else
          System.out.println("You cannot pick up this item.");
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
    /*
    System.out.println("You are lost. You are alone. You wander");
    System.out.println("around at Monash Uni, Peninsula Campus.");
    System.out.println();
    */
    System.out.println("Your command words are:");
    parser.showCommands();
  }

  /**
   * Try to go to one direction. If there is an exit, enter the new room,
   * otherwise print an error message.
   */

  //CREATE THIRD WORD FOR COMMAND BY GOING TO COMMAND FILE AND ADDING THIRD WORD
  private boolean goRoom(Command command) {
    if (!command.hasSecondWord()) {
      // if there is no second word, we don't know where to go...
      System.out.println("Go where?");
    }
    String direction = command.getSecondWord();
    // Try to leave current room.
    Room nextRoom = currentRoom.nextRoom(direction);
    if (nextRoom == null)
      System.out.println("You can't go that way!");
    else {
      currentRoom = nextRoom;
    
      //be able to go past mountain only if you have climbing gear 
      if (currentRoom.getRoomName().equalsIgnoreCase("1/2 Way Up Mountain")){
        if (inventory.contains("climbing gear") == null){ //if you don't have climbing gear in your inventory
          currentRoom = currentRoom.nextRoom("north");
          System.out.println("You can't surpass this mountain without your climbing gear!");
        }
      }
      
      if (currentRoom.getRoomName().equalsIgnoreCase("Mountain Peak")){
        if (inventory.contains("climbing gear") == null){ //if you don't have climbing gear in your inventory
          currentRoom = currentRoom.nextRoom("south");
          System.out.println("You can't surpass this mountain without your climbing gear!");
        }
      }

      


      

      //hunger/time

      //if one is on the first stage of the game (on the space station), don't include any time/hunger metrics
      if (currentRoom.getRoomName().equalsIgnoreCase("hangar") || currentRoom.getRoomName().equalsIgnoreCase("dorm") || currentRoom.getRoomName().equalsIgnoreCase("cafeteria") || currentRoom.getRoomName().equalsIgnoreCase("storage") || currentRoom.getRoomName().equalsIgnoreCase("navigation") || currentRoom.getRoomName().equalsIgnoreCase("weapons") || currentRoom.getRoomName().equalsIgnoreCase("washroom")){
        hunger = 0;
        time = 0;
        System.out.println(currentRoom.longDescription());
      }else{ //if we are in one of the rooms on the planet
        System.out.println(currentRoom.longDescriptionPlanet());
        //display hunger level
        System.out.println("Hunger level: " + hunger + "/15");

        //if hunger is greater than 15, you die
        if (hunger > 15){
          System.out.println("You've died from hunger! The end.");
          return true;
        }

        //if time reaches 48, then bring it back to zero (12:00 am)
        if (time == 48)
          time = 0;

        String timeDisplay = "";

        //displaying proper time based off of time variable value
        if (time == 0 || time == 1)
          timeDisplay = "Time: 12:00pm";
        else if (time == 2 || time == 3)
          timeDisplay = "Time: 1:00pm";
        else if (time == 4 || time == 5)
          timeDisplay = "Time: 2:00pm";
        else if (time == 6 || time == 7)
          timeDisplay = "Time: 3:00pm";
        else if (time == 8 || time == 9)
        timeDisplay = "Time: 4:00pm";
        else if (time == 10 || time == 11) 
          timeDisplay = "Time: 5:00pm";
        else if (time == 12 || time == 13)
          timeDisplay = "Time: 6:00pm";
        else if (time == 14 || time == 15)
          timeDisplay = "Time: 7:00pm";
        else if (time == 16 || time == 17)
          timeDisplay = "Time: 8:00pm";
        else if (time == 18 || time == 19)
          timeDisplay = "Time: 9:00pm";
        else if (time == 20 || time == 21)
          timeDisplay = "Time: 10:00pm";
        else if (time == 22 || time == 23)
          timeDisplay = "Time: 11:00pm";
        else if (time == 24 || time == 25)
          timeDisplay = "Time: 12:00am";
        else if (time == 26 || time == 27)
          timeDisplay = "Time: 1:00am";
        else if (time == 28 || time == 29)
          timeDisplay = "Time: 2:00am";
        else if (time == 30 || time == 31)
          timeDisplay = "Time: 3:00am";
        else if (time == 32 || time == 33)
          timeDisplay = "Time: 4:00am";
        else if (time == 34 || time == 35)
          timeDisplay = "Time: 5:00am";
        else if (time == 36 || time == 37)
          timeDisplay = "Time: 6:00am";
        else if (time == 38 || time == 39)
          timeDisplay = "Time: 7:00am";
        else if (time == 40 || time == 41)
          timeDisplay = "Time: 8:00am";
        else if (time == 42 || time == 43)
          timeDisplay = "Time: 9:00am";
        else if (time == 44 || time == 45)
          timeDisplay = "Time: 10:00am";
        else if (time == 46 || time == 47)
          timeDisplay = "Time: 11:00am";

        System.out.println(timeDisplay);

        //if time is 10:00pm (time == 22) and the player is not back to the crash site base and has not gathered all the items to the crash shite
        if ((time == 20) && (!currentRoom.getRoomName().equalsIgnoreCase("CRASH_SITE") && !currentRoom.getInventory().inInventory("climbing gear") && !currentRoom.getInventory().inInventory("water jug") && !currentRoom.getInventory().inInventory("blaster") && !currentRoom.getInventory().inInventory("tarp") && !currentRoom.getInventory().inInventory("med kit") && !currentRoom.getInventory().inInventory("torch") && !currentRoom.getInventory().inInventory("rocks") && !currentRoom.getInventory().inInventory("wood"))){
          System.out.println("You didn't complete your base in time! You died.");
          return true;
        }



      }

      //prompt user for code if they reach the hidden escape base
      if (currentRoom.getRoomName().equalsIgnoreCase("Hidden Escape Base")){
        System.out.println("You have made it to the escape base. Here, several still-intact ships are stored. Please type in code to enter. If you do not know the code, type the word \"stop\" to exit and continue to look around the planet.");
        String code = in.nextLine();
        while (!code.equals("8864")){
          if (code.equalsIgnoreCase("stop"))
            break;
          System.out.println("Access denied. Wrong code.");
          code = in.nextLine();
        }
      
        if (code.equals("8864")){
          System.out.println("Access Granted. You've entered the escape base, boarded the ship, and flew away from the planet.");
          System.out.println("Congradulations! You've escaped!");
          return true;
        }
      
      }
    }
    return false;
  }
}
