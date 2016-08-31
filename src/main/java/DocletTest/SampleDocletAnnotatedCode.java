package DocletTest;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author achristensen
 * @date 8/10/2016
 * @purpose To test Crimson instrument
 * @worthnoting  Avoid modifying either the requisition_number, patient_id, patient_name, message_id or message_type elements - the req/patient info and message IDs need to be unique to avoid appending requests
 * and to identify ACKs, and the message_type is used to determine success of the request
 * If we need to run some tests against requests with messed up req IDs or message IDs, maybe this can be expanded a bit
 */
public class SampleDocletAnnotatedCode
{
    protected IcsysInstrument icsysInstrument;
    /**
     * @issue IC-4780
     * @purpose Testing to verify that the value in PLT-O is swapped into PLT in the case of Equine (Procyte)
     * @assertrequirement That PLT should now have the value that was in PLT-O
     * @todo Might want to test for PLT not being equal to what it was before
     * @worthnoting This is for equine
     * @worthnoting Catalyst Dx will return null when QcV2 is rejected and there's no QC Lot data in the db
     * @link http://aws-fisheye.vet2pet.idexxi.com/cru/IVLS-629
     * @coverage routing of PLT-O to PLT independent of whether instrument choses PLT-I or PLT-O
     * @throws Exception
     */
    public void TestPLTORouting()
            throws Exception
    {
        final int pltvalue=100;
        LabRequestTaskHelper labRequestTaskHelper=new LabRequestTaskHelper();
        icsysInstrument.setNextResultsModifier(
                new ResultsModifier()
                {
                    @Override
                    public void modifyResults(ResultSet resultSets)
                    {
                        resultSets.addResult(Result.createNumericResult(200,"PLT"));
                        resultSets.addResult(Result.createNumericResult(pltvalue,"PLT-O"));
                        resultSets.addResult(Result.createNumericResult(300,"PLT-I"));
                    }
                });
        LabRequestDto labRequestDto = labRequestTaskHelper.sendNewLabRequestAndWaitForResults("instrument" , "SpeciesType.Equine,waiter");
        InstrumentResultDto res=labRequestDto.getInstrumentRunDtos().get(0).getResultForAssay("PLT");
        Assert.assertEquals(pltvalue,Integer.parseInt(res.getResultValue()));
    }
}


class InstrumentRunStatusDto
{
}
//class MessageMatcher<T extends DtoInterface>
//{
//}



class MessageMatcherFactory{

}

class LabRequestDto {
    public ArrayList<InstrumentResultDto> getInstrumentRunDtos(){return new ArrayList<InstrumentResultDto>();}
}
class LabRequestTaskHelper{
    public LabRequestDto sendNewLabRequestAndWaitForResults(Object o,Object i){return null;}

}
class IcsysInstrument{
    public Object setNextResultsModifier(Object o){ return null;}
    public void setNextResultFileName(Object o){}
}
class InstrumentResultDto {
    public ArrayList getInstrumentRunDtos(){
        return new ArrayList();
    }
    public InstrumentResultDto getResultForAssay(String s){return null;}
    public String getResultValue(){return "";}
}
class Assert{
    public static void assertEquals(Object a, int i){}
}
class ResultsModifier{
    public void modifyResults(ResultSet res){}
}
class Result{
    public static Object createNumericResult(int i,Object o){
        return null;
    }
}
class ResultSet{
    public void addResult(Object o){

    }
}
