package uk.ac.qmul.sbcs.evolution.convergence.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.ac.qmul.sbcs.evolution.convergence.ParsimonyReconstruction;
import uk.ac.qmul.sbcs.evolution.convergence.StateComparison;
import uk.ac.qmul.sbcs.evolution.convergence.TreeNode;
import uk.ac.qmul.sbcs.evolution.convergence.gui.DisplayPhylogenyPanel;
import uk.ac.qmul.sbcs.evolution.convergence.util.SitewiseSpecificLikelihoodSupportAaml;
import uk.ac.qmul.sbcs.evolution.sandbox.DisplayCoordinatesPanel;
import junit.framework.TestCase;

public class TreeNodeTest extends TestCase {

	public TreeNodeTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testTreeNodeStringInt() {
		TreeNode n1 = new TreeNode("((a,b),c)",1);
		TreeNode n2 = new TreeNode("((a:0.01,b:0.02):0.01,c:0.03)",1);
		TreeNode n3 = new TreeNode("((a:0.01,b:0.02):-0.01,c:0.03)",1);
		TreeNode n4 = new TreeNode("((a,b),(c,(d,e)))",1);
		assert(true);
	}

	public void testAssignStates(){
		HashMap<String, HashSet<String>[]> states = new HashMap<String,HashSet<String>[]>();
		HashSet<String>[] hseta = (HashSet<String>[]) Array.newInstance(HashSet.class, 2);
		HashSet<String>[] hsetb = (HashSet<String>[]) Array.newInstance(HashSet.class, 2);
		HashSet<String>[] hsetc = (HashSet<String>[]) Array.newInstance(HashSet.class, 2);
		hseta[0] = new HashSet<String>();
		hsetb[0] = new HashSet<String>();
		hsetc[0] = new HashSet<String>();
		hseta[0].add("black");
		hsetb[0].add("white");
		hsetc[0].add("black");
		hseta[1] = new HashSet<String>();
		hsetb[1] = new HashSet<String>();
		hsetc[1] = new HashSet<String>();
		hseta[1].add("black");
		hsetb[1].add("white");
		hsetc[1].add("fushcia");
		
		states.put("a", hseta);
		states.put("b", hsetb);
		states.put("c", hsetc);
		for (int i = 0; i < 10; i++) {
			System.out.println("rep "+i);
			TreeNode n1 = new TreeNode("((a,b),c)", 1);
			HashSet<String>[] baseStates = n1.getFitchStates(states);
			System.out.println("rep "+i+" set initial states");
			n1.printStates();
			n1.resolveFitchStatesTopnode();
			n1.resolveFitchStates(n1.states);
			n1.getEndPos();
			System.out.println("rep "+i+" final states");
			n1.printStates();
		}
	}
	
	/*
	 * Test that the TreeNode is able to label tips separately and correctly
	 */
	public void testIterativeTipLabelling(){
		String input = "(((LOXODONTA:0.080618,DASYPUS:0.028235):0.002756,(((((CANIS:0.012983,FELIS:0.013897):0.005719,(EQUUS:0.028437,((TURSIOPS:0.038936,BOS:0.016707):0.003048,VICUGNA:0.031996):0.004509):0.006443):0.000004,(MYOTIS:0.056507,((RHINOLOPHUS:0.066174,MEGADERMA:0.021473):0.006671,PTEROPUS:0.015521):0.000004):0.008379):0.002227,(SOREX:0.022136,ERINACEUS:0.013937):0.004338):0.004428,((MUS:0.034943,(ORYCTOLAGUS:0.021193,OCHOTONA:0.063783):0.025907):0.003677,(PAN:0.010448,HOMO:0.001622):0.021809):0.002889):0.000004):0.144025,MONODELPHIS:0.113014)";
		TreeNode n1 = new TreeNode(input,1);
		String[] lox = {"LOXODONTA"};
		String[] das = {"DASYPUS"};
		String[] cak = {"cakse"};

		// generally explore the behaviour of TreeNode
		System.out.println(n1.printRecursivelyLabelling(lox)+";");
		System.out.println(n1.printRecursivelyLabelling(das)+";");
		System.out.println(n1.printRecursivelyLabelling(cak)+";");
		if(!input.equals(n1.printRecursivelyLabelling(cak))){
			fail();
		}
	}


	/*
	 * Test that the TreeNode finds the lowest internal node containing target taxa, and returns its ID / numbering
	 * Tests for lowest clade containing all the taxa in the target set, which in this case are strictly monophyletic (e.g. n tips == n set, or only listed taxa are present in the clade, to put it another way)
	 */
	public void testGetnodeNumberingIDContainingTaxaCheckStrictMonophyly(){
		// instantiate the tree
		String input="(((LOXODONTA:0.023584,DASYPUS:0.029504):0.000004,((((CANIS:0.076115,(EQUUS:0.014067,((TURSIOPS:0.000004,BOS:0.014131):0.003492,VICUGNA:0.021123):0.010546):0.000004):0.000004,((PTERONOTUS:0.025088,MYOTIS:0.032407):0.003456,((RHINOLOPHUS:0.008430,MEGADERMA:0.031984):0.005840,(PTEROPUS:0.000004,EIDOLON:0.006953):0.021190):0.000004):0.000004):0.000004,(SOREX:0.088536,ERINACEUS:0.044769):0.010306):0.003510,((MUS:0.090365,(ORYCTOLAGUS:0.011232,OCHOTONA:0.044380):0.036082):0.001096,(PAN:0.000004,HOMO:0.000004):0.013368):0.006229):0.001213):0.165422,MONODELPHIS:0.138559);";
		TreeNode n1 = new TreeNode(input,1);
		// create a taxa list to look for
		String[] bats = {
				"PTERONOTUS",
				"MYOTIS",
				"RHINOLOPHUS",
				"MEGADERMA",
				"PTEROPUS",
				"EIDOLON"
		};
		HashSet<String> echoMap = new HashSet<String>(Arrays.asList(bats));
		// init retval
		int retval = -1;
		// IMPORTANT!!! at the moment node numbers / IDs *NOT* set when node instantiated!!
		// TODO set node numbers at instantiation
		// FIXME set node numbers at instantiation
		n1.setNodeNumbers(0, n1.howManyTips());
		// test the method. retval should ==32 at the end
		retval = n1.getNodeNumberingIDContainingTaxa(echoMap);
		if(retval != 32){
			fail("Incorrect node ID found!");
		}
	}

	/*
	 * Test that the TreeNode finds the lowest internal node containing target taxa, and returns ID / numbering of branch leading to it
	 * Tests for lowest clade containing all the taxa in the target set, which in this case are actually paraphyletic (e.g. n tips > n set, or a non-listed taxon is present in the clade, to put it another way)
	 */
	public void testGetBranchNumberingIDContainingTaxaCheckMonophyly(){
		// instantiate the tree
		String input="(((LOXODONTA:0.023584,DASYPUS:0.029504):0.000004,((((CANIS:0.076115,(EQUUS:0.014067,((TURSIOPS:0.000004,BOS:0.014131):0.003492,VICUGNA:0.021123):0.010546):0.000004):0.000004,((PTERONOTUS:0.025088,MYOTIS:0.032407):0.003456,((RHINOLOPHUS:0.008430,MEGADERMA:0.031984):0.005840,(PTEROPUS:0.000004,EIDOLON:0.006953):0.021190):0.000004):0.000004):0.000004,(SOREX:0.088536,ERINACEUS:0.044769):0.010306):0.003510,((MUS:0.090365,(ORYCTOLAGUS:0.011232,OCHOTONA:0.044380):0.036082):0.001096,(PAN:0.000004,HOMO:0.000004):0.013368):0.006229):0.001213):0.165422,MONODELPHIS:0.138559);";
		TreeNode n1 = new TreeNode(input,1);
		// create a taxa list to look for
		String[] bats = {
				"PTERONOTUS",
				"MYOTIS",
				"RHINOLOPHUS",
				"PTEROPUS",
				"EIDOLON"
		};
		HashSet<String> echoMap = new HashSet<String>(Arrays.asList(bats));
		// init retval
		int[] retval = {-1,-1};
		// IMPORTANT!!! at the moment node numbers / IDs *NOT* set when node instantiated!!
		// TODO set node numbers at instantiation
		// FIXME set node numbers at instantiation
		n1.setNodeNumbers(0, n1.howManyTips());
		// test the method. retval should ==32 at the end
		retval = n1.getBranchNumberingIDContainingTaxa(echoMap);
		if(retval[0] != 32){
			fail("Incorrect node 'MRCA/to' ID found!");
		}
		if(retval[1] != 27){
			fail("Incorrect node 'from' ID found!");
		}
	}

	/*
	 * Test that the TreeNode finds the lowest internal node containing target taxa, and returns its ID / numbering
	 * Tests for lowest clade containing all the taxa in the target set, which in this case are actually paraphyletic (e.g. n tips > n set, or a non-listed taxon is present in the clade, to put it another way)
	 */
	public void testGetnodeNumberingIDContainingTaxaCheckMonophyly(){
		// instantiate the tree
		String input="(((LOXODONTA:0.023584,DASYPUS:0.029504):0.000004,((((CANIS:0.076115,(EQUUS:0.014067,((TURSIOPS:0.000004,BOS:0.014131):0.003492,VICUGNA:0.021123):0.010546):0.000004):0.000004,((PTERONOTUS:0.025088,MYOTIS:0.032407):0.003456,((RHINOLOPHUS:0.008430,MEGADERMA:0.031984):0.005840,(PTEROPUS:0.000004,EIDOLON:0.006953):0.021190):0.000004):0.000004):0.000004,(SOREX:0.088536,ERINACEUS:0.044769):0.010306):0.003510,((MUS:0.090365,(ORYCTOLAGUS:0.011232,OCHOTONA:0.044380):0.036082):0.001096,(PAN:0.000004,HOMO:0.000004):0.013368):0.006229):0.001213):0.165422,MONODELPHIS:0.138559);";
		TreeNode n1 = new TreeNode(input,1);
		// create a taxa list to look for
		String[] bats = {
				"PTERONOTUS",
				"MYOTIS",
				"RHINOLOPHUS",
				"PTEROPUS",
				"EIDOLON"
		};
		HashSet<String> echoMap = new HashSet<String>(Arrays.asList(bats));
		// init retval
		int retval = -1;
		// IMPORTANT!!! at the moment node numbers / IDs *NOT* set when node instantiated!!
		// TODO set node numbers at instantiation
		// FIXME set node numbers at instantiation
		n1.setNodeNumbers(0, n1.howManyTips());
		// test the method. retval should ==32 at the end
		retval = n1.getNodeNumberingIDContainingTaxa(echoMap);
		if(retval != 32){
			fail("Incorrect node ID found!");
		}
	}
	
	/*
	 * Test that the TreeNode is able to label tips separately and correctly
	 */
	public void testIterativeNodenumbering(){
		String input="(((LOXODONTA:0.023584,DASYPUS:0.029504):0.000004,((((CANIS:0.076115,(EQUUS:0.014067,((TURSIOPS:0.000004,BOS:0.014131):0.003492,VICUGNA:0.021123):0.010546):0.000004):0.000004,((PTERONOTUS:0.025088,MYOTIS:0.032407):0.003456,((RHINOLOPHUS:0.008430,MEGADERMA:0.031984):0.005840,(PTEROPUS:0.000004,EIDOLON:0.006953):0.021190):0.000004):0.000004):0.000004,(SOREX:0.088536,ERINACEUS:0.044769):0.010306):0.003510,((MUS:0.090365,(ORYCTOLAGUS:0.011232,OCHOTONA:0.044380):0.036082):0.001096,(PAN:0.000004,HOMO:0.000004):0.013368):0.006229):0.001213):0.165422,MONODELPHIS:0.138559);";
		TreeNode n1 = new TreeNode(input,1);
		
		// test the numbering
		n1.setNodeNumbers(0,n1.howManyTips());
		
		System.out.println(n1.printRecursivelyAsNumberedNodes());
		System.out.println(n1.printRecursively());

		String inputNumberedTips="(((8,3),((((2,(5,((20,1),21))),((16,12),((18,9),(17,4)))),(19,6)),((11,(14,13)),(15,7)))),10);";
		TreeNode numbered = new TreeNode(inputNumberedTips,1);
		
		// test the numbering
		numbered.setNodeNumbers(0,numbered.howManyTips());
		
		System.out.println(numbered.printRecursivelyAsNumberedNodes());
		System.out.println(numbered.printRecursively());

		inputNumberedTips="(((LOXODONTA,DASYPUS),((((2,(5,((20,1),21))),((16,12),((18,9),(17,4)))),(19,6)),((11,(ORYCTOLAGUS,13_OCHOTONA)),(15,HOMO)))),10);";
		numbered = new TreeNode(inputNumberedTips,1);
		
		// test the numbering
		numbered.setNodeNumbers(0,numbered.howManyTips());
		
		System.out.println(numbered.printRecursivelyAsNumberedNodes());
		System.out.println(numbered.printRecursively());
	
	}
	
	public void testPrestinTrees(){
		TreeNode n1 = new TreeNode("(((LOXODONTA:0.080618,DASYPUS:0.028235):0.002756,(((((CANIS:0.012983,FELIS:0.013897):0.005719,(EQUUS:0.028437,((TURSIOPS:0.038936,BOS:0.016707):0.003048,VICUGNA:0.031996):0.004509):0.006443):0.000004,(MYOTIS:0.056507,((RHINOLOPHUS:0.066174,MEGADERMA:0.021473):0.006671,PTEROPUS:0.015521):0.000004):0.008379):0.002227,(SOREX:0.022136,ERINACEUS:0.013937):0.004338):0.004428,((MUS:0.034943,(ORYCTOLAGUS:0.021193,OCHOTONA:0.063783):0.025907):0.003677,(PAN:0.010448,HOMO:0.001622):0.021809):0.002889):0.000004):0.144025,MONODELPHIS:0.113014)",1);
//		TreeNode n2 = new TreeNode("(((LOXODONTA: 0.080618, DASYPUS: 0.028235): 0.002756, (((((CANIS: 0.012983, FELIS: 0.013897): 0.005719, (EQUUS: 0.028437, ((TURSIOPS: 0.038936, BOS: 0.016707): 0.003048, VICUGNA: 0.031996): 0.004509): 0.006443): 0.000004, (MYOTIS: 0.056507, ((RHINOLOPHUS: 0.066174, MEGADERMA: 0.021473): 0.006671, PTEROPUS: 0.015521): 0.000004): 0.008379): 0.002227, (SOREX: 0.022136, ERINACEUS: 0.013937): 0.004338): 0.004428, ((MUS: 0.034943, (ORYCTOLAGUS: 0.021193, OCHOTONA: 0.063783): 0.025907): 0.003677, (PAN: 0.010448, HOMO: 0.001622): 0.021809): 0.002889): 0.000004): 0.144025, MONODELPHIS: 0.113014)",1);
//		TreeNode n3 = new TreeNode("(((LOXODONTA: 0.080618, DASYPUS: 0.028235): 0.002756, (((((CANIS: 0.012983, FELIS: 0.013897): 0.005719, (EQUUS: 0.028437, ((TURSIOPS: 0.038936, BOS: 0.016707): 0.003048, VICUGNA: 0.031996): 0.004509): 0.006443): 0.000004, (MYOTIS: 0.056507, ((RHINOLOPHUS: 0.066174, MEGADERMA: 0.021473): 0.006671, PTEROPUS: 0.015521): 0.000004): 0.008379): 0.002227, (SOREX: 0.022136, ERINACEUS: 0.013937): 0.004338): 0.004428, ((MUS: 0.034943, (ORYCTOLAGUS: 0.021193, OCHOTONA: 0.063783): 0.025907): 0.003677, (PAN: 0.010448, HOMO: 0.001622): 0.021809): 0.002889): 0.000004): 0.144025, MONODELPHIS: 0.113014);",1);
		n1.getEndPos();
	}

	public void testCalculateTreeStatsFelsenstein2004Data(){
		/*
		 * Use the tree in Felsenstein (2004)pp.563 toTree stats from TreeStat v1.7.4:
		 * External/Internal ratio	1.25
		 * Treeness	0.444444444
		 * Tree Height	5
		 * Tree Length	18
		 * Cherry count	4
		 * Colless tree-imbalance	0.194444444
		 * Tips 10
		 */
		
		// implemented so far; test should pass these
		final double predictedTreeLength = 18;
		final double predictedCherryCount = 4;
		final double predictedTreeness = 0.444444444;
		final double predictedTreeHeight = 5;
		final double predictedExternalInternalRatio = 1.25;
		final double predictedTipCount = 10;
		// TODO TOP priority to implement
		final double predictedCollessTreeImbalance = 0.194444444;

		// tolerable rounding errors
		final double tolerableError = 0.00001d;
		// the tree which should have these
		String felsenstein = "(((((a1:1.0,a2:1.0):1.0,b:1.0):1.0,c:1.0):1.0,(d1:1.0,d2:1.0):1.0):1.0,((f1:1.0,f2:1.0):1.0,(g1:1.0,g2:1.0):1.0):1.0);";
		TreeNode felsensteinTree = new TreeNode(felsenstein,1);
		// get the stats
		double treeTreeLength = felsensteinTree.getTreeLength();
		double treeCherryCount = felsensteinTree.getTreeCherryCount();
		double treeCollessTreeImbalance = felsensteinTree.getTreeCollessNormalised();
		double treeTreeness = felsensteinTree.getTreeTreeness();
		double treeTreeHeight = felsensteinTree.getTreeHeight();
		double treeExternalInternalRatio = felsensteinTree.getTreeExternalInternalRatio();
		double treeTipCount = (double)felsensteinTree.getCountTipsBelow();
		// quick check on tips count for interest
		if(felsensteinTree.getCountTipsBelow() != felsensteinTree.getTipsBelow().length){
			fail("Tree tip counts by String[]  and int methods disagree!");
		}
		// print the stats (verbose, convenient for now)
		System.out.println("length\t"+treeTreeLength + "\t" + felsensteinTree.getContent());
		System.out.println("height\t"+treeTreeHeight + "\t" + felsensteinTree.getContent());
		// compare the stats
		HashMap<String,Double> testResults;
		// should PASS
		testResults = new HashMap<String,Double>();
		testResults.put("treeErrorLength",Math.abs(treeTreeLength-predictedTreeLength));
		testResults.put("treeErrorHeight",Math.abs(treeTreeHeight-predictedTreeHeight));
		testResults.put("treeErrorTreeness",Math.abs(treeTreeness-predictedTreeness));
		testResults.put("treeErrorExternalInternalRatio",Math.abs(treeExternalInternalRatio-predictedExternalInternalRatio));
		testResults.put("treeErrorCherryCount",Math.abs(treeCherryCount-predictedCherryCount));
		testResults.put("treeErrorCollessTreeImbalance",Math.abs(treeCollessTreeImbalance-predictedCollessTreeImbalance));
		// fail test
		Iterator<String> resultsChecker;
		resultsChecker = testResults.keySet().iterator();
		while(resultsChecker.hasNext()){
			String stat = (String) resultsChecker.next();
			if(testResults.get(stat)>tolerableError){
				fail("Wrong "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}else{
				System.out.println("Passed "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}
		}
		// currently FAILING
		testResults = new HashMap<String,Double>();
		testResults.put("treeErrorCollessTreeImbalance",Math.abs(treeCollessTreeImbalance-predictedCollessTreeImbalance));
		// fail test
		resultsChecker = testResults.keySet().iterator();
		while(resultsChecker.hasNext()){
			String stat = (String) resultsChecker.next();
			if(testResults.get(stat)>tolerableError){
				fail("Wrong "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}else{
				System.out.println("Passed "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}
		}
	}
	
	public void testDrawTreesUsingPhylogenyDisplayPanel(){
		TreeNode n1 = new TreeNode("(((LOXODONTA:0.080618,DASYPUS:0.028235):0.002756,(((((CANIS:0.012983,FELIS:0.013897):0.005719,(EQUUS:0.028437,((TURSIOPS:0.038936,BOS:0.016707):0.003048,VICUGNA:0.031996):0.004509):0.006443):0.000004,(MYOTIS:0.056507,((RHINOLOPHUS:0.066174,MEGADERMA:0.021473):0.006671,PTEROPUS:0.015521):0.000004):0.008379):0.002227,(SOREX:0.022136,ERINACEUS:0.013937):0.004338):0.004428,((MUS:0.034943,(ORYCTOLAGUS:0.021193,OCHOTONA:0.063783):0.025907):0.003677,(PAN:0.010448,HOMO:0.001622):0.021809):0.002889):0.000004):0.144025,MONODELPHIS:0.113014)",1);
		/*
		 * 26/12/2014
		 * Test the display of a phylogeny as a rendered object
		 * 
		 * Implementation of this not decided yet, perhaps:
		 * 
		 *  - get the co-ordinates for the lines representing internal nodes/edges
		 *  	>these will need vertical offset to decrease exponentially as the tree branches
		 *  	>maybe supply # tips as an argument so that the offset function can be used sensibly
		 *  - also get the tip labels
		 *  	>this could be achieved through a function to return tips in tree traversal order
		 *  	>that array can then be used to place phylogeny labels
		 *  - so DisplayCoordinatesPanel might be renamed PhylogenyDisplayPanel, and constructor overloaded with tip names array as well as edge co-ords.
		 */
		ArrayList<Integer[]> coords = n1.getBranchesAsCoOrdinates(0, 100, 10, 10);
		ArrayList<Integer[]> coordsFromBranches = n1.getBranchesAsCoordinatesFromTips(0, 0);
		ArrayList<String> names = n1.getTipsInOrder();
		JFrame frame = new JFrame();
		DisplayPhylogenyPanel panel = new DisplayPhylogenyPanel(coords, names);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,1000);
		frame.setVisible(true);
		JFrame frame2 = new JFrame();
		DisplayPhylogenyPanel panel2 = new DisplayPhylogenyPanel(coordsFromBranches, names);
		frame2.add(panel2);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setSize(1000,1000);
		frame2.setVisible(true);
		n1.getEndPos();
	}

	public void testDrawTreesUsingDisplayCoordinatesPanel(){
		TreeNode n1 = new TreeNode("(((LOXODONTA:0.080618,DASYPUS:0.028235):0.002756,(((((CANIS:0.012983,FELIS:0.013897):0.005719,(EQUUS:0.028437,((TURSIOPS:0.038936,BOS:0.016707):0.003048,VICUGNA:0.031996):0.004509):0.006443):0.000004,(MYOTIS:0.056507,((RHINOLOPHUS:0.066174,MEGADERMA:0.021473):0.006671,PTEROPUS:0.015521):0.000004):0.008379):0.002227,(SOREX:0.022136,ERINACEUS:0.013937):0.004338):0.004428,((MUS:0.034943,(ORYCTOLAGUS:0.021193,OCHOTONA:0.063783):0.025907):0.003677,(PAN:0.010448,HOMO:0.001622):0.021809):0.002889):0.000004):0.144025,MONODELPHIS:0.113014)",1);
		/*
		 * 26/12/2014
		 * Test the display of a phylogeny as a rendered object
		 * 
		 * Implementation of this not decided yet, perhaps:
		 * 
		 *  - get the co-ordinates for the lines representing internal nodes/edges
		 *  	>these will need vertical offset to decrease exponentially as the tree branches
		 *  	>maybe supply # tips as an argument so that the offset function can be used sensibly
		 *  - also get the tip labels
		 *  	>this could be achieved through a function to return tips in tree traversal order
		 *  	>that array can then be used to place phylogeny labels
		 *  - so DisplayCoordinatesPanel might be renamed PhylogenyDisplayPanel, and constructor overloaded with tip names array as well as edge co-ords.
		 */
		ArrayList<Integer[]> coords = n1.getBranchesAsCoOrdinates(0, 100, 10, 10);
		JFrame frame = new JFrame();
		DisplayCoordinatesPanel panel = new DisplayCoordinatesPanel(coords);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,1000);
		frame.setVisible(true);
		n1.getEndPos();
	}

	public void testReinflateSerAndAssignStates() throws IOException, ClassNotFoundException{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		HashMap<String, HashSet<String>[]> states = candidate.getDataset().getAminoAcidsAsFitchStates();
		HashSet<String>[] baseStates = species.getFitchStates(states).clone();
		int ambiguousAtRoot = 0;
		for(HashSet<String> statesSet:baseStates){
			if(statesSet.size()>1){
				ambiguousAtRoot++;
			}
		}
		species.resolveFitchStatesTopnode();
		species.resolveFitchStates(species.states);
		ParsimonyReconstruction pr = new ParsimonyReconstruction(states, species);
		pr.printAncestralComparison();
		String[] echolocators = {"MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		String[] echolocatorsH2 = {"TURSIOPS","MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		String[] controls = {"EIDOLON","PTEROPUS"};
		int pll_H1 = pr.findParallelSubtitutionsFromAncestral(echolocators, true);
		int pll_H1c= pr.findParallelSubtitutionsFromAncestralRejectingAmbiguities(echolocators,baseStates, true);
		int pll_H2 = pr.findParallelSubtitutionsFromAncestral(echolocatorsH2, true);
		int pll_H2c= pr.findParallelSubtitutionsFromAncestralRejectingAmbiguities(echolocatorsH2,baseStates,true);
		int pll_H2o= pr.findParallelSubtitutionsFromAncestralRejectingAmbiguitiesControllingOutgroups(echolocatorsH2,baseStates,true,controls);
		System.out.println("\nParallel H1\t\t"+pll_H1+"\nParallel H1c\t\t"+pll_H1c+"\nParallel H2\t\t"+pll_H2+"\nParallel H2c\t\t"+pll_H2c+"\nParallel H2o\t\t"+pll_H2o+"\n(Ambiguous at root:\t"+ambiguousAtRoot+")\n");
		species.getEndPos();
	}

	public void testPrint() throws IOException, ClassNotFoundException{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		String[] echolocatorsH2 = {"TURSIOPS","MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		species.printTree();
		System.out.println(species.printRecursively()+";");
		System.out.println(species.printRecursivelyLabelling(echolocatorsH2)+";");
		species.getEndPos();
	}

	public void testSubtreeContains() throws IOException, ClassNotFoundException{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		String[] echolocatorsH2 = {"TURSIOPS","MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		if(species.subtreeContains(echolocatorsH2)){
			fail();
		}
	}

	public void testSubtreeContainsAll() throws IOException, ClassNotFoundException{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		String[] allTaxa = {"LOXODONTA","DASYPUS","CANIS","FELIS","EQUUS","TURSIOPS","BOS","VICUGNA","MYOTIS","RHINOLOPHUS","MEGADERMA","PTEROPUS","SOREX","ERINACEUS","MUS","ORYCTOLAGUS","OCHOTONA","PAN","HOMO","MONODELPHIS"};
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		if(!species.subtreeContains(allTaxa)){
			fail();
		}
	}

	public void testPrintPaml() throws IOException, ClassNotFoundException{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		String[] echolocatorsH2 = {"TURSIOPS","MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		String[] trees = candidate.getFittedTrees();
		for(int i=0;i<trees.length;i++){
			TreeNode tree = new TreeNode(trees[i].replaceAll("\\s", ""),1);
			System.out.println("tree_"+i+" = "+tree.printRecursivelyLabelling(echolocatorsH2)+";");
		}
		candidate.getDataset().writePhylipFile("junit-test-inputs/quick.phy");
	}
	
	public void testAreTipsPresentNonePresent() throws Exception{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		HashMap<String, HashSet<String>[]> states = candidate.getDataset().getAminoAcidsAsFitchStates();
		HashSet<String>[] baseStates = species.getFitchStates(states).clone();
		int ambiguousAtRoot = 0;
		for(HashSet<String> statesSet:baseStates){
			if(statesSet.size()>1){
				ambiguousAtRoot++;
			}
		}
		species.resolveFitchStatesTopnode();
		species.resolveFitchStates(species.states);
		ParsimonyReconstruction pr = new ParsimonyReconstruction(states, species);
		pr.printAncestralComparison();
		String[] echolocators = {"LOXO","MYO","TUR","HOM"};
		HashSet<String> echoMap = new HashSet<String>(Arrays.asList(echolocators));
		echoMap = species.areTipsPresent(echoMap);
		if(echoMap.size() != 0){
			fail();
		}
	}

	public void testAreTipsPresentAllPresent() throws Exception{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		HashMap<String, HashSet<String>[]> states = candidate.getDataset().getAminoAcidsAsFitchStates();
		HashSet<String>[] baseStates = species.getFitchStates(states).clone();
		int ambiguousAtRoot = 0;
		for(HashSet<String> statesSet:baseStates){
			if(statesSet.size()>1){
				ambiguousAtRoot++;
			}
		}
		species.resolveFitchStatesTopnode();
		species.resolveFitchStates(species.states);
		ParsimonyReconstruction pr = new ParsimonyReconstruction(states, species);
		pr.printAncestralComparison();
		String[] echolocators = {"MEGADERMA","RHINOLOPHUS","MYOTIS"};
		HashSet<String> echoMap = new HashSet<String>(Arrays.asList(echolocators));
		echoMap = species.areTipsPresent(echoMap);
		if(echoMap.size() != 3){
			fail();
		}
	}

	public void testAreTipsPresent() throws Exception{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		HashMap<String, HashSet<String>[]> states = candidate.getDataset().getAminoAcidsAsFitchStates();
		HashSet<String>[] baseStates = species.getFitchStates(states).clone();
		int ambiguousAtRoot = 0;
		for(HashSet<String> statesSet:baseStates){
			if(statesSet.size()>1){
				ambiguousAtRoot++;
			}
		}
		species.resolveFitchStatesTopnode();
		species.resolveFitchStates(species.states);
		ParsimonyReconstruction pr = new ParsimonyReconstruction(states, species);
		pr.printAncestralComparison();
		String[] echolocators = {"MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		HashSet<String> echoMap = new HashSet<String>(Arrays.asList(echolocators));
		echoMap = species.areTipsPresent(echoMap);
		if(echoMap.size() != 3){
			fail();
		}
	}
	
	public void testGetTipAndMCRAStatesOf() throws Exception{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		HashMap<String, HashSet<String>[]> states = candidate.getDataset().getAminoAcidsAsFitchStates();
		HashSet<String>[] baseStates = species.getFitchStates(states).clone();
		int ambiguousAtRoot = 0;
		for(HashSet<String> statesSet:baseStates){
			if(statesSet.size()>1){
				ambiguousAtRoot++;
			}
		}
		species.resolveFitchStatesTopnode();
		species.resolveFitchStates(species.states);
		ParsimonyReconstruction pr = new ParsimonyReconstruction(states, species);
		pr.printAncestralComparison();
		String[] echolocators = {"MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		HashSet<String> echoMap = new HashSet<String>(Arrays.asList(echolocators));
		echoMap = species.areTipsPresent(echoMap);
		HashMap<String,HashSet<String>[]> ancAndOthers = species.getTipAndMRCAStatesOf(echoMap);
		HashSet<String>[] MRCAstates = ancAndOthers.remove("MRCA");
		int numParallel = new StateComparison(MRCAstates,ancAndOthers).countParallelChanges();
		String[] echolocatorsH2 = {"TURSIOPS","MEGADERMA","RHINOLOPHUS","MYOTIS","PTERONOTUS"};
		String[] controls = {"EIDOLON","PTEROPUS"};
		int pll_H1 = pr.findParallelSubtitutionsFromAncestral(echolocators, true);
		int pll_H1c= pr.findParallelSubtitutionsFromAncestralRejectingAmbiguities(echolocators,baseStates, true);
		int pll_H2 = pr.findParallelSubtitutionsFromAncestral(echolocatorsH2, true);
		int pll_H2c= pr.findParallelSubtitutionsFromAncestralRejectingAmbiguities(echolocatorsH2,baseStates,true);
		int pll_H2o= pr.findParallelSubtitutionsFromAncestralRejectingAmbiguitiesControllingOutgroups(echolocatorsH2,baseStates,true,controls);
		System.out.println("\nParallel H1\t\t"+pll_H1+"\nParallel H1c\t\t"+pll_H1c+"\nParallel H2\t\t"+pll_H2+"\nParallel H2c\t\t"+pll_H2c+"\nParallel H2o\t\t"+pll_H2o+"\n(Ambiguous at root:\t"+ambiguousAtRoot+")\n");
		
		species.getEndPos();
	}
	
	public void testHowManyTips() throws Exception{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		if(species.howManyTips() != 20){
			fail();
		}
	}

	public void testHowManyFromMonophyleticSetTree13() {
		String t13 = "(VN21: 0.000004, (((JJD001: 0.000004, (((MN23: 0.000004, ZY28: 0.000004): 0.000004, (RHFE: 0.030798, (PTEROPUS: 0.059729, (((ERINACEUS: 0.000004, (((MUS: 0.064403, (ORYCTOLAGUS: 0.036155, OCHOTONA: 0.023128): 0.018503): 0.036734, (PAN: 0.000004, HOMO: 0.000004): 0.000004): 0.000004, (MONODELPHIS: 0.124955, LOXODONTA: 0.000004): 0.000004): 0.000004): 0.020019, ((CANIS: 0.019464, FELIS: 0.018551): 0.018938, (EQUUS: 0.042617, (TURSIOPS: 0.021097, BOS: 0.000004): 0.084114): 0.041578): 0.000004): 0.000004, MYOTIS: 0.062492): 0.000004): 0.057992): 0.010215): 0.000004, (RHPECH: 0.000004, (((YLD001: 0.000004, WYS0705: 0.000004): 0.000004, ZY11: 0.000004): 0.000004, JSL055: 0.000004): 0.000004): 0.018900): 0.000004): 0.000004, ((VN005: 0.000004, ((NBCP011: 0.000004, YL005: 0.000004): 0.000004, RHYU: 0.000004): 0.000004): 0.000004, RHPEPE: 0.000004): 0.000004): 0.000004, FLD002: 0.000004): 0.000004, B014: 0.000004)";
		TreeNode species = new TreeNode(t13.replaceAll("\\s", ""),1);
		HashSet<String> taxaChinensis = new HashSet<String>();
		taxaChinensis.add("RHPECH");
		taxaChinensis.add("JSL055");
		taxaChinensis.add("WYS0705");
		taxaChinensis.add("YLD001");
		taxaChinensis.add("ZY11");
		int howMany = species.howManyFromMonophyleticSet(taxaChinensis);
		if(howMany != taxaChinensis.size()){
			fail();
		}
	}

	public void testHowManyFromMonophyleticSetTree1() {
		String t13 = "((NBCP011: 0.100004, ((RHYU: 0.100004, ((WYS0705: 0.100004, (ZY11: 0.100004, (FLD002: 0.100004, ((JSL055: 0.100004, (RHPECH: 0.100004, ((RHPEPE: 0.100004, ((MN23: 0.100004, ZY28: 0.100004): 0.100004, (RHFE: 0.187342, (PTEROPUS: 0.336618, (((ERINACEUS: 0.797420, (((MUS: 0.262004, (ORYCTOLAGUS: 0.168029, OCHOTONA: 0.197419): 0.131167): 0.163877, (PAN: 0.100004, HOMO: 0.100004): 0.102283): 0.103581, (MONODELPHIS: 0.215115, DASYPUS: 0.318737): 0.105996): 0.100004): 0.100004, ((CANIS: 0.186406, FELIS: 0.314986): 0.163642, (EQUUS: 0.265279, ((TURSIOPS: 0.120532, BOS: 0.186697): 0.100004, VICUGNA: 0.413095): 0.100004): 0.100004): 0.100004): 0.148946, MYOTIS: 0.314254): 0.133378): 0.100004): 0.100004): 0.126881): 0.100004, YL005: 0.100004): 0.100004): 0.100004): 0.100004, YLD001: 0.100004): 0.100004): 0.100004): 0.100004): 0.100004, VN005: 0.100004): 0.100004): 0.100004, JJD001: 0.100004): 0.100004): 0.100004, VN21: 6.116110, B014: 0.100004)";
		TreeNode species = new TreeNode(t13.replaceAll("\\s", ""),1);
		HashSet<String> taxaChinensis = new HashSet<String>();
		taxaChinensis.add("RHPECH");
		taxaChinensis.add("JSL055");
		taxaChinensis.add("WYS0705");
		taxaChinensis.add("YLD001");
		taxaChinensis.add("ZY11");
		int howMany = species.howManyFromMonophyleticSet(taxaChinensis);
		if(howMany == taxaChinensis.size()){
			fail();
		}
	}

	public void testHowManyFromMonophyleticSetTree2() {
		String t13 = "((NBCP011: 0.100004, ((RHYU: 0.100004, ((WYS0705: 0.100004, (B014: 0.100004, (FLD002: 0.100004, ((JSL055: 0.100004, (VN21: 0.100004, ((RHPEPE: 0.100004, ((MN23: 0.100004, ZY28: 0.100004): 0.100004, (RHFE: 0.187342, (PTEROPUS: 0.336618, (((ERINACEUS: 0.797420, (((MUS: 0.262004, (ORYCTOLAGUS: 0.168029, OCHOTONA: 0.197419): 0.131167): 0.163877, (PAN: 0.100004, HOMO: 0.100004): 0.102283): 0.103581, (MONODELPHIS: 0.215115, DASYPUS: 0.318737): 0.105996): 0.100004): 0.100004, ((CANIS: 0.186406, FELIS: 0.314986): 0.163642, (EQUUS: 0.265279, ((TURSIOPS: 0.120532, BOS: 0.186697): 0.100004, VICUGNA: 0.413095): 0.100004): 0.100004): 0.100004): 0.148946, MYOTIS: 0.314254): 0.133378): 0.100004): 0.100004): 0.126881): 0.100004, YL005: 0.100004): 0.100004): 0.100004): 0.100004, YLD001: 0.100004): 0.100004): 0.100004): 0.100004): 0.100004, VN005: 0.100004): 0.100004): 0.100004, JJD001: 0.100004): 0.100004): 0.100004, RPECH: 6.116110, ZY11: 0.100004)";
		TreeNode species = new TreeNode(t13.replaceAll("\\s", ""),1);
		HashSet<String> taxaChinensis = new HashSet<String>();
		taxaChinensis.add("RHPECH");
		taxaChinensis.add("JSL055");
		taxaChinensis.add("WYS0705");
		taxaChinensis.add("YLD001");
		taxaChinensis.add("ZY11");
		int howMany = species.howManyFromMonophyleticSet(taxaChinensis);
		if(howMany == taxaChinensis.size()){
			fail();
		}
	}

	public void testContainsMonophyleticCladeTree13() {
		String t13 = "(VN21: 0.000004, (((JJD001: 0.000004, (((MN23: 0.000004, ZY28: 0.000004): 0.000004, (RHFE: 0.030798, (PTEROPUS: 0.059729, (((ERINACEUS: 0.000004, (((MUS: 0.064403, (ORYCTOLAGUS: 0.036155, OCHOTONA: 0.023128): 0.018503): 0.036734, (PAN: 0.000004, HOMO: 0.000004): 0.000004): 0.000004, (MONODELPHIS: 0.124955, LOXODONTA: 0.000004): 0.000004): 0.000004): 0.020019, ((CANIS: 0.019464, FELIS: 0.018551): 0.018938, (EQUUS: 0.042617, (TURSIOPS: 0.021097, BOS: 0.000004): 0.084114): 0.041578): 0.000004): 0.000004, MYOTIS: 0.062492): 0.000004): 0.057992): 0.010215): 0.000004, (RHPECH: 0.000004, (((YLD001: 0.000004, WYS0705: 0.000004): 0.000004, ZY11: 0.000004): 0.000004, JSL055: 0.000004): 0.000004): 0.018900): 0.000004): 0.000004, ((VN005: 0.000004, ((NBCP011: 0.000004, YL005: 0.000004): 0.000004, RHYU: 0.000004): 0.000004): 0.000004, RHPEPE: 0.000004): 0.000004): 0.000004, FLD002: 0.000004): 0.000004, B014: 0.000004)";
		TreeNode species = new TreeNode(t13.replaceAll("\\s", ""),1);
		HashSet<String> taxaChinensis = new HashSet<String>();
		taxaChinensis.add("RHPECH");
		taxaChinensis.add("JSL055");
		taxaChinensis.add("WYS0705");
		taxaChinensis.add("YLD001");
		taxaChinensis.add("ZY11");
		int howMany = species.howManyFromMonophyleticSet(taxaChinensis);
		if(!species.containsMonophyleticClade(taxaChinensis)){
			fail();
		}
	}
	
	public void testNodeNumbers(){
		String numberedTree = "(((9_LOXODONTA, 3_DASYPUS) 25 , (((((2_CANIS, 7_FELIS) 30 , (5_EQUUS, ((21_TURSIOPS, 1_BOS) 33 , 22_VICUGNA) 32 ) 31 ) 29 , ((17_PTERONOTUS, 13_MYOTIS) 35 , ((19_RHINOLOPHUS, 10_MEGADERMA) 37 , (18_PTEROPUS, 4_EIDOLON) 38 ) 36 ) 34 ) 28 , (20_SOREX, 6_ERINACEUS) 39 ) 27 , ((12_MUS, (15_ORYCTOLAGUS, 14_OCHOTONA) 42 ) 41 , (16_PAN, 8_HOMO) 43 ) 40 ) 26 ) 24 , 11_MONODELPHIS) 23 ;";
		// ought to process this tree to get a) acceptable tree with terminal taxa for treenode constructor; b) list of terminal taxa numbers, incl. highest number
		TreeNode numbered = new TreeNode(numberedTree.replaceAll("\\s", ""),1);
		// then root numbering... check it follows preorder traversal rules..
		numbered.printRecursively();
		numbered.printTree();
	}
	
	public void testContainsMonophyleticClade() throws Exception{
		InputStream serfile = new FileInputStream("junit-test-inputs/g_100_ENSG0000PRESTIN_ng.fasinput100.faconv1367283909044wag.ser");
		ObjectInputStream inOne = new ObjectInputStream(serfile);
		SitewiseSpecificLikelihoodSupportAaml candidate = (SitewiseSpecificLikelihoodSupportAaml) inOne.readObject();
		TreeNode species = new TreeNode(candidate.getFittedTrees()[0].replaceAll("\\s", ""),1);
		HashSet<String> someTaxa = new HashSet<String>();
		if(species.containsMonophyleticClade(someTaxa)){
			fail();
		}
		someTaxa.add("RHINOLOPHUS");
//		if(!species.containsMonophyleticClade(someTaxa)){
//			fail();
//		}
		someTaxa.add("MEGADERMA");
		if(!species.containsMonophyleticClade(someTaxa)){
			fail();
		}
		someTaxa.add("LOXODONTA");
		if(species.containsMonophyleticClade(someTaxa)){
			fail();
		}
		someTaxa = new HashSet<String>();
		someTaxa.add("LOXODONTA");
		someTaxa.add("DASYPUS");
		someTaxa.add("CANIS");
		someTaxa.add("FELIS");
		someTaxa.add("EQUUS");
		someTaxa.add("TURSIOPS");
		someTaxa.add("BOS");
		someTaxa.add("VICUGNA");
		someTaxa.add("MYOTIS");
		someTaxa.add("RHINOLOPHUS");
		someTaxa.add("MEGADERMA");
		someTaxa.add("PTEROPUS");
		someTaxa.add("SOREX");
		someTaxa.add("ERINACEUS");
		someTaxa.add("MUS");
		someTaxa.add("ORYCTOLAGUS");
		someTaxa.add("OCHOTONA");
		someTaxa.add("PAN");
		someTaxa.add("HOMO");
		someTaxa.add("MONODELPHIS");
		if(!species.containsMonophyleticClade(someTaxa)){
			fail();
		}
	}

	/*
	 * Test that the TreeNode is able to label tips separately and correctly where a HashMap<String,int> mapping for tip names -> numbers is provided
	 */
	public void testIterativeNodeNumberingWithTipMappingSet(){
		// create the mapping set
		HashMap<String,Integer> tipNumberMap = new HashMap<String,Integer>();
		tipNumberMap.put("LOXODONTA",8 );
		tipNumberMap.put("DASYPUS",3 );
		tipNumberMap.put("CANIS",2);
		tipNumberMap.put("EQUUS",5);
		tipNumberMap.put("TURSIOPS",20);
		tipNumberMap.put("BOS",1);
		tipNumberMap.put("VICUGNA",21);
		tipNumberMap.put("PTERONOTUS",16);
		tipNumberMap.put("MYOTIS",12);
		tipNumberMap.put("RHINOLOPHUS",18);
		tipNumberMap.put("MEGADERMA",9);
		tipNumberMap.put("PTEROPUS",17);
		tipNumberMap.put("EIDOLON",4 );
		tipNumberMap.put("SOREX",19);
		tipNumberMap.put("ERINACEUS",6);
		tipNumberMap.put("MUS",11);
		tipNumberMap.put("ORYCTOLAGUS",14);
		tipNumberMap.put("OCHOTONA",13);
		tipNumberMap.put("PAN",15);
		tipNumberMap.put("HOMO",7);
		tipNumberMap.put("MONODELPHIS",10);
		
		String input="(((LOXODONTA:0.023584,DASYPUS:0.029504):0.000004,((((CANIS:0.076115,(EQUUS:0.014067,((TURSIOPS:0.000004,BOS:0.014131):0.003492,VICUGNA:0.021123):0.010546):0.000004):0.000004,((PTERONOTUS:0.025088,MYOTIS:0.032407):0.003456,((RHINOLOPHUS:0.008430,MEGADERMA:0.031984):0.005840,(PTEROPUS:0.000004,EIDOLON:0.006953):0.021190):0.000004):0.000004):0.000004,(SOREX:0.088536,ERINACEUS:0.044769):0.010306):0.003510,((MUS:0.090365,(ORYCTOLAGUS:0.011232,OCHOTONA:0.044380):0.036082):0.001096,(PAN:0.000004,HOMO:0.000004):0.013368):0.006229):0.001213):0.165422,MONODELPHIS:0.138559);";
		TreeNode n1 = new TreeNode(input,1);
		
		// set the mapping set
		n1.setTipNameNumberMapping(tipNumberMap);
		
		// set the numbering
		n1.setNodeNumbers(0,n1.howManyTips());
		
		// test the numbering (implicit)
		System.out.println(n1.printRecursivelyAsNumberedNodes());
		System.out.println(n1.printRecursively());
	
		// test the numbering (explicit)
		if(n1.getTipNumber("PAN") != 15){
			fail("tip numbering incorrect (should be: 15; value in memory: "+n1.getTipNumber("PAN")+").");
		}
		String inputNumberedTips="(((8,3),((((2,(5,((20,1),21))),((16,12),((18,9),(17,4)))),(19,6)),((11,(14,13)),(15,7)))),10);";
		TreeNode numbered = new TreeNode(inputNumberedTips,1);
		
		// test the numbering
		numbered.setNodeNumbers(0,numbered.howManyTips());
		
		System.out.println(numbered.printRecursivelyAsNumberedNodes());
		System.out.println(numbered.printRecursively());
	
		inputNumberedTips="(((LOXODONTA,DASYPUS),((((2,(5,((20,1),21))),((16,12),((18,9),(17,4)))),(19,6)),((11,(ORYCTOLAGUS,13_OCHOTONA)),(15,HOMO)))),10);";
		numbered = new TreeNode(inputNumberedTips,1);
		
		// test the numbering
		numbered.setNodeNumbers(0,numbered.howManyTips());
		
		System.out.println(numbered.printRecursivelyAsNumberedNodes());
		System.out.println(numbered.printRecursively());
	
	}

	public void testCalculateTreeStats(){
		/*
		 * Tree stats from TreeStat v1.7.4:
		 * Tree Length	36
		 * Tree Height	18
		 * Height 1	5
		 * Height 2	8
		 * Height 3	11
		 * Height 4	18
		 * Branch 1	3
		 * Branch 2	6
		 * Branch 3	7
		 * a	11
		 * b	12
		 * c	17
		 * d	18
		 * e	8
		 * tMRCA	18
		 * B1	2.5
		 * Cherry count	2
		 * Colless tree-imbalance	0.5
		 * N_bar	2.6
		 * Treeness	0.444444444
		 * Gamma	-0.6
		 * Delta	-1.642857143
		 * External/Internal ratio	1.25
		 * Fu & Li's D	-0.209394767
		 */
		
		// implemented so far; test should pass these
		final double predictedTreeLength = 36;
		final double predictedCherryCount = 2;
		final double predictedTreeness = 0.444444444;
		final double predictedTreeHeight = 18;
		final double predictedExternalInternalRatio = 1.25;
		// TODO TOP priority to implement
		final double predictedCollessTreeImbalance = 0.5;
		// TODO next priotity to implement
		final double predictedB1 = 2.5;
		final double predictedN_bar = 2.6;
		final double predictedGamma = -0.6;
		final double predictedDelta	= -1.642857143;
		final double predictedFuAndLiD = -0.209394767;
		// not a priority to implement
		final double predictedHeight_1 = 5;
		final double predictedHeight_2 = 8;
		final double predictedHeight_3 = 11;
		final double predictedHeight_4 = 18;
		final double predictedBranch_1 = 3;
		final double predictedBranch_2 = 6;
		final double predictedBranch_3 = 7;
		final double predicted_a = 11;
		final double predicted_b = 12;
		final double predicted_c = 17;
		final double predicted_d = 18;
		final double predicted_e = 8;
		// tolerable rounding errors
		final double tolerableError = 0.00001d;
		// the tree which should have these
		TreeNode testTree = new TreeNode("(((a:1,b:2):3,(c:4,d:5):6):7,e:8);",1);
		// get the stats
		double treeTreeLength = testTree.getTreeLength();
		double treeCherryCount = testTree.getTreeCherryCount();
		double treeCollessTreeImbalance = testTree.getTreeCollessTreeImbalanceNumerator();
		double treeTreeness = testTree.getTreeTreeness();
		double treeTreeHeight = testTree.getTreeHeight();
		double treeExternalInternalRatio = testTree.getTreeExternalInternalRatio();
		// print the stats (verbose, convenient for now)
		System.out.println("length\t"+treeTreeLength + "\t" + testTree.getContent());
		System.out.println("height\t"+treeTreeHeight + "\t" + testTree.getContent());
		// compare the stats
		HashMap<String,Double> testResults;
		// should PASS
		testResults = new HashMap<String,Double>();
		testResults.put("treeErrorLength",Math.abs(treeTreeLength-predictedTreeLength));
		testResults.put("treeErrorHeight",Math.abs(treeTreeHeight-predictedTreeHeight));
		testResults.put("treeErrorTreeness",Math.abs(treeTreeness-predictedTreeness));
		testResults.put("treeErrorExternalInternalRatio",Math.abs(treeExternalInternalRatio-predictedExternalInternalRatio));
		testResults.put("treeErrorCherryCount",Math.abs(treeCherryCount-predictedCherryCount));
		// fail test
		Iterator<String> resultsChecker;
		resultsChecker = testResults.keySet().iterator();
		while(resultsChecker.hasNext()){
			String stat = (String) resultsChecker.next();
			if(testResults.get(stat)>tolerableError){
				fail("Wrong "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}else{
				System.out.println("Passed "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}
		}
		// currently FAILING
		testResults = new HashMap<String,Double>();
		testResults.put("treeErrorCollessTreeImbalance",Math.abs(treeCollessTreeImbalance-predictedCollessTreeImbalance));
		// fail test
		resultsChecker = testResults.keySet().iterator();
		while(resultsChecker.hasNext()){
			String stat = (String) resultsChecker.next();
			if(testResults.get(stat)>tolerableError){
				fail("Wrong "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}else{
				System.out.println("Passed "+stat+",\terror "+testResults.get(stat)+", tolerable error "+tolerableError+")");
			}
		}
	}
}
