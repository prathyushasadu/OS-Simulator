import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
/* a. Prathyusha Reddy Sadu
    b. CS 5323
    c. Phase III Multi Scheduling system
    d. 5/1/2018
    f. The SYSTEM CLASS is the driver of the project.
        Spooling and PROCCESS_MANAGER classes were called.
        Spooling  loads the input file to disk and
       then CPU will execute the Instructions in the Memory.
    g. The SYSTEM class is the starting step of the porject.
       It handles the segment fault and page faults.
       I divide each module into a seperate class. It checks
       the trace file and the output files were already existing.
       It will delete the existing tracefile and output_file.
    */

public class SYSTEM
{
  public static Map<Integer, PCB> pcbs = new HashMap<Integer, PCB>();
  public static Map<Integer, PMT> PROGRAM_PMTs = new HashMap<Integer, PMT>();
  public static Map<Integer, PMT> INPUT_PMTs = new HashMap<Integer, PMT>();
  public static Map<Integer, PMT> OUTPUT_PMTs = new HashMap<Integer, PMT>();
  public static Map<Integer, String> TRACE_FILEs = new HashMap<Integer, String>();
  public static String File;
  public static String INFINTE_JOBS = "";
  public static int NORMAL_JOBS;
  public static int ABNORMAL_JOBS;
  public static int ABNORMAL_TIME_LOST;
  public static int INFINITE_TIME_LOST;
  public static String[][] MEMORY = new String[32][8];
  public static String[][] DISK = new String[256][8];
  public static Queue<Integer> JOB_QUEUE = new LinkedList<Integer>();
  public static Queue<Integer> READY_QUEUE = new LinkedList<Integer>();
  public static Queue<Integer> BLOCKED_QUEUE = new LinkedList<Integer>();
  public static METERING METER = new METERING();
  public static PCB CURRENT_pcb;
  public static boolean KILL_JOB = false;
  public static int[] MEMORY_ALLOCATED = new int[32];
  public static int[] DISK_FMBV = new int[256];
  public static int[] MEMORY_FMBV = new int[32];
  public static String PC ;
  public static String IR;
  public static String EA = null;
  public static String TOS;
  public static String BR;
  public static PMT pmt;
  public static PMT UPDATE_PMT;
  public static int TIME;
  public static int CURRENT_JOB;
  public static PrintWriter write;
  public static BufferedReader Reader;
  public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  
  public static void main(String[] args)
  {
    File = args[0];
    try
    {
      Reader = new BufferedReader(new FileReader(File));
    }
    catch (FileNotFoundException ex)
    {
      System.out.println("FILE NOT FOUND");
      System.exit(0);
    }
    try
    {
      write = new PrintWriter("execution_profile");
    }
    catch (FileNotFoundException ex) {}
    Spooling.Readline();
    Spooling.Input_Spooling();
    while ((BLOCKED_QUEUE.size() != 0) || (READY_QUEUE.size() != 0) || (JOB_QUEUE.size() != 0)) {
      PROCESS_MANAGER.process_manager();
    }
    OUTPUT.INFORMATION();
    write.flush();
  }
  
  public static void createpcb(int ID)
  {
    pcbs.put(Integer.valueOf(ID), new PCB());
  }
  
  public static void createProgPMT(int ID)
  {
    PROGRAM_PMTs.put(Integer.valueOf(ID), new PMT());
  }
  
  public static void createInpPMT(int ID)
  {
    INPUT_PMTs.put(Integer.valueOf(ID), new PMT());
  }
  
  public static void createOutPMT(int ID)
  {
    OUTPUT_PMTs.put(Integer.valueOf(ID), new PMT());
  }
  
  public static int CHECK_MEMORY_SIZE()
  {
    int count = 0;
    for (int i = 0; i < MEMORY_ALLOCATED.length; i++) {
      if (MEMORY_ALLOCATED[i] == 0) {
        count++;
      }
    }
    return count;
  }
  
  public static int ALLOCATE_MEMORY()
  {
    int frame = -1;
    for (int i = 0; i < MEMORY_ALLOCATED.length; i++) {
      if (MEMORY_ALLOCATED[i] == 0)
      {
        frame = i;
        break;
      }
    }
    MEMORY_ALLOCATED[frame] = 1;
    
    return frame;
  }
  
  public static int DISK_UTILIZED()
  {
    int count = 0;
    for (int i = 0; i < DISK_FMBV.length; i++) {
      if (DISK_FMBV[i] == 1) {
        count++;
      }
    }
    return count;
  }
  
  public static int MEMORY_UTILIZED()
  {
    int count = 0;
    for (int i = 0; i < MEMORY_ALLOCATED.length; i++) {
      if (MEMORY_ALLOCATED[i] == 1) {
        count++;
      }
    }
    return count;
  }
  
  public static void createTraceFile(int id, String name)
  {
    String filename = "trace_" + name + ".txt";
    
    TRACE_FILEs.put(Integer.valueOf(id), filename);
    try
    {
      FileWriter initial = new FileWriter(filename);
      initial.write("PC\tBR\tIR\tTOS\tS[TOS]\t");
      initial.write("EA\tMEM[EA]\tTOS\tS[TOS]\tEA\tMEM[EA])");
      initial.write("\n");
      initial.flush();
    }
    catch (IOException ex) {}
  }
  
}

