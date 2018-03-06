package asteroids;

public class Update {
	enum Type {
		ASTEROID,
		PROJECTILE,
		PLAYER,
		DEATH;
	}
	
	int id;
	int playerID;
	int size;
	double rotation;
	Vector position;
	Vector velocity;
	Type type;
	
	public Update(int id, int size, double rotation, Vector position, Vector velocity) {
		this.id = id;
		this.size = size;
		this.rotation = rotation;
		this.position = position;
		this.velocity = velocity;
		this.type = Type.ASTEROID;
	}
	
	public Update(int id, int playerID, Vector position, Vector velocity) {
		this.id = id;
		this.playerID = playerID;
		this.position = position;
		this.velocity = velocity;
		this.type = Type.PROJECTILE;
	}
	
	public Update(int id, double rotation, Vector position, Vector velocity) {
		this.id = id;
		this.rotation = rotation;
		this.position = position;
		this.velocity = velocity;
		this.type = Type.PLAYER;
	}
	
	public Update(int id) {
		this.id = id;
		this.type = Type.DEATH;
	}
}
