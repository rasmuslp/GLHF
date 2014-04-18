package glhf.common;

public class User {

	private final int id;

	private String name = "Player";
	private boolean isReady = false;
	private int ping = -1;

	public User( final int id ) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public boolean isReady() {
		return this.isReady;
	}

	public void setReady( boolean isReady ) {
		this.isReady = isReady;
	}

	public int getPing() {
		return this.ping;
	}

	public void setPing( int ping ) {
		this.ping = ping;
	}

}
