package trafficdriver;

/**
 * A class to represent a street.
 * 
 * Streets are constructed between intersections, and a street keeps
 * references to the intersections that connect to it.
 *
 * A street may have no turns or one turn. If there is a turn, it must
 * be to the right if you are traveling from the intersection where
 * construction starts to the intersection where construction ends.
 */
public class Street implements RoadInterface {

  /** Intersection 1: the intersection where construction starts. */
  private RoadInterface interOne = null;

  /** Direction to travel to enter intersection one */
  private Direction dirOne = Direction.east;

  /** X and Y coordinates of intersection one */
  private int xOne = -1;
  private int yOne = -1;
  
  /** The assigned name of the intersection */
  private String streetName = "" ;

  /** Intersection 2: the intersection where construction ends. */
  private RoadInterface interTwo = null;

  /** Direction to travel to enter intersection two */
  private Direction dirTwo = Direction.south;

  /** X coordinate of intersection two */
  private int xTwo = -1;

  /** Y coordinate of intersection two */
  private int yTwo = -1;

  /** X coordinate of turn (-1 if no turn) */
  private int xTurn = -1;

  /** Y coordinate of turn (-1 if no turn) */
  private int yTurn = -1;

  /** Default constructor */
  public Street () {};

  /**
   * Construct a street and register it with a map
   * 
   * @param roadMap map where the street should register itself
   */
  public Street (SimpleMap roadMap) {
    if (roadMap != null) roadMap.addToMap(this);
  }

  /**
   * Test if we know how to connect to the specified object. For a
   * street, the only possibility is an intersection. A null newObj
   * will return false.
   * 
   * @param newObj the candidate for a connection
   */
  private boolean isConnectable (RoadInterface newObj) {
    boolean result = false;
    if (newObj instanceof Intersection) {
      result = true;
    }
    return (result);
  }

  /**
   * Check if this street is already attached to the specified
   * intersection in the correct direction.
   * 
   * @param newObj an intersection
   * @param attachAt the direction to enter the intersection
   */
  private boolean isAttached (RoadInterface newObj, Direction attachAt) {
    boolean result = false;

    if ((interOne == newObj && dirOne == attachAt) ||
            (interTwo == newObj && dirTwo == attachAt)) {
      result = true;
    }
    return (result);
  }

  /**
   * Check if there's an open end of the street to attach to.
   */
  private boolean isOpen () {
    boolean result = false;
    if (interOne == null || interTwo == null) result = true;
    return (result);
  }

  /**
   * Add the necessary information to attach an intersection.
   * 
   * The intersection passed in the first call to this method will be
   * the start intersection, the second the end intersection. Any
   * additional calls are quietly ignored as long as the intersection
   * is already attached.
   * 
   * @param me the intersection to be attached
   * @param attachAt the direction to enter the intersection
   */
  private boolean attachMe (RoadInterface me, Direction attachAt) {

    // Can't do anything if the intersection or direction is null.
    if (me == null || attachAt == null) return (false);

    // If the intersection is already attached, that's fine.
    if (interOne == me && dirOne == attachAt) return (true);
    if (interTwo == me && dirTwo == attachAt) return (true);

    /*
     * We need to attempt to attach this intersection. You can't
     * attach the same intersection to both ends of a road.
     */
    boolean retval = true;
    if (interOne == null) {
      interOne = me;
      dirOne = attachAt;
      xOne = ((Intersection) me).getXPos();
      yOne = ((Intersection) me).getYPos();
    } else if (interTwo == null && interOne != me) {
      interTwo = me;
      dirTwo = attachAt;
      xTwo = ((Intersection) me).getXPos();
      yTwo = ((Intersection) me).getYPos();
    } else {
      retval = false;
    }
    return (retval);
  }

  /**
   * Return the intersection connected in the specified direction
   * 
   * @param dir the specified direction
   * @return the connected intersection, or null if no intersection is
   *         connected.
   */

  public RoadInterface getConnectedRoad (Direction dir) {
    if (dirOne == dir) {
      return (interOne);
    } else if (dirTwo == dir) {
      return (interTwo);
    } else {
      return (null);
    }
  }

  /**
   * Connect a street to an object satisfying RoadInterface. Just a
   * shell to check the type and call the specific method appropriate
   * for the object.
   * 
   * @param newObj the object to connect to
   * @param attachAt direction to travel to enter the attached object
   */
  public boolean connectTo (RoadInterface newObj, Direction attachAt)
    throws ClassCastException {
    boolean result = false;
    // Check for compatible type; null newObj will also throw an exception.
    if (!isConnectable(newObj)) {
      ClassCastException oops = new ClassCastException();
      throw oops;
    }
    /*
     * Are we already connected to this object? If so, we're done. If
     * not, and the street still has an open end, make the connection.
     */
    if (isAttached(newObj,attachAt)) {
      result = true;
    } else if (isOpen()) {
      result = connectTo((Intersection) newObj,attachAt);
    }
    return (result);
  }

  /**
   * Connect a street to an intersection. Note that this method is
   * only called from within the class. It's assumed that the
   * necessary correctness tests (connectable, not attached, and an
   * open attachment point) have been made.
   * 
   * @param newInter the intersection to be connected
   * @param attachAt the direction to travel to enter the intersection
   */
  private boolean connectTo (Intersection newInter, Direction attachAt) {
    boolean result = false;
    /*
     * Install a reference to the intersection, and install a back
     * reference to the street in the intersection. This can fail if
     * the street is already connected to two intersections, or if the
     * intersection already has a connected street in the specified
     * direction.
     */
    if (attachMe(newInter,attachAt) &&
            newInter.connectTo(this,attachAt.opposite())) result = true;
    /*
     * If this is the final connection, calculate the coordinates of
     * the turn, if any.
     */
    if (result == true && !isOpen()) calculateTurn();
    return (result);
  }

  /**
   * A method to calculate the coordinates of the turn (if any) in
   * this street. We need these coordinates every time we print, might
   * as well figure them out ahead of time.
   */
  private void calculateTurn () {
    // Do we need a turn? If not, we're done.
    if (dirOne.opposite() == dirTwo) return;
    /*
     * We need a turn. The coordinates of the turn point will be the x
     * coordinate of the north/south leg and the y coordinate of the
     * east/west leg.
     */
    if (dirOne == Direction.north || dirOne == Direction.south) {
      xTurn = xOne;
      yTurn = yTwo;
    } else {
      xTurn = xTwo;
      yTurn = yOne;
    }
  }

  /**
   * Paint the road onto the grid.
   * 
   * It will be the case that the x coordinate of the turn point
   * matches one end, and the y coordinate matches the other. It's a
   * matter of filling in the correct lines. If there's no turn, the
   * turn coordinates will both be -1. The details are are a pain.
   * 
   * @param roadMap a character array to draw on
   */
  public void drawOnMap (char[][] roadMap) {
    int deltaX = 0;
    int deltaY = 0;
    int startX = 0;
    int startY = 0;
    /*
     * If there's no turn, pick an end and calculate the delta. To
     * avoid overwriting the intersections, reduce the delta by 1 and,
     * at the equality end (the initial i value in the loop) shift the
     * start by one for the same reason.
     */
    if (xTurn == -1) {
      startX = xOne;
      startY = yOne;
      deltaX = xTwo - xOne;
      deltaY = yTwo - yOne;
      if (deltaY == 0) {
        if (deltaX < 0) {
          deltaX += 1;
        } else {
          startX++;
          deltaX -= 1;
        }
      } else {
        if (deltaY < 0) {
          deltaY += 1;
        } else {
          startY++;
          deltaY -= 1;
        }
      }
    }
    /*
     * There's a turn. Our starting point will be the turn
     * coordinates. Calculate deltaX and deltaY. Here, we want to
     * overwrite the turn (offset of zero in the loops) and reduce
     * delta to avoid overwriting the intersections at each end.
     */
    else {
      startX = xTurn;
      startY = yTurn;
      if (startX == xOne) {
        deltaX = xTwo - startX;
        deltaY = yOne - startY;
      } else {
        deltaX = xOne - startX;
        deltaY = yTwo - startY;
      }
      if (deltaX < 0) {
        deltaX++;
      }
      if (deltaY < 0) {
        deltaY++;
      }
    }
    /*
     * Now we can run two loops to fill in the necessary chars.
     */
    if (deltaX != 0) {
      for (int i = Math.min(deltaX,0) ; i < Math.max(deltaX,0) ; i++) {
        roadMap[startY][startX + i] = '*';
      }
    }
    if (deltaY != 0) {
      for (int i = Math.min(deltaY,0) ; i < Math.max(deltaY,0) ; i++) {
        roadMap[startY + i][startX] = '*';
      }
    }
    /*
     * Blind spot: in the case where both deltaX and deltaY are
     * counting back toward the turn, an offset of zero, and not quite
     * getting there.
     */
    if (deltaX < 0 && deltaY < 0) roadMap[startY][startX] = '*';
  }

  
  /**
   * Assigns a name to the street.
   * 
   * @param name that you want to assign to the street
   */
  public void setName (String name) {
	  streetName = name ;
  }
  
  /**
   * Retrieves the name of the street.
   */
  public String getName () {
	  return (streetName) ;
  }
}
