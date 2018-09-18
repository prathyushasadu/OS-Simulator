import java.util.Map;
import java.util.Queue;

/*
 * f. LOADER class takes the input
 * file from user and loads it into memory
 * Object for EEROR_HANDLER is
 *  created to address the errors on the Loadfile.
 */
public class LOADER
{
  public static int words = 8;
  public static int JOB_ID = -1;
  public static int initial_pc;
  public static int initial_pc_page;
  public static int disk_page;
  public static PCB pcb;
  
  public static void LOADER()
  {
    try
    {
      JOB_ID = (SYSTEM.JOB_QUEUE.element()).intValue();
      LOAD();
    }
    catch (Exception e)
    {
      Spooling.Input_Spooling();
      if (SYSTEM.JOB_QUEUE.size() == 0)
      {
        JOB_ID = -1;
      }
      else
      {
        JOB_ID = (SYSTEM.JOB_QUEUE.element()).intValue();
        LOAD();
      }
    }
  }
  
  public static void LOAD()
  {
    int reference = 0;int dirty = 0;
    
    pcb = SYSTEM.pcbs.get(Integer.valueOf(JOB_ID));
    
    int size = pcb.GET_JOB_WORDS();
    int pages;
    if (size % words == 0) {
      pages = size / words + 2;
    } else {
      pages = size / words + 3;
    }
    if (pages > 6) {
      pages = 6;
    }
    pcb.SET_NUMBER_OF_FRAMES(pages);
    for (int i = 0; i < pages; i++)
    {
      int frame = SYSTEM.ALLOCATE_MEMORY();
      pcb.ADD_ALLOCATED_MEMORY(frame);
    }
    initial_pc = pcb.GET_PC();
    initial_pc_page = initial_pc / words;
    
    disk_page = pcb.GET_PROGRAM_DISK_PAGE(initial_pc_page);
    int page = pcb.GET_ALLOCATED_MEMORY();
    for (int i = 0; i < words; i++) {
      SYSTEM.MEMORY[page][i] = SYSTEM.DISK[disk_page][i];
    }
    SYSTEM.MEMORY_FMBV[page] = 1;
    
    SYSTEM.createProgPMT(JOB_ID);
    SYSTEM.pmt = SYSTEM.PROGRAM_PMTs.get(Integer.valueOf(JOB_ID));
    
    SYSTEM.pmt.ADD_PAGE(initial_pc_page, page, reference, dirty);
    
    SYSTEM.JOB_QUEUE.poll();
    SYSTEM.READY_QUEUE.add(Integer.valueOf(JOB_ID));
    SYSTEM.METER.ADD_CPU_SHOTS(JOB_ID, 0);
    SYSTEM.METER.ADD_IO_REQUESTS(JOB_ID, 0);
    pcb.ENTRY_TIME = CLOCK.GET_TIME();
  }
}

