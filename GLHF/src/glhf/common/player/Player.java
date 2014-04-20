package glhf.common.player;

public class Player {

	private final int id;

	private String name = "Player";
	private boolean isReady = false;
	private int ping = -1;

	public Player( final int id ) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	void setName( String name ) {
		this.name = name;
	}

	public boolean isReady() {
		return this.isReady;
	}

	void setReady( boolean isReady ) {
		this.isReady = isReady;
	}

	public int getPing() {
		return this.ping;
	}

	void setPing( int ping ) {
		this.ping = ping;
	}

}
