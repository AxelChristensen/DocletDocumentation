package DocletTest;

/**
 * @issue This is a fake class
 * @purpose Absolutely none, except to educate.
 * @author Santa Clauss
 * @date a distant year in the future...
 */
public class DoNothingSecondClass
{
    /**
     * @issue NOT A REAL ISSUE
     * @purpose A simple fake class to show that multiple classes are scanned
     * and here's some more purpose
     * @worthnoting This isn't a real method
     */
    public void donothing(){
    }
    /**
     * @issue Also not an issue
     * @todo nothing to do, since this is a fake class
     * @worthnoting The legacy instruments were problematic in that they got stuck in BUSY states when they weren't busy.
      * @todo Add the legacy instruments back eventually.
     * @param newInstrument
     * @param nextResultFileName
     */
    public void somethingelse(String newInstrument, String nextResultFileName){
    }
    /**
       * @defunct Before fixing DE16834, this test threw
       * commenting this out... I'm not really sure what I was thinking with the cancel.  We don't allow cancelling of runs that aren at
       * the instrument from the UI.
     */
    public void defunct(){
// old code here, commented out
   }
}
