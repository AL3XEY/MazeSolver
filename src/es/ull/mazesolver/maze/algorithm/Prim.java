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
 * @file Prim.java
 * @date 26 Oct 2014
 */
package es.ull.mazesolver.maze.algorithm;

import java.util.ArrayList;

import es.ull.mazesolver.maze.MazeCreationAlgorithm;
import es.ull.mazesolver.util.Direction;
import es.ull.mazesolver.util.Pair;

/**
 * Implementación del algoritmo de Prim para la generación aleatoria de
 * laberintos perfectos.
 */
public class Prim extends MazeCreationAlgorithm {
  private ArrayList <ArrayList <Boolean>> m_included_cells;
  private ArrayList <short []> walls;

  /**
   * @param rows Número de filas del laberinto.
   * @param columns Número de columnas del laberinto.
   */
  public Prim (int rows, int columns) {
    super(rows, columns);
    walls = new ArrayList <short []>();
    m_included_cells = new ArrayList <ArrayList <Boolean>>(rows);

    // Creamos una matriz de visitados para saber en cada momento cuáles son
    // las celdas que no se han visitado todavía.
    for (int i = 0; i < rows; i++) {
      m_included_cells.add(new ArrayList <Boolean>(columns));
      for (int j = 0; j < columns; j++)
        m_included_cells.get(i).add(false);
    }
  }

  /* (non-Javadoc)
   * @see maze.MazeCreationAlgorithm#runCreationAlgorithm()
   */
  @Override
  public void runCreationAlgorithm () {
    short i = 0;
    short j = 0;
    // Empezar el laberinto con todo lleno de paredes y selecionar una celda.
    m_included_cells.get(i).set(j, true);
    addCell(i, j);
    // Mientras haya celdas sin visitar, seguir visitando.
    int nextWall = 0;
    while (!walls.isEmpty()) {
      // Seleccionamos una celda y una direccion de dentro de las posibles que
      // no hemos escogido aun.
      nextWall = (int) Math.round(0 + (Math.random() * (walls.size() - 1)));
      i = walls.get(nextWall)[0];
      j = walls.get(nextWall)[1];
      Direction dir = Direction.fromValue(walls.get(nextWall)[2]);
      Pair <Integer, Integer> desp = dir.decompose();

      // Si la celda vecina a la posicion i,j +dir sigue estando disponible
      // la elegimos y agregamos las celdas vecinas a esta al conjunto, si no
      // eliminamos dicha posicion con dicha direccion para que no vuelva
      // a salir de forma aleatoria
      if (!m_included_cells.get(i + desp.second).get(j + desp.first)) {
        openPassage(j, i, dir);
        m_included_cells.get(i + desp.second).set(j + desp.first, true);
        addCell(i + desp.second, j + desp.first);
      }
      walls.remove(nextWall);
    }
  }

  /**
   * @param x Posición en el eje X desde la que se quiere partir.
   * @param y Posición en el eje Y desde la que se quiere partir.
   */
  private void addCell (final int i, final int j) {
    for (short k = 1; k < Direction.MAX_DIRECTIONS; k++) {
      Direction dir = Direction.fromIndex(k);
      Pair <Integer, Integer> desp = dir.decompose();
      if ((i + desp.second >= 0) && (j + desp.first >= 0) && (i + desp.second < m_rows)
          && (j + desp.first < m_columns)
          && !m_included_cells.get(i + desp.second).get(j + desp.first)) {
        short [] aux = {(short) i, (short) j, dir.val};
        walls.add(aux);
      }
    }
  }
}