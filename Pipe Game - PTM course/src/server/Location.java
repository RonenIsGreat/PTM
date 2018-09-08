package server;

public class Location {
	
	public int row;
	public int col;
	
	public Location(int col, int row) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public int hashCode() {
		StringBuilder boardString = new StringBuilder();
		boardString.append(row);
		boardString.append(col);
		return boardString.toString().hashCode();
	}
	
	
	@Override
	public boolean equals(Object o) {
		Location other = (Location) o;
		if(other.row == this.row && other.col == this.col) {
			return true;
		}

		return false;
	}
}
