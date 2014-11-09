/**
 * @file FileDialogs.java
 * @date 8/11/2014
 */
package gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import maze.Maze;

/**
 * Clase que contiene los métodos estáticos para mostrar los diálogos para
 * guardar y cargar ficheros.
 */
public class FileDialog {
  // Esta clase no se puede instanciar
  private FileDialog () {}

  /**
   * Muestra un diálogo al usuario para que indique el fichero de salida donde
   * desea guardar el laberinto.
   * @param maze Laberinto que se desea guardar.
   * @throws IOException Si no se pueden obtener permisos de escritura en el
   * fichero seleccionado.
   */
  public static void saveMaze (Maze maze) throws IOException {
    if (maze == null)
      throw new IllegalArgumentException("El laberinto especificado es inválido.");

    JFileChooser chooser = createFileChooser();
    int result = chooser.showSaveDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();

      // Si va a sobreescribir un archivo preguntamos primero
      if (file.exists()) {
        int choice = JOptionPane.showConfirmDialog(null,
            "The selected file already exists, do you want to overwrite it?",
            "File already exists", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
          maze.saveFile(file.getAbsolutePath() + extension(file.getName()));
      }
      maze.saveFile(file.getAbsolutePath() + extension(file.getName()));
    }
  }

  /**
   * Muestra un diálogo para que el usuario seleccione un conjunto de ficheros
   * de los que cargar laberintos.
   * @return El laberinto cargado.
   * @throws IOException Si hay un problema al leer el fichero seleccionado.
   */
  public static Maze[] loadMazes () throws IOException {
    JFileChooser chooser = createFileChooser();
    chooser.setMultiSelectionEnabled(true);
    int result = chooser.showOpenDialog(null);

    Maze[] mazes = new Maze[0];

    if (result == JFileChooser.APPROVE_OPTION) {
      File[] files = chooser.getSelectedFiles();
      mazes = new Maze[files.length];

      for (int i = 0; i < files.length; i++) {
        Maze maze = new Maze(files[i].getAbsolutePath());
        mazes[i] = maze;
      }
    }

    return mazes;
  }

  /**
   * Muestra un diálogo para que el usuario seleccione un fichero del que cargar
   * un laberinto.
   * @return El laberinto cargado.
   * @throws IOException Si hay un problema al leer el fichero seleccionado.
   */
  public static Maze loadMaze () throws IOException {
    JFileChooser chooser = createFileChooser();
    int result = chooser.showOpenDialog(null);

    Maze maze = null;
    if (result == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      maze = new Maze(file.getAbsolutePath());
    }

    return maze;
  }

  /**
   * @return Un diálogo para seleccionar ficheros con extensión ".maze".
   */
  private static JFileChooser createFileChooser () {
    JFileChooser chooser = new JFileChooser();
    FileFilter filter = new FileNameExtensionFilter("Maze files (*.maze)", "maze");
    chooser.setFileFilter(filter);

    return chooser;
  }

  /**
   * Decide si el nombre del fichero especificado necesita la extensión o no y
   * la devuelve si hace falta.
   * @param name Nombre del fichero que quiere guardar el usuario.
   * @return Extensión que habría que añadir al final del nombre para que éste
   * sea correcto.
   */
  private static String extension (String name) {
    String[] parts = name.split("\\.");
    if (parts.length == 1 || !parts[parts.length-1].equals("maze"))
      return ".maze";
    else
      return "";
  }

}
