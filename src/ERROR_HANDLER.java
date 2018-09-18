import java.util.Map;
/*
 * f. ERROR_HANDLER class is used to
 * handle all the possible errors
 * in the project.
 *    outputWrite Method is used to
 *    write the output in the specified
 *    format in the output file.
 *    NATURE is used to tell the
 *    system exit is Natural or Abnormal.
 */

public class ERROR_HANDLER
{
  public void ERROR_HANDLER(int number, int JOB)
  {
    PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(JOB));
    switch (number)
    {
    case 1: 
      pcb.ERROR = "RUNTIME ERROR: STACK OVERFLOW";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 2: 
      pcb.ERROR = "RUNTIME ERROR: STACK UNDERFLOW";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 3: 
      pcb.ERROR = "RUNTIME ERROR: DATA OUT OF RANGE. DATA SHOULD BE BETWEEN -32768 AND 32767";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 4: 
      pcb.ERROR = "RUNTIME ERROR: MEMEORY ADDRESS OUT OF RANGE";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 5: 
      pcb.ERROR = "INPUT ERROR: INVALID INPUT";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 6: 
      pcb.ERROR = "RUNTIME ERROR: INVALID OPERAND";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 7: 
      pcb.ERROR = "LOADER ERROR: INVALID LOADER FORMAT";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 8: 
      pcb.ERROR = "RUNTIME ERROR: DIVIDE BY ZERO";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 9: 
      pcb.WARNING = "RUNTIME WARNING : BAD TRACE FLAG";
      break;
    case 10: 
      pcb.ERROR = "RUNTIME ERROR: INVALID OPCODE";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.KILL_JOB = true;
      break;
    case 11: 
      pcb.ERROR = "RUNTIME ERROR: SUSPECTED INFINITE LOOP";
      SYSTEM.INFINTE_JOBS = SYSTEM.INFINTE_JOBS + JOB + "\t";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.ABNORMAL_JOBS += 1;
      SYSTEM.KILL_JOB = true;
      break;
    case 12: 
      pcb.ERROR = "SPOOLER ERROR: MISSING JOB LABEL";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.ABNORMAL_JOBS += 1;
      OUTPUT.Output_Spooling(pcb);
      break;
    case 13: 
      pcb.ERROR = "SPOOLER ERROR: MULTIPLE INPUT SEGMENTS";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.ABNORMAL_JOBS += 1;
      OUTPUT.Output_Spooling(pcb);
      break;
    case 14: 
      pcb.ERROR = "SPOOLER ERROR: MISSING INPUT LABEL";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.ABNORMAL_JOBS += 1;
      OUTPUT.Output_Spooling(pcb);
      break;
    case 15: 
      pcb.ERROR = "SPOOLER ERROR: MISSING FIN LABEL";
      SYSTEM.ABNORMAL_JOBS += 1;
      OUTPUT.Output_Spooling(pcb);
      break;
    case 16: 
      pcb.ERROR = "SPOOLER ERROR: INVALID LOADER FORMAT";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.ABNORMAL_JOBS += 1;
      OUTPUT.Output_Spooling(pcb);
      break;
    case 17: 
      pcb.WARNING = "SPOOLER WARNING: MISMATCH OF NUMBER OF INPUTS";
      break;
    case 18: 
      pcb.ERROR = "SPOOLER ERROR: MULTIPLE FIN LABELS";
      pcb.NATURE_OF_TERMINATION = "ABNORMAL";
      SYSTEM.ABNORMAL_JOBS += 1;
      OUTPUT.Output_Spooling(pcb);
    }
  }
}

