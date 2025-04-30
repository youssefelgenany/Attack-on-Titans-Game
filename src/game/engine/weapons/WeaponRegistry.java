package game.engine.weapons;



public class WeaponRegistry {
	private final int code;
	public int getCode() {
		return code;
	}
	public int getPrice() {
		return price;
	}
	public int getDamage() {
		return damage;
	}
	public String getName() {
		return name;
	}
	public int getMinRange() {
		return minRange;
	}
	public int getMaxRange() {
		return maxRange;
	}
	private int price;
	private int damage;
	private String name;
	private int minRange;
	private int maxRange;
	
	public WeaponRegistry(int code, int price){
		this.code = code;
		this.price = price;
	}
	
	public WeaponRegistry(int code, int price, int damage, String name){
		
		this.code = code;
		this.price =price ;
		this.damage = damage;
		this.name = name;
	}
	
	public WeaponRegistry(int code, int price, int damage, String name, int minRange, int maxRange){
		this.code = code;
		this.price = price;
		this.damage = damage;
		this.name = name;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}
	public Weapon buildWeapon() {
		    Weapon weapon = null;
		    switch (code) {
		        case 1:
		            weapon = new PiercingCannon(damage);
		            break;
		        case 2:
		            weapon = new SniperCannon(damage);
		            break;
		        case 3:
		            weapon = new VolleySpreadCannon(damage,minRange,maxRange);
		            break;
		        case 4:
		            weapon = new WallTrap(damage);
		            break;
		        default:
		           System.out.println("Invalid registry code: " + code);
		    }
		    return weapon;
	}

}
