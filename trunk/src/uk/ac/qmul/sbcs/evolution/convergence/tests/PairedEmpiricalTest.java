/**
 * 
 */
package uk.ac.qmul.sbcs.evolution.convergence.tests;

import java.math.BigDecimal;
import java.math.MathContext;

import uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:joe@kitson-consulting.co.uk">Joe Parker, Kitson Consulting / Queen Mary University of London</a>
 *
 */
public class PairedEmpiricalTest extends TestCase {

	double[] ordered = {-10.0f,-9.0f,-8.0f,-7.0f,-6.0f,-5.0f,-4.0f,-3.0f,-2.0f,-1.0f,0.0f,1.0f,2.0f,3.0f,4.0f,5.0f,6.0f,7.0f,8.0f,9.0f,10f};
	double[] uniff = {-6.35918084997684f,-3.93759851809591f,-2.21174590289593f,-1.24442126601934f, 3.04381834808737f,-8.2444783160463f,-5.95814447849989f,0.0671290513128042f,1.97554216720164f,0.875178603455424f,4.69742903951555f,-6.42660288605839f,9.176234616898f,-9.40432901959866f,-0.732000516727567f,-5.7827518042177f,-4.51565932482481f,6.52691628783941f,7.89572019129992f,1.31803135387599f,-1.22617395594716f};
	double[] unif10 = {1.02738475659862f,1.50811678031459f,3.19657302228734f,4.21244767261669f,8.40045061893761f,5.29490644112229f,5.59071297058836f,2.08167154341936f,9.11403979640454f,9.07950394088402f,1.92111485404894f,1.08860559063032f,2.02587054576725f,8.21685578208417f,7.37943846033886f,6.59235533326864f,4.41377065842971f,4.75730574922636f,5.40445396210998f,3.02306494442746f,4.83131889021024f};
	double[] norm = {0.660572839333479f,0.0797906165244468f,1.63857542586835f,1.09376269979563f,0.176119081865946f,-0.97670653977667f,-0.471337425296921f,0.9635692753208f,0.83533323030851f,-0.287172779927618f,-0.332954605200311f,-0.389648493988506f,0.613728976414984f,1.44623291777936f,-0.0221722440108239f,0.416531670268897f,0.479983397711684f,-0.656481509449174f,-0.540704527888314f,-0.407246525275763f,-0.705449592323635f};
	double[] norm_10 = {5.7633638667616f,6.39802453645112f,4.75428644078549f,5.68351492957594f,6.1875414048615f,5.54739491703022f,5.78275240047244f,4.81502628423033f,4.93636421207665f,3.96353158612114f,5.21219941981893f,5.4310849397964f,2.91225993150518f,4.01424944417459f,4.18085534127271f,5.49409098352079f,4.86387738765962f,4.92171254794222f,6.54323395876331f,6.00583230420153f,4.69602008483443f};
	double[] norm5 = {4.12044236252056f,3.50979062375186f,6.62513059005838f,4.31996468214842f,6.41398749422907f,4.68360388453782f,5.49941207522795f,4.50533450337568f,6.52952016087318f,4.47154240056864f,5.07314166432082f,6.52378491714482f,4.66536650455161f,6.70806582828734f,7.07819172092462f,4.44320164272203f,3.61840779373539f,4.77155875391304f,6.03317841423138f,4.43413813122948f,5.26195237787355f};
	double[] norm_5 = {-3.80748223856822f,-5.75077164896431f,-3.3267984143375f,-4.07695338688592f,-4.92837429453196f,-5.83751845517887f,-4.98852233485545f,-4.78730748167994f,-6.5122338538245f,-4.77525803582946f,-3.85880205303318f,-4.75541150502231f,-4.84580506880202f,-7.32034278508732f,-3.05172271444051f,-5.48393140018985f,-5.05252061174948f,-4.81640785318711f,-4.02223193637001f,-4.12751611259949f,-3.94840819380712};
	// ks stuff for distributions on (0:1]
	double[] order1 = {0,0.0344827586206897,0.0689655172413793,0.103448275862069,0.137931034482759,0.172413793103448,0.206896551724138,0.241379310344828,0.275862068965517,0.310344827586207,0.344827586206897,0.379310344827586,0.413793103448276,0.448275862068966,0.482758620689655,0.517241379310345,0.551724137931034,0.586206896551724,0.620689655172414,0.655172413793103,0.689655172413793,0.724137931034483,0.758620689655172,0.793103448275862,0.827586206896552,0.862068965517241,0.896551724137931,0.931034482758621,0.96551724137931,1};
	double[] unif1 = {0.526772355660796,0.398782416479662,0.901285741012543,0.455820410279557,0.642212885431945,0.861228563589975,0.215375886065885,0.184008366661146,0.778669717721641,0.534172367071733,0.882508781040087,0.356307946611196,0.936016893479973,0.245627876371145,0.538643065374345,0.638791358564049,0.158813018817455,0.155748213874176,0.0135723217390478,0.529211234068498,0.24575675977394,0.971221415093169,0.384230916155502,0.874618443660438,0.549820814514533,0.781037411885336,0.618207290768623,0.699134208029136,0.487841309746727,0.0582582557108253};

	/**
	 * @param name
	 */
	public PairedEmpiricalTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical#PairedEmpirical(double[], double[])}.
	 */
	public void testPairedEmpirical() {
		PairedEmpirical p = new PairedEmpirical(uniff,ordered);
		double D_direct = p.getKS();
		double D_estimated = p.getApproxD();
		if(Math.abs((D_direct - D_estimated)) > 0.01){
			fail("D estimates do not match:\t"+D_direct+"\t"+D_estimated);
		}else{
			System.out.println("D estimates:\t"+D_direct+"\t"+D_estimated);
			
		}
	}

	/**
	 * Test method for {@link uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical#getKS()}.
	 */
	public void testGetKS() {
		PairedEmpirical p = new PairedEmpirical(uniff,ordered);
		double D_direct = p.getKS();
		double D_estimated = p.getApproxD();
		if(Math.abs((D_direct - D_estimated)) > 0.01){
			fail("D estimates do not match:\t"+D_direct+"\t"+D_estimated);
		}else{
			System.out.println("D estimates:\t"+D_direct+"\t"+D_estimated);
		}
	}

	/**
	 * Test method for {@link uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical#getKS_sp()}.
	 */
	public void testGetKS_sp() {
		PairedEmpirical p = new PairedEmpirical(uniff,ordered);
		double SP = p.getKS_sp();
		if(!((SP >= 0)&&(SP <= 1))){
			fail("P-value is bobbins");
		}else{
			System.out.println("P estimates:\t"+SP);			
		}
	}

	/**
	 * Test method for {@link uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical#getDensity_A(double)}.
	 */
	public void testGetDensity_A() {
		PairedEmpirical p = new PairedEmpirical(uniff,ordered);
		for(int i=0;i<uniff.length;i++){
			double density = p.getDensity_A(uniff[i]);
			if(!((density >= 0)&&(density <= 1))){
				fail("density "+density+" is bobbins");
			}else{
				System.out.println("density estimate:\t"+uniff[i]+"\t"+density);			
			}
		}
	}

	/**
	 * Test method for {@link uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical#getDensity_B(double)}.
	 */
	public void testGetDensity_B() {
		PairedEmpirical p = new PairedEmpirical(uniff,ordered);
		for(int i=0;i<ordered.length;i++){
			double density = p.getDensity_B(ordered[i]);
			if(!((density >= 0)&&(density <= 1))){
				fail("density "+density+" is bobbins");
			}else{
				System.out.println("density estimate:\t"+uniff[i]+"\t"+density);			
			}
		}
	}

	/**
	 * Test method for {@link uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical#getDensityOverlapAt(double)}.
	 */
	public void testGetDensityOverlapAt() {
		PairedEmpirical p = new PairedEmpirical(uniff,ordered);
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.qmul.sbcs.evolution.convergence.util.stats.PairedEmpirical#getDensity_A_at_percentile_B(double)}.
	 */
	public void testGetDensity_A_at_percentile_B() {
		PairedEmpirical p = new PairedEmpirical(uniff,ordered);
		fail("Not yet implemented"); // TODO
	}

}
