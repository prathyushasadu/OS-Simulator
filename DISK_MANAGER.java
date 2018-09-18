/*
 *  f. The Disk manager class is used to implement the disk
 *     methods.
 *     It hepls to load disk pages to memory and job pages to Disk.
 *     The GET_DISK_PAGE retuen the allocated disk page.
 */

public class DISK_MANAGER
{
  public static int DISK_VACANCY()
  {
    int length = SYSTEM.DISK_FMBV.length;
    int count = 0;
    for (int i = 0; i < length; i++) {
      if (SYSTEM.DISK_FMBV[i] == 0) {
        count++;
      }
    }
    return count;
  }
  
  public static int GET_DISK_PAGE()
  {
    int page = -1;
    for (int i = 0; i < SYSTEM.DISK_FMBV.length; i++) {
      if (SYSTEM.DISK_FMBV[i] == 0)
      {
        page = i;
        break;
      }
    }
    return page;
  }
  
  public static void LOAD_TO_MEMORY(int page, int memory_frame)
  {
	     PCB pcb = SYSTEM.pcbs.get(SYSTEM.CURRENT_JOB);
	    pcb.ADD_PAGE_MAP(page,memory_frame);
    int words = 8;
    for (int i = 0; i < words; i++) {
      SYSTEM.MEMORY[memory_frame][i] = SYSTEM.DISK[page][i];
    }
    CLOCK.INCREMENT(20);
    SYSTEM.MEMORY_FMBV[memory_frame] = 1;
  }
  
  public static void LOAD_TO_DISK(int memory_frame, int disk_page)
  {
    int words = 8;
    for (int i = 0; i < words; i++) {
      SYSTEM.DISK[disk_page][i] = SYSTEM.MEMORY[memory_frame][i];
    }
  }
}

