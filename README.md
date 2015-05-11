# Introduction #

The Genome Convergence Pipeline library is an API for running batch-enabled high-throughput analyses of aligned genomic data, primarily to explore signals of adaptive molecular convergence.


# Details #

**This API is at alpha level only**
This readme is a placeholder until we have tested the package to beta level.
For more questions please contact Joe Parker: [mailto:j.d.parker@qmul.ac.uk](mailto:j.d.parker@qmul.ac.uk)

## Capabilities ##
The API functions include parsing and I/O support for most common alignment and phylogeny formats, including Phylip/PAML, Nexus and FASTA (sequential and interleaved). Interface layers for analyses such as [RAxML](https://github.com/stamatak/standard-RAxML), PAML/codeml, MrBayes, [BEAST](https://code.google.com/p/beast-mcmc/), Phylobayes and NewickUtilities are provided. Functionality includes:
  * Alignment and tree I/O and operations including summary stats, translation, trimming, filtering, labelling, pruning and rerooting.
  * Ancestral sequence reconstruction by ML or parsimony.
  * Sitewise ∆SSLS (delta site-specific likelihood support).
  * dN/dS (positive selection) estimation by codeml.
  * Sequence simulation by evolver or MrBayes/Phylobayes
  * Phylogeny inference by RAxML: **note** that phylogeny inference _de_ _novo_ is **not** a primary motivation for this work.

## Using the API ##

At present we are working on a release of the relevant binaries that enable pipeline analyses as used in our recent [papers](http://scholar.google.com/citations?user=_WCn7AYAAAAJ&hl=en). These are not at stable release level but to get a development copy, please email the author at: [mailto:j.d.parker@qmul.ac.uk](mailto:j.d.parker@qmul.ac.uk).

If you are interested in contributing to the project, please also contact us. However this API is openly available for use/re-use and will remain so for the lifetime of this project. If you want to checkout or fork the API contact us for help if you need. You might want to start with the Javadoc, at
https://code.google.com/a/eclipselabs.org/p/qmul-genome-convergence-pipeline/source/browse/trunk/doc/index.html

# Acknowledgements #

This work at Queen Mary, University of London, is supported by the European Research Council, BBSRC (UK) and the Royal Society.

Written by Joe Parker in the Rossiter research group.

Infrastructure support for this project comes from [Steve Moss](http://about.me/gawbul) (School of Biological and Chemical Sciences) and Christopher Walker (Engineering and Physical Sciences)