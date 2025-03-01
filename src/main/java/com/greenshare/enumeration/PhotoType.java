package com.greenshare.enumeration;

/**
 * Enum for type(Entity) of image
 * 
 * @author joao.silva
 */
public enum PhotoType {
	USER(0, "User"), POST(1, "Post"), FLOWER_SHOP(2, "FlowerShop"), SPECIES(3, "Species"), ACHIEVEMENT(4,
			"Achievement"), FLOWER(5, "Flower"), FRUIT(6, "Fruit"), OFFER(7, "Offer");

	private String directoryName;
	private int numberType;

	PhotoType(int numberType, String directoryName) {
		this.numberType = numberType;
		this.directoryName = directoryName;
	}

	public int getNumberType() {
		return this.numberType;
	}

	public String getDirectoryName() {
		return this.directoryName;
	}

	public static boolean exists(int numberType) {
		for (PhotoType e : values()) {
			if (e.getNumberType() == numberType) {
				return true;
			}
		}
		return false;
	}
}
