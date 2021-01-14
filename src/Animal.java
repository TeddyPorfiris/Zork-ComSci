public class Animal {
    private String name;
    private String description;
    private boolean isDead;
    private int nutrition;


    public Animal(){
        
    }

    public void setName(String name) {
		this.name = name;
    }
    
	public void setDescription(String description) {
		this.description = description;
  }

  public void setIsDead(boolean isDead) {
		this.isDead = isDead;
  }

  //how much health one gets when eating a certain animal
  public void setNutrition(int nutrition){
      this.nutrition = nutrition;
  }
    
    public Animal(String name, String description, String initialText, boolean isDead, int nutrition){
        super();
        this.name = name;
        this.description = description;
        this.isDead = isDead;
        this.nutrition = nutrition;
    }


    public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
    }
    
    public boolean isDead(){
        return isDead;
    }

    public int getNutrition() {
        return nutrition;
    }



}
