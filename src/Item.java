//ADD WEIGHT

public class Item {
	private String name;
	private String description;
	private Inventory items;
	private boolean isOpenable;
	private int weight;
	
	
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


	public Item(String name, String description, boolean isOpenable, int weight) {
		super();
		this.name = name;
		this.description = description;
		this.isOpenable = isOpenable;
		if(isOpenable)
			this.items = new Inventory(); 
		this.weight = weight;
	}
	
	public Item(String name, String description, int weight) {
		super();
		this.name = name;
		this.description = description;
		this.isOpenable = false;
		this.weight = weight;
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
	
	public String displayContents() {
        if (!isOpenable) 
            return null;
		return "The " + name + " contains:\n" + items;
	}
}
