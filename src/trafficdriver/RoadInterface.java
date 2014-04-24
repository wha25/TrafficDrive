package trafficdriver;

/**
 * An interface for objects that exist in a road network.
 * 
 * Objects in the road network should know how to draw themselves on a
 * map of the road network. They should also know how to connect
 * themselves to other objects in the network, as appropriate.
 */
public interface RoadInterface {

  /**
   * Augment the map of the road network by adding the characters
   * needed to represent this object to the two-dimensional character
   * array supplied as a parameter.
   * 
   * The implementation of drawOnMap for a given object should not
   * touch entries in the array that are not part of the object's
   * representation.
   * 
   * @param roadMap character grid
   */
  public void drawOnMap (char[][] roadMap);

  /**
   * Connect this object to the object supplied as a parameter, at the
   * indicated compass point of this object.
   * 
   * A connection attempt can fail if the requested attachment point
   * is not empty.
   * 
   * If newObj is not compatible with this object (e.g., an attempt to
   * connect a Street directly to another Street) the method should
   * throw a ClassCastException.
   * 
   * @param newObj the object to be attached
   * @param attachAt the compass point where the object will be
   *          attached
   * @return true if the connection is made successfully, false
   *         otherwise.
   */
  public boolean connectTo (RoadInterface newObj, Direction attachAt)
    throws ClassCastException;

  /**
   * Return the road (intersection or street) connected in the
   * specified direction.
   * 
   * @param dir the specified direction
   * @return the connected object, or null if no object is connected
   */
  public RoadInterface getConnectedRoad (Direction dir);
}

/**
 * The four compass directions and utility methods.
 * 
 * As a convenience for working with a `Manhattan-style' road network
 * with roads oriented north, east, south, and west, this enum
 * prevents the accidental use of invalid directions and implements
 * utility methods to return the opposite direction as well as the
 * correct direction for left and right turns.
 */
enum Direction {
  /*
   * Methods defined for an enum constant are considered overrides of
   * methods defined for the enum as a whole. Thus the definitions
   * here for opposite, rightTurn, and leftTurn are considered as
   * overrides for the abstract methods below. The compiler will make
   * sure that each enum constant provides a definition for each
   * abstract method.
   */
  west {

    public Direction opposite () {
      return (east);
    }

    public Direction rightTurn () {
      return (north);
    }

    public Direction leftTurn () {
      return (south);
    }

  },
  east {

    public Direction opposite () {
      return (west);
    }

    public Direction rightTurn () {
      return (south);
    }

    public Direction leftTurn () {
      return (north);
    }
  },
  north {

    public Direction opposite () {
      return (south);
    }

    public Direction rightTurn () {
      return (east);
    }

    public Direction leftTurn () {
      return (west);
    }
  },
  south {

    public Direction opposite () {
      return (north);
    }

    public Direction rightTurn () {
      return (west);
    }

    public Direction leftTurn () {
      return (east);
    }
  };

  /**
   * Return the opposite direction.
   * 
   * E.g., {@code north.opposite()} will return {@code south}.
   */
  public abstract Direction opposite ();

  /**
   * Return the appropriate direction for a right turn.
   * 
   * E.g., {@code north.rightTurn()} will return {@code east}.
   */
  public abstract Direction rightTurn ();

  /**
   * Return the appropriate direction for a left turn.
   * 
   * E.g., {@code north.leftTurn()} will return {@code west}.
   */
  public abstract Direction leftTurn ();

}
