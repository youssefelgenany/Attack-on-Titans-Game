package game.engine.interfaces;

public interface Mobil {
	
	int getDistance();
	
	void setDistance(int distance);
	
	int getSpeed();
	
	void setSpeed(int speed);
	default boolean hasReachedTarget() {
		if (getDistance()<=0) {
			return true;
		}
		return false;
	}
	default boolean move() {
		setDistance(getDistance()-getSpeed());
		if (hasReachedTarget())
			return true;
		return false;
	}

}
