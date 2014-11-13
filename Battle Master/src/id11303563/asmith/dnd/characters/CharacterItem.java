package id11303563.asmith.dnd.characters;

/**
 * @author Alex
 * 
 * The Class CharacterItem.
 * 
 * This class represents a singular character object. A character object contains a name, class name,
 * level, max health points (HP), current HP, gold pieces (GP), id, gender. 
 */
public class CharacterItem {		
	/** The character's class name. */
	private String className;	
	/** The character's name. */
	private String name;	
	/** The character's level. */
	private String level;	
	/** The character's max health points. */
	private String maxHP;	
	/** The character's description. */
	private String description;	
	/** The character's id. */
	private long id;	
	/** The character's current health points. */
	private int currentHP;	
	/** The character's gender. */
	private String gender;	
	/** The character's gold pieces. */
	private String gp;		
	
	/**
	 * Instantiates a new character item.
	 *
	 * @param className the class name
	 * @param name the name
	 * @param gender the gender
	 * @param level the level
	 * @param maxHP the max health points
	 * @param gp the gold pieces
	 * @param description the description
	 */
	public CharacterItem(String className, String name, String gender, String level, String maxHP, String gp, String description)
	{
		this.className = className;
		this.name = name;
		this.level = level;
		this.maxHP = maxHP;
		this.description = description;
		this.gender = gender;
		this.gp = gp;
	}
	
	/**
	 * Gets the character's max hp.
	 *
	 * @return the max hp
	 */
	public String getMaxHP() {
		return maxHP;
	}

	/**
	 * Sets the character's max hp.
	 *
	 * @param maxHP the new max hp
	 */
	public void setMaxHP(String maxHP) {
		this.maxHP = maxHP;
	}

	/**
	 * Gets the character's gp.
	 *
	 * @return the gp
	 */
	public String getGp() {
		return gp;
	}

	/**
	 * Sets the character's gp.
	 *
	 * @param gp the new gp
	 */
	public void setGp(String gp) {
		this.gp = gp;
	}

	/**
	 * Gets the character's class name.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Sets the character's class name.
	 *
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Gets the character's name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the character's name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the character's level.
	 *
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	
	/**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	
	/**
	 * Gets the max hp.
	 *
	 * @return the max hp
	 */
	public String getMaxHp() {
		return maxHP;
	}
	
	/**
	 * Sets the max hp.
	 *
	 * @param hp the new max hp
	 */
	public void setMaxHp(String hp) {
		this.maxHP = hp;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the character's description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the character's id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the character's id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the character's current hp.
	 *
	 * @return the current hp
	 */
	public int getCurrentHP() {
		return currentHP;
	}

	/**
	 * Sets the character's current hp.
	 *
	 * @param currentHP the new current hp
	 */
	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	/**
	 * Gets the character's gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the character's gender.
	 *
	 * @param gender the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}	
}
