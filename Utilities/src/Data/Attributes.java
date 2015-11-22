package Data;

public class Attributes {
	private final String[] attr = {"Shape", "Horns", "Spines", "Pores", "Meshwork"};
	private boolean isSphericalShape = true;
	private boolean hasHorns = false;
	private boolean hasSpines = false;
	private int hornCount = 0;
	private int spineCount = 0;
	private int poreShape = 0;
	private int meshwork = 0;
	/* 1 - circular, 2 - elliptical */
	
	public boolean isSphericalShape() {
		return isSphericalShape;
	}
	public void setSphericalShape(boolean isSphericalShape) {
		this.isSphericalShape = isSphericalShape;
	}
	public boolean hasHorns() {
		return hasHorns;
	}
	public void setHasHorns(boolean hasHorns) {
		this.hasHorns = hasHorns;
	}
	public boolean hasSpines() {
		return hasSpines;
	}
	public void setHasSpines(boolean hasSpines) {
		this.hasSpines = hasSpines;
	}
	public int getHornCount() {
		return hornCount;
	}
	public void setHornCount(int hornCount) {
		this.hornCount = hornCount;
	}
	public int getSpineCount() {
		return spineCount;
	}
	public void setSpineCount(int spineCount) {
		this.spineCount = spineCount;
	}
	public int getPoreShape() {
		return poreShape;
	}
	public void setPoreShape(int poreShape) {
		this.poreShape = poreShape;
	}
	public int getMeshwork() {
		return meshwork;
	}
	public void setMeshwork(int meshwork) {
		this.meshwork = meshwork;
	}
	public String[] getAttr() {
		return attr;
	}
}
