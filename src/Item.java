//ADD WEIGHT

public class Item {
	//defining variables that each item will have
	private String name;
	private String description;
	private Inventory items;
	private boolean isOpenable;
	private int weight;
	private boolean pickUpable;
	
	
	public Item() {
		
	}
	
	//setting whether or not is openable or not (you can store other items in this item) with a boolean
	public boolean isOpenable() {
		return isOpenable;
	}

	//if item is openable, then this item now has an inventory to store other items
	public void setOpenable(boolean isOpenable) {
		this.isOpenable = isOpenable;
		if(isOpenable)
			this.items = new Inventory(); 
	}

	//set the name of the item
	public void setName(String name) {
		this.name = name;
	}

	//set the description of the item
	public void setDescription(String description) {
		this.description = description;
	}

	//set the weight of the item
	public void setWeight(int weight) {
		this.weight = weight;
	}

	//set whether or not the item can be picked up by using a boolean
	public void setPickUpable(boolean pickUpable){
		this.pickUpable = pickUpable;
	}


	//item constructors
	public Item(String name, String description, boolean isOpenable, int weight, boolean pickUpable) {
		super();
		this.name = name;
		this.description = description;
		this.isOpenable = isOpenable;
		if(isOpenable)
			this.items = new Inventory(); 
		this.weight = weight;
		this.pickUpable = pickUpable;
	}
	
	public Item(String name, String description, int weight, boolean pickUpable, int nutrition) {
		super();
		this.name = name;
		this.description = description;
		this.isOpenable = false;
		this.weight = weight;
		this.pickUpable = pickUpable;
	}

	//get item name
	public String getName() {
		return name;
	}

	//get item description
	public String getDescription() {
		return description;
	}

	//get item weight
	public int getWeight() {
		return weight;
	}
	
	//get the contents of an item if it can store other items
	public Inventory getContents() {
        if (!isOpenable) 
            return null;
		return items;
	}
	
	//add item to item that can store other items
	public boolean addItem(Item item) {
        if (!isOpenable) 
            return false;
		return items.addItem(item);
	}
	
	//remove item from item that can store other items
	public Item removeItem(String item) {
        if (!isOpenable) 
            return null;
		return items.removeItem(item);
	}

	//check whether or not an item can be picked up with a boolean
	public boolean pickUpable(){
		return pickUpable;
	}
	
	
	//display the contents of an item if it can store other items
	public String displayContents() {
        if (!isOpenable) 
            return null;
		return "The " + name + " contains:\n" + items;
	}
}
