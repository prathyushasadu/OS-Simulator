import java.util.ArrayList;
/* f. The PCB class maintains the information
 *    of JOB, program segment information,
 *    input segment and output segment information.
 */
public class PCB
{
  int JOB_ID;
  int INTERNAL_JOB_ID;
  int int_inf;
  int i;
  int j;
  int k;
  int p;
  int frames;
  int REPLACE_POINTER;
  int PAGE_FAULTS;
  int PC;
  int send_frames = 0;
  int input_th_page;
  String TRACE;
  String IR;
  String BR;
  String NATURE_OF_TERMINATION = "NORMAL";
  String WARNING;
  String ERROR;
  String stored_result = "";
  String stored_value = "";
  int FRAMES;
  int EXPECTED_IO_COMPLETION;
  int inputs_read;
  int outputs_written;
  ArrayList<String> STACK;
  int IO_TIME;
  int PAGE_FAULT_TIME;
  int SEGMENT_FAULT_TIME;
  int ENTRY_TIME;
  int EXIT_TIME;
  int EXECUTION_TIME;
  int input_words;
  int output_words;
  int job_words;
  int output_th_page;
  int INPUT_INDEX = -1;
  int OUTPUT_INDEX = -1;
  int Segment_value = 0;
  int INPUT_POINTER;
  int OUTPUT_POINTER;
  int PROGRAM_POINTER = 0;
  public static ERROR_HANDLER ERR = new ERROR_HANDLER();
  String EA;
  ArrayList<Integer> INPUT_PAGES;
  ArrayList<Integer> OUTPUT_PAGES;
  ArrayList<ArrayList<Integer>> PROGRAM_DISK_PAGES;
  ArrayList<ArrayList<Integer>> pageArr;
  ArrayList<ArrayList<Integer>> INPUT_DISK_PAGES;
  ArrayList<ArrayList<Integer>> OUTPUT_DISK_PAGES;
  ArrayList<ArrayList<Integer>> ALLOCATED_MEMORY;
  
  PCB()
  {
    this.PROGRAM_DISK_PAGES = new ArrayList<ArrayList<Integer>>();
    this.INPUT_DISK_PAGES = new ArrayList<ArrayList<Integer>>();
    this.OUTPUT_DISK_PAGES = new ArrayList<ArrayList<Integer>>();
    this.INPUT_PAGES = new ArrayList<Integer>();
    this.OUTPUT_PAGES = new ArrayList<Integer>();
    this.ALLOCATED_MEMORY = new ArrayList<ArrayList<Integer>>();
    this.STACK = new ArrayList<String>();
    this.i = 0;this.j = 0;this.k = 0;this.p = 0;
    this.frames = 0;
    this.pageArr = new ArrayList<ArrayList<Integer>>();
    //29 april
    this.EA = null;
  }

  
  void SET_JOB_ID(int id)
  {
    this.JOB_ID = id;
  }
  
  int GET_JOB_ID()
  {
    return this.JOB_ID;
  }
  
  void SET_NUMBER_OF_FRAMES(int number)
  {
    this.FRAMES = number;
  }
  
  void SET_INPUT_WORDS(int words)
  {
    this.input_words = words;
  }
  
  int GET_INPUT_WORDS()
  {
    return this.input_words;
  }
  
  void SET_OUTPUT_WORDS(int words)
  {
    this.output_words = words;
  }
  
  int GET_OUTPUT_WORDS()
  {
    return this.output_words;
  }
  
  void SET_JOB_WORDS(int words)
  {
    this.job_words = words;
  }
  
  int GET_JOB_WORDS()
  {
    return this.job_words;
  }
  
  void SET_PC(int position)
  {
    this.PC = position;
  }
  
  int GET_PC()
  {
    return this.PC;
  }
  
  void SET_TRACE(String bit)
  {
    this.TRACE = bit;
  }
  
  String GET_TRACE()
  {
    return this.TRACE;
  }
  
  void SET_IR(String value)
  {
    this.IR = value;
  }
  
  String GET_IR()
  {
    return this.IR;
  }
  
  void ADD_PROGRAM_DISK_PAGES(int page, int disk_page)
  {
    this.PROGRAM_DISK_PAGES.add(new ArrayList<Integer>());
    (this.PROGRAM_DISK_PAGES.get(this.i)).add(Integer.valueOf(page));
    (this.PROGRAM_DISK_PAGES.get(this.i)).add(Integer.valueOf(disk_page));
    this.i += 1;
  }
  void ADD_PAGE_MAP(int pageno, int frameno)
  {
    this.pageArr.add(new ArrayList<Integer>());
    (this.pageArr.get(this.p)).add(Integer.valueOf(pageno));
    (this.pageArr.get(this.p)).add(Integer.valueOf(frameno));
    this.p += 1;

  }
  
  int GET_PROGRAM_DISK_PAGE(int page)
  {
    int disk_page = -1;
    for (int d = 0; d < this.PROGRAM_DISK_PAGES.size(); d++) {
      if (((Integer)((ArrayList)this.PROGRAM_DISK_PAGES.get(d)).get(0)).intValue() == page) {
        disk_page = ((Integer)((ArrayList)this.PROGRAM_DISK_PAGES.get(d)).get(1)).intValue();
      }
    }
    return disk_page;
  }
  
  void ADD_INPUT_PAGES(int page)
  {
    this.INPUT_PAGES.add(Integer.valueOf(page));
  }
  
  int GET_INPUT_PAGES(int position)
  {
    if(!(this.INPUT_PAGES.isEmpty()))
    {
    return (this.INPUT_PAGES.get(position)).intValue();
    }
    else
    {
	ERR.ERROR_HANDLER(4, SYSTEM.CURRENT_JOB);
	return -1;
    }
  }
  
  void ADD_INPUT_DISK_PAGES(int page, int disk_page)
  {
    this.INPUT_DISK_PAGES.add(new ArrayList<Integer>());
    (this.INPUT_DISK_PAGES.get(this.j)).add(Integer.valueOf(page));
    (this.INPUT_DISK_PAGES.get(this.j)).add(Integer.valueOf(disk_page));
    this.j += 1;
  }
  
  int GET_INPUT_DISK_PAGE(int page)
  {
    int disk_page = -1;
    for (int d = 0; d < this.INPUT_DISK_PAGES.size(); d++) {
      if (((Integer)((ArrayList)this.INPUT_DISK_PAGES.get(d)).get(0)).intValue() == page) {
        disk_page = ((Integer)((ArrayList)this.INPUT_DISK_PAGES.get(d)).get(1)).intValue();
      }
    }
    return disk_page;
  }
  
  void ADD_OUTPUT_PAGES(int page)
  {
    this.OUTPUT_PAGES.add(Integer.valueOf(page));
  }
  
  int GET_OUTPUT_PAGES(int position)
  {
    return (this.OUTPUT_PAGES.get(position)).intValue();
  }
  
  void ADD_OUTPUT_DISK_PAGES(int page, int disk_page)
  {
    this.OUTPUT_DISK_PAGES.add(new ArrayList<Integer>());
    (this.OUTPUT_DISK_PAGES.get(this.k)).add(Integer.valueOf(page));
    (this.OUTPUT_DISK_PAGES.get(this.k)).add(Integer.valueOf(disk_page));
    this.k += 1;
  }
  
  int GET_OUTPUT_DISK_PAGE(int page)
  {
    int disk_page = -1;
    for (int d = 0; d < this.OUTPUT_DISK_PAGES.size(); d++) {
      if (((Integer)((ArrayList)this.OUTPUT_DISK_PAGES.get(d)).get(0)).intValue() == page) {
        disk_page = ((Integer)((ArrayList)this.OUTPUT_DISK_PAGES.get(d)).get(1)).intValue();
      }
    }
    return disk_page;
  }
  
  void ADD_ALLOCATED_MEMORY(int frame)
  {
    this.ALLOCATED_MEMORY.add(new ArrayList<Integer>());
    (this.ALLOCATED_MEMORY.get(this.frames)).add(Integer.valueOf(frame));
    this.frames += 1;
  }
  
  int GET_ALLOCATED_MEMORY()
  {
    if (this.send_frames == this.FRAMES) {
      this.send_frames = 0;
    }
    int frame = ((Integer)((ArrayList)this.ALLOCATED_MEMORY.get(this.send_frames)).get(0)).intValue();
    this.send_frames += 1;
    
    return frame;
  }
  
  ArrayList<ArrayList<Integer>> GET_ALLOCATED_LIST()
  {
    return this.ALLOCATED_MEMORY;
  }
  
  int GET_INPUT_INDEX()
  {
    return ++this.INPUT_INDEX;
  }
  
  int GET_OUTPUT_INDEX()
  {
    return ++this.OUTPUT_INDEX;
  }
  
  void DUMP_STACK(String data)
  {
    this.STACK.add(data);
  }
  
  int GET_TIME()
  {
    return this.EXPECTED_IO_COMPLETION;
  }
  
  ArrayList<String> GET_STACK()
  {
    return this.STACK;
  }
  
  ArrayList<ArrayList<Integer>> GET_P_DISK_PAGES()
  {
    return this.PROGRAM_DISK_PAGES;
  }
  
  ArrayList<ArrayList<Integer>> GET_I_DISK_PAGES()
  {
    return this.INPUT_DISK_PAGES;
  }
  
  ArrayList<ArrayList<Integer>> GET_O_DISK_PAGES()
  {
    return this.OUTPUT_DISK_PAGES;
  }
}

