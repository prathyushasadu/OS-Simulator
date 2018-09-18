import java.util.ArrayList;

/*
f. PMT is the Page Map Table.
The Page Map Table holds the data about which page is located
in which memory frame.
The PMT has reference, modify(dirty) and a reference pointer.
*/
class PMT
{
  public ArrayList<ArrayList<Integer>> pmt;
  public int i;
  
  PMT()
  {
    this.pmt = new ArrayList<ArrayList<Integer>>();
    this.i = 0;
  }
  
  void ADD_PAGE(int pageno, int frame_no, int reference, int dirty)
  {
    this.pmt.add(new ArrayList<Integer>());
    (this.pmt.get(this.i)).add(Integer.valueOf(pageno));
    (this.pmt.get(this.i)).add(Integer.valueOf(frame_no));
    (this.pmt.get(this.i)).add(Integer.valueOf(reference));
    (this.pmt.get(this.i)).add(Integer.valueOf(dirty));
    this.i += 1;
  }
  
  int GET_FRAME(int pageno)
  {
    int frame_no = -1;
    for (int f = 0; f < this.pmt.size(); f++) {
      if (((this.pmt.get(f)).get(0)).intValue() == pageno) {
        frame_no = ((this.pmt.get(f)).get(1)).intValue();
      }
    }
    return frame_no;
  }
  
  void SET_REFERENCE(int pageno, int bit)
  {
    for (int f = 0; f < this.pmt.size(); f++) {
      if (((this.pmt.get(f)).get(0)).intValue() == pageno)
      {
        (this.pmt.get(f)).set(2, Integer.valueOf(bit));
        break;
      }
    }
  }
  
  void SET_DIRTY(int pageno, int bit)
  {
    for (int f = 0; f < this.pmt.size(); f++) {
      if (((this.pmt.get(f)).get(0)).intValue() == pageno)
      {
        (this.pmt.get(f)).set(3, Integer.valueOf(bit));
        break;
      }
    }
  }
  
  int GET_REFERENCE(int frame)
  {
    int bit = -1;
    for (int f = 0; f < this.pmt.size(); f++) {
      if (((this.pmt.get(f)).get(1)).intValue() == frame)
      {
        bit = ((this.pmt.get(f)).get(2)).intValue();
        break;
      }
    }
    return bit;
  }
  
  int GET_DIRTY(int frame)
  {
    int bit = -1;
    for (int f = 0; f < this.pmt.size(); f++) {
      if (((this.pmt.get(f)).get(1)).intValue() == frame)
      {
        bit = ((this.pmt.get(f)).get(3)).intValue();
        break;
      }
    }
    return bit;
  }
  
  void REMOVE_PAGE(int pageno)
  {
    for (int r = 0; r < this.pmt.size(); r++) {
      if (((this.pmt.get(r)).get(0)).intValue() == pageno)
      {
        (this.pmt.get(r)).set(0, Integer.valueOf(-1));
        (this.pmt.get(r)).set(1, Integer.valueOf(-1));
        (this.pmt.get(r)).set(2, Integer.valueOf(-1));
        (this.pmt.get(r)).set(3, Integer.valueOf(-1));
        break;
      }
    }
  }
  
  void PAGE_REPLACE(int base_page, int new_page, int new_frame)
  {
    for (int r = 0; r < this.pmt.size(); r++) {
      if (((this.pmt.get(r)).get(0)).intValue() == base_page)
      {
        (this.pmt.get(r)).set(0, Integer.valueOf(new_page));
        (this.pmt.get(r)).set(1, Integer.valueOf(new_frame));
      }
    }
  }
  
  ArrayList<ArrayList<Integer>> SHOW_PMT()
  {
    return this.pmt;
  }
}

