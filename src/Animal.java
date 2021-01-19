public class Animal {
    private String name;
    private String description;
    private boolean isDead;
    private int nutrition;


    public Animal(){
        
    }

    //sets animal name
    public void setName(String name) {
		this.name = name;
    }
    
    //sets animal description
	public void setDescription(String description) {
		this.description = description;
    }

    //sets whether or not animal is dead
    public void setIsDead(boolean isDead) {
            this.isDead = isDead;
    }

    // set nutritional value of animal (how much hunger is reduced when eating a certain animal)
    public void setNutrition(int nutrition){
        this.nutrition = nutrition;
    }
    
    //constructor
    public Animal(String name, String description, String initialText, boolean isDead, int nutrition){
        super();
        this.name = name;
        this.description = description;
        this.isDead = isDead;
        this.nutrition = nutrition;
    }


    //get animal name
    public String getName() {
		return name;
	}

    //get animal description
	public String getDescription() {
		return description;
    }
    
    //return whether or not the animal is dead
    public boolean isDead(){
        return isDead;
    }

    //get the nutritional value of the animal
    public int getNutrition() {
        return nutrition;
    }



}
