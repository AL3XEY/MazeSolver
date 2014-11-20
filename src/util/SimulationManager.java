/**
 * @file SimulationManager.java
 * @date 3/11/2014
 */
package util;

import gui.environment.Environment;
import gui.environment.EnvironmentSet;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import maze.Maze;
import agent.Agent;

/**
 * Gestor de la simulación.
 */
public class SimulationManager extends Observable implements Runnable {
  private static int DEFAULT_INTERVAL = 200;

  private int m_interval;
  private ScheduledThreadPoolExecutor m_executor;
  private ScheduledFuture <?> m_future;

  private EnvironmentSet m_environments;
  private boolean[] m_finished;
  private boolean m_sim_finished;

  private SimulationResults m_results;

  /**
   * Constructor por defecto del simulador.
   * @param env_set Conjunto de entornos que va a manejar.
   */
  public SimulationManager (EnvironmentSet env_set) {
    m_interval = DEFAULT_INTERVAL;
    m_executor = new ScheduledThreadPoolExecutor(1);
    m_results = new SimulationResults();
    setEnvironments(env_set);
  }

  /**
   * @param msec Milisegundos que pasarán entre cada paso de la simulación.
   */
  public void setInterval (int msec) {
    if (msec > 0)
      m_interval = msec;
  }

  /**
   * Establece el conjunto de entornos que manipula la simulación.
   * @param env_set Conjunto de entornos.
   */
  public void setEnvironments (EnvironmentSet env_set) {
    if (env_set == null)
      throw new IllegalArgumentException("No se puede asignar un entorno nulo.");

    m_environments = env_set;
  }

  /**
   * Comienza la simulación. Si está pausada, la reanuda.
   * Nota: No se pueden agregar o eliminar entornos mientras la simulación se
   * está ejecutando.
   */
  public void startSimulation () {
    m_sim_finished = false;

    // Actualizamos el tamaño de la lista de entornos finalizados por si hay un
    // número diferente de entornos que en la última ejecución
    if (isStopped()) {
      m_finished = new boolean[m_environments.getEnvironmentCount()];
      m_results.clear();
    }

    // Lanzamos un hilo sólo si no se está ejecutando todavía
    if (!isRunning()) {
      m_future = m_executor.scheduleAtFixedRate(this, 0, m_interval,
        TimeUnit.MILLISECONDS);
      m_results.startTimer();
    }
  }

  /**
   * Pausa la simulación actual si se está ejecutando.
   */
  public void pauseSimulation () {
    if (isRunning()) {
      m_future.cancel(false);
      m_results.pauseTimer();
    }
  }

  /**
   * Para la simulación actual si se está ejecutando.
   */
  public void stopSimulation () {
    if (m_future != null) {
      m_future.cancel(false);
      m_future = null;
      m_results.pauseTimer();

      // Avisamos a los observadores que la simulación ha terminado
      setChanged();
      notifyObservers(m_results);
    }
  }

  /**
   * @return Si la simulación se está ejecutando.
   */
  public boolean isRunning () {
    return m_future != null && !m_future.isDone();
  }

  /**
   * @return Si la simulación está pausada.
   */
  public boolean isPaused () {
    return m_future != null && m_future.isCancelled();
  }

  /**
   * @return Si la simulación está parada.
   */
  public boolean isStopped () {
    return m_future == null;
  }

  /**
   * @return Si la simulación ha acabado (todos los agentes están parados).
   */
  public boolean isFinished () {
    return m_sim_finished;
  }

  /**
   * @return Resultados de la simulación actual. Puede ser que sean incompletos,
   * dado que puede ser que la simulación no haya acabado.
   */
  public final SimulationResults getResults () {
    return m_results;
  }

  /* (non-Javadoc)
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run () {
    int amount_finished = 0;

    // Hacemos que ejecuten un paso todos los agentes de todos los entornos
    // donde no haya acabado algún agente
    ArrayList <Environment> envs = m_environments.getEnvironmentList();
    for (int i = 0; i < envs.size(); i++) {
      if (!m_finished[i]) {
        m_finished[i] = envs.get(i).runStep();

        // Actualizamos la información de la simulación
        for (int j = 0; j < envs.get(i).getAgentCount(); j++) {
          Agent ag = envs.get(i).getAgent(j);
          Maze mz = envs.get(i).getMaze();

          // Todos los agentes del entorno han caminado
          m_results.agentWalked(ag);
          // Si acaba de terminar algún agente, lo buscamos para decir que
          // ha llegado
          if (m_finished[i] && (ag.getX() < 0 || ag.getY() < 0 ||
              ag.getX() >= mz.getWidth() || ag.getY() >= mz.getHeight()))
            m_results.agentFinished(ag);
        }
      }
      else
        amount_finished++;
    }

    // Si todos los agentes han terminado de ejecutar, paramos la simulación
    if (amount_finished == m_environments.getEnvironmentCount()) {
      stopSimulation();
      m_sim_finished = true;

      // Avisamos a los observadores que la simulación ha terminado
      setChanged();
      notifyObservers(m_results);
    }
  }

}
