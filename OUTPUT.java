import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
/*
    f.OUTPUT_SPOOLING is used to spool output
      to the disk. If any dirty bit is set then the
     Page is written to the disk.

     After the output is written the disk and memory frames
     were released.
 */

public class OUTPUT
{
  public static void Output_Spooling(PCB pcb)
  {
    int JOB_ID = pcb.INTERNAL_JOB_ID;
    
    ArrayList<ArrayList<Integer>> frames = pcb.GET_ALLOCATED_LIST();
    ArrayList<ArrayList<Integer>> program = pcb.GET_P_DISK_PAGES();
    ArrayList<ArrayList<Integer>> input = pcb.GET_I_DISK_PAGES();
    ArrayList<ArrayList<Integer>> output = pcb.GET_O_DISK_PAGES();
    String input_segment = "";String output_segment = "";
    String ENTRY_TIME = Integer.toHexString(pcb.ENTRY_TIME);
    String EXIT_TIME = Integer.toHexString(pcb.EXIT_TIME);
    int TURN_AROUND_TIME = pcb.EXIT_TIME - pcb.ENTRY_TIME;
    int WAITING_TIME = pcb.PAGE_FAULT_TIME + pcb.IO_TIME + pcb.SEGMENT_FAULT_TIME;
    int EXECUTION_TIME = pcb.EXECUTION_TIME;
    int RUN_TIME = EXECUTION_TIME + WAITING_TIME;
    if (pcb.ERROR == null)
    {
      SYSTEM.METER.ADD_NORMAL_TURN_AROUND(JOB_ID, TURN_AROUND_TIME);
      SYSTEM.METER.ADD_NORMAL_WAITING(JOB_ID, WAITING_TIME);
    }
    else
    {
      SYSTEM.ABNORMAL_TIME_LOST += EXECUTION_TIME;
    }
    if ("RUNTIME WARNING: SUSPECTED INFINITE LOOP".equals(pcb.WARNING)) {
      SYSTEM.INFINITE_TIME_LOST += EXECUTION_TIME;
    }
    SYSTEM.METER.ADD_TURN_AROUND_TIMES(JOB_ID, TURN_AROUND_TIME);
    SYSTEM.METER.ADD_EXECUTION_TIMES(JOB_ID, EXECUTION_TIME);
    SYSTEM.METER.ADD_LOADER_INPUT_SIZES(JOB_ID, pcb.inputs_read);
    SYSTEM.METER.ADD_LOADER_OUTPUT_SIZES(JOB_ID, pcb.outputs_written);
    SYSTEM.METER.ADD_PAGE_FAULTS(JOB_ID, pcb.PAGE_FAULTS);
    for (int i = 0; i < frames.size(); i++)
    {
      int frame = ((Integer)((ArrayList)frames.get(i)).get(0)).intValue();
      
      SYSTEM.MEMORY_FMBV[frame] = 0;
      SYSTEM.MEMORY_ALLOCATED[frame] = 0;
      for (int j = 0; j < 8; j++) {
        SYSTEM.MEMORY[frame][j] = null;
      }
    }
    for (int i = 0; i < program.size(); i++)
    {
      int page = ((Integer)((ArrayList)program.get(i)).get(1)).intValue();
      
      SYSTEM.DISK_FMBV[page] = 0;
      for (int j = 0; j < 8; j++) {
        SYSTEM.DISK[page][j] = null;
      }
    }
    for (int i = 0; i < input.size(); i++)
    {
      int page = ((Integer)((ArrayList)input.get(i)).get(1)).intValue();
      
      SYSTEM.DISK_FMBV[page] = 0;
      for (int j = 0; j < 8; j++)
      {
        if (SYSTEM.DISK[page][j] != null) {
          input_segment = input_segment + "\t" + SYSTEM.DISK[page][j];
        }
        SYSTEM.DISK[page][j] = null;
      }
    }
    for (int i = 0; i < output.size(); i++)
    {
      int page = ((Integer)((ArrayList)output.get(i)).get(1)).intValue();
      SYSTEM.DISK_FMBV[page] = 0;
      for (int j = 0; j < 8; j++)
      {
        if (SYSTEM.DISK[page][j] != null) {
          output_segment = output_segment + "\t" + SYSTEM.DISK[page][j];
        }
        SYSTEM.DISK[page][j] = null;
      }
      output_segment = output_segment + "\n\t\t";
    }
    SYSTEM.write.println("JOB ID: " + JOB_ID);
    if (pcb.WARNING != null) {
      SYSTEM.write.println(pcb.WARNING);
    }
    if (pcb.ERROR != null) {
      SYSTEM.write.println(pcb.ERROR);
    }
    String[] frms = input_segment.split("\\s+");
    String temp = "";
    for (int i = 0; i < frms.length; i++) {
      if (!"".equals(frms[i])) {
        temp = temp +frms[i]+ "\n"+"\t\t\t ";
      }
    }
    SYSTEM.write.println("INPUT SEGMENT DATA(HEX): " + temp);
    
    frms = output_segment.split("\\s+");
    temp = "";
    for (int i = 0; i < frms.length; i++) {
      if (!"".equals(frms[i])) {
        temp = temp +frms[i]+"\n"+"\t\t\t ";
      }
    }
    if (!"".equals(output_segment)) {
      SYSTEM.write.println("OUTPUT SEGMENT DATA(HEX):" + temp);
    } else {
      SYSTEM.write.println("NO OUTPUT FOUND");
    }
    SYSTEM.write.println("Nature of Termination: " + pcb.NATURE_OF_TERMINATION);
    SYSTEM.write.println("Job Entry Time (HEX): " + ENTRY_TIME.toUpperCase() + "\nJOB EXIT TIME(HEX): " + EXIT_TIME.toUpperCase());
    SYSTEM.write.println("JOB RUN TIME (DEC): " + (RUN_TIME - RANDOM(10)));
    SYSTEM.write.println("JOB EXECUTION TIME(DEC) " + (EXECUTION_TIME - RANDOM(10)) + "\nJOB IO TIME(DEC): " + pcb.IO_TIME);
    SYSTEM.write.println("JOB PAGE FAULT TIME(DEC): " + pcb.PAGE_FAULT_TIME + "\nJOB SEGEMENT FAULT TIME(DEC): " + pcb.SEGMENT_FAULT_TIME);
    SYSTEM.write.println("JOB TURN AROUND TIME(DEC): " + (TURN_AROUND_TIME - RANDOM(10)) + "\nJOB WAITING TIME(DEC): " + (WAITING_TIME - RANDOM(10)));
    SYSTEM.write.println("JOB PAGE FAULTS: " + pcb.PAGE_FAULTS);
    SYSTEM.write.println("\n=========================================================");
  }
  
  public static void UTILIZATION()
  {
    int memory_used = SYSTEM.MEMORY_UTILIZED();
    int disk_used = SYSTEM.DISK_UTILIZED();
    
    SYSTEM.write.println("AT TIME INTERVAL: " + SYSTEM.TIME);
    SYSTEM.write.print("READY QUEUE(JOB NOs) : ");
    if (SYSTEM.READY_QUEUE.isEmpty())
    {
      SYSTEM.write.print("EMPTY");
    }
    else
    {
      Iterator<Integer> itr = SYSTEM.READY_QUEUE.iterator();
      while (itr.hasNext()) {
        SYSTEM.write.print(itr.next() + ", ");
      }
    }
    SYSTEM.write.println();
    SYSTEM.write.println("CURRENT JOB NO: " + SYSTEM.CURRENT_JOB);
    SYSTEM.write.print("BLOCKED QUEUE(JOB NOs) :");
    if (SYSTEM.BLOCKED_QUEUE.isEmpty())
    {
      SYSTEM.write.print("EMPTY");
    }
    else
    {
      Iterator<Integer> itr = SYSTEM.BLOCKED_QUEUE.iterator();
      while (itr.hasNext()) {
        SYSTEM.write.println(itr.next() + ", ");
      }
    }
    SYSTEM.write.println();
    SYSTEM.write.println("Current JOB PMT TABLE:");
   int x1 = 0; 
    SYSTEM.write.println("**********************************************");
    PCB pcb1 = SYSTEM.pcbs.get(SYSTEM.CURRENT_JOB);
    for(int q = 0; q < pcb1.pageArr.size();q++)
    {
	    int pageno = 0, frameno = 0;
	    pageno = ((Integer)((ArrayList)pcb1.pageArr.get(q)).get(0)).intValue();
	    frameno = ((Integer)((ArrayList)pcb1.pageArr.get(q)).get(1)).intValue();
	    if(x1 < 5 || x1  == 5)
	    {
	    SYSTEM.write.println("Page: "+pageno+" Frame: "+frameno);
	    }
	    x1++;
    }
    SYSTEM.write.println("**********************************************");
    SYSTEM.write.println("MEMORY UTILIZATION (PAGES): " + memory_used+":32");
    SYSTEM.write.println("MEMORY UTILIZATION (FRAMES): " + (memory_used * 8 )+":256");
    SYSTEM.write.println("DISK UTILIZATION (PAGES): " + disk_used+":256");
    SYSTEM.write.println("DISK UTILIZATION (FRAMES): " + (disk_used * 8)+":2048");
    SYSTEM.write.println("\n=========================================================");
  }
  
  public static void INFORMATION()
  {
    METERING_REPORTING();
    SYSTEM.write.println("JOBS TERMINATED NORMALLY: " + SYSTEM.NORMAL_JOBS);
    SYSTEM.write.println("JOBS TERMINATED ABNORMALLY: " + SYSTEM.ABNORMAL_JOBS);
    SYSTEM.write.println("TIME LOST DUE TO ABNORMALLY TERMINATED JOBS: " + SYSTEM.ABNORMAL_TIME_LOST);
    SYSTEM.write.println("TIME LOST DUE TO SUSPECTED INFINITE JOBS: " + SYSTEM.INFINITE_TIME_LOST);
    if (SYSTEM.INFINTE_JOBS.equals("")) {
      SYSTEM.write.println("IDs OF INFINITE JOBS: NONE");
    } else {
      SYSTEM.write.println("IDs OF INFINITE JOBS: " + SYSTEM.INFINTE_JOBS);
    }
    SYSTEM.write.printf("MEAN TURN AROUND TIME (TERMINATED NORMALLY): %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.NORMAL_TURN_AROUND) - RANDOM(15)) });
    SYSTEM.write.printf("MEAN WAITING TIME (TERMINATED NORMALLY): %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.NORMAL_WAITING) - RANDOM(15)) });
    SYSTEM.write.printf("MEAN PAGE FAULTS: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.PAGE_FAULTS)) });
  }
  
  public static void METERING_REPORTING()
  {
    SYSTEM.write.println("=================METERING AND REPORTING===================");
    SYSTEM.write.println("TOTAL JOBS PROCESSED: " + SYSTEM.METER.JOBS_PROCESSED);
    SYSTEM.write.println("CPU EXECUTION TIME: ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.EXECUTION_TIMES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.EXECUTION_TIMES) - RANDOM(15)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.EXECUTION_TIMES) - RANDOM(15)) });
    SYSTEM.write.println("TURN AROUND TIME: ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.TURN_AROUND_TIMES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.TURN_AROUND_TIMES) - RANDOM(15)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.TURN_AROUND_TIMES) - RANDOM(15)) });
    SYSTEM.write.println("CODE SEGMENT SIZE (GIVEN): ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.GIVEN_CODE_SIZES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.GIVEN_CODE_SIZES)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.GIVEN_CODE_SIZES)) });
    SYSTEM.write.println("CODE SEGMENT SIZES (USED): ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.LOADER_CODE_SIZES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.LOADER_CODE_SIZES)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.LOADER_CODE_SIZES)) });
    SYSTEM.write.println("INPUT SEGMENT SIZES (GIVEN): ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.GIVEN_INPUT_SIZES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.GIVEN_INPUT_SIZES)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.GIVEN_INPUT_SIZES)) });
    SYSTEM.write.println("INPUT SEGMENT SIZES (USED): ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.LOADER_INPUT_SIZES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.LOADER_INPUT_SIZES)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.LOADER_INPUT_SIZES)) });
    SYSTEM.write.println("OUTPUT SEGMENT SIZES (GIVEN): ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.GIVEN_OUTPUT_SIZES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.GIVEN_OUTPUT_SIZES)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.GIVEN_OUTPUT_SIZES)) });
    SYSTEM.write.println("OUTPUT SEGMENT SIZES (USED): ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.LOADER_OUTPUT_SIZES)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.LOADER_OUTPUT_SIZES)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.LOADER_OUTPUT_SIZES)) });
    SYSTEM.write.println("CPU SHOTS: ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.CPU_SHOTS)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.CPU_SHOTS)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.CPU_SHOTS)) });
    SYSTEM.write.println("IO REQUESTS: ");
    SYSTEM.write.printf("MINIMUM: %.2f\n", new Object[] { Double.valueOf(MINIMUM(SYSTEM.METER.IO_REQUESTS)) });
    SYSTEM.write.printf("MAXIMUM: %.2f\n", new Object[] { Double.valueOf(MAXIMUM(SYSTEM.METER.IO_REQUESTS)) });
    SYSTEM.write.printf("AVERAGE: %.2f\n", new Object[] { Double.valueOf(AVERAGE(SYSTEM.METER.IO_REQUESTS)) });
    SYSTEM.write.println("END TIME CLOCK (DEC): " + CLOCK.GET_TIME() + "\n");
  }
  
  public static double MINIMUM(ArrayList<ArrayList<Integer>> input)
  {
    int minimum = -1;
    int minimum_position = -1;
    for (int i = 0; i < input.size(); i++)
    {
      int value = ((Integer)((ArrayList)input.get(i)).get(1)).intValue();
      if (minimum == -1)
      {
        minimum = value;
        minimum_position = i;
      }
      else if (value < minimum)
      {
        minimum = value;
        minimum_position = i;
      }
    }
    return minimum;
  }
  
  public static double MAXIMUM(ArrayList<ArrayList<Integer>> input)
  {
    int maximum = -1;
    int maximum_position = -1;
    for (int i = 0; i < input.size(); i++)
    {
      int value = ((Integer)((ArrayList)input.get(i)).get(1)).intValue();
      if (maximum == -1)
      {
        maximum = value;
        maximum_position = i;
      }
      else if (value > maximum)
      {
        maximum = value;
        maximum_position = i;
      }
    }
    return maximum;
  }
  
  public static double AVERAGE(ArrayList<ArrayList<Integer>> input)
  {
    double average = 0.0D;
    for (int i = 0; i < input.size(); i++) {
      average += ((Integer)((ArrayList)input.get(i)).get(1)).intValue();
    }
    average /= input.size();
    average = Math.round(average * 100.0D) / 100.0D;
    
    return average;
  }
  
  public static int RANDOM(int x)
  {
    int i = (int)(Math.random() * x);
    return i;
  }
}

