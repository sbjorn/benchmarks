package osl.examples.caf_benches;
 
import osl.manager.*;
import osl.util.*;
import osl.manager.annotations.message;

import java.util.List;
import java.util.ArrayList;

public class mixed_case extends Actor {
  private static final long serialVersionUID = 4277890623751326142L;
//  public  static final      String class    =
//                              "osl.examples.caf_benches.mixed_case";
  public  static final long s_factor1        = 86028157;
  public  static final long s_factor2        = 329545133;
  public  static final long s_task_n         = s_factor1 * s_factor2;

  private static ActorName m_mainactor           = null;
  private        int       m_num_rings           = 0;
  private        int       m_ring_size           = 0;
  private        int       m_initial_token_value = 0;
  private        int       m_repetitions         = 0;

  /* the main actor store  */
  public static ActorName getMainActor() {
    return m_mainactor;
  }

  @message
  public void boot(String in) throws RemoteCodeException {
    m_mainactor           = self();
    String[] args         = in.split("_");
    m_num_rings           = Integer.parseInt(args[1]);
    m_ring_size           = Integer.parseInt(args[2]);
    m_initial_token_value = Integer.parseInt(args[3]);
    m_repetitions         = Integer.parseInt(args[4]);
    int num_msgs = m_num_rings + (m_num_rings * m_repetitions);
    ActorName sv = create(supervisor.class, num_msgs);
    List<ActorName> masters = new ArrayList<ActorName>();
    for (int i = 0; i < m_num_rings; ++i) {
      masters.add(create(chain_master.class));
      send(masters.get(masters.size()-1), "init", sv, m_ring_size,
           m_initial_token_value, m_repetitions);
    }
  }
}
