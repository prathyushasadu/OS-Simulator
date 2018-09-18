import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;

/*
f. The process manager is event driven. Dependant on the message from CPU
the CPU acts accordingly.
*/
public class PROCESS_MANAGER
{
  public static void process_manager()
  {
    /*
    Starts the Job runs until Ready Queue and Blocked queue are empty
    */
    if ((SYSTEM.BLOCKED_QUEUE.size() != 0) && (SYSTEM.READY_QUEUE.size() == 0)) {
      CLOCK.INCREMENT(20);
    }
    if (SYSTEM.BLOCKED_QUEUE.size() != 0)
    {
      int JOB_ID = (SYSTEM.BLOCKED_QUEUE.element()).intValue();
      PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(JOB_ID));
      if (pcb.GET_TIME() <= CLOCK.GET_TIME())
      {
        SYSTEM.READY_QUEUE.add(Integer.valueOf(JOB_ID));
        SYSTEM.BLOCKED_QUEUE.poll();
      }
    }
    if (SYSTEM.CHECK_MEMORY_SIZE() >= 6) {
      LOADER.LOADER();
    }
    if (SYSTEM.READY_QUEUE.size() != 0)
    {
      int JOB_ID = (SYSTEM.READY_QUEUE.poll()).intValue();
      SYSTEM.CURRENT_JOB = JOB_ID;
      PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(JOB_ID));
      SYSTEM.CURRENT_pcb = pcb;
      SYSTEM.BR = pcb.BR;
      SYSTEM.EA = pcb.EA;
      int PC = pcb.GET_PC();
      
      String TRACE = pcb.GET_TRACE();
      LOAD_STACK(pcb);
      
      CPU.HLT = false;
      SYSTEM.KILL_JOB = false;
      CPU.STATUS = null;
      
      SYSTEM.METER.UPDATE_CPU_SHOTS(JOB_ID);
      String MESSAGE = CPU.CPU(PC, TRACE);
      CONTEXT_SWITCH(MESSAGE);
    }
  }
  
  public static void CONTEXT_SWITCH(String MESSAGE)
  {
    PCB JOB_pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    switch (MESSAGE)
    {
    case "HLT":

      if(!(JOB_pcb.NATURE_OF_TERMINATION == "ABNORMAL")) 
      {
      SYSTEM.NORMAL_JOBS += 1;
      }
      JOB_pcb.EXIT_TIME = CLOCK.GET_TIME();
      OUTPUT.Output_Spooling(JOB_pcb);
      
      EMPTY_STACK();
      break;
    case "TIME EXPIRED": 
      JOB_pcb.SET_PC(Integer.parseInt(SYSTEM.PC, 2));
      JOB_pcb.SET_IR(SYSTEM.IR);
      DUMP_STACK(JOB_pcb);
      
      SYSTEM.READY_QUEUE.add(Integer.valueOf(SYSTEM.CURRENT_JOB));
      break;
    case "ERROR":
      SYSTEM.ABNORMAL_JOBS += 1;
      JOB_pcb.EXIT_TIME = CLOCK.GET_TIME();
      OUTPUT.Output_Spooling(JOB_pcb);
      EMPTY_STACK();
      break;
    case "PAGE FAULT": 
      JOB_pcb.SET_PC(Integer.parseInt(SYSTEM.PC, 2));
      JOB_pcb.SET_IR(SYSTEM.IR);
      DUMP_STACK(JOB_pcb);
      JOB_pcb.EXPECTED_IO_COMPLETION = (CLOCK.GET_TIME() + 20);
      SYSTEM.BLOCKED_QUEUE.add(Integer.valueOf(SYSTEM.CURRENT_JOB));
      break;
    case "SEGMENT FAULT": 
      JOB_pcb.SET_PC(Integer.parseInt(SYSTEM.PC, 2));
      JOB_pcb.SET_IR(SYSTEM.IR);
      
      DUMP_STACK(JOB_pcb);
      
      JOB_pcb.EXPECTED_IO_COMPLETION = (CLOCK.GET_TIME() + 20);
      SYSTEM.BLOCKED_QUEUE.add(Integer.valueOf(SYSTEM.CURRENT_JOB));
      break;
    case "IO": 
      JOB_pcb.SET_PC(Integer.parseInt(SYSTEM.PC, 2));
      JOB_pcb.SET_IR(SYSTEM.IR);
      
      DUMP_STACK(JOB_pcb);
      
      SYSTEM.READY_QUEUE.add(Integer.valueOf(SYSTEM.CURRENT_JOB));
    }
  }
  
  public static void DUMP_STACK(PCB pcb)
  {
    int size = CPU.stack.SIZE();
    while (CPU.stack.SIZE() > 0) {
      pcb.DUMP_STACK(CPU.stack.POP());
    }
  }
  
  public static void LOAD_STACK(PCB pcb)
  {
    ArrayList<String> stack = pcb.GET_STACK();
    try
    {
      for (int i = stack.size() - 1; i >= 0; i--) {
        CPU.stack.PUSH(stack.get(i));
      }
      pcb.STACK.clear();
    }
    catch (Exception ex) {}
  }
  
  public static void EMPTY_STACK()
  {
    while (CPU.stack.SIZE() > 0) {
      CPU.stack.POP();
    }
  }
}

