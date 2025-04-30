package game.engine.weapons.factory;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.weapons.Weapon;
import game.engine.weapons.WeaponRegistry;

import java.io.IOException;
import java.util.HashMap;

public class WeaponFactory {
	private final HashMap<Integer, WeaponRegistry> weaponShop;
	public WeaponFactory() throws IOException{
		this.weaponShop=DataLoader.readWeaponRegistry();
	}
	public HashMap<Integer, WeaponRegistry> getWeaponShop() {
		return weaponShop;
	}
	
	public FactoryResponse buyWeapon(int resources, int weaponCode) throws InsufficientResourcesException {
        WeaponRegistry weapon = weaponShop.get(weaponCode);
        if (weapon == null) {
            throw new IllegalArgumentException("Invalid weapon code.");
        }
        if (resources < weapon.getPrice()) {
            throw new InsufficientResourcesException("Insufficient resources to buy the weapon: ", resources);
        }
        resources -= weapon.getPrice();
        Weapon w=weapon.buildWeapon();
        return new FactoryResponse(w, resources);
    }
	
    public void addWeaponToShop(int code, int price) {
           
    WeaponRegistry weapon = new WeaponRegistry (code,price);
    weaponShop.put(code, weapon);
    }
    public void addWeaponToShop(int code, int price, int damage, String name) {
             WeaponRegistry weapon = new WeaponRegistry (code, price, damage, name);
         weaponShop.put(code, weapon);
    }
    public void addWeaponToShop(int code, int price, int damage, String name,  int minRange, int maxRange) {
             WeaponRegistry weapon = new WeaponRegistry (code, price, damage, name, minRange, maxRange);
         weaponShop.put(code, weapon);
    }

}
