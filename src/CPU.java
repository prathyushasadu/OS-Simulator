import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
/*
 * f. CPU class is used for executing instructions based
 *      on the instruction type.InstructionType, get_Value, converttoHex,
 *    traceFile methods were used to
 *    get the instruction type, to get the instruction value,
 *    for converting to Hex and to write to trace flag respectively.
 *    Objects to ZeroAddressInstruction,
 *    OneAddressInstruction and ERROR_HANDLER are created.
 */

public class CPU
{
  public static Stack stack = new Stack(7);
  public static boolean HLT = false;
  public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  public static int Time_Quantum = 20;
  public static String STATUS;
  public static FileWriter print;
  public static String PRINT_PC;
  public static String PRINT_BR;
  public static String PRINT_IR;
  public static String BEFORE_TOS;
  public static String BEFORE_TOS_DATA;
  public static String BEFORE_EA;
  public static String BEFORE_EA_DATA;
  public static String AFTER_TOS;
  public static String AFTER_TOS_DATA;
  public static String AFTER_EA;
  public static String AFTER_EA_DATA;
  
  public static String CPU(int X, String Y)
  {
    int Timer = 0;
    int inc = 1;
    SYSTEM.PC = CONVERSIONS.DECIMAL_BINARY(X);
    while (((!HLT) && (!SYSTEM.KILL_JOB)))
    {
      SYSTEM.PC = OPERATIONS.PADDING(SYSTEM.PC, 8);
      SYSTEM.IR = MEMORY.MEMORY("READ", SYSTEM.PC, null);
      if(SYSTEM.IR == "" || SYSTEM.IR == null)
      {
      STATUS = "ERROR";
      break;
	  
      }
      int int_PC = Integer.parseInt(SYSTEM.PC, 2);
      int_PC++;
      SYSTEM.PC = CONVERSIONS.DECIMAL_BINARY(int_PC);
      SYSTEM.PC = OPERATIONS.PADDING(SYSTEM.PC, 8);
      char TYPE = SYSTEM.IR.charAt(0);
      
      PRINT_PC = CONVERSIONS.BINARY_TO_HEX(SYSTEM.PC);
      PRINT_BR = SYSTEM.BR;
      PRINT_IR = CONVERSIONS.BINARY_TO_HEX(SYSTEM.IR);
      BEFORE_TOS = CONVERSIONS.BINARY_TO_HEX(CALCULATE_TOS());
      BEFORE_TOS_DATA = CONVERSIONS.BINARY_TO_HEX(stack.PEEK());
      BEFORE_EA = CONVERSIONS.BINARY_TO_HEX(SYSTEM.EA);
      BEFORE_EA_DATA = SYSTEM.EA;
     if(SYSTEM.EA != null)
      {
       String s1 = MEMORY.MEMORY("READ",SYSTEM.EA,null);
       BEFORE_EA_DATA = CONVERSIONS.BINARY_TO_HEX(s1);
      }
      String UNUSED;
      String OPCODE;
      switch (TYPE)
      {
      case '0': 
        String Short_one = SYSTEM.IR.substring(0, 8);
        String Short_two = SYSTEM.IR.substring(8);
        
        UNUSED = Short_one.substring(1, 3);
        OPCODE = Short_one.substring(3);
        CLOCK.INCREMENT(1);
        Timer += 1;
        SYSTEM.CURRENT_pcb.EXECUTION_TIME += 1;
        ZeroAddressInstruction.ZERO_ADDRESS_EXECUTION(OPCODE);
        if (!"10101".equals(OPCODE))
        {
          UNUSED = Short_two.substring(1, 3);
          OPCODE = Short_two.substring(3);
          CLOCK.INCREMENT(1);
          Timer += 1;
          SYSTEM.CURRENT_pcb.EXECUTION_TIME += 1;
          ZeroAddressInstruction.ZERO_ADDRESS_EXECUTION(OPCODE);
	  SYSTEM.EA = null;
        }
        break;
      case '1': 
        OPCODE = SYSTEM.IR.substring(1, 6);
        String INDEX = SYSTEM.IR.substring(6, 7);
        UNUSED = SYSTEM.IR.substring(7, 9);
        String DADDR = SYSTEM.IR.substring(9);
        switch (INDEX)
        {
        case "0": 
          SYSTEM.EA = DADDR;
          break;
        case "1": 
          String temp = stack.PEEK();
          int int_temp = Integer.parseInt(temp, 2);
          int int_DADDR = Integer.parseInt(DADDR, 2);
          int int_EA = int_temp + int_DADDR;
          SYSTEM.EA = CONVERSIONS.DECIMAL_BINARY(int_EA);
        }
        CLOCK.INCREMENT(4);
        Timer += 4;
        SYSTEM.CURRENT_pcb.EXECUTION_TIME += 4;
        OneAddressInstruction.ONE_ADDRESS_EXECUTION(OPCODE);
      }
      AFTER_TOS = CONVERSIONS.BINARY_TO_HEX(CALCULATE_TOS());
      AFTER_TOS_DATA = CONVERSIONS.BINARY_TO_HEX(stack.PEEK());
      AFTER_EA = CONVERSIONS.BINARY_TO_HEX(SYSTEM.EA);
      AFTER_EA_DATA = SYSTEM.EA;
      if(SYSTEM.EA != null)
      {
     	 AFTER_EA_DATA =CONVERSIONS.BINARY_TO_HEX(MEMORY.MEMORY("READ",SYSTEM.EA, null));
      }
      switch (Y)
      {
      case "1": 
        PRINT_TRACE();
        break;
      case "0": 
        break;
      default: 
        ERROR.ERROR_HANDLER(9, SYSTEM.CURRENT_JOB);
      }
      if((SYSTEM.CURRENT_pcb.EXECUTION_TIME+SYSTEM.CURRENT_pcb.PAGE_FAULT_TIME + SYSTEM.CURRENT_pcb.IO_TIME + SYSTEM.CURRENT_pcb.SEGMENT_FAULT_TIME) > 4000)
      {
	ERROR.ERROR_HANDLER(11, SYSTEM.CURRENT_JOB);
	STATUS = "ERROR";
	break;
      }
      if (HLT == true)
      {
        STATUS = "HLT";
      }
      else if (Timer >= Time_Quantum)
      {
        STATUS = "TIME EXPIRED";
      }
      else if (SYSTEM.KILL_JOB == true)
      {
        SYSTEM.KILL_JOB = false;
        
        STATUS = "ERROR";
      }
      else if (STATUS != null)
      {
        break;
      }
    }
    return STATUS;
  }
  
  
  public static String CALCULATE_TOS()
  {
    SYSTEM.TOS = CONVERSIONS.DECIMAL_BINARY(stack.SIZE());
    SYSTEM.TOS = OPERATIONS.PADDING(SYSTEM.TOS, 3);
    if (SYSTEM.TOS.length() > 3) {
      ERROR.ERROR_HANDLER(1, SYSTEM.CURRENT_JOB);
    }
    return SYSTEM.TOS;
  }
  
  public static void PRINT_TRACE()
  {
    String file = SYSTEM.TRACE_FILEs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    try
    {
      print = new FileWriter(file, true);
      if (BEFORE_EA_DATA == null) {
        BEFORE_EA_DATA = "EMPTY";
      }
      if (BEFORE_TOS_DATA == null) {
        BEFORE_TOS_DATA = "EMPTY";
      }
      if (AFTER_TOS_DATA == null) {
        AFTER_TOS_DATA = "EMPTY";
      }
      if (AFTER_EA_DATA == null) {
        AFTER_EA_DATA = "EMPTY";
      }
      print.append(PRINT_PC + "\t" + PRINT_BR + "\t" + PRINT_IR + "\t" + BEFORE_TOS + "\t" + BEFORE_TOS_DATA + "\t");
      print.append(BEFORE_EA + "\t" + BEFORE_EA_DATA + "\t" + AFTER_TOS + "\t" + AFTER_TOS_DATA + "\t" + AFTER_EA + "\t" + AFTER_EA_DATA + "\t");
      print.append("\n");
      print.flush();
    }
    catch (IOException ex) {}
  }
}

