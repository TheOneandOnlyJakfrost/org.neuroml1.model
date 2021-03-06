package org.neuroml1.model.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.neuroml1.model.NeuroMLLevel3;
import org.neuroml1.model.channel.ChannelML;
import org.neuroml1.model.channel.ChannelType;
import org.neuroml1.model.channel.CurrentVoltageRelation;
import org.neuroml1.model.channel.GatingComplex;
import org.neuroml1.model.channel.Offset;
import org.neuroml1.model.channel.OpenState;
import org.neuroml1.model.channel.Q10Settings;
import org.neuroml1.model.util.NeuroML1ValidatorTest;
import org.neuroml1.model.util.NeuroMLConverter;

public class ChannelTest
{
	
	@Test public void testKCaChannelUnmarshallingAndMarshalling() throws Exception
	{	
		NeuroMLConverter conv = new NeuroMLConverter();
        
        
        // Test Reading from file
		
		String wdir = System.getProperty("user.dir");
		String tdir = wdir + File.separator + "src/test/resources";
		String fpath = tdir + File.separator +"KCa_chan.xml";
		
        System.out.println("Performing test on "+fpath);
		
		ChannelML chan = conv.xmlToChannel(fpath);
		
		List<ChannelType> ctypes = chan.getChannelType();
		assertTrue(ctypes.size() == 1);
		
		ChannelType kca = chan.getChannelTypeByName("Gran_KCa_98");
		assertTrue("Gran_KCa_98".equals(kca.getName()));
		
		CurrentVoltageRelation iv = kca.getCurrentVoltageRelation();
		
		assertTrue("k".equals(iv.getIon()));
		
		assertTrue(iv.getDefaultGmax() ==  0.179811);
		
		
		Q10Settings q10 = iv.getQ10Settings().get(0);
		assertTrue(q10.getExperimentalTemp() == 17.350264793);
		assertTrue(q10.getQ10Factor() == 3.0);
		
		Offset off = iv.getOffset();
		assertTrue(off.getValue() == 0.010);
				
		GatingComplex g = iv.getGate().get(0);
		
		assertTrue(g.getInstances().toString().equals("1"));
		
		assertTrue(g.getName().equals("m"));
		
		OpenState os = g.getOpenState().get(0);
		
		assertTrue(os.getFraction() == 1.0);
        
        
        // Test Saving to file

        File cmlFile = new File(getTempDir(),"kca-as-channelml.xml");
		conv.channelToXml(chan, cmlFile.getAbsolutePath());
        System.out.println("Saved to a file: "+ cmlFile);
		assertTrue(cmlFile.exists());
        
        NeuroML1ValidatorTest.testValidate(cmlFile);
        
        NeuroMLLevel3 nmlL3 = new NeuroMLLevel3();
        nmlL3.setChannels(chan);
 
        File nmlFile = new File(getTempDir(),"kca-as-neuroml.xml");
		conv.neuromlToXml(nmlL3, nmlFile.getAbsolutePath());
        System.out.println("Saved to a file: "+ nmlFile);
		assertTrue(nmlFile.exists());
        
        NeuroML1ValidatorTest.testValidate(nmlFile);

        System.out.println("All done..");
        
	}


	
	protected static File getTempDir()
	{

		String wdir = System.getProperty("user.dir");
        String tempdirname = wdir + File.separator + "src/test/resources/tmp";
        File tempdir = new File(tempdirname);
        if (!tempdir.exists()) tempdir.mkdir();
		return tempdir;
	}
}
