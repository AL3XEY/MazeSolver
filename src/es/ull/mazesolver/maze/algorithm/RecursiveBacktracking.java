/*
 * This file is part of MazeSolver.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2014 MazeSolver
 * Sergio M. Afonso Fumero <theSkatrak@gmail.com>
 * Kevin I. Robayna Hernández <kevinirobaynahdez@gmail.com>
 */

/**
 * @file RecursiveBacktracking.java
 * @date 10 Nov 2014
 */
package es.ull.mazesolver.maze.algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import es.ull.mazesolver.maze.MazeCreationAlgorithm;
import es.ull.mazesolver.util.Direction;

/**
 * Implementación de algoritmo recursivo con backtracking para la generación
 * aleatoria de laberintos perfectos. (implementado de forma iterativa)
 */
public class RecursiveBacktracking extends MazeCreationAlgorithm {
  private ArrayList <ArrayList <Boolean>> m_included_cells;

  /**
   * @param rows
   *          Número de filas del laberinto.
   * @param columns
   *          Número de columnas del laberinto.
   */
  public RecursiveBacktracking (int rows, int columns) {
    super(rows, columns);
    m_included_cells = new ArrayList <ArrayList <Boolean>>(rows);
    // Creamos una matriz de visitados para saber en cada momento cuáles son
    // las celdas que no se han visitado todavía.
    for (int y = 0; y < rows; y++) {
      m_included_cells.add(new ArrayList <Boolean>(columns));
      for (int x = 0; x < columns; x++)
        m_included_cells.get(y).add(false);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see maze.MazeCreationAlgorithm#runCreationAlgorithm()
   */
  @Override
  public void runCreationAlgorithm () {
    Stack <Point> stack = new Stack <Point>();

    stack.push(new Point(0, 0));
    m_included_cells.get(0).set(0, true);

    while (!stack.isEmpty()) {
      Point p = stack.peek();
      Direction dir = getRandomDirection(p.y, p.x);
      if (dir == Direction.NONE) {
        stack.pop();
      }
      else {
        openPassage(p.y, p.x, dir);
        p = dir.movePoint(p);
        m_included_cells.get(p.y).set(p.x, true);
        stack.push(p);
      }
    }
  }

  /**
   * @param y
   *          Posición en el eje Y desde la que se quiere partir.
   * @param x
   *          Posición en el eje X desde la que se quiere partir.
   * @return Dirección aleatoria hacia la que el agente se puede mover.
   */
  private Direction getRandomDirection (int y, int x) {
    ArrayList <Direction> directions = new ArrayList <Direction>();
    Point actual = new Point(x, y);

    // Comprobamos qué posiciones de alrededor son válidas y no se han visitado
    // Suponemos que la posición proporcionada es válida para empezar
    for (int i = 1; i < Direction.MAX_DIRECTIONS; i++) {
      Direction dir = Direction.fromIndex(i);
      Point next = dir.movePoint(actual);

      if (next.y >= 0 && next.y < m_rows && next.x >= 0 && next.x < m_columns
          && !m_included_cells.get(next.y).get(next.x))
        directions.add(dir);
    }

    if (directions.isEmpty())
      return Direction.NONE;
    else
      return directions.get((int) (Math.random() * directions.size()));
  }

}
