package id11303563.asmith.dnd.characters;

/**
 * @author Alex
 * 
 * The Class MinionItem.
 * 
 * This class represents a singular minion object. A minion object contains a name,
 * level, max health points (HP), current HP, loot, id, race. 
 */
public class MinionItem {
		
	/** The minion's class name. */
	private String className;	
	/** The minion's name. */
	private String name;	
	/** The minion's level. */
	private String level;	
	/** The minion's max hp. */
	private String maxHP;
	/** The minion's loot. */
	private String loot;	
	/** The minion's id. */
	private long id;	
	/** The minion's current hp. */
	private int currentHP;	
	/** The minion's race. */
	private String race;
		
	/**
	 * Instantiates a new minion item.
	 *
	 * @param name the name
	 * @param race the race
	 * @param level the level
	 * @param maxHP the max hp
	 * @param loot the loot
	 */
	public MinionItem(String name, String race, String level, String maxHP, String loot)
	{
		this.name = name;
		this.level = level;
		this.maxHP = maxHP;
		this.loot = loot;
		this.race = race;	
	}
	
	/**
	 * Gets the minion's class name.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Sets the minion's class name.
	 *
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Gets the minion's name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the minion's name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the minion's level.
	 *
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	
	/**
	 * Sets the minion's level.
	 *
	 * @param level the new level
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	
	/**
	 * Gets the minion's max hp.
	 *
	 * @return the max hp
	 */
	public String getMaxHp() {
		return maxHP;
	}
	
	/**
	 * Sets the minion's max hp.
	 *
	 * @param hp the new max hp
	 */
	public void setMaxHp(String hp) {
		this.maxHP = hp;
	}

	/**
	 * Gets the minion's id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the minion's id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the minion's current hp.
	 *
	 * @return the current hp
	 */
	public int getCurrentHP() {
		return currentHP;
	}

	/**
	 * Sets the minion's current hp.
	 *
	 * @param currentHP the new current hp
	 */
	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	/**
	 * Gets the minion's loot.
	 *
	 * @return the loot
	 */
	public String getLoot() {
		return loot;
	}

	/**
	 * Sets the minion's loot.
	 *
	 * @param loot the new loot
	 */
	public void setLoot(String loot) {
		this.loot = loot;
	}

	/**
	 * Gets the minion's race.
	 *
	 * @return the race
	 */
	public String getRace() {
		return race;
	}

	/**
	 * Sets the minion's race.
	 *
	 * @param race the new race
	 */
	public void setRace(String race) {
		this.race = race;
	}	
}
