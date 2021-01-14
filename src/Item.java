//ADD WEIGHT

public class Item {
	private String name;
	private String description;
	private Inventory items;
	private boolean isOpenable;
	private int weight;
	private boolean pickUpable;
	private int nutrition;
	
	
	public Item() {
		
	}
	

	public boolean isOpenable() {
		return isOpenable;
	}

	public void setOpenable(boolean isOpenable) {
		this.isOpenable = isOpenable;
		if(isOpenable)
			this.items = new Inventory(); 
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setPickUpable(boolean pickUpable){
		this.pickUpable = pickUpable;
	}

	//how much health one gets when eating a certain animal
	public void setNutrition(int nutrition){
		this.nutrition = nutrition;
	}


	public Item(String name, String description, boolean isOpenable, int weight, boolean pickUpable, int nutrition) {
		super();
		this.name = name;
		this.description = description;
		this.isOpenable = isOpenable;
		if(isOpenable)
			this.items = new Inventory(); 
		this.weight = weight;
		this.pickUpable = pickUpable;
		this.nutrition = nutrition;
	}
	
	public Item(String name, String description, int weight, boolean pickUpable, int nutrition) {
		super();
		this.name = name;
		this.description = description;
		this.isOpenable = false;
		this.weight = weight;
		this.pickUpable = pickUpable;
		this.nutrition = nutrition;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getWeight() {
		return weight;
	}
	
	public Inventory getContents() {
        if (!isOpenable) 
            return null;
		return items;
	}
	
	public boolean addItem(Item item) {
        if (!isOpenable) 
            return false;
		return items.addItem(item);
	}
	
	public Item removeItem(String item) {
        if (!isOpenable) 
            return null;
		return items.removeItem(item);
	}

	public boolean pickUpable(){
		return pickUpable;
	}

	public int getNutrition() {
        return nutrition;
    }
	
	public String displayContents() {
        if (!isOpenable) 
            return null;
		return "The " + name + " contains:\n" + items;
	}
}
