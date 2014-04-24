package trafficdriver;

public class TrafficDriver {

  /**
   * @param args
   */
  public static void main (String[] args) {

    System.out.println("Testing intersections and streets.");

    SimpleMap roadMap = new SimpleMap(31,11);

    // Road built east to west
    Intersection i1 = new Intersection(2,1,roadMap);
    Intersection i2 = new Intersection(9,1,roadMap);
    i1.buildStreetTo(i2,roadMap);
    checkStreet(i1,Direction.east,i2,Direction.west);
    // Road built west to east
    Intersection i3 = new Intersection(15,1,roadMap);
    Intersection i4 = new Intersection(22,1,roadMap);
    i4.buildStreetTo(i3,roadMap);
    checkStreet(i3,Direction.east,i4,Direction.west);
    // Road built south to north
    Intersection i5 = new Intersection(12,1,roadMap);
    Intersection i6 = new Intersection(12,4,roadMap);
    i5.buildStreetTo(i6,roadMap);
    checkStreet(i5,Direction.south,i6,Direction.north);
    // Road built north to south
    Intersection i7 = new Intersection(25,1,roadMap);
    Intersection i8 = new Intersection(25,4,roadMap);
    i8.buildStreetTo(i7,roadMap);
    checkStreet(i7,Direction.south,i8,Direction.north);

    // Road starting east and turning south
    Intersection i9 = new Intersection(2,3,roadMap);
    Intersection i10 = new Intersection(9,6,roadMap);
    i9.buildStreetTo(i10,roadMap);
    checkStreet(i9,Direction.east,i10,Direction.north);

    // Road starting south and turning west
    Intersection i11 = new Intersection(22,5,roadMap);
    Intersection i12 = new Intersection(15,8,roadMap);
    i11.buildStreetTo(i12,roadMap);
    checkStreet(i11,Direction.south,i12,Direction.east);

    // Road starting west and turning north
    Intersection i13 = new Intersection(9,8,roadMap);
    Intersection i14 = new Intersection(2,5,roadMap);
    i13.buildStreetTo(i14,roadMap);
    checkStreet(i13,Direction.west,i14,Direction.south);

    // Road starting north and turning east
    Intersection i15 = new Intersection(15,6,roadMap);
    Intersection i16 = new Intersection(22,3,roadMap);
    i15.buildStreetTo(i16,roadMap);
    checkStreet(i15,Direction.north,i16,Direction.west);

    // Print the map and see what we have.
    System.out.println(roadMap);

    // Try connecting an intersection to an intersection.
    try {
      System.out.println("Checking for ClassCastException.");
      i15.connectTo(i16,Direction.west);
    } catch (ClassCastException ex) {
      System.out.println("Caught ClassCastException as expected.");
    }
  }


  /**
   * A method to test that a street is properly connected between two
   * intersections.
   * 
   * @param i1 the starting intersection
   * @param dir1 the direction where the street is attached
   * @param i2 the ending intersection
   * @param dir2 the direction where the street is attached
   */
  static void checkStreet (Intersection i1, Direction dir1, Intersection i2,
                           Direction dir2) {
    // Acquire the street from each intersection.
    Street s1 = (Street) i1.getConnectedRoad(dir1);
    Street s2 = (Street) i2.getConnectedRoad(dir2);
    // Neither street should be null.
    if (s1 == null) {
      System.out
              .println("No street " + dir1 + " from intersection " + i1 + "!");
    }
    if (s2 == null) {
      System.out
              .println("No street " + dir2 + " from intersection " + i2 + "!");
    }
    // Both intersections should point to the same street.
    if (s1 != s2) {
      System.out.println("Street changes from " + s1 + " to " + s2 +
              " between intersections " + i1 + " and " + i2 + "!");
    }
    // If s1 and s2 are both null, we can't go any further.
    if (s1 == null && s2 == null) return;
    /*
     * Now get the intersections that each street thinks it's
     * connected to. In theory s1 and s2 should be the same, but we're
     * not counting on that in this test routine.
     */
    Intersection s1i1 = null;
    Direction s1i1dir = dir1.opposite();
    Intersection s2i2 = null;
    Direction s2i2dir = dir2.opposite();
    // Check that s1 points back to i1 in the correct direction.
    if (s1 != null) {
      s1i1 = (Intersection) s1.getConnectedRoad(s1i1dir);
      if (s1i1 == null) {
        System.out.println("Intersection " + i1 +
                " thinks it's connected to street " + s1 + "in direction " +
                dir1 + " but street has no " + s1i1dir + " connection.");
      }
      if (s1i1 != i1) {
        System.out.println("Intersection " + i1 +
                " thinks it's connected to street " + s1 + "in direction " +
                dir1 + " but street " + s1 +
                " thinks it's connected to intersection " + s1i1 +
                " in direction " + s1i1dir);
      }
    }
    // Check that s2 points back to i2 in the correct direction.
    if (s2 != null) {
      s2i2 = (Intersection) s2.getConnectedRoad(s2i2dir);
      if (s2i2 == null) {
        System.out.println("Intersection " + i2 +
                " thinks it's connected to street " + s2 + "in direction " +
                dir2 + " but street has no " + s2i2dir + " connection.");
      }
      if (s2i2 != i2) {
        System.out.println("Intersection " + i2 +
                " thinks it's connected to street " + s2 + "in direction " +
                dir2 + " but street " + s2 +
                " thinks it's connected to intersection " + s2i2 +
                " in direction " + s2i2dir);
      }
    }
  }
}
