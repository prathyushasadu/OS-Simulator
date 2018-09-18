import java.io.*;
import java.util.*;
/*
f.Page faults are handled here

Intially Frames are allocated. If the Max Allocated Frames is greater than 6
The allocated frames are replaced using the replacement algorithm

For Replacmeent, Second Chance Algorithm has been used.

If the dirty(modify) bit it set then the page is copied to the disk before replacement.



*/
public class PageFaultHandler {

	public static PCB pcb;
static void PageFaultHandler(int pageno)
  {
    int reference = 1;int dirty = 0;
    
    pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    int disk_page = pcb.GET_PROGRAM_DISK_PAGE(pageno);
    int memory_page = pcb.GET_ALLOCATED_MEMORY();
    if (SYSTEM.MEMORY_FMBV[memory_page] == 1)
    {
      runReplacement(pageno, disk_page, 0);
    }
    else
    {
      DISK_MANAGER.LOAD_TO_MEMORY(disk_page, memory_page);
      SYSTEM.pmt = SYSTEM.PROGRAM_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
      SYSTEM.pmt.ADD_PAGE(pageno, memory_page, reference, dirty);
    }
  }
  
  static void runReplacement(int page, int dis_page, int code)
  {
    ArrayList<ArrayList<Integer>> frames = new ArrayList<ArrayList<Integer>>();
    int lines = 0;
    if (code == 0) {
      SYSTEM.UPDATE_PMT = SYSTEM.PROGRAM_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    } else if (code == 1) {
      SYSTEM.UPDATE_PMT = SYSTEM.INPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    } else if (code == 2) {
      SYSTEM.UPDATE_PMT = SYSTEM.OUTPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    }
    PCB JOB_pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    
    boolean isReplaced = false;
    while (!isReplaced)
    {
      SYSTEM.pmt = SYSTEM.PROGRAM_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
      ArrayList<ArrayList<Integer>> program_pmt = SYSTEM.pmt.SHOW_PMT();
      for (int pointer = 0; pointer < program_pmt.size(); pointer++)
      {
        int reference = ((Integer)((ArrayList)program_pmt.get(pointer)).get(2)).intValue();
        if (reference == 0)
        {
          int dirty = ((Integer)((ArrayList)program_pmt.get(pointer)).get(3)).intValue();
          int memory_frame = ((Integer)((ArrayList)program_pmt.get(pointer)).get(1)).intValue();
          int disk_page = JOB_pcb.GET_PROGRAM_DISK_PAGE(((Integer)((ArrayList)program_pmt.get(pointer)).get(0)).intValue());
          if (dirty == 1)
          {
            DISK_MANAGER.LOAD_TO_DISK(memory_frame, disk_page);
            SYSTEM.pmt.SET_DIRTY(((Integer)((ArrayList)program_pmt.get(pointer)).get(0)).intValue(), 0);
          }
          DISK_MANAGER.LOAD_TO_MEMORY(dis_page, memory_frame);
          SYSTEM.UPDATE_PMT.ADD_PAGE(page, memory_frame, 1, 0);
          SYSTEM.pmt.REMOVE_PAGE(((Integer)((ArrayList)program_pmt.get(pointer)).get(0)).intValue());
          isReplaced = true;
          break;
        }
        if (reference == 1) {
          SYSTEM.pmt.SET_REFERENCE(((Integer)((ArrayList)program_pmt.get(pointer)).get(0)).intValue(), 0);
        }
      }
      SYSTEM.pmt = SYSTEM.INPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
      ArrayList<ArrayList<Integer>> input_pmt = SYSTEM.pmt.SHOW_PMT();
      for (int pointer = 0; pointer < input_pmt.size(); pointer++)
      {
        int reference = ((Integer)((ArrayList)input_pmt.get(pointer)).get(2)).intValue();
        if (reference == 0)
        {
          int dirty = ((Integer)((ArrayList)input_pmt.get(pointer)).get(3)).intValue();
          int memory_frame = ((Integer)((ArrayList)input_pmt.get(pointer)).get(1)).intValue();
          int disk_page = JOB_pcb.GET_INPUT_DISK_PAGE(((Integer)((ArrayList)input_pmt.get(pointer)).get(0)).intValue());
          if (dirty == 1)
          {
            DISK_MANAGER.LOAD_TO_DISK(memory_frame, disk_page);
            SYSTEM.pmt.SET_DIRTY(((Integer)((ArrayList)input_pmt.get(pointer)).get(0)).intValue(), 0);
          }
          DISK_MANAGER.LOAD_TO_MEMORY(dis_page, memory_frame);
          SYSTEM.UPDATE_PMT.ADD_PAGE(page, memory_frame, 1, 0);
          SYSTEM.pmt.REMOVE_PAGE(((Integer)((ArrayList)input_pmt.get(pointer)).get(0)).intValue());
          isReplaced = true;
        }
        else if (reference == 1)
        {
          SYSTEM.pmt.SET_REFERENCE(((Integer)((ArrayList)input_pmt.get(pointer)).get(0)).intValue(), 0);
        }
      }
      try
      {
        SYSTEM.pmt = SYSTEM.OUTPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
        ArrayList<ArrayList<Integer>> output_pmt = SYSTEM.pmt.SHOW_PMT();
        for (int pointer = 0; pointer < output_pmt.size(); pointer++)
        {
          int reference = ((Integer)((ArrayList)output_pmt.get(pointer)).get(2)).intValue();
          if (reference == 0)
          {
            int dirty = ((Integer)((ArrayList)output_pmt.get(pointer)).get(3)).intValue();
            int memory_frame = ((Integer)((ArrayList)output_pmt.get(pointer)).get(1)).intValue();
            int disk_page = JOB_pcb.GET_OUTPUT_DISK_PAGE(((Integer)((ArrayList)output_pmt.get(pointer)).get(0)).intValue());
            if (dirty == 1)
            {
              DISK_MANAGER.LOAD_TO_DISK(memory_frame, disk_page);
              SYSTEM.pmt.SET_DIRTY(((Integer)((ArrayList)output_pmt.get(pointer)).get(0)).intValue(), 0);
            }
            DISK_MANAGER.LOAD_TO_MEMORY(dis_page, memory_frame);
            SYSTEM.UPDATE_PMT.ADD_PAGE(page, memory_frame, 1, 0);
            SYSTEM.pmt.REMOVE_PAGE(((Integer)((ArrayList)output_pmt.get(pointer)).get(0)).intValue());
            isReplaced = true;
            break;
          }
          if (reference == 1) {
            SYSTEM.pmt.SET_REFERENCE(((Integer)((ArrayList)output_pmt.get(pointer)).get(0)).intValue(), 0);
          }
        }
      }
      catch (Exception e) {}
    }
  }
}
