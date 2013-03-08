package uk.ac.qmul.sbcs.evolution.convergence.runners;

import java.io.File;
import java.util.TreeSet;

import uk.ac.qmul.sbcs.evolution.convergence.analyses.*;

public class CongruenceRunner {

	/**
	 * @param args
	 * @since r125: 06/03/2013
	 * This convergence runner is designed to detect convergence using: 
	 * 		a specified set of AA models in Aaml, 
	 * 		and an unlimited number of topologies. 
	 * It will additionally prune labelled trees for downstream codeml selection analyses.
	 * 
	 * Workflow for revised pipeline:
	 *
	 *	-    alignment parsing, taxon list building
	 *	-    determine # of substitution models, build list of them
	 *	-    prune input.trees
	 *	-    prune labelled.trees
	 *	-    prune constraint.tre
	 *	-    RAxML -g on constraint.tre
	 *	-    cat RAxML.tre with input.trees
	 *	-    for each (model:models) {get sitewise lnL for all trees}
	 *	
     * 	@TODO generally reorganise args
     *	@TODO RAxML -g method
     *	@TODO produce some random trees
     *	@TODO class to read multi-tree lnL files
     *	@TODO taxa list as a file 
     *	@TODO models list as a file
     *	@TODO <b>BIG DEAL</b> - sitewise lnL class to be written
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dataSet = new File(args[0]);
		File mainTreesFile = new File(args[1]);
		File constraintTreeFile = new File(args[2]);
		File labelledTreesFile = new File(args[3]);
		File workDir = new File(args[4]);
		String runID = args[5];
		File binaries = new File(args[6]);
		int thisFilter = Integer.parseInt(args[7]);
		boolean doFactor = false;
		switch(Integer.parseInt(args[8])){
			case (1): doFactor = true; break;
			case (0): doFactor = false; break;
		}
		TreeSet<String> taxaList = new TreeSet<String>();
		taxaList.add("TURSIOPS");
		taxaList.add("CANIS");
		taxaList.add("FELIS");
		taxaList.add("LOXODONTA");
		taxaList.add("ERINACEUS");
		taxaList.add("MUS");
		taxaList.add("MONODELPHIS");
		taxaList.add("PAN");
		taxaList.add("HOMO");
		taxaList.add("PTERONOTUS");
		taxaList.add("RHINOLOPHUS");
		taxaList.add("PTEROPUS");
		taxaList.add("EIDOLON");
		taxaList.add("DASYPUS");
		taxaList.add("EQUUS");
		taxaList.add("MEGADERMA");
		taxaList.add("MYOTIS");
		taxaList.add("BOS");
		taxaList.add("VICUGNA");
		taxaList.add("OCHOTONA");
		taxaList.add("ORYCTOLAGUS");
		taxaList.add("SOREX");
		String[] modelsList = {"dayhoff","wag","jones"};
		MultiHnCongruenceAnalysis analysis = new MultiHnCongruenceAnalysis(dataSet, mainTreesFile, constraintTreeFile, labelledTreesFile, workDir, binaries, runID, taxaList, modelsList, thisFilter, doFactor);
		analysis.go();
	}

}

