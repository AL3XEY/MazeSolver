/**
 * @file RecursiveBacktracking.java
 * @date 10 Nov 2014
 */
package maze.algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import maze.Direction;
import maze.MazeCell;
import maze.MazeCreationAlgorithm;

/**
 *
 */
public class RecursiveBacktracking extends MazeCreationAlgorithm {
  private ArrayList <ArrayList <Boolean>> m_included_cells;

  /**
   * @param rows
   * @param columns
   */
  public RecursiveBacktracking (int rows, int columns) {
    super(rows, columns);
    m_included_cells = new ArrayList <ArrayList <Boolean>>(rows);
    // Creamos una matriz de visitados para saber en cada momento cuáles son
    // las celdas que no se han visitado todavía.
    for (int i = 0; i < rows; i++) {
      m_included_cells.add(new ArrayList <Boolean>(columns));
      for (int j = 0; j < columns; j++)
        m_included_cells.get(i).add(false);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see maze.MazeCreationAlgorithm#createMaze()
   */
  @Override
  public ArrayList <ArrayList <MazeCell>> createMaze () {
    Stack<Point> stack = new Stack<Point>();

    stack.push(new Point(0, 0));
    m_included_cells.get(0).set(0, true);

    while (!stack.isEmpty()) {
      Point p = stack.peek();
      Direction dir = getRandomDirection(p.x, p.y);
      if (dir == Direction.NONE) {
        stack.pop();
      }
      else {
        openPassage(p.x, p.y, dir);
        p = dir.movePoint(p);
        m_included_cells.get(p.y).set(p.x, true);
        stack.push(p);
      }
    }
    return m_maze;
  }

  /**
   * @param x Posición en el eje X desde la que se quiere partir (COLUMNA).
   * @param y Posición en el eje Y desde la que se quiere partir (FILA).
   * @return Dirección aleatoria hacia la que el agente se puede mover.
   */
  private Direction getRandomDirection (int x, int y) {
    ArrayList <Direction> directions = new ArrayList <Direction>();
    Point actual = new Point(x, y);

    // Comprobamos qué posiciones de alrededor son válidas y no se han visitado
    // Suponemos que la posición proporcionada es válida para empezar
    for (int i = 1; i < Direction.MAX_DIRECTIONS; i++) {
      Direction dir = Direction.fromIndex(i);
      Point next = dir.movePoint(actual);

      if (next.y >= 0 && next.y < m_rows &&
          next.x >= 0 && next.x < m_columns &&
          !m_included_cells.get(next.y).get(next.x))
        directions.add(dir);
    }

    if (directions.isEmpty())
      return Direction.NONE;
    else
      return directions.get((int)(Math.random() * directions.size()));
  }

}
