package trafficdriver;

import java.util.Arrays;

/**
 * A simple ascii character-based visualization of the road network.
 * 
 * This class implements a character-based grid that can be `written
 * on' by RoadInterface objects when their drawOnMap method is
 * invoked. The origin is assumed to be the northwest corner. X
 * increases to the east, Y increases to the south. The print
 * representation automatically draws a border around the actual grid.
 */
public class SimpleMap {

  /**
   * Array of objects to be included in the map. The objects must
   * satisfy the RoadInterface; in particular, they must provide a
   * drawOnMap method.
   */
  RoadInterface[] roadObjects = null;
  /** capacity of roadObjects */
  int maxObjects = 0;
  /** current number of objects */
  int numObjects = 0;
  /**
   * 2-D character array for the simple map.
   * 
   * The grid is stored in row-major order, as grid[y][x].
   */
  private char[][] grid = null;
  /** X dimension (one greater than maximum x coordinate) */
  private int xDim = 0;
  /** Y dimension (one greater than maximum y coordinate) */
  private int yDim = 0;

  /** Default constructor for null grid */
  public SimpleMap () {
    roadObjects = new RoadInterface[0];
    maxObjects = 0;
    numObjects = 0;
    grid = new char[0][0];
    xDim = 0;
    yDim = 0;
  }

  /**
   * Constructor for specified grid size
   * 
   * @param xDim x dimension for grid
   * @param yDim y dimension for grid
   */
  public SimpleMap (int xDim, int yDim) {
    roadObjects = new RoadInterface[0];
    maxObjects = 0;
    numObjects = 0;
    this.xDim = xDim;
    this.yDim = yDim;
    grid = new char[yDim][xDim];
  }

  /** Return grid x dimension */
  public int getxDim () {
    return (xDim);
  }

  /** Return grid y dimension */
  public int getyDim () {
    return (yDim);
  }

  /**
   * Register an object implementing RoadInterface with the grid so
   * that it will be asked to draw itself when the grid is printed.
   * 
   * @param roadObject the object to be registered
   */
  public void addToMap (RoadInterface roadObject) {
    // Expand the size of the array, if necessary
    if (numObjects >= maxObjects) {
      maxObjects = numObjects * 2 + 1;
      roadObjects = Arrays.copyOf(roadObjects,maxObjects);
    }
    // Register the object.
    roadObjects[numObjects] = roadObject;
    numObjects++;
  }

  /**
   * Generate a string representation of the grid. A border is
   * automatically added.
   */
  public String toString () {
    /*
     * Clear the grid and ask the registered objects to draw
     * themselves.
     */
    for (int y = 0 ; y < yDim ; y++) {
      for (int x = 0 ; x < xDim ; x++) {
        grid[y][x] = ' ';
      }
    }
    for (int i = 0 ; i < numObjects ; i++) {
      roadObjects[i].drawOnMap(grid);
    }
    /*
     * Now transcribe the map grid into a string, adding a border and
     * newlines as we go. Use the StringBuffer class, which allows
     * modification to the string.
     */
    StringBuffer result = new StringBuffer((yDim + 2) * (xDim + 2));
    result.append("\n  ");

    for (int x = 0 ; x < xDim ; x++) {
      if (x % 10 == 0) {
        result.append(x / 10);
      } else {
        result.append(' ');
      }
    }
    result.append("\n ");
    for (int x = 0 ; x < xDim + 2 ; x++)
      result.append('=');
    result.append('\n');
    for (int y = 0 ; y < yDim ; y++) {
      if (y % 10 == 0) {
        result.append(y / 10);
      } else {
        result.append(' ');
      }
      result.append('|');
      for (int x = 0 ; x < xDim ; x++) {
        result.append(grid[y][x]);
      }
      result.append("|\n");
    }
    result.append(' ');
    for (int x = 0 ; x < xDim + 2 ; x++)
      result.append('=');
    String temp = new String(result);
    return (temp);
  }

}
