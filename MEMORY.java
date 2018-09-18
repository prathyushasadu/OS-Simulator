import java.util.ArrayList;
import java.util.Map;
 /*
    f. The MEMORY CLASS is used for Reading and
    Writing the instructions from and to the MEMORY.
       Object for ERROR_HANDLER was created to
       wirte the memory errors in the output.
       MEMORY returns the
       instruction value of the requested address.
       Pages from disk are written into MEMORY.

    g. MEMORY class meets the
    specification that it can contain upto 256 words
    */

public class MEMORY
{
  public static PCB pcb;
  public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  
  public static String MEMORY(String X, String Y, String Z)
  {
    int words = 8;
    

    int int_Y = Integer.parseInt(Y, 2);
    boolean page_fault = false;
    
    int displacement = int_Y;
    int virtual_pagenumber = displacement / words;
    
    pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    if (int_Y > pcb.job_words || int_Y == pcb.job_words)
    {
      ERROR.ERROR_HANDLER(4, SYSTEM.CURRENT_JOB);
      return "";
    }
    SYSTEM.pmt = SYSTEM.PROGRAM_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    
    int frame_number = SYSTEM.pmt.GET_FRAME(virtual_pagenumber);
    if (frame_number == -1)
    {
      pcb.PAGE_FAULTS += 1;
      PageFaultHandler.PageFaultHandler(virtual_pagenumber);
      pcb.PAGE_FAULT_TIME += 20;
      page_fault = true;
      SYSTEM.pmt = SYSTEM.PROGRAM_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
      frame_number = SYSTEM.pmt.GET_FRAME(virtual_pagenumber);

    }
    int memory_offset = displacement % words;
    switch (X)
    {
    case "READ": 
      SYSTEM.pmt.SET_REFERENCE(virtual_pagenumber, 1);
      Z = SYSTEM.MEMORY[frame_number][memory_offset];
      break;
    case "WRITE": 
      SYSTEM.pmt.SET_REFERENCE(virtual_pagenumber, 1);
      SYSTEM.pmt.SET_DIRTY(virtual_pagenumber, 1);
      SYSTEM.MEMORY[frame_number][memory_offset] = Z;
      Z = "";
    }
    if (page_fault == true) {
      CPU.STATUS = "PAGE FAULT";
    }
    return Z;
  }
  
  }

