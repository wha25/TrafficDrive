package trafficdriver;

/**
 * A class to model an intersection with up to four streets, one at
 * each compass point.
 * 
 * An intersection is represented as a set of (x,y) coordinates. It
 * keeps references to any connected streets.
 */
public class Intersection implements RoadInterface {

  /** X and Y coordinates of the intersection */
  private int xPos = -1;
  private int yPos = -1;

  /** An array to hold the references to connected roads in 4 directions. */
  private RoadInterface[] streets = new RoadInterface[4];
  
  /** The assigned name of the intersection */
  private String interName = "" ;

  /** Default constructor; creates an empty, unplaced intersection */
  public Intersection () {
    yPos = -1;
    xPos = -1;
    for (Direction dir : Direction.values())
      streets[dir.ordinal()] = null;
  };

  /**
   * Construct an unconnected intersection at the specified location.
   * 
   * @param xPos X coordinate of the intersection
   * @param yPos Y coordinate of the intersection
   */
  public Intersection (int xPos, int yPos) {
    this.yPos = yPos;
    this.xPos = xPos;
    for (Direction dir : Direction.values())
      streets[dir.ordinal()] = null;
  }

  /**
   * Construct an unconnected intersection at the specified location.
   * 
   * Also register the intersection with a map.
   * 
   * @param xPos X coordinate of the intersection
   * @param yPos Y coordinate of the intersection
   * @param roadMap map where the intersection should register itself
   */
  public Intersection (int xPos, int yPos, SimpleMap roadMap) {
    this.yPos = yPos;
    this.xPos = xPos;
    for (Direction dir : Direction.values())
      streets[dir.ordinal()] = null;
    if (roadMap != null) {
      roadMap.addToMap(this);
    }
  }

  /**
   * Draw the intersection on the map.
   * 
   * The representation is a single `+' at the coordinates of the
   * intersection. If the intersection doesn't show on the map because
   * the map is too small, simply return.
   * 
   * @param roadMap a character array to draw on
   */
  public void drawOnMap (char[][] roadMap) {
    int yDim = -1;
    int xDim = -1;
    if (roadMap == null) return;
    
    // Check if map is big enough
    yDim = roadMap.length;
    xDim = roadMap[0].length;
    if (xDim <= 0 || yDim <= 0) return;
    
    // Add the intersection, provided it's on the map
    if (xPos >= 0 && xPos < xDim && yPos >= 0 && yPos < yDim) {
      roadMap[yPos][xPos] = '+';
    }
  }

  /**
   * Return the X coordinate of this intersection
   * 
   * @return the value of the x coordinate of the intersection
   */
  public int getXPos () {
    return xPos;
  }

  /**
   * Return the Y coordinate of this intersection
   * 
   * @return the value of the y coordinate of the intersection
   */
  public int getYPos () {
    return yPos;
  }

  /**
   * Check if the specified direction is open
   * 
   * @param attachAt direction to check
   * @return true if the direction is open, false otherwise
   */
  private boolean isOpen (Direction attachAt) {
    if (attachAt == null) return (false);
    boolean result = (streets[attachAt.ordinal()] == null);
    return (result);
  }

  /**
   * Store the specified reference in the specified direction.
   * 
   * @param me reference to the street to be attached
   * @param attachAt direction where the street should be attached
   */
  private void attachMe (RoadInterface me, Direction attachAt) {
    streets[attachAt.ordinal()] = me;
  }

  /**
   * Check if the specified street is attached in the specified
   * direction.
   * 
   * @param newObj a reference to a street
   * @param attachedAt direction to check for the specified street
   * @return true if the specified street is attached in the specified
   *         direction, false otherwise.
   */
  private boolean isAttached (RoadInterface newObj, Direction attachedAt) {
    boolean result = (streets[attachedAt.ordinal()] == newObj);
    return (result);
  }

  /**
   * Test if we know how to connect to the specified object. For an
   * Intersection, the only possibility is a Street. A null parameter
   * simply returns false, as null is not an instance of Street
   * 
   * @param newObj reference to the object to be checked
   * @return true if newObj is connectable, false otherwise
   */
  private boolean isConnectable (RoadInterface newObj) {
    boolean result = false;
    if (newObj instanceof Street) {
      result = true;
    }
    return (result);
  }

  /**
   * Return the street connected in the specified direction
   * 
   * @param dir the specified direction
   * @return the connected street, or null if no street is connected.
   */
  public RoadInterface getConnectedRoad (Direction dir) {
    RoadInterface retval = null;
    if (dir != null) {
      retval = streets[dir.ordinal()];
    }
    return (retval);
  }

  /**
   * Connect an intersection to an object satisfying RoadInterface.
   * Just a shell to check the type and call the specific method
   * appropriate for the object.
   * 
   * @param newObj the object to be connected
   * @param attachAt the direction where newObj should be connected
   * @return true if the conClassCastExceptionnection succeeds, false
   *         otherwise
   * @throws ClassCastException if this object is not suitable for
   *           connection
   */
  public boolean connectTo (RoadInterface newObj, Direction attachAt)
    throws ClassCastException {
    boolean result = false;
    /*
     * Check for compatible type; null newObj will also throw an
     * exception.
     */
    if (!isConnectable(newObj)) {
      ClassCastException oops = new ClassCastException();
      throw oops;
    }
    /*
     * Are we already connected to this object? If so, we're done. If
     * not, and the position is open, make the connection.
     */
    if (isAttached(newObj,attachAt)) {
      result = true;
    } else if (isOpen(attachAt)) {
      result = connectTo((Street) newObj,attachAt);
    }
    return (result);
  }

  /**
   * Connect an intersection to a street.
   * 
   * The method will install the link to the street and will also
   * ensure that the street points back to the intersection.
   * 
   * @param newStreet street to be attached
   * @param attachAt direction the street leaves the intersection
   * @return true if the crosslinkage is built without error, false
   *         otherwise
   */
  private boolean connectTo (Street newStreet, Direction attachAt) {
    boolean result = false;
    // Install a reference to the road.
    attachMe(newStreet,attachAt);
    // Install a back reference to this intersection in the road.
    result = newStreet.connectTo(this,attachAt.opposite());
    return (result);
  }

  /**
   * Connect an intersection to another intersection.
   * 
   * This creates a Street and attaches it to both
   * Intersections based on the relative position of the two
   * intersections.
   * 
   * @param otherInter the far intersection
   * @return true if the road is successfully built, false otherwise
   */
  public boolean buildStreetTo (Intersection otherInter, SimpleMap roadMap) {
    boolean result = true;

    int otherX = otherInter.getXPos();
    int otherY = otherInter.getYPos();
    int deltaX = xPos - otherX;
    int deltaY = yPos - otherY;
    Direction thisDir = Direction.north;
    Direction otherDir = Direction.south;
    if (deltaX == 0 && deltaY == 0) {
      result = false;
    } else if (deltaX == 0) {
      if (deltaY < 0) {
        thisDir = Direction.south;
        otherDir = Direction.north;
      } else {
        thisDir = Direction.north;
        otherDir = Direction.south;
      }
    } else if (deltaY == 0) {
      if (deltaX < 0) {
        thisDir = Direction.east;
        otherDir = Direction.west;
      } else {
        thisDir = Direction.west;
        otherDir = Direction.east;
      }
    } else if (deltaX > 0) {
      if (deltaY < 0) {
        thisDir = Direction.south;
        otherDir = Direction.east;
      } else {
        thisDir = Direction.west;
        otherDir = Direction.south;
      }
    } else if (deltaX < 0) {
      if (deltaY < 0) {
        thisDir = Direction.east;
        otherDir = Direction.north;
      } else {
        thisDir = Direction.north;
        otherDir = Direction.west;
      }
    }

    if (result == true) {
      Street newStreet = new Street();
      result = false;
      if (connectTo(newStreet,thisDir)) {
        if (otherInter.connectTo(newStreet,otherDir)) {
          roadMap.addToMap(newStreet);
          result = true;
        }
      }
    }
    return (result);
  }
  
  /**
   * Assigns a name to the intersection.
   * 
   * @param name that you want to assign to the intersection
   */
  public void setName (String name) {
    this.interName = name ;
  }
  
  /**
   * Retrieves the name of the intersection.
   */
  public String getName () {
    return (interName) ;
  }
  
}
