import java.io.*;
import java.util.*;

/*
 *  f. ZeroAddressInstruction class is used to execute 
 *  the ZeroAddress Instructions.
 *  Object to ERROR_HANDLER is created which is used to address
 *  the errors in the execution.
 *  In this class stack push , pop and memory operations were done.
 *  System CLOCK is also updated 
 *   as given in the specification.
 *  RD and WR operation are 
 *  used to get the system input and write output
 */

public class ZeroAddressInstruction {

	public static ERROR_HANDLER ERROR = new ERROR_HANDLER();

public static void ZERO_ADDRESS_EXECUTION(String OPCODE)
{
	String one;
	String two;
	String result;
	switch (OPCODE)
	{
		case "00000": 
			break;
		case "00001": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.OR(one, two);
			CPU.stack.PUSH(result);
			break;
		case "00010": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.AND(one, two);
			CPU.stack.PUSH(result);
			break;
		case "00011": 
			one = CPU.stack.POP();
			result = OPERATIONS.NOT(one);
			CPU.stack.PUSH(result);
			break;
		case "00100": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.XOR(one, two);
			CPU.stack.PUSH(result);
			break;
		case "00101": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.ADD(one, two);
			CPU.stack.PUSH(result);
			break;
		case "00110": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.SUB(two, one);
			CPU.stack.PUSH(result);
			break;
		case "00111": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.MUL(one, two);
			CPU.stack.PUSH(result);
			break;
		case "01000": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.DIV(two, one);
			CPU.stack.PUSH(result);
			break;
		case "01001": 
			one = CPU.stack.POP();
			two = CPU.stack.POP();
			result = OPERATIONS.MOD(two, one);
			CPU.stack.PUSH(result);
			break;
		case "01010": 
			one = CPU.stack.POP();
			result = OPERATIONS.SL(one);
			CPU.stack.PUSH(result);
			break;
		case "01011": 
			one = CPU.stack.POP();
			result = OPERATIONS.SR(one);
			CPU.stack.PUSH(result);
			break;
		case "01100": 
			one = CPU.stack.POP();
			two = CPU.stack.PEEK();
			CPU.stack.PUSH(one);
			result = OPERATIONS.CPG(two, one);
			CPU.stack.PUSH(result);
			break;
		case "01101": 
			one = CPU.stack.POP();
			two = CPU.stack.PEEK();
			CPU.stack.PUSH(one);
			result = OPERATIONS.CPL(two, one);
			CPU.stack.PUSH(result);
			break;
		case "01110": 
			one = CPU.stack.POP();
			two = CPU.stack.PEEK();
			CPU.stack.PUSH(one);
			result = OPERATIONS.CPE(two, one);
			CPU.stack.PUSH(result);
			break;
		case "10011": 
			inputRead();
			break;
		case "10100": 
			outputWrite();
			break;
		case "10101": 
			SYSTEM.PC = CPU.stack.POP();
			break;
		case "11000": 
			CPU.HLT = true;

			break;
		default: 
			ERROR.ERROR_HANDLER(10, SYSTEM.CURRENT_JOB);
	}
}

 public static void inputRead()
  {
    boolean segment_fault = false;
    MEMORY M = new MEMORY();

    PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    pcb.inputs_read += 1;
    SYSTEM.METER.UPDATE_IO_REQUESTS(SYSTEM.CURRENT_JOB);
    int index = pcb.GET_INPUT_INDEX();
    PMT IN_PMT = SYSTEM.INPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    if (IN_PMT == null)
    {
      SegmentFaultHandler.SegmentFaultHandler_func(1);
      segment_fault = true;
      IN_PMT = SYSTEM.INPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    }
    if (index > 7)
    {
      pcb.INPUT_INDEX = 0;
      index = 0;
      pcb.input_th_page += 1;
    }
    int page = pcb.GET_INPUT_PAGES(pcb.input_th_page);
    int memory_page = IN_PMT.GET_FRAME(page);
    if(page == -1 && memory_page == -1)
    {
	    ERROR.ERROR_HANDLER(4, SYSTEM.CURRENT_JOB);
	    CPU.STATUS = "ERROR";
    }

    if (memory_page == -1)
    {
      pcb.PAGE_FAULTS += 1;
      int disk_page = pcb.GET_INPUT_DISK_PAGE(page);
      memory_page = pcb.GET_ALLOCATED_MEMORY();
      if (SYSTEM.MEMORY_FMBV[memory_page] == 1)
      {
        PageFaultHandler.runReplacement(page, disk_page, 1);
        memory_page = IN_PMT.GET_FRAME(page);
      }
    }

    String data = SYSTEM.MEMORY[memory_page][index];

    CPU.stack.PUSH(data);
    pcb.IO_TIME += 20;
    if (segment_fault == true)
    {
      CPU.STATUS = "SEGMENT FAULT";
      pcb.SEGMENT_FAULT_TIME += 20;
      return;
    }
    CPU.STATUS = "IO";
  }
public static void outputWrite()
  {
    boolean segment_fault = false;
    
    MEMORY M = new MEMORY();
    
    String data = CPU.stack.POP();
    PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    pcb.outputs_written += 1;
    SYSTEM.METER.UPDATE_IO_REQUESTS(SYSTEM.CURRENT_JOB);
    int index = pcb.GET_OUTPUT_INDEX();
    PMT OUT_PMT = SYSTEM.OUTPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    if (OUT_PMT == null)
    {
      SegmentFaultHandler.SegmentFaultHandler_func(2);
      segment_fault = true;
      OUT_PMT = SYSTEM.OUTPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
    }
    if (index > 7)
    {
      pcb.OUTPUT_INDEX = 0;
      index = 0;
      pcb.output_th_page += 1;
    }
    int page = pcb.GET_OUTPUT_PAGES(pcb.output_th_page);
    
    int memory_page = OUT_PMT.GET_FRAME(page);
    if (memory_page == -1)
    {
      pcb.PAGE_FAULTS += 1;
      int disk_page = pcb.GET_OUTPUT_DISK_PAGE(page);
      memory_page = pcb.GET_ALLOCATED_MEMORY();
      if (SYSTEM.MEMORY_FMBV[memory_page] == 1)
      {
        PageFaultHandler.runReplacement(page, disk_page, 2);
        memory_page = OUT_PMT.GET_FRAME(page);
      }
    }
    SYSTEM.MEMORY[memory_page][index] = data;
    
    int disk_page = pcb.GET_OUTPUT_DISK_PAGE(page);
    SYSTEM.DISK[disk_page][index] = data;
    
    pcb.IO_TIME += 20;
    if (segment_fault == true)
    {
      CPU.STATUS = "SEGMENT FAULT";
      pcb.SEGMENT_FAULT_TIME += 20;
      return;
    }
    CPU.STATUS = "IO";
  }


}
  
