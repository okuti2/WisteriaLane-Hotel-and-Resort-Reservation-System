package enumarators;

public enum RoomType {
    SINGLE (80.00, 2, 0) ,
    DOUBLE (120.00, 4, 0) ,
    DELUXE (300.00, 4, 0) ,
    PENTHOUSE (500.00, 4, 0);

    private final double rate;
    private final int max;
    private int noOfRooms;

    RoomType(double rate, int max, int noOfRooms) { this.rate = rate; this.max = max; this.noOfRooms = noOfRooms;}

    public double getRate() { return rate; }
    public int getMax() { return max; }
    public int getNoOfRooms() { return noOfRooms; }
    public void setNoOfRooms(int noOfRooms) { this.noOfRooms = noOfRooms; }


}
