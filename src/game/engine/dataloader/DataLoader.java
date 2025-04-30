package game.engine.dataloader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import game.engine.titans.TitanRegistry;
import game.engine.weapons.WeaponRegistry;


public class DataLoader {
	private final static String TITANS_FILE_NAME = "titans.csv";
	private final static String WEAPONS_FILE_NAME = "weapons.csv";

	
	
	// fn --> file name 
	// br --> buffered reader
	// hm --> hash map
	// tr --> titan registry 
	
	public static HashMap<Integer, TitanRegistry> readTitanRegistry() throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(TITANS_FILE_NAME))){
		String line = br.readLine();
		HashMap<Integer, TitanRegistry> hm = new HashMap<Integer, TitanRegistry>();
		while (line!= null){
			System.out.println(line);
			String[] titan = line.split(",");
			TitanRegistry tr = new TitanRegistry(Integer.parseInt(titan[0]), 
												 Integer.parseInt(titan[1]), 
												 Integer.parseInt(titan[2]), 
												 Integer.parseInt(titan[3]), 
												 Integer.parseInt(titan[4]), 
												 Integer.parseInt(titan[5]), 
												 Integer.parseInt(titan[6]));
			hm.put(Integer.parseInt(titan[0]), tr);
			line = br.readLine();
			
		}
		return hm;
		}
	}
	public static HashMap<Integer, WeaponRegistry> readWeaponRegistry() throws IOException{
		 HashMap<Integer, WeaponRegistry> weaponRegistry = new HashMap<>();
	        

	        try (BufferedReader reader = Files.newBufferedReader(Paths.get(WEAPONS_FILE_NAME))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                String[] values = line.split(",");
	                int code = Integer.parseInt(values[0]);
	                int price = Integer.parseInt(values[1]);
	                int damage = 0;
	                String name = "";
	                int minRange = 0;
	                int maxRange = 0;

	                if (values.length > 2) {
	                    damage = Integer.parseInt(values[2]);
	                    name = values[3];

	                    if (values.length > 4){
	                        minRange = Integer.parseInt(values[4]);
	                        maxRange = Integer.parseInt(values[5]);
	                    }
	                }

	                WeaponRegistry newWeapon = new WeaponRegistry(code, price, damage, name, minRange, maxRange);
	                weaponRegistry.put(code, newWeapon);
	            }
	        }

	        return weaponRegistry;
	}
    
    
	
}
