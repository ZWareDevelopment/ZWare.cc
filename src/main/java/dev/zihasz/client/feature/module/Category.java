package dev.zihasz.client.feature.module;

public enum Category {

	CLIENT("Client"),
	COMBAT("Combat"),
	PLAYER("Player"),
	MOVEMENT("Movement"),
	RENDER("Visual"),
	MISC("Miscellaneous");

	String name;

	Category(String nameIn) {
		name = nameIn;
	}

	public String display() {
		return name;
	}

}
